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

/**
 * Spring MVC에서 Resource 객체를 처리하기 위한 커스텀 HttpMessageConverter
 * Resource 객체의 읽기 작업 지원, 쓰기 작업은 미지원
 *
 * HttpMessageConverter는 HTTP 요청과 응답의 본문(body)을 객체로 변환 및 객체를 HTTP 요청 또는 응답의 본문으로 변환하는 역할을 수행
 * 해당 컨버터는 특히 Resource 객체를 다루기 위해 사용
 *
 * Resource는 스프링 프레임워크에서 제공하는 인터페이스로, 다양한 저장 매체에 존재하는 리소스에 대한 추상화를 제공
 * 예를 들어, 파일 시스템, 클래스패스, URL 등의 리소스를 일관된 방식으로 다룰 수 있음
 */
public class CustomResourceHttpMessageConverter extends AbstractHttpMessageConverter<Resource> {

    /**
     * 생성자에서 지원하는 미디어 타입을 APPLICATION_OCTET_STREAM으로 설정
     * APPLICATION_OCTET_STREAM은 모든 종류의 이진 데이터를 나타내는 미디어 타입
     */
    public CustomResourceHttpMessageConverter() {
        super(MediaType.APPLICATION_OCTET_STREAM);
    }

    /**
     * 주어진 클래스가 Resource 클래스 또는 그 하위 클래스인지 확인
     * 해당 메서드가 true를 반환하면, 이 컨버터가 해당 클래스를 읽을 수 있다는 것을 나타냄
     *
     * @param clazz    확인할 클래스
     * @param mediaType 미디어 타입 (사용되지 않음)
     * @return 주어진 클래스가 Resource 클래스 또는 그 하위 클래스인 경우 true, 그렇지 않으면 false
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return Resource.class.isAssignableFrom(clazz);
    }

    /**
     * 주어진 클래스가 Resource 클래스 또는 그 하위 클래스인지 확인
     * 해당 메서드가 true를 반환하면, 이 컨버터가 해당 클래스를 쓸 수 있다는 것을 나타냄
     * 여기서는 항상 false를 반환하므로, 이 컨버터는 쓰기 작업 미지원
     *
     * @param clazz    확인할 클래스
     * @param mediaType 미디어 타입 (사용되지 않음)
     * @return 항상 false를 반환하여 쓰기 작업 미지원을 나타냄
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * 주어진 클래스가 Resource 클래스 또는 그 하위 클래스인지 확인
     * 이 메서드는 canRead 및 canWrite 메서드와 같은 로직을 공유하기 위해 사용
     *
     * @param clazz 확인할 클래스
     * @return 주어진 클래스가 Resource 클래스 또는 그 하위 클래스인 경우 true, 그렇지 않으면 false
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return Resource.class.isAssignableFrom(clazz);
    }

    /**
     * HttpInputMessage에서 Resource 객체 읽음
     * 해당 메서드는 HTTP 요청의 본문을 Resource 객체로 변환하는 역할 수행
     *
     * InputStreamResource를 사용하여 HttpInputMessage의 본문을 읽어와 Resource 객체로 변환
     * InputStreamResource는 InputStream을 Resource로 래핑하는 구현체
     *
     * @param clazz        Resource 클래스 또는 그 하위 클래스
     * @param inputMessage HTTP 입력 메시지
     * @return HttpInputMessage의 본문으로부터 읽어온 Resource 객체
     * @throws IOException                     I/O 예외가 발생한 경우
     * @throws HttpMessageNotReadableException 메시지를 읽을 수 없는 경우
     */
    @Override
    protected Resource readInternal(Class<? extends Resource> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return new InputStreamResource(inputMessage.getBody());
    }

    /**
     * Resource 객체를 HttpOutputMessage에 작성
     * 해당 메서드는 Resource 객체를 HTTP 응답의 본문으로 변환하는 역할 수행
     * AbstractHttpMessageConverter를 상속받을 때, writeInternal 메서드를 반드시 구현해야 함
     *
     * 이 컨버터는 쓰기 작업을 지원하지 않으므로, 이 메서드는 실제로 구현할 필요 없음
     * canWrite 메서드가 항상 false를 반환하므로, 이 메서드는 호출되지 않음
     *
     * @param resource      쓸 Resource 객체
     * @param outputMessage HTTP 출력 메시지
     * @throws IOException                      I/O 예외가 발생한 경우
     * @throws HttpMessageNotWritableException 메시지를 쓸 수 없는 경우
     */
    @Override
    protected void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new UnsupportedOperationException("write operation is not supported");
    }

}