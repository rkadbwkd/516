/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file.xroshot.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anylogic.demo.common.file.xroshot.common.LoggerMessage;
import com.anylogic.demo.common.file.xroshot.constant.XroshotConstant;
import com.anylogic.demo.common.file.xroshot.exception.BatchProcessingException;
import com.anylogic.demo.common.file.xroshot.exception.XroshotException;
import com.anylogic.demo.common.file.xroshot.common.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
* @class XroshotXMLDocument.java
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
public class XroshotXMLDocument extends BaseXMLDocument
{
    private static Logger logger = LoggerFactory.getLogger(XroshotXMLDocument.class);

    public static class XroshotXMLVO
    {
        private String customMessageID;
        private String reserveType;
        private String reserveTime;
        private String reserveDTime;
        private String subject;
        private String content;
        private String callbackNumber;
        private List<String> receiveNumberList;
        /**
         * @return the customMessageID
         */
        public String getCustomMessageID() {
            return customMessageID;
        }
        /**
         * @param customMessageID the customMessageID to set
         */
        public void setCustomMessageID(String customMessageID) {
            this.customMessageID = customMessageID;
        }
        /**
         * @return the reserveType
         */
        public String getReserveType() {
            return reserveType;
        }
        /**
         * @param reserveType the reserveType to set
         */
        public void setReserveType(String reserveType) {
            this.reserveType = reserveType;
        }
        /**
         * @return the reserveTime
         */
        public String getReserveTime() {
            return reserveTime;
        }
        /**
         * @param reserveTime the reserveTime to set
         */
        public void setReserveTime(String reserveTime) {
            this.reserveTime = reserveTime;
        }
        /**
         * @return the reserveDTime
         */
        public String getReserveDTime() {
            return reserveDTime;
        }
        /**
         * @param reserveDTime the reserveDTime to set
         */
        public void setReserveDTime(String reserveDTime) {
            this.reserveDTime = reserveDTime;
        }
        /**
         * @return the subject
         */
        public String getSubject() {
            return subject;
        }
        /**
         * @param subject the subject to set
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }
        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }
        /**
         * @param content the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }
        /**
         * @return the callbackNumber
         */
        public String getCallbackNumber() {
            return callbackNumber;
        }
        /**
         * @param callbackNumber the callbackNumber to set
         */
        public void setCallbackNumber(String callbackNumber) {
            this.callbackNumber = callbackNumber;
        }
        /**
         * @return the receiveNumberList
         */
        public List<String> getReceiveNumberList() {
            return receiveNumberList;
        }
        /**
         * @param receiveNumberList the receiveNumberList to set
         */
        public void setReceiveNumberList(List<String> receiveNumberList) {
            this.receiveNumberList = receiveNumberList;
        }
    }

//    public static void setCommonProperties(Properties properties) {
//        BaseXMLDocument.setCommonProperties(properties);
//    }

