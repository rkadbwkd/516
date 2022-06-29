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

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @class XroshotUtil.java
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
public class XroshotUtil {
    private static Logger logger = LoggerFactory.getLogger(XroshotUtil.class);
    
    public static List<Map<String, String>> getDestNumberList(String destInfo) {
        List<Map<String, String>> destNumberList = null;
        
        if(isEmptyString(destInfo)) {
            return destNumberList;
        }
        
        if(!(destInfo.indexOf("^") > 0)) {
            return destNumberList;
        }
        
        String[] destNumberInfos = null;
        
        if(destInfo.indexOf("|") > 0) {
            destNumberInfos = destInfo.split("\\|");
        }
        else {
            destNumberInfos = new String[1];
            destNumberInfos[0] = destInfo;
        }
        
        destNumberList = new ArrayList<Map<String, String>>();
        
        for(String destNumberInfo : destNumberInfos) {
            if(destNumberInfo.indexOf("^") > 0) {
                String[] destNumName = destNumberInfo.split("\\^");
                Map<String, String> numNameMap = new HashMap<String, String>();
                numNameMap.put(destNumName[1], destNumName[0]);
                destNumberList.add(numNameMap);
            }
        }

        return destNumberList;
    }
    
    public static boolean isEmptyString(String str) {
        if(str == null || str.length() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 날짜 형식 체크
     * 
     * @param  dateFomat yyyyMMdd 날짜
     * @return (true,false)
     */
    public static boolean isValidDateFomat(String date, String format) {

        boolean dateValidity = true;
        
        if (date==null) {
            return false;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.KOREA);
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
        } catch (ParseException pe) {
            dateValidity = false;
        } catch (IllegalArgumentException ae) {
            dateValidity = false;
        }
        return dateValidity;
    }
    
    public static String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyz"
                     + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                     + "0123456789!@%$%&^?~'\"#+="
                     + "\\*/.,:;[]()-_<>";

        final int PW_LENGTH = 20;
        Random rnd = new SecureRandom();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < PW_LENGTH; i++)
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        return pass.toString();
    }
    
    public static String addTime(String dateTime) throws Exception {
        String logTitle = "getAddTime";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        
        String addedTime = "";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        Date date = null;
        
        try {
            date = dateFormat.parse(dateTime);
        } 
        catch (ParseException e) {
            logMsg = "날짜 파싱 중 예외가 발생하였습니다.";
            errorCode = ErrorCode.DATE_PARSE_ERROR;
            
            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));
            throw e;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, 1);
        Date calDate = cal.getTime();
        addedTime = dateFormat.format(calDate);
        return addedTime;
    }
}

