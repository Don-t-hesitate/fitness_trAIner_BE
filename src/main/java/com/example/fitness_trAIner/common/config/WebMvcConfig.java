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

    // 정적 리소스 핸들러 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/" 패턴의 요청을 "classpath:/static/build/" 위치에서 찾도록 설정
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);

        // "/static/js/**" 패턴의 요청을 "classpath:/static/build/static/js/" 위치에서 찾도록 설정
        registry.addResourceHandler("/static/js/**")
                .addResourceLocations("classpath:/static/static/js/")
                .setCachePeriod(0);

        // "/static/css/**" 패턴의 요청을 "classpath:/static/build/static/css/" 위치에서 찾도록 설정
        registry.addResourceHandler("/static/css/**")
                .addResourceLocations("classpath:/static/static/css/")
                .setCachePeriod(0);

    }

    // 커스텀 HttpMessageConverter 추가
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CustomResourceHttpMessageConverter());
    }
}