    public static Map<String, String> parseMasServerInfo(String masServerInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseMasServerInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Map<String, String> masServerInfoMap = null;

        Document doc = null;

        if(StringUtils.isEmpty(masServerInfo)){    // if(CommonUtil.isEmptyString(masServerInfo)) { // PMD : Avoid unnecessary comparisons in boolean expressions
            return null;
        }

        try {
            doc = getXmlDocument(masServerInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

//        if(!(rootNodeName.equals("RCP"))
//                || CommonUtil.isEmptyString(method)
//                || !(method.equals("ressvr_res"))) {  // PMD : Avoid unnecessary comparisons in boolean expressions
        if(!(rootNodeName.equals("RCP"))
                || StringUtils.isEmpty(method)
                || !(method.equals("ressvr_res"))) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        Node resourceNode = null;

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String result = getNodeTextValue(node);
                        if(result.equals("0")) {
                            continue;
                        }
                        else {
                            logMsg = "Mas 서버 정보를 얻는데 실패하였습니다.(" + result + ")";
                            errorCode = ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL;

                            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                            throw new XroshotException(logMsg, errorCode, result);
                        }
                    }
                    else if(node.getNodeName().equals(XroshotConstant.RESOURCE_NODE)) {
                        resourceNode = node;
                        continue;
                    }
                }
            }
        }
        catch(XroshotException e) {
            throw e;
        }
        catch(Exception e) {
            logMsg = "Mas 서버 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(resourceNode == null) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Resource node is null)";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        NodeList resourceChildNodeList = resourceNode.getChildNodes();
        masServerInfoMap = new HashMap<String, String>();

        try {
            for(int i=0; i<resourceChildNodeList.getLength(); i++) {
                Node node = resourceChildNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.ADDRESS_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String address = getNodeTextValue(node);
                        masServerInfoMap.put(XroshotConstant.ADDRESS_NODE, address);
                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.PORT_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String port = getNodeTextValue(node);
                        masServerInfoMap.put(XroshotConstant.PORT_NODE, port);
                        continue;
                    }
                }
            }
        }
        catch(Exception e) {
            errorCode = ErrorCode.UNKNOWN_ERROR;
            logMsg = "ServerInfo Map 생성 중 알 수 없는 예외가 발생하였습니다.";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(!(masServerInfoMap.containsKey(XroshotConstant.ADDRESS_NODE))
                || !(masServerInfoMap.containsKey(XroshotConstant.PORT_NODE))) {  // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "MAS 서버 응답 포맷이 잘 못 되었습니다." ;
            errorCode = ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String masServerAddress = masServerInfoMap.get(XroshotConstant.ADDRESS_NODE);
        String masServerPort = masServerInfoMap.get(XroshotConstant.PORT_NODE);

        if(StringUtils.isEmpty(masServerAddress) || StringUtils.isEmpty(masServerPort)) {    //    if(CommonUtil.isEmptyString(masServerAddress)|| CommonUtil.isEmptyString(masServerPort)) {   // PMD
            logMsg = "Mas 서버 Ip/Port 정보가 없습니다.";
            errorCode = ErrorCode.MAS_SERVER_INFO_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return masServerInfoMap;
    }

    public static String getMasServerTimeRequestXML(String spId) throws BatchProcessingException {
        String logTitle = "getMasServerTimeRequestXML";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(StringUtils.isEmpty(spId)) {     // if(CommonUtil.isEmptyString(spId)) { Avoid unnecessary comparisons in boolean expressions
            logMsg = "spId 정보가 없습니다.";
            errorCode = ErrorCode.NULL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='req_auth'>");
        sb.append("<ServiceProviderID>" + spId + "</ServiceProviderID>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }

    public static Map<String, String> parseMasServerTimeInfo(String masServerTimeInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseMasServerTimeInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Map<String, String> masServerTimeInfoMap = null;

        Document doc = null;

        if(StringUtils.isEmpty(masServerTimeInfo)){    // if(CommonUtil.isEmptyString(masServerTimeInfo)) { // PMD : Avoid unnecessary comparisons in boolean expressions
            return null;
        }

        try {
            doc = getXmlDocument(masServerTimeInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_auth")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_auth") == false) { // PMD : Avoid unnecessary comparisons in boolean expressions
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        masServerTimeInfoMap = new HashMap<String, String>();

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {
                        String result = getNodeTextValue(node);
                        if(result.equals("0")) {
                            continue;
                        }
                        else {
                            logMsg = "Mas 서버 시간 정보를 얻는데 실패하였습니다.(" + result + ")";
                            errorCode = ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL;

                            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                            throw new XroshotException(logMsg, errorCode, result);
                        }
                    }
                    else if(node.getNodeName().equals(XroshotConstant.TIME_NODE)) {
                        String time = getNodeTextValue(node);
                        masServerTimeInfoMap.put(XroshotConstant.TIME_NODE, time);
                        break;
                    }
                }
            }
        }
        catch(XroshotException e) {
            throw e;
        }
        catch(Exception e) {
            logMsg = "Mas 서버 시간 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(!masServerTimeInfoMap.containsKey(XroshotConstant.TIME_NODE)) {      // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "MAS 서버 시간 응답 포맷이 잘 못 되었습니다." ;
            errorCode = ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String masServerTime = masServerTimeInfoMap.get(XroshotConstant.TIME_NODE);

        if(StringUtils.isEmpty(masServerTime)){    //if(CommonUtil.isEmptyString(masServerTime) == true) { // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "서버 시간 요청 정보가 없습니다.";
            errorCode = ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return masServerTimeInfoMap;
    }

    public static String getLoginRequestXML(
            String spId
            , String endUserId
            , String authTicket
            , int authKey
            , String version) throws BatchProcessingException {
        String logTitle = "getLoginRequestXML";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if( StringUtils.isEmpty(spId)){    //if(CommonUtil.isEmptyString(spId) == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "요청 정보가 없습니다.";
            errorCode = ErrorCode.NULL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='req_regist'>");
        sb.append("<ServiceProviderID>" + spId + "</ServiceProviderID>");
        sb.append("<EndUserID>" + endUserId + "</EndUserID>");
        sb.append("<AuthTicket>" + authTicket + "</AuthTicket>");
        sb.append("<AuthKey>" + authKey + "</AuthKey>");
        sb.append("<Version>" + version + "</Version>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }

    public static Map<String, Object> parseLoginInfo(String loginInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseLoginInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Map<String, Object> loginInfoMap = null;

        Document doc = null;

        if(StringUtils.isEmpty(loginInfo)){    //if(CommonUtil.isEmptyString(loginInfo) == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            return null;
        }

        try {
            doc = getXmlDocument(loginInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_regist")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_regist") == false) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        loginInfoMap = new HashMap<String, Object>();
        Map<String, String> sendLimitPerSecondMap = new HashMap<String, String>();
        Map<String, String> productStatusMap = new HashMap<String, String>();
        Map<String, String> sendLimitPerMonthMap = new HashMap<String, String>();
        loginInfoMap.put(XroshotConstant.SEND_LIMIT_PER_SECOND_NODE, sendLimitPerSecondMap);
        loginInfoMap.put(XroshotConstant.PRODUCT_STATUS_NODE, productStatusMap);
        loginInfoMap.put(XroshotConstant.SEND_LIMIT_PER_MONTH_NODE, sendLimitPerMonthMap);

        String msgTypeAttrName = "msgType";

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {
                        String result = getNodeTextValue(node);
                        if(result.equals("0")) {
                            continue;
                        }
                        else {
                            logMsg = "로그인에 실패하였습니다.(" + result + ")";
                            errorCode = ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL;

                            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                            throw new XroshotException(logMsg, errorCode, result);
                        }
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SESSION_ID_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String nodeValue = getNodeTextValue(node);
                        loginInfoMap.put(XroshotConstant.SESSION_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SEND_LIMIT_PER_SECOND_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String nodeValue = getNodeTextValue(node);
                        String attrValue = ((Element)node).getAttribute(msgTypeAttrName);
                        sendLimitPerSecondMap.put(attrValue, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.PRODUCT_STATUS_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String nodeValue = getNodeTextValue(node);
                        String attrValue = ((Element)node).getAttribute(msgTypeAttrName);
                        productStatusMap.put(attrValue, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SEND_LIMIT_PER_MONTH_NODE)) {  // PMD : Avoid unnecessary comparisons in boolean expressions
                        String nodeValue = getNodeTextValue(node);
                        String attrValue = ((Element)node).getAttribute(msgTypeAttrName);
                        sendLimitPerMonthMap.put(attrValue, nodeValue);

                        continue;
                    }
                }
            }
        }
        catch(XroshotException e) {
            throw e;
        }
        catch(Exception e) {
            logMsg = "로그인 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(!loginInfoMap.containsKey(XroshotConstant.SESSION_ID_NODE)){// == false) {  // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "MAS 로그인 응답 포맷이 잘 못 되었습니다.(SessionID 값이 없습니다.)" ;
            errorCode = ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String sessionId = (String)loginInfoMap.get(XroshotConstant.SESSION_ID_NODE);

        if(StringUtils.isEmpty(sessionId)){// == true) {// PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "SessionID 값이 없습니다.";
            errorCode = ErrorCode.MAS_SERVER_LOGIN_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return loginInfoMap;
    }

    public static String getOneSmsRequestXML(
            String callbackNumber
            , String customMessageID
            , String receiveNumber
            , String subject
            , String content) throws BatchProcessingException {
        String logTitle = "getOneSmsRequestXML";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(StringUtils.isEmpty(callbackNumber)
                || StringUtils.isEmpty(customMessageID)
                || StringUtils.isEmpty(receiveNumber)
                || StringUtils.isEmpty(subject)
                || StringUtils.isEmpty(content)) {    // PMD : Avoid unnecessary comparisons in boolean expressions
//            if(CommonUtil.isEmptyString(callbackNumber) == true
//                    || CommonUtil.isEmptyString(customMessageID) == true
//                    || CommonUtil.isEmptyString(receiveNumber) == true
//                    || CommonUtil.isEmptyString(subject) == true
//                    || CommonUtil.isEmptyString(content) == true) {
            logMsg = "SMS 전송 요청 정보가 없습니다.";
            errorCode = ErrorCode.NULL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='req_send_message_2'>");
        sb.append("<MessageType>4</MessageType>");             // 1 : SMS
        sb.append("<MessageSubType>1</MessageSubType>");     // 1 : 일반메세지, 2 : URL SMS
        sb.append("<CallbackNumber>" + callbackNumber + "</CallbackNumber>");            // 회신번호(암호화)
        sb.append("<CustomMessageID>" + customMessageID + "</CustomMessageID>");    // SP가 생성한 메세지 ID
        sb.append("<Message>");
        sb.append("<ReceiveNumber seqNo='1'>" + receiveNumber + "</ReceiveNumber>");// 수신번호(암호화)
        sb.append("<Subject>" + subject + "</Subject>");    // 메세지 제목, XML 예약문자 (escape문자)가 포함된 경우 , 반드시 치환해서 전송해야 합니다.
        sb.append("<Content>" + content + "</Content>");    // 메세지 내용(암호화)
        sb.append("</Message>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }
    public static String getSmsRequestXML(XroshotXMLVO xroshotXMLVO)throws BatchProcessingException
    {
        return XroshotXMLDocument.getSmsRequestXML
                (xroshotXMLVO.getCustomMessageID()
                        , xroshotXMLVO.getReserveType()
                        , xroshotXMLVO.getReserveTime()
                        , xroshotXMLVO.getReserveDTime()
                        , xroshotXMLVO.getSubject()
                        , xroshotXMLVO.getContent()
                        , xroshotXMLVO.getCallbackNumber()
                        , xroshotXMLVO.getReceiveNumberList());
    }

    public static String getSmsRequestXML(
            String customMessageID
            , String reserveType
            , String reserveTime
            , String reserveDTime
            , String subject
            , String content
            , String callbackNumber
            , List<String> receiveNumberList) throws BatchProcessingException {
        String logTitle = "getOneSmsRequestXML";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(StringUtils.isEmpty(customMessageID)
                || StringUtils.isEmpty(reserveType)
                || StringUtils.isEmpty(subject)
                || StringUtils.isEmpty(content)
                || receiveNumberList == null) {
//            if(CommonUtil.isEmptyString(customMessageID) == true    // PMD : Avoid unnecessary comparisons in boolean expressions
//                    || CommonUtil.isEmptyString(reserveType) == true// PMD : Avoid unnecessary comparisons in boolean expressions
//                    || CommonUtil.isEmptyString(subject) == true// PMD : Avoid unnecessary comparisons in boolean expressions
//                    || CommonUtil.isEmptyString(content) == true// PMD : Avoid unnecessary comparisons in boolean expressions
//                    || receiveNumberList == null) {
            logMsg = "SMS 전송 요청 정보가 없습니다.";
            errorCode = ErrorCode.NULL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='req_send_message_2'>");
        sb.append("<MessageType>4</MessageType>");             // 1 : SMS
        sb.append("<MessageSubType>1</MessageSubType>");     // 1 : 일반메세지, 2 : URL SMS

        if(StringUtils.isNotEmpty(callbackNumber)) {
            sb.append("<CallbackNumber>" + callbackNumber + "</CallbackNumber>");            // 회신번호(암호화)
        }

        sb.append("<CustomMessageID>" + customMessageID + "</CustomMessageID>");    // SP가 생성한 메세지 ID

        if(reserveType.equals(XroshotConstant.RESERVATION_SCHEDULE_TYPE)) {
            if(StringUtils.isEmpty(reserveTime)
                || StringUtils.isEmpty(reserveDTime)) {            // PMD :
                logMsg = "SMS 전송 예약 정보가 없습니다.";
                errorCode = ErrorCode.NULL_ERROR;

                logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                throw new BatchProcessingException(logMsg, errorCode);
            }

            sb.append("<ReserveType>" + XroshotConstant.MSC_RESERVATION_SCHEDULE_TYPE + "</ReserveType>");    // 예약 타입
            sb.append("<ReserveTime>" + reserveTime + "</ReserveTime>");    // 예약 시간
            sb.append("<ReserveDTime>" + reserveDTime + "</ReserveDTime>");    // 예약 만료 시간
        }

        sb.append("<Message>");

        for(int i=0; i<receiveNumberList.size(); i++) {
            String iVal = String.valueOf(i+1);
            sb.append("<ReceiveNumber seqNo='" + iVal + "'>" + receiveNumberList.get(i) + "</ReceiveNumber>");// 수신번호(암호화)
        }

        sb.append("<Subject>" + subject + "</Subject>");    // 메세지 제목, XML 예약문자 (escape문자)가 포함된 경우 , 반드시 치환해서 전송해야 합니다.
        sb.append("<Content>" + content + "</Content>");    // 메세지 내용(암호화)
        sb.append("</Message>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }

    public static String getLogoutRequestXML(String reason) {
        String reasonVal = reason;
        if(StringUtils.isEmpty(reasonVal)) {
            reasonVal = "0";
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='req_unregist'>");
        sb.append("<Reason>" + reasonVal + "</Reason>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }

    public static String parseLogoutInfo(String logoutInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseLogoutInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        if(StringUtils.isEmpty(logoutInfo)) {
            return null;
        }

        try {
            doc = getXmlDocument(logoutInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_unregist")) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        String result = null;

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {
                        result = getNodeTextValue(node);

                        if(!result.equals("0")) {
                            logMsg = "로그아웃에 실패하였습니다.(" + result + ")";
                            errorCode = ErrorCode.MAS_SERVER_TIME_INFO_REQUEST_FAIL;

                            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

                            throw new XroshotException(logMsg, errorCode, result);
                        }

                        break;
                    }
                }
            }
        }
        catch(XroshotException e) {
            throw e;
        }
        catch(Exception e) {
            logMsg = "로그아웃 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return result;
    }

    public static String getResReportResponsetXML(String result, String jobID) throws BatchProcessingException {
        String logTitle = "getResReportResponsetXML";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(StringUtils.isEmpty(result) || StringUtils.isEmpty(jobID)) {
            logMsg = "요청 정보가 없습니다.";
            errorCode = ErrorCode.NULL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String requestXML = "";

        StringBuilder sb = new StringBuilder();
        sb.append(XroshotConstant.XML_DOC_TYPE);
        sb.append("<MAS method='res_report'>");
        sb.append("<Result>" + result + "</Result>");
        sb.append("<JobID>" + jobID + "</JobID>");
        sb.append("</MAS>");

        requestXML = sb.toString();

        return requestXML;
    }

    public static boolean isReport(String msgInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "isReport";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        boolean reportFlag = false;

        if(StringUtils.isEmpty(msgInfo)) {
            return reportFlag;
        }

        try {
            doc = getXmlDocument(msgInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("req_report")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("req_report") == false) {
            return reportFlag;
        }
        else {
            reportFlag = true;
        }

        return reportFlag;
    }

    public static boolean isLogin(String msgInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "isLogin";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        boolean logoutFlag = false;

        if(StringUtils.isEmpty(msgInfo)){    //if(CommonUtil.isEmptyString(msgInfo) == true) { // PMD : Avoid unnecessary comparisons in boolean expressions
            return logoutFlag;
        }

        try {
            doc = getXmlDocument(msgInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                ||!method.equals("res_regist")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_regist") == false) {
            return logoutFlag;
        }
        else {
            logoutFlag = true;
        }

        return logoutFlag;
    }

    public static boolean isLogout(String msgInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "isLogout";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        boolean logoutFlag = false;

        if(StringUtils.isEmpty(msgInfo)) {
            return logoutFlag;
        }

        logger.debug(LoggerMessage.getMessage(msgInfo));

        try {
            doc = getXmlDocument(msgInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_unregist")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_unregist") == false) {
            return logoutFlag;
        }
        else {
            logoutFlag = true;
        }

        return logoutFlag;
    }

    public static boolean isResSendMessageAll(String msgInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "isReport";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        boolean reportFlag = false;

        if(StringUtils.isEmpty(msgInfo)) {
            return reportFlag;
        }

        try {
            doc = getXmlDocument(msgInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_send_message_all") ) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_send_message_all") == false) {
            return reportFlag;
        }
        else {
            reportFlag = true;
        }

        return reportFlag;
    }

    public static boolean isResSendMessage(String msgInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "isReport";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Document doc = null;

        boolean reportFlag = false;

        if(StringUtils.isEmpty(msgInfo)) {
            return reportFlag;
        }

        try {
            doc = getXmlDocument(msgInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_send_message") ) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_send_message") == false) {
            return reportFlag;
        }
        else {
            reportFlag = true;
        }

        return reportFlag;
    }

    public static Map<String, String> parseReportInfo(String reportInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseReportInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Map<String, String> reportInfoMap = null;

        Document doc = null;

        if(StringUtils.isEmpty(reportInfo)) {
            return null;
        }

        try {
            doc = getXmlDocument(reportInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("req_report") ) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("req_report") == false) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        reportInfoMap = new HashMap<String, String>();

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.SERVICE_PROVIDER_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.SERVICE_PROVIDER_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.END_USER_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.END_USER_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.RESULT_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.TIME_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.TIME_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.FINISH_PAGE_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.FINISH_PAGE_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.DURATION_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.DURATION_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.CUSTOM_MESSAGE_ID_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.CUSTOM_MESSAGE_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SEQUENCE_NUMBER_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.SEQUENCE_NUMBER_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.JOB_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.JOB_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.GROUP_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.GROUP_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.MESSAGE_TYPE_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.MESSAGE_TYPE_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SEND_NUMBER_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.SEND_NUMBER_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.RECEIVE_NUMBER_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.RECEIVE_NUMBER_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.CALLBACK_NUMBER_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.CALLBACK_NUMBER_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.REPLY_INFO_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.REPLY_INFO_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.TELCO_INFO_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.TELCO_INFO_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.FEE_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.FEE_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.STIME_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.STIME_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.RTIME_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.RTIME_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.SUBMIT_TIME_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.SUBMIT_TIME_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.STATUS_TEXT_NODE) ) {
                        String nodeValue = getNodeTextValue(node);
                        reportInfoMap.put(XroshotConstant.STATUS_TEXT_NODE, nodeValue);

                        continue;
                    }
                }
            }
        }
        catch(Exception e) {
            logMsg = "리포트 정보 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(!reportInfoMap.containsKey(XroshotConstant.JOB_ID_NODE)){// == false) {  // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "리포트 요청 포맷이 잘 못 되었습니다.(JobID 값이 없습니다.)" ;
            errorCode = ErrorCode.MAS_SERVER_REPORT_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String jobId = (String)reportInfoMap.get(XroshotConstant.JOB_ID_NODE);

        if(StringUtils.isEmpty(jobId) ) {
            logMsg = "JobID 값이 없습니다.";
            errorCode = ErrorCode.MAS_SERVER_REPORT_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return reportInfoMap;
    }

    public static Map<String, Object> parseSendMessageInfo(String sendMessageInfo) throws XroshotException, BatchProcessingException {
        String logTitle = "parseSendMessageInfo";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Map<String, Object> sendMessageInfoMap = null;

        Document doc = null;

        if(StringUtils.isEmpty(sendMessageInfo) ) {
            return null;
        }

        try {
            doc = getXmlDocument(sendMessageInfo);
        }
        catch(BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw e;
        }

        if(doc == null) {
            logMsg = "XML 파싱 중 예외가 발생하였습니다.(Xml document is null!)";
            errorCode = ErrorCode.INVALID_XML_FORMAT;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 1. Root Element
         */
        Element rootElement = doc.getDocumentElement();
        String rootNodeName = rootElement.getNodeName();
        String method = rootElement.getAttribute("method");

        if(!rootNodeName.equals("MAS")
                || StringUtils.isEmpty(method)
                || !method.equals("res_send_message_all")) {
//            if(rootNodeName.equals("MAS") == false
//                    || CommonUtil.isEmptyString(method) == true
//                    || method.equals("res_send_message_all") == false) {
            errorCode = ErrorCode.INVALID_XML_FORMAT;
            logMsg = "규격과 일치하지 않은 XML 입니다.(Root : " + rootNodeName + ")";

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, rootNodeName));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        /*
         * 2. Child Elements
         */
        NodeList headNodeList = rootElement.getChildNodes();
        sendMessageInfoMap = new HashMap<String, Object>();
        Map<String, String> jobIdMap = new HashMap<String, String>();
        sendMessageInfoMap.put(XroshotConstant.JOB_ID_NODE, jobIdMap);

        String seqNoAttrName = "seqNo";

        try {
            for(int i=0; i<headNodeList.getLength(); i++) {
                Node node = headNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    if(node.getNodeName().equals(XroshotConstant.RESULT_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        sendMessageInfoMap.put(XroshotConstant.RESULT_NODE, nodeValue);
                    }
                    else if(node.getNodeName().equals(XroshotConstant.TIME_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        sendMessageInfoMap.put(XroshotConstant.TIME_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.CUSTOM_MESSAGE_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        sendMessageInfoMap.put(XroshotConstant.CUSTOM_MESSAGE_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.COUNT_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        sendMessageInfoMap.put(XroshotConstant.COUNT_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.GROUP_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        sendMessageInfoMap.put(XroshotConstant.GROUP_ID_NODE, nodeValue);

                        continue;
                    }
                    else if(node.getNodeName().equals(XroshotConstant.JOB_ID_NODE)) {
                        String nodeValue = getNodeTextValue(node);
                        String attrValue = ((Element)node).getAttribute(seqNoAttrName);
                        jobIdMap.put(attrValue, nodeValue);

                        continue;
                    }
                }
            }
        }
        catch(Exception e) {
            logMsg = "SMS 전송 응답 XML 파싱 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.UNKNOWN_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(!sendMessageInfoMap.containsKey(XroshotConstant.RESULT_NODE)){// == false) {  // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "SMS 전송 응답 포맷이 잘 못 되었습니다.(Result 값이 없습니다.)" ;
            errorCode = ErrorCode.MAS_SERVER_RES_SEND_MESSAGE_ALL_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String result = (String)sendMessageInfoMap.get(XroshotConstant.RESULT_NODE);

        if(StringUtils.isEmpty(result)) {
            logMsg = "Result 값이 없습니다.";
            errorCode = ErrorCode.MAS_SERVER_RES_SEND_MESSAGE_ALL_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(jobIdMap.size() == 0) {
            logMsg = "JobID 값이 없습니다.)" ;
            errorCode = ErrorCode.MAS_SERVER_RES_SEND_MESSAGE_ALL_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return sendMessageInfoMap;
    }
}

