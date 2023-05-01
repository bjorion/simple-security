package org.jorion.simplesecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/main")
                .setViewName("main");
        registry.addViewController("/done")
                .setViewName("done");
        registry.addViewController("/access-denied")
                .setViewName("access-denied");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        final long ageInDays = 1000;
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(ageInDays, TimeUnit.DAYS));
    }

}
