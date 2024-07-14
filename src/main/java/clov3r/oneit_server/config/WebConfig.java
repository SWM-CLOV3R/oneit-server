package clov3r.oneit_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://oneit.gift", "https://client-dev.oneit.gift")
                .allowedMethods("GET","POST","PUT", "PATCH", "HEAD")
                .allowedHeaders("*");
    }
}