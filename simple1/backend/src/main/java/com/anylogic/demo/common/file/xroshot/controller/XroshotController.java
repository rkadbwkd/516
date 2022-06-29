/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file.xroshot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.anylogic.demo.common.file.xroshot.common.*;
import com.anylogic.demo.common.file.xroshot.exception.XroshotException;
import com.anylogic.demo.common.util.PropUtil;
import com.anylogic.demo.common.file.xroshot.common.*;
import com.anylogic.demo.common.file.xroshot.constant.XroshotConstant;
import com.anylogic.demo.common.file.xroshot.exception.BatchProcessingException;
import com.anylogic.demo.common.file.xroshot.model.SdkSmsSendModel;
import com.anylogic.demo.common.file.xroshot.socket.SocketClient;
import com.anylogic.demo.common.file.xroshot.xml.XroshotXMLDocument;
import com.anylogic.demo.common.file.xroshot.xml.XroshotXMLDocument.XroshotXMLVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @class XroshotController.java
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
public class XroshotController {
    private static Logger logger = LoggerFactory.getLogger(XroshotController.class);

    private final String certString;

    public XroshotController(String certString)
    {
        this.certString = certString;

    }


    public boolean execute(XroshotXMLVO xroshotXMLVO, String spId, String spPass) throws BatchProcessingException
    {
        String logTitle = "******* Xroshot SMS Send Start *******";

        logger.info(LoggerMessage.getMessage(logTitle));

        /** 1. MAS 서버 정보 얻기 */
        ServiceProviderInfo spInfo = null;

        try
        {
            spInfo = getMasServerInfo();
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch(BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("MAS 서버 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        /** 2. 소켓 클라이언트 설정 */
        String hostName = "";
        int port = 0;
        String charSet = "";

        try
        {
            /** 개발 모드 확인
            if(!(commonProperties.getProperty("xroshot.dev.mode.yn").equals(XroshotConstant.DEV_MODE_Y))) {
                hostName = spInfo.getMasServerIp();
                port = spInfo.getMasServerPort();
            }
            else {
                hostName = commonProperties.getProperty("xroshot.dev.mas.server.ip");
                port = Integer.parseInt(commonProperties.getProperty("xroshot.dev.mas.server.port"));

                spInfo.setMasServerIp(hostName);
                spInfo.setMasServerPort(port);
            }
            */
            hostName = spInfo.getMasServerIp();
            port = spInfo.getMasServerPort();
            charSet = spInfo.getMasServerCharSet();
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("서버 IP/Port/CharSet 설정 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        if(XroshotUtil.isEmptyString(hostName) || port == 0 || XroshotUtil.isEmptyString(charSet))
        {
            throw new BatchProcessingException("Mas 서버 Ip/Port/charSet 정보가 없습니다.", ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL);
        }

        SocketClient.setServerInfo(spInfo);

        /** 3. Service Provider 일반 정보 설정 */

//        String spId = PropUtil.getPropValue("xroshot.spid");
//        String spPass = PropUtil.getPropValue("xroshot.sppw");
//        String endUserId = PropUtil.getPropValue("xroshot.spid");

//        String spId = XroshotConstant.SP_ID;
//        String spPass = XroshotConstant.SP_PASS;
//        String endUserId = XroshotConstant.SP_ENDUSER_ID;
        String clientVersion = XroshotConstant.PROTOCOL_VER;

        spInfo.setSpId(spId);
        spInfo.setSpPassword(spPass);
        spInfo.setEndUserId(spId);
        spInfo.setClientVersion(clientVersion);

        /*
         * 4. 서버 시간 동기화
         */
        Date masServerDate = null;

        try {
            masServerDate = getMasServerDate(spInfo);
        }
        catch(XroshotException e) {
            throw e;
        }
        catch (BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("MAS 서버 시간 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        spInfo.setMasServerDate(masServerDate);

        /*
         * 5. AuthTicket 생성
         */

        spInfo.setCert(this.certString);

        String authTicket = null;

        try {
            authTicket = createAuthTicket(spInfo);
        }
        catch(BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("AuthTicket 생성 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        spInfo.setAuthTicket(authTicket);

        /** 6. SMS 전송 */
        try
        {
            /** 6.1 로그인 */
            login(spInfo);
            /** 6.2 메세지 전송 */
            sendMessage(spInfo, xroshotXMLVO);
            /** 6.3 Report 수신 */
            waitReportRequest(spInfo);

            return true;
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("SMS 전송 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        finally {
            /** 6.4 로그 아웃 */
            try
            {
                logout(spInfo);
                logger.info(LoggerMessage.getMessage(logTitle, "Success logout!"));
            }
            catch (BatchProcessingException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new BatchProcessingException("로그아웃 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
            }
            finally
            {
                if(spInfo != null && spInfo.getLoginInfo() != null)
                {
                    Socket socket = spInfo.getLoginInfo().getSocket();
                    SocketClient.closeSocket(socket);
                }
            }

            logTitle = "******* Xroshot SMS Send End *******";
            logger.info(LoggerMessage.getMessage(logTitle, "Sms Send Complete"));
        }
    }

    public boolean execute(SdkSmsSendModel smsModel, String spId, String spPass) throws BatchProcessingException
    {
        String logTitle = "******* Xroshot SMS Send Start *******";

        logger.info(LoggerMessage.getMessage(logTitle));

        /** 1. MAS 서버 정보 얻기 */
        ServiceProviderInfo spInfo = null;

        try
        {
            spInfo = getMasServerInfo();
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch(BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("MAS 서버 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        /** 2. 소켓 클라이언트 설정 */
        String hostName = "";
        int port = 0;
        String charSet = "";

        try
        {
            /** 개발 모드 확인
            if(!(commonProperties.getProperty("xroshot.dev.mode.yn").equals(XroshotConstant.DEV_MODE_Y))) {
                hostName = spInfo.getMasServerIp();
                port = spInfo.getMasServerPort();
            }
            else {
                hostName = commonProperties.getProperty("xroshot.dev.mas.server.ip");
                port = Integer.parseInt(commonProperties.getProperty("xroshot.dev.mas.server.port"));

                spInfo.setMasServerIp(hostName);
                spInfo.setMasServerPort(port);
            }
            */
            hostName = spInfo.getMasServerIp();
            port = spInfo.getMasServerPort();
            charSet = spInfo.getMasServerCharSet();
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("서버 IP/Port/CharSet 설정 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        if(XroshotUtil.isEmptyString(hostName) || port == 0 || XroshotUtil.isEmptyString(charSet))
        {
            throw new BatchProcessingException("Mas 서버 Ip/Port/charSet 정보가 없습니다.", ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL);
        }

        SocketClient.setServerInfo(spInfo);

        /** 3. Service Provider 일반 정보 설정 d*/

//        String sdId = PropUtil.getPropValue("xroshot.spid");
//        String sdPass = PropUtil.getPropValue("xroshot.sppw");
//        String certfilePath = PropUtil.getPropValue("xroshot.certfile.path");

//        String spId = XroshotConstant.SP_ID;
//        String spPassword = XroshotConstant.SP_PASS;
        String endUserId = XroshotConstant.SP_ENDUSER_ID;
        String clientVersion = XroshotConstant.PROTOCOL_VER;

        spInfo.setSpId(spId);
        spInfo.setSpPassword(spPass);
        spInfo.setEndUserId(endUserId);
        spInfo.setClientVersion(clientVersion);

        /*
         * 4. 서버 시간 동기화
         */
        Date masServerDate = null;

        try {
            masServerDate = getMasServerDate(spInfo);
        }
        catch(XroshotException e) {
            throw e;
        }
        catch (BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("MAS 서버 시간 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        spInfo.setMasServerDate(masServerDate);

        /*
         * 5. AuthTicket 생성
         */

        spInfo.setCert(this.certString);

        String authTicket = null;

        try {
            authTicket = createAuthTicket(spInfo);
        }
        catch(BatchProcessingException e) {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("AuthTicket 생성 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        spInfo.setAuthTicket(authTicket);

        /** 6. SMS 전송 */
        try
        {
            /** 6.1 로그인 */
            login(spInfo);
            /** 6.2 메세지 전송 */
            sendMessage(spInfo, smsModel);
            /** 6.3 Report 수신 */
            waitReportRequest(spInfo);

            return true;
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("SMS 전송 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
        finally {
            /** 6.4 로그 아웃 */
            try
            {
                logout(spInfo);
                logger.info(LoggerMessage.getMessage(logTitle, "Success logout!"));
            }
            catch (BatchProcessingException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new BatchProcessingException("로그아웃 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
            }
            finally
            {
                if(spInfo != null && spInfo.getLoginInfo() != null)
                {
                    Socket socket = spInfo.getLoginInfo().getSocket();
                    SocketClient.closeSocket(socket);
                }
            }

            logTitle = "******* Xroshot SMS Send End *******";
            logger.info(LoggerMessage.getMessage(logTitle, "Sms Send Complete"));
        }
    }

    private ServiceProviderInfo getMasServerInfo() throws Exception {

        String masServerInfo = null;
//        String mcsInfoServerUrl = XroshotConstant.MAS_SERVER_INFO;
        String mcsInfoServerUrl = PropUtil.getInstance().getPropValue(XroshotConstant.DEFAULT_PROP_FILE,
                                                   XroshotConstant.XROSUOT_SERVER_INFO);

        logger.info(LoggerMessage.getMessage("MAS Info Server Url", mcsInfoServerUrl));

        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader rd = null;

        try
        {
            url = new URL(mcsInfoServerUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();

            line = rd.readLine();
            while(line != null)
            {
              response.append(line);
              line = rd.readLine();
            }

            logger.info(LoggerMessage.getMessage(response.toString()));

            masServerInfo = response.toString();
        }
        catch(IOException e)
        {
            throw new BatchProcessingException("MCS 서버 정보 요청 중 I/O 예외가 발생하였습니다.(Url : " + mcsInfoServerUrl + ")", ErrorCode.IO_ERROR, e);
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("MCS 서버 정보 요청 중 알 수 없는 예외가 발생하였습니다.(Url : " + mcsInfoServerUrl + ")", ErrorCode.UNKNOWN_ERROR, e);
        }
        finally
        {
            try
            {
                if(rd != null)
                {
                    rd.close();
                    rd = null;
                }
                if(is != null)
                {
                    is.close();
                    is = null;
                }
            }
            catch(IOException e)
            {
                throw new BatchProcessingException("스트림을 닫는 중 예외가 발생하였습니다.", ErrorCode.IO_ERROR, e);
            }
            if(connection != null)
            {
                connection.disconnect();
            }
        }

        if(XroshotUtil.isEmptyString(masServerInfo))
        {
            throw new BatchProcessingException("서버 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL);
        }

        Map<String, String> masServerInfoMap = null;

        try
        {
            masServerInfoMap = XroshotXMLDocument.parseMasServerInfo(masServerInfo);
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("MAS 서버 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        String masServerAddress = masServerInfoMap.get(XroshotConstant.ADDRESS_NODE);
        String masServerPort = masServerInfoMap.get(XroshotConstant.PORT_NODE);
        int serverPort = 0;

        try {
            serverPort = Integer.parseInt(masServerPort);
        }
        catch(NumberFormatException e)
        {
            throw new BatchProcessingException("MAS 서버 포트 값이 숫자가 아닙니다.", ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL, e);
        }

        ServiceProviderInfo spInfo = new ServiceProviderInfo();

        spInfo.setMasServerIp(masServerAddress);
//        spInfo.setMasServerIp(XroshotConstant.MAS_SERVER_ADDR);
        spInfo.setMasServerPort(serverPort);
        spInfo.setMasServerCharSet(XroshotConstant.XROSHOT_ENCODING);
        spInfo.setCryptCharSet(XroshotConstant.XROSHOT_CRYPTO_ENCODING);
        return spInfo;
    }

    private Date getMasServerDate(ServiceProviderInfo spInfo) throws BatchProcessingException {

        String spId = spInfo.getSpId();
        String requestServerTime = null;
        String responseServerTime = null;

        try
        {
            requestServerTime = XroshotXMLDocument.getMasServerTimeRequestXML(spId);
            responseServerTime = SocketClient.requestNormalCommand(requestServerTime);
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e) {
            throw new BatchProcessingException("서버 시간 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        if(XroshotUtil.isEmptyString(responseServerTime))
        {
            throw new BatchProcessingException("서버 시간 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL);
        }

        Map<String, String> masServerTimeInfoMap = null;

        try
        {
            masServerTimeInfoMap = XroshotXMLDocument.parseMasServerTimeInfo(responseServerTime);
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("MAS 서버 시간 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        String masServerTime = masServerTimeInfoMap.get(XroshotConstant.TIME_NODE);
        Locale locale = Locale.KOREA;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", locale);
        Date masServerDate = null;

        try
        {
            masServerDate = sdf.parse(masServerTime);
        }
        catch (ParseException e)
        {
            throw new BatchProcessingException("서버 시간 파싱 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
        }

        return masServerDate;
    }

    private String createAuthTicket(ServiceProviderInfo spInfo) throws BatchProcessingException {

        String certString = spInfo.getCert();
        Date date = spInfo.getMasServerDate();
        String cryptAuthTicket = "";

        /*
         * 1. Cert File Public Key 생성
         * KT가 전달한 Cert File에 저장된 스트링에서 랜덤하게 시작점을 정하여(시작점 : 0~Cert File크기에서 128을 뺀 값 중 선택) 128 byte를 읽습니다.(①)
         */
        int readBytes = 128;
        int min = 0;
        int max = 3970;
        int randomIndex = (int)(Math.random() * (max - min + 1)) + min;

        int certStringLength = certString.length();
        if(randomIndex + readBytes > certStringLength)
        {
            randomIndex = certStringLength - readBytes;
        }

        spInfo.setAuthKey(randomIndex);

        String publicKey = certString.substring(randomIndex, randomIndex + readBytes);
        spInfo.setPublicKey(publicKey);

        /*
         * 2. AuthTicket 원본 생성
         * SP ID, SP Password, End-User ID, 서버시간, One Time Secret Key를 조합하여 AuthTicket 원본을 만듭니다. (④)
         * (예: test_sp|test_pw|phs|20100127014706|OnetimeSecretKey)
         */
        String spId = spInfo.getSpId();
        String spPassword = spInfo.getSpPassword();
        String endUserId = spInfo.getEndUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String masServerTime = sdf.format(date);
        String oneTimeSecretKey = XroshotUtil.generatePassword();
        String authTicket = spId + "|" + spPassword + "|" + endUserId + "|" + masServerTime + "|" + oneTimeSecretKey;

        /*
         * 3. Public Key 생성
         * “①”에서 생성한 Cert File Public Key를 SHA1방식으로 Hashing하여 Public Key를 만듭니다. (②)
         *
         * 4. Public Key 가공
         * “②”에서 생성한 20byte와 0x36 값으로 채워진 64byte를 byte단위로 XOR 연산을 하여 다시 SHA1방식으로 Hashing합니다. (최종 Public Key는 16byte로 절사한 데이터를 사용합니다.) (③)
         */
        try
        {
            cryptAuthTicket = Crypt.encrypto2(authTicket.getBytes(spInfo.getCryptCharSet()), publicKey.getBytes(spInfo.getCryptCharSet()));
        }
        catch (Exception e)
        {
            throw new BatchProcessingException("AuthTicket 생성 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        logger.info(LoggerMessage.getMessage("OneTime Key : ", oneTimeSecretKey));
        spInfo.setOneTimeSecretKey(oneTimeSecretKey);


        return cryptAuthTicket;
    }

    private String encryptMsg(ServiceProviderInfo spInfo, String msg) throws BatchProcessingException {

        String encryptMsg = null;

        try
        {
            encryptMsg = Crypt.encrypto2(msg.getBytes(spInfo.getCryptCharSet()), spInfo.getOneTimeSecretKey().getBytes(spInfo.getCryptCharSet()));
        }
        catch (Exception e)
        {
            throw new BatchProcessingException("데이터 암호화 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        return encryptMsg;
    }

    private LoginInfo login(ServiceProviderInfo spInfo) throws BatchProcessingException {

        String spId = spInfo.getSpId();
        String endUserId = spInfo.getEndUserId();
        String authTicket = spInfo.getAuthTicket();
        int authKey = spInfo.getAuthKey();
        String version = spInfo.getClientVersion();

        logger.info("login");

        /** 1. Socket 생성 */
        Socket socket = null;

        try
        {
            socket = SocketClient.openSocket(spInfo.getMasServerIp(), spInfo.getMasServerPort());
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("소켓 open 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        String requestLogin = null;
        String responseLogin = null;

        try
        {
            requestLogin = XroshotXMLDocument.getLoginRequestXML(spId, endUserId, authTicket, authKey, version);
            responseLogin = SocketClient.requestConnectionCommand(socket, requestLogin);
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("로그인 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        if(StringUtils.isEmpty(responseLogin))
        {
            throw new BatchProcessingException("로그인 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL);
        }

        Map<String, Object> loginInfoMap = null;

        try {
            while(loginInfoMap == null) {
                if(XroshotXMLDocument.isLogin(responseLogin))
                {
                    loginInfoMap = XroshotXMLDocument.parseLoginInfo(responseLogin);
                    break;
                }
                else if(XroshotXMLDocument.isReport(responseLogin))
                {
//                    Map<String, String> reportInfoMap = XroshotXMLDocument.parseReportInfo(responseLogin);
//                    processReport(spInfo, reportInfoMap);
                    responseLogin = SocketClient.requestConnectionCommand(socket, requestLogin);

                    if(StringUtils.isEmpty(responseLogin))
                    {
                        throw new BatchProcessingException("로그인 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL);
                    }
                    continue;
                }
                else
                {
                    throw new BatchProcessingException("로그인 응답 정보를 파싱 할 수 없습니다.", ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL);
                }
            }
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("로그인 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR);
        }

        /** 2. LoginInfo 객체 생성 */
        LoginInfo loginInfo = new LoginInfo();
        spInfo.setLoginInfo(loginInfo);

        /** 2.1 Session Id */
        String sessionId = (String)loginInfoMap.get(XroshotConstant.SESSION_ID_NODE);
        loginInfo.setSessionId(sessionId);

        @SuppressWarnings("unchecked")
        Map<String, String> sendLimitPerSecondMap = (Map<String, String>)loginInfoMap.get(XroshotConstant.SEND_LIMIT_PER_SECOND_NODE);
        @SuppressWarnings("unchecked")
        Map<String, String> productStatusMap = (Map<String, String>)loginInfoMap.get(XroshotConstant.PRODUCT_STATUS_NODE);
        @SuppressWarnings("unchecked")
        Map<String, String> sendLimitPerMonthMap = (Map<String, String>)loginInfoMap.get(XroshotConstant.SEND_LIMIT_PER_MONTH_NODE);

        /** 2.2 ProductStatus */
        if(productStatusMap.containsKey(XroshotConstant.SMS_MESSAGE_TYPE))
        {
            String value = productStatusMap.get(XroshotConstant.SMS_MESSAGE_TYPE);

            if(StringUtils.isNotEmpty(value) && value.equals(XroshotConstant.MESSAGE_Y))
            {
                loginInfo.setMsgType_1_ProductStatus(XroshotConstant.MESSAGE_Y);
            }
            else
            {
                loginInfo.setMsgType_1_ProductStatus(XroshotConstant.MESSAGE_N);
            }
        }

        if(productStatusMap.containsKey(XroshotConstant.VMS_MESSAGE_TYPE))
        {
            String value = productStatusMap.get(XroshotConstant.VMS_MESSAGE_TYPE);
            if(StringUtils.isNotEmpty(value) && value.equals(XroshotConstant.MESSAGE_Y))
            {
                loginInfo.setMsgType_2_ProductStatus(XroshotConstant.MESSAGE_Y);
            }
            else
            {
                loginInfo.setMsgType_2_ProductStatus(XroshotConstant.MESSAGE_N);
            }
        }

        if(productStatusMap.containsKey(XroshotConstant.FMS_MESSAGE_TYPE))
        {
            String value = productStatusMap.get(XroshotConstant.FMS_MESSAGE_TYPE);
            if(StringUtils.isNotEmpty(value) && value.equals(XroshotConstant.MESSAGE_Y))
            {
                loginInfo.setMsgType_3_ProductStatus(XroshotConstant.MESSAGE_Y);
            }
            else
            {
                loginInfo.setMsgType_3_ProductStatus(XroshotConstant.MESSAGE_N);
            }
        }

        if(productStatusMap.containsKey(XroshotConstant.MMS_MESSAGE_TYPE))
        {
            String value = productStatusMap.get(XroshotConstant.MMS_MESSAGE_TYPE);
            if(StringUtils.isNotEmpty(value) && value.equals(XroshotConstant.MESSAGE_Y))
            {
                loginInfo.setMsgType_4_ProductStatus(XroshotConstant.MESSAGE_Y);
            }
            else
            {
                loginInfo.setMsgType_4_ProductStatus(XroshotConstant.MESSAGE_N);
            }
        }

        /** 2.3 SendLimitPerSecond */
        if(sendLimitPerSecondMap.containsKey(XroshotConstant.SMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerSecondMap.get(XroshotConstant.SMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_1_SendLimitPerSecond(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_1_SendLimitPerSecond(count);
            }
        }

        if(sendLimitPerSecondMap.containsKey(XroshotConstant.VMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerSecondMap.get(XroshotConstant.VMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_2_SendLimitPerSecond(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_2_SendLimitPerSecond(count);
            }
        }

        if(sendLimitPerSecondMap.containsKey(XroshotConstant.FMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerSecondMap.get(XroshotConstant.FMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_3_SendLimitPerSecond(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_3_SendLimitPerSecond(count);
            }
        }

        if(sendLimitPerSecondMap.containsKey(XroshotConstant.MMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerSecondMap.get(XroshotConstant.MMS_MESSAGE_TYPE);

            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_4_SendLimitPerSecond(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e) {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_4_SendLimitPerSecond(count);
            }
        }

        /** 2.3 SendLimitPerMonth */
        if(sendLimitPerMonthMap.containsKey(XroshotConstant.SMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerMonthMap.get(XroshotConstant.SMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_1_SendLimitPerMonth(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_1_SendLimitPerMonth(count);
            }
        }

        if(sendLimitPerMonthMap.containsKey(XroshotConstant.VMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerMonthMap.get(XroshotConstant.VMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_2_SendLimitPerMonth(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_2_SendLimitPerMonth(count);
            }
        }

        if(sendLimitPerMonthMap.containsKey(XroshotConstant.FMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerMonthMap.get(XroshotConstant.FMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_3_SendLimitPerMonth(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_3_SendLimitPerMonth(count);
            }
        }

        if(sendLimitPerMonthMap.containsKey(XroshotConstant.MMS_MESSAGE_TYPE))
        {
            String value = sendLimitPerMonthMap.get(XroshotConstant.MMS_MESSAGE_TYPE);
            if(StringUtils.isEmpty(value))
            {
                loginInfo.setMsgType_4_SendLimitPerMonth(-1);
            }
            else
            {
                int count = 0;
                try
                {
                    count = Integer.parseInt(value);
                }
                catch(NumberFormatException e)
                {
                    throw new BatchProcessingException("숫자 변환 중 예외가 발생하였습니다.", ErrorCode.TYPE_CONVERSION_ERROR, e);
                }

                loginInfo.setMsgType_4_SendLimitPerMonth(count);
            }
        }

        if(socket == null || socket.isClosed())
        {
            throw new BatchProcessingException("소켓을 생성하지 못했습니다.", ErrorCode.SOKCET_CONNECT_ERROR);
        }

        loginInfo.setSocket(socket);

        return loginInfo;
    }

    /**
     * <PRE>
     *  MethodName : sendMessage
     * </PRE>
     * @author : CBJ
     * @date   : 2014. 7. 9. 오후 7:34:23
     * @brief  :
     * @param spInfo: SMS 전송자 정보
     * @param smsModel: SMS 객체
     * @throws Exception
     */
    private void sendMessage(ServiceProviderInfo spInfo, SdkSmsSendModel smsModel) throws BatchProcessingException
    {
        //1. 데이터변환(smsModel-->smsModel)
        //2. 메세지 전송

        //1. 데이터변환(smsModel-->smsModel)
        XroshotXMLVO xroshotXMLVO = toXroshotXMLVO(spInfo, smsModel);
        //2. 메세지 전송
        sendMessage(spInfo, xroshotXMLVO);

    }

    private void sendMessage(ServiceProviderInfo spInfo, XroshotXMLVO xroshotXMLVO) throws BatchProcessingException
    {

        String scheduleType = xroshotXMLVO.getReserveType();
        String sendDate = xroshotXMLVO.getReserveTime();
        String sendDDate = xroshotXMLVO.getReserveDTime();
        String content = xroshotXMLVO.getContent();
        String callbackNumber = xroshotXMLVO.getCallbackNumber();
        List<String> receiveNumberList = xroshotXMLVO.getReceiveNumberList();

        //3. 예약발송시간 처리
        if(scheduleType.equals(XroshotConstant.RESERVATION_SCHEDULE_TYPE))
        {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            if(XroshotUtil.isValidDateFomat(sendDate, "yyyyMMddHHmmss"))
            {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                try
                {
                    sendDDate = XroshotUtil.addTime(sendDate);
                }
                catch(Exception e)
                {
                    throw new BatchProcessingException("예약 만료일 계산 중 예외가 발생하였습니다.", ErrorCode.DATE_PARSE_ERROR, e);
                }
            }
            else
            {
                throw new BatchProcessingException("메세지 전송 예약 날짜가 올바르지 않습니다.", ErrorCode.DATE_FORMAT_ERROR);
            }
        }

        //4. 데이터 암호화
        String encryptCallbackNumber = null;
        List<String> encryptReceiveNumberList = new ArrayList<String>();
        String encryptContent = null;

        try
        {
            if(StringUtils.isNotEmpty(callbackNumber))
            {
//                        if(CommonUtil.isEmptyString(callbackNumber) == false) {// PMD : Avoid unnecessary comparisons in boolean expressions
                encryptCallbackNumber = encryptMsg(spInfo, callbackNumber);
            }

            for(String receiveNumber : receiveNumberList)
            {
                String encryptReceiveNumber = encryptMsg(spInfo, receiveNumber);
                encryptReceiveNumberList.add(encryptReceiveNumber);
            }

            encryptContent = encryptMsg(spInfo, content);
        }
        catch (Exception e)
        {
            throw new BatchProcessingException("요청 정보 암호화 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
        String requestSmsSend= null;
//                ResSendMessageAllInfo messageAllInfo = null;
        String responseMsg = null;

        xroshotXMLVO.setReserveType(scheduleType);
        xroshotXMLVO.setReserveTime(sendDate);
        xroshotXMLVO.setReserveDTime(sendDDate);
        xroshotXMLVO.setContent(encryptContent);
        xroshotXMLVO.setCallbackNumber(encryptCallbackNumber);
        xroshotXMLVO.setReceiveNumberList(encryptReceiveNumberList);

        try
        {
            //5. 메세지 전송
            logger.info(LoggerMessage.getMessage("Sms Send", spInfo.getMasServerFormatDateTime()));
            requestSmsSend = XroshotXMLDocument.getSmsRequestXML(xroshotXMLVO);

            logger.info(LoggerMessage.getMessage("*** Sms Request ***", requestSmsSend));

            Socket socket = spInfo.getLoginInfo().getSocket();
            SocketClient.requestCommand(socket, requestSmsSend);

            //6. 메세지 전송 상태 Update

            //7. 응답 처리

            boolean response_status = false;

//            long timeOut = 10*1000;
//            long sendStartTime = System.currentTimeMillis();

            while(true)
            {
//                        if(System.currentTimeMillis() >= (sendStartTime+timeOut) )
//                        {
//                            throw new BatchProcessingException("타임아웃이 발생하였습니다. timeOut= "+timeOut, ErrorCode.UNKNOWN_ERROR);
//                        }

                if(response_status)
                {
                    break;
                }

                logger.info("sendMessage");
                responseMsg = SocketClient.responseCommand(socket);

                if(StringUtils.isEmpty(responseMsg))
                {
                    throw new BatchProcessingException("요청에 대한 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_RESPONSE_FAIL);
                }

                logger.info(LoggerMessage.getMessage("Response Sms Message", responseMsg));

                String[] responseMsgs = responseMsg.split("\0");

                for(String msg : responseMsgs)
                {
                    if(XroshotXMLDocument.isReport(msg))
                    {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info("Report Response");
                        Map<String, String> reportInfoMap = XroshotXMLDocument.parseReportInfo(msg);
                        String jobId = reportInfoMap.get(XroshotConstant.JOB_ID_NODE);

                        try
                        {
                            String resRport = XroshotXMLDocument.getResReportResponsetXML("0", jobId);
                            logger.info(LoggerMessage.getMessage("Request response Message : ", resRport));
                            SocketClient.requestCommand(spInfo.getLoginInfo().getSocket(), resRport);
                        }
                        catch(Exception e)
                        {
                            throw new BatchProcessingException("Report 요청 응답 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
                        }
//                                processReport(spInfo, reportInfoMap);
                    }
                    else if(XroshotXMLDocument.isResSendMessageAll(msg))
                    {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info(LoggerMessage.getMessage("ResSendAll Response: msg = ", msg));
//                                Map<String, Object> sendMessageInfoMap = XroshotXMLDocument.parseSendMessageInfo(msg);
//                                messageAllInfo = processSendMessageAll(smsModel, sendMessageInfoMap);
//                                smsModel.setSend_result(Long.parseLong((String)sendMessageInfoMap.get(XroshotConstant.RESULT_NODE)));
//                                messageAllInfo = processSendMessageAll(smsModel,sendMessageInfoMap);
                        response_status = true;
                    }
                    else if(XroshotXMLDocument.isResSendMessage(msg))
                    {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info(LoggerMessage.getMessage("ResSend Response: msg = ", msg));
                    }
                    else
                    {
                        logger.info(LoggerMessage.getMessage("Response: msg = ", msg));
                    }

                    //Thread.sleep(10);

                }
            }
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("SMS 전송 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
    }

    private XroshotXMLVO toXroshotXMLVO(ServiceProviderInfo spInfo, SdkSmsSendModel smsModel) throws BatchProcessingException
    {
        //1. 수신자목록 가져오기
        //2. 수신자정보 설정
        //3. 예약발송시간 처리
        //4. 데이터 암호화

        //1. 수신자목록 가져오기
        long destCount = smsModel.getDest_count();                // 수신자 목록 개수
        String destInfo = smsModel.getDest_info();                // 수신자 정보
        List<String> receiveNumberList = null;                    // 수신 번호 리스트
        receiveNumberList = getReceiveNumberList(destCount, destInfo);

        if(receiveNumberList == null || receiveNumberList.size() == 0)
        {
            throw new BatchProcessingException("수신자 정보가 없습니다.", ErrorCode.UNKNOWN_ERROR);
        }

        String customMessageID = String.valueOf("smcp00001");    // 메세지 아이디
        String scheduleType = XroshotConstant.IMMEDIATELY_SCHEDULE_TYPE;                // 발송시점구분
        String sendDate = null;                                    // 발송희망시간
        String sendDDate = null;                                // 발송만료시간
        String subject = "Coworekrs Infomation";                        // 제목
        String content = smsModel.getSms_msg();                    // 단문 메세지
        String callbackNumber = smsModel.getCallback();            // 회신 번호

        //3. 예약발송시간 처리
        if(scheduleType.equals(XroshotConstant.RESERVATION_SCHEDULE_TYPE))
        {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            if(XroshotUtil.isValidDateFomat(sendDate, "yyyyMMddHHmmss"))
            {    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                try
                {
                    sendDDate = XroshotUtil.addTime(sendDate);
                }
                catch(Exception e)
                {
                    throw new BatchProcessingException("예약 만료일 계산 중 예외가 발생하였습니다.", ErrorCode.DATE_PARSE_ERROR, e);
                }
            }
            else
            {
                throw new BatchProcessingException("메세지 전송 예약 날짜가 올바르지 않습니다.", ErrorCode.DATE_FORMAT_ERROR);
            }
        }

        //4. 데이터 암호화
        String encryptCallbackNumber = null;
        List<String> encryptReceiveNumberList = new ArrayList<String>();
        String encryptContent = null;

        try
        {
            if(StringUtils.isNotEmpty(callbackNumber))
            {
//                        if(CommonUtil.isEmptyString(callbackNumber) == false) {// PMD : Avoid unnecessary comparisons in boolean expressions
                encryptCallbackNumber = encryptMsg(spInfo, callbackNumber);
            }

            for(String receiveNumber : receiveNumberList)
            {
                String encryptReceiveNumber = encryptMsg(spInfo, receiveNumber);
                encryptReceiveNumberList.add(encryptReceiveNumber);
            }

            encryptContent = encryptMsg(spInfo, content);
        }
        catch (Exception e)
        {
            throw new BatchProcessingException("요청 정보 암호화 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        XroshotXMLVO xroshotXMLVO = new XroshotXMLVO();
        xroshotXMLVO.setCustomMessageID(customMessageID);
        xroshotXMLVO.setReserveType(scheduleType);
        xroshotXMLVO.setReserveTime(sendDate);
        xroshotXMLVO.setReserveDTime(sendDDate);
        xroshotXMLVO.setSubject(subject);
        xroshotXMLVO.setContent(encryptContent);
        xroshotXMLVO.setCallbackNumber(encryptCallbackNumber);
        xroshotXMLVO.setReceiveNumberList(encryptReceiveNumberList);

        return xroshotXMLVO;
    }

    /**
     * <PRE>
     *  MethodName : sendMessage
     * </PRE>
     * @author : CBJ
     * @date   : 2014. 7. 9. 오후 7:34:23
     * @brief  :
     * @param spInfo: SMS 전송자 정보
     * @param smsModel: SMS 객체
     * @throws Exception
     */
    private void sendMessage1(ServiceProviderInfo spInfo, SdkSmsSendModel smsModel) throws Exception
    {
        String logTitle = "sendMessage";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Socket socket = spInfo.getLoginInfo().getSocket();

        //1. 수신자정보 설정
        /*
         * 1. 수신자 정보 설정
         */
        String customMessageID = String.valueOf("smcp00001");    // 메세지 아이디
        String scheduleType = String.valueOf("0");                // 발송시점구분
        String sendDate = null;                                    // 발송희망시간
        String sendDDate = null;                                // 발송만료시간
        String subject = "Coworekrs Infomation";                        // 제목
        String content = smsModel.getSms_msg();                    // 단문 메세지
        String callbackNumber = smsModel.getCallback();            // 회신 번호
        long destCount = smsModel.getDest_count();                // 수신자 목록 개수
        String destInfo = smsModel.getDest_info();                // 수신자 정보
        List<String> receiveNumberList = null;                    // 수신 번호 리스트

        //2. 수신자목록 가져오기
        try {
            receiveNumberList = getReceiveNumberList(destCount, destInfo);
        }
        catch (BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }
        catch(Exception e) {
            logMsg = "수신자 정보 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(receiveNumberList == null || receiveNumberList.size() == 0) {
            logMsg = "수신자 정보가 없습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        //3. 데이터 유효성체크
        if(scheduleType.equals(XroshotConstant.RESERVATION_SCHEDULE_TYPE)){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            if(XroshotUtil.isValidDateFomat(sendDate, "yyyyMMddHHmmss")){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                try {
                    sendDDate = XroshotUtil.addTime(sendDate);
                }
                catch(Exception e) {
                    logMsg = "예약 만료일 계산 중 예외가 발생하였습니다.";
                    errorCode = ErrorCode.DATE_PARSE_ERROR;

                    logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

                    throw new BatchProcessingException(logMsg, errorCode);
                }
            }
            else {
                logMsg = "메세지 전송 예약 날짜가 올바르지 않습니다.";
                errorCode = ErrorCode.DATE_FORMAT_ERROR;

                logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                throw new BatchProcessingException(logMsg, errorCode);
            }
        }

        //4. 데이터 암호화
        String encryptCallbackNumber = null;
        List<String> encryptReceiveNumberList = new ArrayList<String>();
        String encryptContent = null;

        try {
            if(StringUtils.isNotEmpty(callbackNumber)) {
//                if(CommonUtil.isEmptyString(callbackNumber) == false) {// PMD : Avoid unnecessary comparisons in boolean expressions
                encryptCallbackNumber = encryptMsg(spInfo, callbackNumber);
            }

            for(String receiveNumber : receiveNumberList) {
                String encryptReceiveNumber = encryptMsg(spInfo, receiveNumber);
                encryptReceiveNumberList.add(encryptReceiveNumber);
            }

            encryptContent = encryptMsg(spInfo, content);
        }
        catch (Exception e) {
            logMsg = "요청 정보 암호화 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        //5. SMS 전송
        //5.1 메세지 전송
        //5.2. 메세지 전송 상태 Update
        //5.3. 응답 처리
        String requestSmsSend= null;
//        ResSendMessageAllInfo messageAllInfo = null;
        String responseMsg = null;

        try {
            logger.info(LoggerMessage.getMessage("Sms Send", spInfo.getMasServerFormatDateTime()));

            // 1. 메세지 전송
            requestSmsSend = XroshotXMLDocument.getSmsRequestXML(
                    customMessageID
                    , scheduleType
                    , sendDate
                    , sendDDate
                    , subject
                    , encryptContent
                    , encryptCallbackNumber
                    , encryptReceiveNumberList);

            logger.info(LoggerMessage.getMessage("*** Sms Request ***", "Request Sms Message", requestSmsSend));

            SocketClient.requestCommand(socket, requestSmsSend);

            // 2. 메세지 전송 상태 Update
//            smsService.updateSendStatusForSending(smsModel);

            boolean response_status = false;
            // 3. 응답 처리
            while(true) {
                if(response_status) {
                    break;
                }
                else {
                    logger.info("sendMessage");
                    responseMsg = SocketClient.responseCommand(socket);

                    if(StringUtils.isEmpty(responseMsg)){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logMsg = "요청에 대한 응답 정보를 받지 못했습니다.";
                        errorCode = ErrorCode.MAS_SERVER_RESPONSE_FAIL;

                        logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                        throw new BatchProcessingException(logMsg, errorCode);
                    }
                }

                logger.info(LoggerMessage.getMessage("*** Sms Response ***", "Response Sms Message", responseMsg));

                String[] responseMsgs = responseMsg.split("\0");

                for(String msg : responseMsgs) {
                    if(XroshotXMLDocument.isReport(msg)){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info("Report Response");
                        Map<String, String> reportInfoMap = XroshotXMLDocument.parseReportInfo(msg);
                        String jobId = reportInfoMap.get(XroshotConstant.JOB_ID_NODE);

                        try {
                            String resRport = XroshotXMLDocument.getResReportResponsetXML("0", jobId);
                            logger.info("Request response Message : "+resRport);
                            SocketClient.requestCommand(spInfo.getLoginInfo().getSocket(), resRport);
                        }

                        catch(Exception e) {
                            logMsg = "Report 요청 응답 중 알 수 없는 예외가 발생하였습니다.";
                            errorCode = ErrorCode.UNKNOWN_ERROR;

                            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));
                            throw new Exception();
                        }
//                        processReport(spInfo, reportInfoMap);
                    }
                    else if(XroshotXMLDocument.isResSendMessage(msg)){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info("ResSend Response");
                        continue;
                    }
                    else if(XroshotXMLDocument.isResSendMessageAll(msg)){    // == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
                        logger.info("ResSendAll Response");
//                        Map<String, Object> sendMessageInfoMap = XroshotXMLDocument.parseSendMessageInfo(msg);
//                        messageAllInfo = processSendMessageAll(smsModel, sendMessageInfoMap);
//                        smsModel.setSend_result(Long.parseLong((String)sendMessageInfoMap.get(XroshotConstant.RESULT_NODE)));
//                        messageAllInfo = processSendMessageAll(smsModel,sendMessageInfoMap);
                        response_status = true;
                    }
                }
            }
        }
        catch (BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.info(LoggerMessage.getMessage("Exception", responseMsg));

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }
//        catch(ServiceException e) {
//            logMsg = e.getMessage();
//            errorCode = e.getErrorCode();
//
//            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));
//
//            return;
//        }
        catch(Exception e) {
            logMsg = "SMS 전송 요청 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.info(LoggerMessage.getMessage("Exception", responseMsg));

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
    }

    private void waitReportRequest(ServiceProviderInfo spInfo) throws BatchProcessingException {

        Socket socket = spInfo.getLoginInfo().getSocket();
        String reportMsg = null;

        try
        {
            logger.info(LoggerMessage.getMessage("Sms Send", spInfo.getMasServerFormatDateTime()));
            logger.info("waitReportRequest");
            reportMsg = SocketClient.responseCommand(socket);

            if(StringUtils.isEmpty(reportMsg))
            {
                throw new BatchProcessingException("요청에 대한 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_RESPONSE_FAIL);
            }

            logger.info(LoggerMessage.getMessage("*** Sms Report ***", "Report Message", reportMsg));

            String[] reportMsgs = reportMsg.split("\0");

            for(String msg : reportMsgs)
            {
                if(XroshotXMLDocument.isReport(msg))
                {
                    Map<String, String> reportInfoMap = XroshotXMLDocument.parseReportInfo(msg);
//                    processReport(spInfo, reportInfoMap);
                    String jobId = reportInfoMap.get(XroshotConstant.JOB_ID_NODE);
                    try
                    {
                        String resRport = XroshotXMLDocument.getResReportResponsetXML("0", jobId);
                        logger.info("Request response Message : "+resRport);
                        SocketClient.requestCommand(spInfo.getLoginInfo().getSocket(), resRport);
                    }
                    catch(Exception e)
                    {
                        throw new BatchProcessingException("Report 요청 응답 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
                    }
                }
            }
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("Report 요청 대기 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
    }

/*
    private ResSendMessageAllInfo processSendMessageAll(SdkSmsSendModel smsModel, Map<String, Object> messageAllInfoMap)
            throws BatchProcessingException, ServiceException {
        String logTitle = "procesSendMessageAll";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        ResSendMessageAllInfo messageAllInfo = new ResSendMessageAllInfo();
        @SuppressWarnings("unchecked")
        Map<String, String> jobInfoMap = (Map<String, String>) messageAllInfoMap.get(XroshotConstant.JOB_ID_NODE);

        String result = (String)messageAllInfoMap.get(XroshotConstant.RESULT_NODE);
        String time = (String)messageAllInfoMap.get(XroshotConstant.TIME_NODE);
        String customMessageId = (String)messageAllInfoMap.get(XroshotConstant.CUSTOM_MESSAGE_ID_NODE);
        String count = (String)messageAllInfoMap.get(XroshotConstant.COUNT_NODE);
        String groupId = (String)messageAllInfoMap.get(XroshotConstant.GROUP_ID_NODE);

        messageAllInfo.setResult(result);
        messageAllInfo.setTime(time);
        messageAllInfo.setCustomMessageId(customMessageId);
        messageAllInfo.setCount(count);
        messageAllInfo.setGroupId(groupId);
        messageAllInfo.setJobInfoMap(jobInfoMap);

        Long sendResult = null;
        Long sendCount = null;

        try {
            sendResult = Long.parseLong(result);
        }
        catch(NumberFormatException e) {
            logMsg = "메세지 전송 결과 코드 타입 변환 중 예외가 발생하였습니다.(" + result + ")";
            errorCode = ErrorCode.TYPE_CONVERSION_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            sendResult = (long) -1;
        }

        sendCount = smsModel.getSend_count() - 1;

            smsModel.setSend_result(sendResult);
            smsModel.setSend_count(sendCount);
            smsModel.setSend_proc_time(messageAllInfo.getTime());

        return messageAllInfo;
    }
*/
    private void logout(ServiceProviderInfo spInfo) throws BatchProcessingException {

        Socket socket = spInfo.getLoginInfo().getSocket();
        String requestLogout = null;
        String responseLogout = null;

        logger.info("logout");

        try
        {
            requestLogout = XroshotXMLDocument.getLogoutRequestXML(null);
            responseLogout = SocketClient.requestConnectionCommand(socket, requestLogout);
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("로그아웃 요청 중 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }

        if(XroshotUtil.isEmptyString(responseLogout))
        {
            throw new BatchProcessingException("로그아웃 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL);
        }

        int debugIndex = 0;
        logger.info(LoggerMessage.getMessage("(" + debugIndex + ")*** Sms Logout Message ***", "Logout Response Message", responseLogout));

        String logoutResult = null;

        try
        {
            while(true)
            {
                if(debugIndex > 2)
                {
                    throw new BatchProcessingException("로그아웃 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_LOGOUT_REQUEST_FAIL);
                }

                String[] responseLogouts = responseLogout.split("\0");

                for(String msg : responseLogouts)
                {
                    if(XroshotXMLDocument.isLogout(msg))
                    {
                        logoutResult = XroshotXMLDocument.parseLogoutInfo(msg);
                        break;
                    }
                    else if(XroshotXMLDocument.isReport(msg))
                    {
                        Map<String, String> reportInfoMap = XroshotXMLDocument.parseReportInfo(msg);
//                        processReport(spInfo, reportInfoMap);
                        String jobId = reportInfoMap.get(XroshotConstant.JOB_ID_NODE);

                        try
                        {
                            String resRport = XroshotXMLDocument.getResReportResponsetXML("0", jobId);
                            logger.info("Request response Message : "+resRport);
                            SocketClient.requestCommand(spInfo.getLoginInfo().getSocket(), resRport);
                        }
                        catch(Exception e)
                        {
                            throw new BatchProcessingException("Report 요청 응답 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
                        }
                        continue;
                    }
                    else
                    {
                        continue;
                    }
                }

                if(logoutResult != null)
                {
                    break;
                }
                else
                {
                    responseLogout = SocketClient.responseCommand(socket);

                    if(XroshotUtil.isEmptyString(responseLogout))
                    {
                        throw new BatchProcessingException("로그아웃 응답 정보를 받지 못했습니다.", ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL);
                    }
                    debugIndex++;
                    logger.info(LoggerMessage.getMessage("(" + debugIndex + ")*** Sms Logout Message ***", "Logout Response Message", responseLogout));
                }
            }
        }
        catch(XroshotException e)
        {
            throw e;
        }
        catch (BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("로그아웃 요청 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
    }

    private List<String> getReceiveNumberList(long destCount, String destInfo) throws BatchProcessingException
    {
        List<String> receiveNumberList = new ArrayList<String>();
        try
        {
            List<Map<String, String>> destNumNameList = XroshotUtil.getDestNumberList(destInfo);

            if(destNumNameList == null || destNumNameList.size() == 0)
            {
                throw new BatchProcessingException("수신자 정보가 없습니다.",  ErrorCode.NULL_ERROR);
            }

            if(destNumNameList.size() != destCount)
            {
                throw new BatchProcessingException("수신자 정보 수와 목록 수가 일치하지 않습니다.",  ErrorCode.INVALID_DEST_COUNT);
            }

            for(Map<String, String> nameNumMap : destNumNameList)
            {
                for(Map.Entry<String, String> entry : nameNumMap.entrySet())
                        receiveNumberList.add(entry.getKey());
            }
        }
        catch(BatchProcessingException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new BatchProcessingException("수신자 정보 파싱 중 알 수 없는 예외가 발생하였습니다.", ErrorCode.UNKNOWN_ERROR, e);
        }
        return receiveNumberList;
    }
}

