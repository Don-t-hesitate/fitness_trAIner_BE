package com.example.fitness_trAIner.common.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class CustomResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource> {

    public CustomResourceHttpMessageConverter() {
        super(MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return Resource.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return Resource.class.isAssignableFrom(clazz);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Resource.class.isAssignableFrom(clazz);
    }

    @Override
    protected Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return new InputStreamResource(inputMessage.getBody());
    }

    @Override
    protected void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // 구현 필요 없음 (canWrite가 false이므로 호출되지 않음)
    }

}