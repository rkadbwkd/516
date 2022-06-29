/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.mail;

/**
 * <PRE>
 *  ClassName : MailConstants
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 25. 오전 11:58:30
 * @author  : jun
 * @brief   :
 */

public class MailConstants {

    public final static String CONTEXT_KEY = "data";
    public static final String DEFAULT_MAIL_TEMPLATE_PROPERTICE = "properties/mail.properties";

    public final static String MAIL_AUTH_ID = "mail.auth.id";
    public final static String MAIL_AUTH_PW = "mail.auth.pw";
    public final static String MAIL_AUTH_PASSWD = "mail.auth.passwd";
    public final static String MAIL_AUTH_HOST = "mail.auth.host";
    public final static String MAIL_AUTH_PORT = "mail.auth.port";
    public final static String MAIL_SENDER_ID = "mail.sender.id";

    //src="data:image
    // 비밀번호 찾기 메일
    private final static String CD_PASSWORD_FIND= "A01";
    private final static String PATH_PASSWORD_FIND = "mail.password";
    
    private final static String CD_ORDER_SEND= "A02";
    private final static String PATH_ORDER_SEND = "mail.order";
    
    private final static String CD_NEWS_SEND= "A03";
    private final static String PATH_NEWS_SEND = "mail.news";

    public static enum MAIL_TYPE{
        PASSWORD_MAIL(CD_PASSWORD_FIND){
            public String getSystemPath() {
                return PATH_PASSWORD_FIND;
            }
        },
        
        ORDER_MAIL(CD_ORDER_SEND){
            public String getSystemPath() {
                return PATH_ORDER_SEND;
            }
        },
        NEWS_MAIL(CD_NEWS_SEND){
            public String getSystemPath() {
                return PATH_NEWS_SEND;
            }
        };

        private String typeCd;
        private String systemPath;

        MAIL_TYPE(String typeCd){
            this.typeCd = typeCd;
        };

        public String getTypeCd() {
            return typeCd;
        }
        public String getSystemPath() {
            return systemPath;
        }
    }
}

