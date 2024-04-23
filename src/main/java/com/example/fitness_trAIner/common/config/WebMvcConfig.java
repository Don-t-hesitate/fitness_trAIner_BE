package com.example.fitness_trAIner.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.fitness_trAIner.common.config.CustomResourceHttpMessageConverter;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/build/")
                .setCachePeriod(0);

        registry.addResourceHandler("/static/js/**")
                .addResourceLocations("classpath:/static/build/static/js/")
                .setCachePeriod(0);
        registry.addResourceHandler("/static/css/**")
                .addResourceLocations("classpath:/static/build/static/css/")
                .setCachePeriod(0);
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CustomResourceHttpMessageConverter());
    }
}