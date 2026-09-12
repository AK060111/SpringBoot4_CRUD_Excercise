package vn.edu.ute.admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@SpringBootApplication public class AdminWebApplication extends SpringBootServletInitializer {
    @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AdminWebApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(AdminWebApplication.class, args);
    }
}
