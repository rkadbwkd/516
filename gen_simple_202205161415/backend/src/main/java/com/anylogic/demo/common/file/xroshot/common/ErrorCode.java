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
 * @Class Name  : ErrorCode.java
 * @Project     : TFM
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2013. 9. 24.           최초생성
 *
 * @version      : 1.0.0.1
 * @since     : 2013. 9. 27.
 * @author    : TFM 개발팀
 * @see        : 에러 코드 관리를 위한 클래스
 */
public class ErrorCode {
    public static final int RC_OK = 0;
    
    /*
     * Common Error (1 ~ 100)
     */
    public static final int NULL_ERROR = 1;
    public static final int IO_ERROR = 2;
    public static final int UNKNOWN_ERROR = 3;
    public static final int TYPE_CONVERSION_ERROR = 4;
    public static final int RESPONSE_IO_ERROR = 5;
    public static final int ENCODING_ERROR = 6;
    public static final int TRANSACTION_ERROR = 7;
    public static final int XML_PARSING_ERROR = 8;
    public static final int SOKCET_DISCONNECT_ERROR = 9;
    public static final int SOKCET_CONNECT_ERROR = 10;
    public static final int DATE_FORMAT_ERROR = 11;
    public static final int DATE_PARSE_ERROR = 12;
    public static final int SOCKET_TIMEOUT_ERROR = 13;
    public static final int CRYPT_ERROR = 14;
    public static final int EMAIL_ERROR = 15;
    public static final int DIRECTORY_CREATE_ERROR = 16;
    
    /*
     * JSON FORMAT ERROR (101 ~ 200)
     */
    public static final int INVALID_CUSTID = 101;
    public static final int INVALID_HEADER_FORMAT = 102;
    public static final int INVALID_BODY_FORMAT = 103;
    public static final int INVALID_PARAM_COUNT = 104;
    public static final int INVALID_PARAM_NAME = 105;
    public static final int INVALID_PARAM_VALUE = 106;
    public static final int INVALID_DATE_FORMAT = 107;
    /*
     * AUTH ERROR(201 ~ 300)
     */
    public static final int INVALID_USERID = 201;
    public static final int INVALID_PASSWD = 202;
    public static final int INVALID_USERINFO = 203;
    public static final int INVALID_ACCESS = 204;
    
    /*
     * Xroshot Error(301 ~ 400)
     */
    public static final int INVALID_XML_FORMAT = 300;
    public static final int MAS_SERVER_REQUEST_FAIL = 301;
    public static final int MAS_SERVER_RESPONSE_FAIL = 302;
    public static final int MAS_SERVER_INFO_REQUEST_FAIL = 303;
    public static final int MAS_SERVER_TIME_INFO_REQUEST_FAIL = 304;
    public static final int MAS_SERVER_LOGIN_REQUEST_FAIL = 305;
    public static final int MAS_SERVER_REPORT_FAIL = 306;
    public static final int MAS_SERVER_RES_SEND_MESSAGE_ALL_FAIL = 307;
    public static final int INVALID_CERT_PATH = 308;
    public static final int INVALID_CERT_LENGTH = 309;
    public static final int INVALID_DEST_COUNT = 310;
    public static final int MAS_SERVER_LOGOUT_REQUEST_FAIL = 305;
    
    /*
     * VDIS Error(401 ~ 500)
     */
    public static final int WEB_SERVICE_CLINET_CREATE_ERROR = 401;
    public static final int TRIP_FILE_SERVICE_CREATE_ERROR = 402;
    public static final int TRIP_FILE_SERVICE_REQUEST_ERROR = 403;
    public static final int WEB_SERVICE_RESPONSE_NULL_ERROR = 404;
    public static final int WEB_SERVICE_RESPONSE_RESULT_CODE_NULL_ERROR = 405;
    public static final int WEB_SERVICE_RESPONSE_RESULT_FAIL = 406;
    public static final int WEB_SERVIDE_RESPONSE_FILE_NULL_ERROR = 407;
    public static final int WEB_SERVIDE_RESPONSE_FILE_PATH_NULL_ERROR = 408;
    public static final int TRIP_CUST_ID_NULL_ERROR = 409;
    public static final int TRIP_SEQ_NULL_ERROR = 410;
    
    /*
     * Email(501 ~ 600)
     */
    public static final int MAX_FILE_COUNT_ERROR = 501;
    public static final int MAX_FILE_SIZE_ERROR = 502;
    
    public static final int CREATE_PDF_FILE_ERROR = 503;
    /*
     * SQL (7000 ~ 8000)
     */
    public static final int SQL_ERROR = 7000;
    public static final int SQL_KEY_VIOLATION_ERROR = 7001;
    public static final int SQL_UNIQUE_VIOLATION_ERROR = 23505;            // 유니크 제약 오류(중복된 키 값)
}

