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

import java.io.File;
import java.util.Date;
import java.util.List;  

/**
 * <PRE>
 *  ClassName : SendMail 
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 25. 오전 10:45:28
 * @author  : jun
 * @brief   : 
 */

public interface SendMail {
    // 메일 전송 기능
    public void sendMailForHtml(String from, List<String> to, List<String> cc,String subject, String content, File[] attachFile, Date sendDate); 
    // 첨부 파일 미존재
    public void sendMailForHtml(String from, List<String> to,List<String> cc, String subject, String content,  Date sendDate); 
    //  메일 전송 기능
    public void sendMailForText(String from, List<String> to, String subject, String content,  File[] attachFile, Date sendDate); 
    // 첨부 파일 미존재
    public void sendMailForText(String from, List<String> to, String subject, String content,  Date sendDate);
}

