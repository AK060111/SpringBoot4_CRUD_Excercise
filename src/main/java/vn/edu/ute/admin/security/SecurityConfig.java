package vn.edu.ute.admin.security;
import vn.edu.ute.admin.repository.UserRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.servlet.DispatcherType;
@Configuration public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean UserDetailsService userDetailsService(UserRepository repository) {
        return username -> repository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
    }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        return http
                .authorizeHttpRequests(a -> a
                        // Allow internal MVC/JSP rendering after the initial request was checked.
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/login", "/error", "/css/**").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/admin", true).permitAll())
                .logout(l -> l.logoutSuccessUrl("/login?logout"))
                .build();
    }
}
