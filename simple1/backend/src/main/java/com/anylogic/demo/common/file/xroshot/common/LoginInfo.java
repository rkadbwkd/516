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

import java.net.Socket;
/**
* @class LoginInfo.java
* @brief xroshot연동 로그인 정보 
*
* @author TFM 개발팀
* @date 2014.3.19
* @version 1.0.0.1
* @Modification  Information 
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014.3.19     
**/
public class LoginInfo {
    private String sessionId;
    private int msgType_1_SendLimitPerSecond;
    private int msgType_2_SendLimitPerSecond;
    private int msgType_3_SendLimitPerSecond;
    private int msgType_4_SendLimitPerSecond;
    private String msgType_1_ProductStatus;
    private String msgType_2_ProductStatus;
    private String msgType_3_ProductStatus;
    private String msgType_4_ProductStatus;
    private int msgType_1_SendLimitPerMonth;
    private int msgType_2_SendLimitPerMonth;
    private int msgType_3_SendLimitPerMonth;
    private int msgType_4_SendLimitPerMonth;
    private Socket socket;
    
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public int getMsgType_1_SendLimitPerSecond() {
        return msgType_1_SendLimitPerSecond;
    }
    public void setMsgType_1_SendLimitPerSecond(
            int msgType_1_SendLimitPerSecond) {
        this.msgType_1_SendLimitPerSecond = msgType_1_SendLimitPerSecond;
    }
    public int getMsgType_2_SendLimitPerSecond() {
        return msgType_2_SendLimitPerSecond;
    }
    public void setMsgType_2_SendLimitPerSecond(
            int msgType_2_SendLimitPerSecond) {
        this.msgType_2_SendLimitPerSecond = msgType_2_SendLimitPerSecond;
    }
    public int getMsgType_3_SendLimitPerSecond() {
        return msgType_3_SendLimitPerSecond;
    }
    public void setMsgType_3_SendLimitPerSecond(
            int msgType_3_SendLimitPerSecond) {
        this.msgType_3_SendLimitPerSecond = msgType_3_SendLimitPerSecond;
    }
    public int getMsgType_4_SendLimitPerSecond() {
        return msgType_4_SendLimitPerSecond;
    }
    public void setMsgType_4_SendLimitPerSecond(
            int msgType_4_SendLimitPerSecond) {
        this.msgType_4_SendLimitPerSecond = msgType_4_SendLimitPerSecond;
    }
    public String getMsgType_1_ProductStatus() {
        return msgType_1_ProductStatus;
    }
    public void setMsgType_1_ProductStatus(String msgType_1_ProductStatus) {
        this.msgType_1_ProductStatus = msgType_1_ProductStatus;
    }
    public String getMsgType_2_ProductStatus() {
        return msgType_2_ProductStatus;
    }
    public void setMsgType_2_ProductStatus(String msgType_2_ProductStatus) {
        this.msgType_2_ProductStatus = msgType_2_ProductStatus;
    }
    public String getMsgType_3_ProductStatus() {
        return msgType_3_ProductStatus;
    }
    public void setMsgType_3_ProductStatus(String msgType_3_ProductStatus) {
        this.msgType_3_ProductStatus = msgType_3_ProductStatus;
    }
    public String getMsgType_4_ProductStatus() {
        return msgType_4_ProductStatus;
    }
    public void setMsgType_4_ProductStatus(String msgType_4_ProductStatus) {
        this.msgType_4_ProductStatus = msgType_4_ProductStatus;
    }
    public int getMsgType_1_SendLimitPerMonth() {
        return msgType_1_SendLimitPerMonth;
    }
    public void setMsgType_1_SendLimitPerMonth(
            int msgType_1_SendLimitPerMonth) {
        this.msgType_1_SendLimitPerMonth = msgType_1_SendLimitPerMonth;
    }
    public int getMsgType_2_SendLimitPerMonth() {
        return msgType_2_SendLimitPerMonth;
    }
    public void setMsgType_2_SendLimitPerMonth(
            int msgType_2_SendLimitPerMonth) {
        this.msgType_2_SendLimitPerMonth = msgType_2_SendLimitPerMonth;
    }
    public int getMsgType_3_SendLimitPerMonth() {
        return msgType_3_SendLimitPerMonth;
    }
    public void setMsgType_3_SendLimitPerMonth(
            int msgType_3_SendLimitPerMonth) {
        this.msgType_3_SendLimitPerMonth = msgType_3_SendLimitPerMonth;
    }
    public int getMsgType_4_SendLimitPerMonth() {
        return msgType_4_SendLimitPerMonth;
    }
    public void setMsgType_4_SendLimitPerMonth(
            int msgType_4_SendLimitPerMonth) {
        this.msgType_4_SendLimitPerMonth = msgType_4_SendLimitPerMonth;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}

