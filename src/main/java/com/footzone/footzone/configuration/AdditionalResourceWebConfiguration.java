package com.footzone.footzone.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/user/**")
                .addResourceLocations("file:src/main/resources/userPhotos/");
        registry.addResourceHandler("/images/stadium/**")
                .addResourceLocations("file:src/main/resources/stadiumPhotos/");
    }
}