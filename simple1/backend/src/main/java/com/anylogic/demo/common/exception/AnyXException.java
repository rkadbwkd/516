/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.exception;

/**
 * <PRE>
 *  ClassName : AnyXException 
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 16. 오후 4:02:29
 * @author  : jun
 * @brief   : 
 */

public class AnyXException extends RuntimeException {
    private static final long serialVersionUID = 3211991520472905865L;

    private final static String DEFCODE = "0";
    private final static String DEFMSG  = "처리중 오류가 발생하였습니다.";

    private String code;
    private String message;

    public AnyXException(String sMessage){
        this.code = DEFCODE;
        this.message = DEFMSG;
        System.out.println(sMessage);
    }

    public AnyXException(){
        this.code = DEFCODE;
        this.message = DEFMSG;
    }

    public AnyXException(String sCode, String sMessage){
        this.code = sCode;
        this.message = sMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String getDefcode() {
        return DEFCODE;
    }

    public static String getDefmsg() {
        return DEFMSG;
    }

}


