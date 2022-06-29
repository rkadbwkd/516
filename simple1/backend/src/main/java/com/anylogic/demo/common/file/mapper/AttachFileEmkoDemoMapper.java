/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.common.file.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttachFileEmkoDemoMapper {

     List<Map<String, Object>> selectAttachFileByRecord(Map<String, Object> parameter);
     Map<String, Object> getAttachFile(Map<String, Object> parameter);
     int insertAttachFile(Map<String, Object> parameter);
     int updateAttachFileByRecord(Map<String, Object> parameter);
     int deleteAttachFile(Map<String, Object> sendInfo);
     int deleteAttachFileDelYN(Map<String, Object> sendInfo);
     int deleteAttachFileByRecord(Map<String, Object> sendInfo);
}

