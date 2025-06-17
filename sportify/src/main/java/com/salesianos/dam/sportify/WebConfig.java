package com.salesianos.dam.sportify;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Solo redirige rutas que NO sean de API ni recursos estáticos
        registry.addViewController("/{spring:(?!api|noticias|auth|deporte|liga|equipo|user|writer|admin|comentario|download|uploads|assets|swagger-ui|webjars|v3|swagger-resources|api-docs)[a-zA-Z0-9-]+}")
                .setViewName("forward:/index.html");
        registry.addViewController("/{spring:(?!api|noticias|auth|deporte|liga|equipo|user|writer|admin|comentario|download|uploads|assets|swagger-ui|webjars|v3|swagger-resources|api-docs)[a-zA-Z0-9-]+}/**")
                .setViewName("forward:/index.html");
    }
}
