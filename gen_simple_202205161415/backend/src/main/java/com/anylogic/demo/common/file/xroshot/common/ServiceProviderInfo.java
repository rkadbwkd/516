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

import com.anylogic.demo.common.file.xroshot.model.SdkSmsSendModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
* @class ServiceProviderInfo.java
* @brief 
*
* @author TFM 개발팀
* @date 2014.3.19
* @version 1.0.0.1
* @Modification  Information 
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014.3.19     
**/
public class ServiceProviderInfo {
    private String masServerCharSet;
    private String masServerIp;
    private int masServerPort;
    private String spId;
    private String spPassword;
    private String endUserId;
    private int authKey;
    private String cert;
    private String authTicket;
    private String oneTimeSecretKey;
    private Date masServerDate;
    private String clientVersion;
    private String publicKey;
    private LoginInfo loginInfo;
    private SdkSmsSendModel sdkSmsSendModel;
    private String cryptCharSet;
    
    public String getMasServerCharSet() {
        return masServerCharSet;
    }
    public void setMasServerCharSet(String masServerCharSet) {
        this.masServerCharSet = masServerCharSet;
    }
    public String getMasServerIp() {
        return masServerIp;
    }
    public void setMasServerIp(String masServerIp) {
        this.masServerIp = masServerIp;
    }
    public int getMasServerPort() {
        return masServerPort;
    }
    public void setMasServerPort(int masServerPort) {
        this.masServerPort = masServerPort;
    }
    public String getSpId() {
        return spId;
    }
    public void setSpId(String spId) {
        this.spId = spId;
    }
    public String getSpPassword() {
        return spPassword;
    }
    public void setSpPassword(String spPassword) {
        this.spPassword = spPassword;
    }
    public String getEndUserId() {
        return endUserId;
    }
    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }
    public int getAuthKey() {
        return authKey;
    }
    public void setAuthKey(int authKey) {
        this.authKey = authKey;
    }
    public String getCert() {
        return cert;
    }
    public void setCert(String cert) {
        this.cert = cert;
    }
    public String getAuthTicket() {
        return authTicket;
    }
    public void setAuthTicket(String authTicket) {
        this.authTicket = authTicket;
    }
    public String getOneTimeSecretKey() {
        return oneTimeSecretKey;
    }
    public void setOneTimeSecretKey(String oneTimeSecretKey) {
        this.oneTimeSecretKey = oneTimeSecretKey;
    }
    public Date getMasServerDate() {
        return masServerDate;
    }
    public void setMasServerDate(Date masServerDate) {
        this.masServerDate = masServerDate;
    }
    public String getMasServerFormatDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
        String masServerFormatDateTime = simpleDateFormat.format(masServerDate);
        return masServerFormatDateTime;
    }
    public String getClientVersion() {
        return clientVersion;
    }
    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }
    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }
    public SdkSmsSendModel getSdkSmsSendModel() {
        return sdkSmsSendModel;
    }
    public void setSdkSmsSendModel(SdkSmsSendModel sdkSmsSendModel) {
        this.sdkSmsSendModel = sdkSmsSendModel;
    }
    public String getCryptCharSet() {
        return cryptCharSet;
    }
    public void setCryptCharSet(String cryptCharSet) {
        this.cryptCharSet = cryptCharSet;
    }
}

