package vn.edu.ute.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
public class JspConfig {
    @Bean
    InternalResourceViewResolver jspViewResolver(
            @Value("${spring.mvc.view.prefix}") String prefix,
            @Value("${spring.mvc.view.suffix}") String suffix) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setViewClass(JstlView.class);
        resolver.setContentType("text/html;charset=UTF-8");
        // Tomcat 11 forwards commit the response before SiteMesh can decorate it.
        resolver.setAlwaysInclude(true);
        return resolver;
    }
}
