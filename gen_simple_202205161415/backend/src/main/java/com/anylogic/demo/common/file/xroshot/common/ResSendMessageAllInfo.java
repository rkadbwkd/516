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

import java.util.Map;
/**
* @class ResSendMessageAllInfo.java
* @brief xroshot연동 메시지 전송 결과 
*
* @author TFM 개발팀
* @date 2014.3.19
* @version 1.0.0.1
* @Modification  Information 
*   수정일          수정자                수정내용
*  ----------------------------------------------------------------------------
*  2014.3.19     
**/
public class ResSendMessageAllInfo {
    private String result;
    private String time;
    private String customMessageId;
    private String count;
    private String groupId;
    private Map<String, String> jobInfoMap;
    
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getCustomMessageId() {
        return customMessageId;
    }
    public void setCustomMessageId(String customMessageId) {
        this.customMessageId = customMessageId;
    }
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public Map<String, String> getJobInfoMap() {
        return jobInfoMap;
    }
    public void setJobInfoMap(Map<String, String> jobInfoMap) {
        this.jobInfoMap = jobInfoMap;
    }
}

