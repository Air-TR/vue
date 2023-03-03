package com.tr.vue.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.vue.common.annotation.NotControllerResponseAdvice;
import com.tr.vue.common.exception.BusinessException;
import com.tr.vue.common.result.Result;
import com.tr.vue.common.result.ResultEnum;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 被 ControllerResponseAdvice 扫描的接口，哪怕返回类型是 void，也会被封装成 Result 类型返回：{ "code": 0, "msg": "成功", "data": null }
 *
 * @author TR
 * @date 2022/6/25 上午9:24
 */
@RestControllerAdvice(basePackages = {"com.tr.vue.controller"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> clazz) {
        // response 是 Result 类型，或者注释了 NotControllerResponseAdvice 都不进行包装
        return !(methodParameter.getParameterType().isAssignableFrom(Result.class)
                || methodParameter.hasMethodAnnotation(NotControllerResponseAdvice.class));
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest request, ServerHttpResponse response) {
        // String 类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在 Result 里后转换为 json 串进行返回
                return objectMapper.writeValueAsString(Result.success(data));
            } catch (JsonProcessingException e) {
                throw new BusinessException(ResultEnum.RESPONSE_PACK_ERROR, e);
            }
        }
        // 否则直接包装成 Result 返回
        return Result.success(data);
    }

}
