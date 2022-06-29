/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.common.file.xroshot.model;
/**
* @class SdkSmsSendModel.java
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
public class SdkSmsSendModel {
    private long msg_id;
    private String user_id;
    private long schedule_type;
    private String subject;
    private String sms_msg;
    private String callback_url;
    private String now_date;
    private String send_date;
    private String callback;
    private Long dest_type;
    private Long dest_count;
    private String dest_info;
    private String kt_office_code;
    private String cdr_id;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String reserved4;
    private String reserved5;
    private String reserved6;
    private Long send_status;
    private Long send_count;
    private Long send_result;
    private String send_proc_time;
    private Long std_id;
    private Long job_id;
    
    public Long getMsg_id() {
        return msg_id;
    }
    public void setMsg_id(Long msg_id) {
        this.msg_id = msg_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public Long getSchedule_type() {
        return schedule_type;
    }
    public void setSchedule_type(Long schedule_type) {
        this.schedule_type = schedule_type;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSms_msg() {
        return sms_msg;
    }
    public void setSms_msg(String sms_msg) {
        this.sms_msg = sms_msg;
    }
    public String getCallback_url() {
        return callback_url;
    }
    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }
    public String getNow_date() {
        return now_date;
    }
    public void setNow_date(String now_date) {
        this.now_date = now_date;
    }
    public String getSend_date() {
        return send_date;
    }
    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }
    public String getCallback() {
        return callback;
    }
    public void setCallback(String callback) {
        this.callback = callback;
    }
    public Long getDest_type() {
        return dest_type;
    }
    public void setDest_type(Long dest_type) {
        this.dest_type = dest_type;
    }
    public Long getDest_count() {
        return dest_count;
    }
    public void setDest_count(Long dest_count) {
        this.dest_count = dest_count;
    }
    public String getDest_info() {
        return dest_info;
    }
    public void setDest_info(String dest_info) {
        this.dest_info = dest_info;
    }
    public String getKt_office_code() {
        return kt_office_code;
    }
    public void setKt_office_code(String kt_office_code) {
        this.kt_office_code = kt_office_code;
    }
    public String getCdr_id() {
        return cdr_id;
    }
    public void setCdr_id(String cdr_id) {
        this.cdr_id = cdr_id;
    }
    public String getReserved1() {
        return reserved1;
    }
    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }
    public String getReserved2() {
        return reserved2;
    }
    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }
    public String getReserved3() {
        return reserved3;
    }
    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }
    public String getReserved4() {
        return reserved4;
    }
    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }
    public String getReserved5() {
        return reserved5;
    }
    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }
    public String getReserved6() {
        return reserved6;
    }
    public void setReserved6(String reserved6) {
        this.reserved6 = reserved6;
    }
    public Long getSend_status() {
        return send_status;
    }
    public void setSend_status(Long send_status) {
        this.send_status = send_status;
    }
    public Long getSend_count() {
        return send_count;
    }
    public void setSend_count(Long send_count) {
        this.send_count = send_count;
    }
    public Long getSend_result() {
        return send_result;
    }
    public void setSend_result(Long send_result) {
        this.send_result = send_result;
    }
    public String getSend_proc_time() {
        return send_proc_time;
    }
    public void setSend_proc_time(String send_proc_time) {
        this.send_proc_time = send_proc_time;
    }
    public Long getStd_id() {
        return std_id;
    }
    public void setStd_id(Long std_id) {
        this.std_id = std_id;
    }
    public Long getJob_id() {
        return job_id;
    }
    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }
}

