/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

/**
 * <PRE>
 *  Project : BCC.admin-api
 *  Package : com.anylogic.demo.common.mail.impl
 * </PRE>
 * @file   : GMailSender.java
 * @date   : 2015. 6. 25. 오후 3:06:44
 * @author : jun
 * @brief  :
 *  변경이력    :
 *        이름     : 일자          : 근거자료   : 변경내용
 *       ------------------------------------
 *        jun  : 2015. 6. 25.       :            : 신규 개발.
 */
package com.anylogic.demo.common.mail.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.anylogic.demo.common.exception.AnyXException;
import com.anylogic.demo.common.mail.MailConstants;
import com.anylogic.demo.common.mail.SendMail;
import com.anylogic.demo.common.util.PropUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * <PRE>
 *  ClassName : GMailSender
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 25. 오후 3:06:44
 * @author  : jun
 * @brief   :
 */
public class GMailSender implements SendMail {

    public static final String DEFAULT_TEANSPORT_PROTOCOLC = "smtp";
    public static String DEFAULT_SMTP_SOCKET_FACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
    public static String DEFAULT_SMTP_HOST = PropUtil.getInstance().getPropValue(MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            MailConstants.MAIL_AUTH_HOST);
    public static int DEFAULT_SMTP_PORT = Integer.parseInt(PropUtil.getInstance().getPropValue(MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            MailConstants.MAIL_AUTH_PORT));

    private static String _authId = PropUtil.getInstance().getPropValue(MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            MailConstants.MAIL_AUTH_ID);
    
    private static String _authPw = PropUtil.getInstance().getPropValue(MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            MailConstants.MAIL_AUTH_PW);

    /*private static String _authPasswd = PropUtil.getInstance().getPropValue(com.anylogic.iot.base.mail.MailConstants.DEFAULT_MAIL_TEMPLATE_PROPERTICE,
            com.anylogic.iot.base.mail.MailConstants.MAIL_AUTH_PASSWD);*/

    private Properties props = null;

    public GMailSender() {
        this(DEFAULT_SMTP_HOST, DEFAULT_SMTP_PORT,DEFAULT_SMTP_SOCKET_FACTORY_CLASS );
    }

    public GMailSender(String host, int port, String socketFactoryClassName) {
        props = new Properties();

        //default Properties Settting
        /*props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", String.valueOf(port));
        props.put("mail.smtp.socketFactory.class", socketFactoryClassName);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.debug", "true");*/
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
      //  props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.auth", "true");
        
        props.put("mail.smtp.socketFactory.port", String.valueOf(port));
        props.put("mail.smtp.socketFactory.class", socketFactoryClassName);
        
        
       // props.put("mail.smtp.socketFactory.port", port);
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        //props.put("mail.smtp.ssl.enable", "true");
        //props.put("mail.smtp.ssl.trust", host);
    }

    @Override
    public void sendMailForHtml(String from, List<String> to, List<String> cc, String subject,
            String content, File[] attachFile, Date sendDate) {
        try {
            Message message = createMessage(from, null, to, cc,subject, content, attachFile );

            message.setSubject(subject);
            message.setSentDate(sendDate);

            message.setHeader("Content-Type", "text/html");
            if(attachFile == null){
                MimeBodyPart contextPart = new MimeBodyPart();
                contextPart.setContent(content , "text/html;charset=UTF-8" );

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(contextPart);

                message.setContent(multipart);
            }else {
                Object obj = message.getContent();

                if(obj != null && obj instanceof Multipart) {
                    MimeBodyPart contextPart = new MimeBodyPart();
                    contextPart.setText(content);
                    contextPart.setHeader("Content-Type", "text/html;charset=utf-8");

                    Multipart mPart = (Multipart)obj;
                    mPart.addBodyPart(contextPart);
                    
                    message.setContent(mPart);
                    
                }else {
                    message.setContent(content, "text/html;charset=utf-8");
                }
            }

            Transport.send(message);
        } catch (Exception e) {
            throw new AnyXException(e.getMessage());
        }
    }


