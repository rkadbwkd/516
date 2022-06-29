/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.anylogic.demo.common.message.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    private static final HttpHeaders headers = new HttpHeaders();

    private static String buildJson(Object body) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonBody;
    }

    public static ResponseEntity<Object> responseOk() {
        Object body = BaseResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("성공");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(buildJson(body));
    }

    public static ResponseEntity<Object> responseOk(Object data) {
        int count = 0;
        if (data instanceof List) {
            count = ((List<?>)data).size();
        } else {
            count = 1;
        }

        Object body = BaseResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("성공")
                .data(data)
                .count(count);

        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(buildJson(body));
    }

    public static ResponseEntity<Object> responseError(HttpStatus status, String message) {
        Object body = BaseResponse.builder()
                .code(status.value())
                .httpStatus(status)
                .message(message);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(buildJson(body));
    }

    public static ResponseEntity<Object> responseError(String message) {
        Object body = BaseResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(message);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(headers)
                .body(buildJson(body));
    }
}

