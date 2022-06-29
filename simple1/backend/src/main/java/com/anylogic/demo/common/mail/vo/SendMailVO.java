/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.mail.vo;

import com.anylogic.demo.common.mail.MailConstants;
import com.anylogic.demo.common.util.PropUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMailVO {
    
    private Date   sendDate; //전송일시
    
    // 송신자 메일
    private String from = PropUtil.getInstance().getPropValue(MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            MailConstants.MAIL_SENDER_ID) ;
    private List<String> to;  //수신자 
    private String subject; //제목
    private MailConstants.MAIL_TYPE mailType; // 전송 Content 내용
    private Object content; // 전송 내용 객체
    private File[] atachfile; // 첨부파일
    private List<String> cc;  //참조 

    @SuppressWarnings("unused")
    private SendMailVO(){
        super();
    }
    
    public SendMailVO(MailConstants.MAIL_TYPE mailType){
        super();
        this.mailType = mailType;
    } 
    
    public MailConstants.MAIL_TYPE getMailType() {
        return mailType;
    }
 
    public File[] getAtachfile() {
        return atachfile;
    }

    public void setAtachfile(File[] atachfile) {
        this.atachfile = atachfile;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    } 
    
     
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addTo(String to){
        if(this.to == null) this.to = new ArrayList<String>();
        this.to.add(to);
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    } 

    
}


