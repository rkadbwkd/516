/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file.xroshot.common;

/**
 * @Class Name  : TfmLoggerMessage.java
 * @Project     : TFM
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2013. 9. 30.           최초생성
 *
 * @version      : 1.0.0.1
 * @since     : 2013. 9. 30.
 * @author    : TFM 개발팀
 * @see        : 공통 메시지 처리를 위한 클래스
 */
public class LoggerMessage {    
    /**
    * @Method Name  : getErrorMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 3건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param title 타이틀
    * @param errorCode 에러 코드 
    * @param errorMessage 사용자 정의 에러 메세지 
    * @return systemMessage 시스템 에러 메세지
    */
    public static String getErrorMessage(String title, int errorCode, String errorMessage, String systemMessage) {
        String errorCodeStr = String.valueOf(errorCode);
        String message = getMessage(title, errorCodeStr, errorMessage, systemMessage);
        
        return message;
    }
    
    /**
    * @Method Name  : getErrorMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 3건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param title 타이틀
    * @param errorCode 에러 코드 
    * @param errorMessage 사용자 정의 에러 메세지 
    */
    public static String getErrorMessage(String title, int errorCode, String errorMessage) {
        String errorCodeStr = String.valueOf(errorCode);
        String message = getMessage(title, errorCodeStr, errorMessage);

        return message;
    }
    
    /**
    * @Method Name  : getMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 3건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param msg1 메시지1
    * @param msg2 메시지2
    * @param msg3 메시지3
    * @param msg4 메시지4
    * @return message
    */
    public static String getMessage(String msg1, String msg2, String msg3, String msg4) {
        String message = "";
        if(msg1 != null) message = "[" + msg1 + "]";
        if(msg2 != null) message += " : [" + msg2 + "]";
        if(msg3 != null) message += " : [" + msg3 + "]";
        if(msg4 != null) message += " : [" + msg4 + "]";
        return message;
    }
    
    /**
    * @Method Name  : getMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 3건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param msg1 메시지1
    * @param msg2 메시지2
    * @param msg3 메시지3
    * @return message
    */
    public static String getMessage(String msg1, String msg2, String msg3) {
        String message = "";
        if(msg1 != null) message = "[" + msg1 + "]";
        if(msg2 != null) message += " : [" + msg2 + "]";
        if(msg3 != null) message += " : [" + msg3 + "]";
        return message;
    }
    
    /**
    * @Method Name  : getMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 2건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param msg1 메시지1
    * @param msg2 메시지2
    * @return message
    */
    public static String getMessage(String msg1, String msg2) {
        String message = "";
        if(msg1 != null) message = "[" + msg1 + "]";
        if(msg2 != null) message += " : [" + msg2 + "]";
        return message;
    }
    
    /**
    * @Method Name  : getMessage
    * @author       : aa
    * @since        : 2013. 9. 30.
    * @see            : 1건의 메시지 파라메터를 받는 메시지 처리 메소드
    * @param msg1 메시지1
    * @return
    */
    public static String getMessage(String msg1) {
        String message = "";
        if(msg1 != null) message = "[" + msg1 + "]";
        return message;
    }
}