    @Override
    public void sendMailForText(String from, List<String> to, String subject,
            String content, File[] attachFile, Date sendDate) {
    }

    @Override
    public void sendMailForText(String from, List<String> to, String subject,
            String content, Date sendDate) {
    }

    /* (non-Javadoc)
     * @see com.anylogic.demo.common.mail.SendMail#sendMailForHtml(java.lang.String, java.util.List, java.lang.String, java.lang.String, java.util.Date)
     */
    @Override
    public void sendMailForHtml(String from, List<String> to,List<String> cc, String subject, String content, Date sendDate) {

        try {
            Message message = createMessage(from, null, to, cc,subject, content, null );

            message.setSubject(subject);
            message.setSentDate(sendDate);

            message.setHeader("Content-Type", "text/html");
            MimeBodyPart contextPart = new MimeBodyPart();
            contextPart.setContent(content , "text/html;charset=UTF-8" );

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(contextPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            throw new AnyXException(e.getMessage());
        }

    }


    /**
     * <PRE>
     *  MethodName : createMessage
     * </PRE>
     * @author : jun
     * @date   : 2015. 6. 25. 오후 3:30:23
     * @param  :
     * @return : Message
     * @brief  : Message 생성
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    private Message createMessage(String from, String fromName, List<String> to, List<String> cc,String subject,
                                                String content, File[] attachFiles) throws UnsupportedEncodingException, MessagingException {
//_authPw
        //javax.mail.Authenticator auth = new GmailSmtpAuthenticator(_authId, decryption());//메일 전송 PW 암호화&복호화
        javax.mail.Authenticator auth = new GmailSmtpAuthenticator(_authId, _authPw);
        Session mailSession = Session.getInstance(props, auth); // session 생성
        mailSession.setDebug(true);

        //Message 생성
        Message msg = new MimeMessage(mailSession);

        if(fromName != null){
            msg.setFrom(new InternetAddress(from, MimeUtility.encodeText(fromName,"UTF-8","B")));
        }else {
            msg.setFrom(new InternetAddress(from));
        }

        InternetAddress[] toAddr = null;

        if(to != null) {
            toAddr = new InternetAddress[to.size()];
            int i = 0;
            for(String addr : to){
                toAddr[i] = new InternetAddress(addr);
                i++;
            }
        }
          // add bcc (숨은참조)

        if (cc != null) {

            InternetAddress[] bccAddr = new InternetAddress[cc.size()];

            for (int i = 0; i < cc.size(); i++) {

                bccAddr[i] = new InternetAddress(cc.get(i));

            }
            msg.setRecipients(Message.RecipientType.BCC, bccAddr);

        }

        msg.setRecipients(Message.RecipientType.TO, toAddr);

        // 첨부 파일이 있을때
        if(attachFiles != null){
            Multipart mult = new MimeMultipart();

            for(File attachFile : attachFiles) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();

                mimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(attachFile)));
                mimeBodyPart.setFileName(attachFile.getName());

                mult.addBodyPart(mimeBodyPart);
            }

            msg.setContent(mult);
        }

        return msg;
    }

     public String decryption(){

            StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
            decryptor.setPassword("encryptkey");            
            
            String res = decryptor.decrypt("zNvkh91BGNo++PFRJbJ3YtKvJ3Wh8EtZ");
            return res;


        }
     
    /**
     * <PRE>
     *  ClassName : GmailSmtpAuthenticator GMailSender
     * </PRE>
     * @version : 1.0
     * @date    : 2015. 6. 25. 오후 3:31:06
     * @author  : jun
     * @brief   :
     */
    private static class GmailSmtpAuthenticator extends javax.mail.Authenticator {
        private String id;
        private String passwd;

        public GmailSmtpAuthenticator(String id, String pwd) {
            this.id = id;
            this.passwd = pwd;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(id, passwd);
        }
    }

}


