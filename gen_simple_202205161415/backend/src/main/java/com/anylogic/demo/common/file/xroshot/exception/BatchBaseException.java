/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.common.file.xroshot.exception;
/**
* @class TfmBatchBaseException.java
* @brief 배치 예외처리 클래스
*
* @author TFM 개발팀
* @date 2014. 3. 18.
* @version 1.0.0.1
* @Modification  Information
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014. 3. 18.       김현수
**/
public class BatchBaseException extends Exception {
    private static final long serialVersionUID = 1L;
    private int errorCode;
    private String message;

    public BatchBaseException(String message, int errorCode, Throwable cause)
    {
        super(message, cause);
        this.setMessage(message);
        this.setErrorCode(errorCode);
    }

    public BatchBaseException(String message, int errorCode)
    {
        super(message);
        this.setMessage(message);
        this.setErrorCode(errorCode);
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

