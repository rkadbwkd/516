/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file.xroshot.constant;
/**
* @class XroshotConstant.java
* @brief xroshot 상수 정의 클래스
*
* @author TFM 개발팀
* @date 2014.3.19
* @version 1.0.0.1
* @Modification  Information
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014.3.19
**/
public class XroshotConstant {
    public static final String RESULT_NODE = "Result";
    public static final String RESOURCE_NODE = "Resource";
    public static final String ADDRESS_NODE = "Address";
    public static final String PORT_NODE = "Port";
    public static final String TIME_NODE = "Time";
    public static final String SESSION_ID_NODE = "SessionID";
    public static final String SEND_LIMIT_PER_SECOND_NODE = "SendLimitPerSecond";
    public static final String PRODUCT_STATUS_NODE = "ProductStatus";
    public static final String SEND_LIMIT_PER_MONTH_NODE = "SendLimitPerMonth";
    public static final String SERVICE_PROVIDER_ID_NODE = "ServiceProviderID";
    public static final String FINISH_PAGE_NODE = "FinishPage";
    public static final String DURATION_NODE = "Duration";
    public static final String CUSTOM_MESSAGE_ID_NODE = "CustomMessageID";
    public static final String SEQUENCE_NUMBER_NODE = "SequenceNumber";
    public static final String JOB_ID_NODE = "JobID";
    public static final String GROUP_ID_NODE = "GroupID";
    public static final String MESSAGE_TYPE_NODE = "MessageType";
    public static final String SEND_NUMBER_NODE = "SendNumber";
    public static final String RECEIVE_NUMBER_NODE = "ReceiveNumber";
    public static final String CALLBACK_NUMBER_NODE = "CallbackNumber";
    public static final String REPLY_INFO_NODE = "ReplyInfo";
    public static final String TELCO_INFO_NODE = "TelcoInfo";
    public static final String FEE_NODE = "Fee";
    public static final String STIME_NODE = "STime";
    public static final String RTIME_NODE = "RTime";
    public static final String SUBMIT_TIME_NODE = "SubmitTime";
    public static final String STATUS_TEXT_NODE = "StatusText";
    public static final String END_USER_ID_NODE = "EndUserID";
    public static final String COUNT_NODE = "Count";

    public static final String XML_DOC_TYPE = "<?xml version='1.0' encoding='UTF-8' ?>";

    public static final String DEV_MODE_Y = "Y";
    public static final String DEV_MODE_N = "N";

    public static final String SMS_MESSAGE_TYPE = "1";
    public static final String VMS_MESSAGE_TYPE = "2";
    public static final String FMS_MESSAGE_TYPE = "3";
    public static final String MMS_MESSAGE_TYPE = "4";

    public static final String MESSAGE_Y = "Y";
    public static final String MESSAGE_N = "N";

    public static final String IMMEDIATELY_SCHEDULE_TYPE = "0";
    public static final String RESERVATION_SCHEDULE_TYPE = "1";

    public static final String MSC_RESERVATION_SCHEDULE_TYPE = "2";

    public static final Long SMS_SEND_STATUS_MSG_INPUT = (long) 0;
    public static final Long SMS_SEND_STATUS_AGENT_FETCH = (long) 1;
    public static final Long SMS_SEND_STATUS_SERVER_SENDING = (long) 2;
    public static final Long SMS_SEND_STATUS_SERVER_SEND_SUCCESS = (long) 3;
    public static final Long SMS_SEND_STATUS_SERVER_SEND_FAIL = (long) -1;

    public static final String MAS_SERVER_INFO = "http://rcs.xroshot.com/catalogs/MAS/recommended/0";
    public static final String SP_ENDUSER_ID = "monobiztest5";
    public static final String PROTOCOL_VER = "01.00";
    //public static final String MAS_SERVER_ADDR = "210.105.195.154"; 
    public static final String MAS_SERVER_ADDR = "119.205.196.240"; 
    public static final String XROSHOT_ENCODING = "UTF-8";
    public static final String XROSHOT_CRYPTO_ENCODING = "EUC-KR";

    /** SMS 전송 Default value */
    public static final String DEFAULT_SUBJECT = "Coworekrs Infomation";
    public static final String DEFAULT_MSG_ID = "SMCP00001";

    public static final String DEFAULT_PROP_FILE = "properties/sms.properties";
    public static final String XROSUOT_SENDER = "xroshot.sender";
    public static final String XROSUOT_SPID = "xroshot.spid";
    public static final String XROSUOT_SPPWD = "xroshot.sppw";
    public static final String XROSUOT_CERTFILE_PATH = "xroshot.certfile.path";
    public static final String XROSUOT_SERVER_INFO = "xroshot.mas.server.info";
}

