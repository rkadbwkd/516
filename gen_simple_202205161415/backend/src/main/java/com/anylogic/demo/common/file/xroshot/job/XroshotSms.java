/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.file.xroshot.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import com.anylogic.demo.common.file.xroshot.common.ErrorCode;
import com.anylogic.demo.common.file.xroshot.common.LoggerMessage;
import com.anylogic.demo.common.file.xroshot.common.XroshotUtil;
import com.anylogic.demo.common.file.xroshot.constant.XroshotConstant;
import com.anylogic.demo.common.file.xroshot.controller.XroshotController;
import com.anylogic.demo.common.file.xroshot.exception.BatchProcessingException;
import com.anylogic.demo.common.file.xroshot.xml.XroshotXMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XroshotSms {

    private static Logger logger = LoggerFactory.getLogger(XroshotSms.class);

    XroshotController xc = null;
    private String spId;
    private String spPass;

    public XroshotSms(String spId, String spPass, String certPath) throws BatchProcessingException
    {
        this.spId = spId;
        this.spPass = spPass;
        // 인증파일 설정
        String certString = this.getCertfile(certPath);
        xc = new XroshotController(certString);
    }

    public boolean  sendSms(String message, String senderNumber,  List<String> receiverNumbers) throws BatchProcessingException
    {
        return this.sendSms(message, senderNumber, receiverNumbers, null, null, null);
    }

    public boolean  sendSms(String message, String senderNumber,  List<String> receiverNumbers, String subject) throws BatchProcessingException
    {
        return this.sendSms(message, senderNumber, receiverNumbers, subject, null, null);
    }

    public boolean  sendSms(String message, String senderNumber,  List<String> receiverNumbers, Date sendDate) throws BatchProcessingException
    {
        return this.sendSms(message, senderNumber, receiverNumbers, null, null, sendDate);
    }

    public boolean  sendSms(String message, String senderNumber,  List<String> receiverNumbers, String subject, String messageId) throws BatchProcessingException
    {
        return this.sendSms(message, senderNumber, receiverNumbers, subject, messageId, null);
    }

    public boolean sendSms(String message, String senderNumber, List<String> receiverNumbers, String subject, String messageId, Date sendDate) throws BatchProcessingException
    {
        //TODO
        //NULL값에 디폴트 처리 및 예외처리 후 작업
        
        // 1.SMS 전송 필수 정보체크 및 예외처리
        if (message == null)
        {
            //TODO : ErrorCode 추후정의
            throw new BatchProcessingException("SMS 전송 내용이 누락되었습니다.", ErrorCode.NULL_ERROR);
        }
        if (senderNumber == null) 
        {
            //TODO : ErrorCode 추후정의
            throw new BatchProcessingException("SMS 발송번호가 누락되었습니다.", ErrorCode.NULL_ERROR);
        }
        if (receiverNumbers == null)
        {
            //TODO : ErrorCode 추후정의
            throw new BatchProcessingException("SMS 수신번호가 누락되었습니다.", ErrorCode.NULL_ERROR);
        }
        
        // 2.Default 값 설정
        if (subject == null)
        {
            subject = XroshotConstant.DEFAULT_SUBJECT;
        }
        if (messageId == null)
        {
            messageId = XroshotConstant.DEFAULT_MSG_ID;
        }
        
        // 3.SMS 전송
//        SdkSmsSendModel smsModel = new SdkSmsSendModel();
        XroshotXMLDocument.XroshotXMLVO xroshotXMLVO = new XroshotXMLDocument.XroshotXMLVO();
        
        // 3-1.메세지ID 설정
        xroshotXMLVO.setCustomMessageID(messageId);
        // 3-2.발송타입 설정
        xroshotXMLVO.setReserveType(XroshotConstant.IMMEDIATELY_SCHEDULE_TYPE);
        // 3-3.예약발송시간 설정
        if (sendDate != null) {
            xroshotXMLVO.setReserveTime(sendDate.toString());            
        }
        // 3-4.메세지제목 설정
        xroshotXMLVO.setSubject(subject);
        // 3-5.메세지내용 설정
        xroshotXMLVO.setContent(message);
        // 3-6.회신번호 설정
        xroshotXMLVO.setCallbackNumber(senderNumber);
        // 3-7.수신번호리스트 설정
        xroshotXMLVO.setReceiveNumberList(receiverNumbers);
                
        return xc.execute(xroshotXMLVO, spId, spPass);
        
    }

    public String getCertfile(String certPath) throws BatchProcessingException {
        String logTitle = "******* Get cert file *******";
        logger.info(LoggerMessage.getMessage(logTitle));
        logTitle = "Get cert file";

        /** Cert File 읽기 */
//        String certFilePath = XroshotConstant.CERT_FILE_PATH;
        String certFilePath = certPath;

        if(XroshotUtil.isEmptyString(certFilePath))
        {
            throw new BatchProcessingException("Cert 파일 경로가 설정되어 있지 않습니다.", ErrorCode.INVALID_CERT_PATH);
        }
        try
        {
            certFilePath = URLDecoder.decode(certFilePath, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new BatchProcessingException("Cert 파일 경로를 url decoding 중 예외가 발생하였습니다.", ErrorCode.ENCODING_ERROR, e);
        }

        File file = new File(certFilePath);
        
        if(!(file.isFile())) {
            throw new BatchProcessingException("Cert 파일이 해당 경로에 존재하지 않습니다.(" + certFilePath + ")", ErrorCode.INVALID_CERT_PATH);
        }

        String certString = null;
        certString = getCertString(file);

        if(XroshotUtil.isEmptyString(certString) || !(certString.length() > 127))
        {
            throw new BatchProcessingException("Cert 값이 유효하지 않습니다.", ErrorCode.INVALID_CERT_LENGTH);
        }

        logTitle = "******* Get cert file Success *******";
        logger.info(LoggerMessage.getMessage(logTitle));

        return certString;
    }

    private String getCertString(File file) throws BatchProcessingException {
        BufferedReader br = null;
        String certInfo = null;

        try
        {
            br = new BufferedReader(new FileReader(file));
            certInfo = br.readLine();
        }
        catch(IOException e)
        {
            throw new BatchProcessingException("Cert 파일을 읽는 중 I/O 예외가 발생하였습니다.", ErrorCode.IO_ERROR, e);
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("Cert 파일을 읽는 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
        finally
        {
            try
            {
                if(br != null)
                {
                    br.close();
                    br = null;
                }
            }
            catch(IOException e)
            {
                throw new BatchProcessingException("스트림을 닫는 중 I/O 예외가 발생하였습니다.", ErrorCode.IO_ERROR, e);
            }
        }
        return certInfo;
    }

    public long getDestcount(String receiverNumbers) {

        int cnt = 0;
        for(int i=0;i<receiverNumbers.length();i++)
        {
            if(receiverNumbers.charAt(i)=='^')
                cnt++;
        }
        return (long)cnt;
    }
}


