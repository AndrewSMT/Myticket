package com.andrew.MyTicket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration spring MVC
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * RestTemplate been
     * @return instance of RestTemplate
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Handler of resource static
     * @param registry registrations of resource
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/picture/**")
                .addResourceLocations("classpath:/picture/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}