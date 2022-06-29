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
* @class ServiceException.java
* @brief SQL 실행 예외처리
*
* @author TFM 개발팀
* @date 2014. 3. 18.
* @version 1.0.0.1
* @Modification  Information 
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014. 3. 18.       김현수    
**/
public class ServiceException extends SQLExecuteException {
    private static final long serialVersionUID = 1L;
    private String serviceErrorCode;
    
    public ServiceException(String message, String queryId, int errorCode) {
        super(message, queryId, errorCode);
    }
    
    public ServiceException(String message, int errorCode) {
        super(message, "", errorCode);
    }

    public String getServiceErrorCode() {
        return serviceErrorCode;
    }

    public void setServiceErrorCode(String serviceErrorCode) {
        this.serviceErrorCode = serviceErrorCode;
    }
}

