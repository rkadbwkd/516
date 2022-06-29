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
* @class SQLExecuteException.java
* @brief SQL예외처리 클래스
*
* @author TFM 개발팀
* @date 2013. 11. 28.
* @version 1.0.0.1
* @Modification  Information 
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2013. 11. 28.       한국인               최초 파일 생성
**/
public class SQLExecuteException extends BatchBaseException {
    private static final long serialVersionUID = 1L;
    private String queryId;
    
    public SQLExecuteException(String message, String queryId, int errorCode){
        super(message, errorCode);
        this.setQueryId(queryId);
    }

    public SQLExecuteException(String message, int errorCode){
        super(message, errorCode);
    }
    
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryId() {
        return queryId;
    }
}

