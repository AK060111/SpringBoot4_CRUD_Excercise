package vn.edu.ute.admin;
import vn.edu.ute.admin.entity.*;
import vn.edu.ute.admin.dto.*;
import vn.edu.ute.admin.repository.*;
import vn.edu.ute.admin.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT) @TestExecutionListeners(listeners=DependencyInjectionTestExecutionListener.class,mergeMode=TestExecutionListeners.MergeMode.REPLACE_DEFAULTS) class AdminIntegrationTest {
    @Value("${local.server.port}") int port;
    @Autowired UserRepository users;
    @Autowired CategoryRepository categories;
    @Autowired CategoryService categoryService;
    @Autowired UserService userService;
    @Autowired PasswordEncoder encoder;
    HttpClient client;
    String password;
    @BeforeEach void setup() {
        categories.deleteAll();
        users.deleteAll();
        password=UUID.randomUUID().toString();
        for(Role role:Role.values()) {
            User u=new User();
            u.setUsername(role.name().toLowerCase());
            u.setEmail(role.name().toLowerCase()+"@example.test");
            u.setFullname("Nguyễn Văn "+role);
            u.setPassword(encoder.encode(password));
            u.setRole(role);
            users.save(u);
        }
        client=HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).build();
    }
    HttpResponse<String> get(String path)throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:"+port+path)).GET().build(),HttpResponse.BodyHandlers.ofString());
    }
    HttpResponse<String> post(String path,Map<String,String> fields)throws Exception {
        String data=fields.entrySet().stream().map(e->URLEncoder.encode(e.getKey(),StandardCharsets.UTF_8)+"="+URLEncoder.encode(e.getValue(),StandardCharsets.UTF_8)).reduce((a,b)->a+"&"+b).orElse("");
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:"+port+path)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(data)).build(),HttpResponse.BodyHandlers.ofString());
    }
    String csrf(String body) {
        Matcher m=Pattern.compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"").matcher(body);
        assertThat(m.find()).isTrue();
        return m.group(1);
    }
    void login(String username)throws Exception {
        String token=csrf(get("/login").body());
        assertThat(post("/login",Map.of("username",username,"password",password,"_csrf",token)).statusCode()).isEqualTo(302);
    }
    @Test void authorizationAndCsrf()throws Exception {
        assertThat(get("/admin/categories").statusCode()).isEqualTo(302);
        login("user");
        assertThat(get("/admin/users").statusCode()).isEqualTo(403);
        assertThat(post("/admin/categories/add",Map.of("name","Test")).statusCode()).isEqualTo(403);
    }
    @Test void categoryCrudValidationDecorationAndPaging()throws Exception {
        login("admin");
        var form=get("/admin/categories/add");
        assertThat(form.statusCode()).isEqualTo(200);
        assertThat(form.body()).contains("UTE · Admin","<footer").doesNotContain("<sitemesh:");
        assertThat(form.body().split("<html",-1)).hasSize(2);
        String token=csrf(form.body());
        var invalid=post("/admin/categories/add",Map.of("name","   ","_csrf",token));
        assertThat(invalid.statusCode()).isEqualTo(200);
        assertThat(invalid.body()).contains("Tên danh mục không được trống");
        assertThat(categories.count()).isZero();
        assertThat(post("/admin/categories/add",Map.of("name","  Điện tử  ","_csrf",token,"id","999")).statusCode()).isEqualTo(302);
        Category saved=categories.findAll().get(0);
        assertThat(saved.getName()).isEqualTo("Điện tử");
        assertThat(saved.getId()).isNotEqualTo(999L);
        assertThat(get("/admin/categories/edit/"+saved.getId()).statusCode()).isEqualTo(200);
        assertThat(post("/admin/categories/edit/"+saved.getId(),Map.of("name","<script>alert(1)</script>","_csrf",token)).statusCode()).isEqualTo(302);
        assertThat(get("/admin/categories").body()).contains("&lt;script&gt;").doesNotContain("<script>alert(1)");
        for(int i=0;i<12;i++) {
            CategoryForm f=new CategoryForm();
            f.setName("Nhóm "+i);
            categoryService.save(null,f);
        }
        assertThat(categoryService.search("Nhóm",Integer.MAX_VALUE).getNumber()).isEqualTo(1);
        assertThat(categoryService.search("Nhóm",-10).getNumber()).isZero();
        assertThat(get("/admin/categories?keyword=Nh%C3%B3m").body()).contains("Next","keyword=Nh");
        assertThat(get("/admin/categories/delete/"+saved.getId()).statusCode()).isEqualTo(405);
        assertThat(post("/admin/categories/delete/"+saved.getId(),Map.of("_csrf",token)).statusCode()).isEqualTo(302);
        assertThat(post("/admin/categories/delete/"+saved.getId(),Map.of("_csrf",token)).statusCode()).isEqualTo(404);
    }
    @Test void userCrudValidationAndPasswordPreservation()throws Exception {
        login("admin");
        String token=csrf(get("/admin/users/add").body());
        Map<String,String> fields=new HashMap<>(Map.of("username","  student  ","email","student@example.test","fullname","Trần Anh Khoa","password",password,"role","USER","_csrf",token));
        assertThat(post("/admin/users/add",fields).statusCode()).isEqualTo(302);
        User saved=users.findByUsername("student").orElseThrow();
        String hash=saved.getPassword();
        assertThat(encoder.matches(password,hash)).isTrue();
        assertThat(get("/admin/users/edit/"+saved.getId()).body()).doesNotContain(hash);
        fields.put("password","");
        fields.put("email","bad");
        assertThat(post("/admin/users/edit/"+saved.getId(),fields).body()).contains("Email không hợp lệ","Trần Anh Khoa");
        assertThat(users.findById(saved.getId()).orElseThrow().getPassword()).isEqualTo(hash);
        fields.put("email","student@example.test");
        fields.put("role","INVALID");
        assertThat(post("/admin/users/edit/"+saved.getId(),fields).statusCode()).isEqualTo(200);
        fields.put("role","USER");
        fields.put("username","admin");
        assertThat(post("/admin/users/edit/"+saved.getId(),fields).body()).contains("Username đã tồn tại");
        fields.put("username","student");
        fields.put("fullname","Nguyễn Thị Mai");
        fields.put("id",users.findByUsername("admin").orElseThrow().getId().toString());
        assertThat(post("/admin/users/edit/"+saved.getId(),fields).statusCode()).isEqualTo(302);
        assertThat(users.findById(saved.getId()).orElseThrow().getPassword()).isEqualTo(hash);
        assertThat(userService.search("Thị Mai",99).getTotalElements()).isEqualTo(1);
        assertThat(userService.search("student@",0).getTotalElements()).isEqualTo(1);
        assertThat(userService.search("student",0).getTotalElements()).isEqualTo(1);
        for(int i=0;i<11;i++) {
            UserForm extra=new UserForm();
            extra.setUsername("paging"+i);
            extra.setEmail("paging"+i+"@example.test");
            extra.setFullname("Phân trang "+i);
            extra.setPassword(password);
            extra.setRole(Role.USER);
            userService.save(null,extra);
        }
        assertThat(userService.search("paging",Integer.MAX_VALUE).getNumber()).isEqualTo(1);
        assertThat(userService.search("paging",0).getContent()).hasSize(10);
        assertThat(get("/admin/users?keyword=paging").body()).contains("Next","keyword=paging");
        assertThat(get("/admin").statusCode()).isEqualTo(200);
        assertThat(post("/admin/users/delete/"+saved.getId(),Map.of("_csrf",token)).statusCode()).isEqualTo(302);
        assertThat(users.existsById(saved.getId())).isFalse();
    }
}
