package vn.edu.ute.admin.config;
import vn.edu.ute.admin.entity.*;
import vn.edu.ute.admin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration public class AdminInitializer {
    @Bean CommandLineRunner initializeAdmin(UserRepository repository,PasswordEncoder encoder,   @Value("${app.admin.username}") String username,@Value("${app.admin.password}") String password,@Value("${app.admin.email}") String email) {
        return args-> {
            if(username.isBlank()||password.isBlank()||email.isBlank())return;
            if(repository.findByUsername(username.strip()).isPresent())return;
            if(password.length()<8||password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)     throw new IllegalArgumentException("ADMIN_PASSWORD phải có ít nhất 8 ký tự và tối đa 72 byte");
            User u=new User();
            u.setUsername(username.strip());
            u.setEmail(email.strip());
            u.setFullname("Quản trị viên");
            u.setRole(Role.ADMIN);
            u.setPassword(encoder.encode(password));
            repository.save(u);
        };
    }
}
