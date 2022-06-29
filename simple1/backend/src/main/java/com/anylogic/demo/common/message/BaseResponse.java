/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;


@Data
@Builder
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 5468480346176063191L;
    public static final String DEFAULT_MSG = new String("success");
    public static final String DEFAULT_CODE = new String("200");

    private HttpStatus httpStatus;

    private Integer code;

    private String message;
    private Integer count;
    private T data;

//    public BaseResponse() {
//        message = "";
//        this.setResponseNG();
//    }
//
//    public String getResponseCode() {
//        return responseCode;
//    }
//
//    public void setResponseCode(String responseCode) {
//        this.responseCode = responseCode;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
//
//    public void putData(Object object) {
//        this.data = object;
//    }
//
//    public void setResponseOK() {
//        this.responseCode = "OK";
//    }
//
//    public void setResponseNG() {
//        this.responseCode = "NG";
//    }

}

