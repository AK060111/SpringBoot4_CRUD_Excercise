package vn.edu.ute.admin.config;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import jakarta.servlet.DispatcherType;
@Configuration public class SiteMeshConfig {
    @Bean FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        ConfigurableSiteMeshFilter filter=new ConfigurableSiteMeshFilter() {
            @Override protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder.setDecoratorPrefix("/WEB-INF/decorators/")
                        .addDecoratorPath("/admin", "main.jsp")
                        .addDecoratorPath("/admin/*", "main.jsp");
            }
        };
        FilterRegistrationBean<ConfigurableSiteMeshFilter> bean=new FilterRegistrationBean<>(filter);
        bean.addUrlPatterns("/admin","/admin/*");
        bean.setDispatcherTypes(DispatcherType.REQUEST);
        bean.setOrder(10);
        return bean;
    }
}
