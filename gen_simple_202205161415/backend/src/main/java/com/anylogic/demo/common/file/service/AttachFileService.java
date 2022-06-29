/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AttachFileService {
     List<Map<String, Object>> selectAttachFileByRecord(Map<String, Object> parameter);
     Map<String, Object> getAttachFile(Map<String, Object> parameter);
     default List<Map<String, Object>> insertAttachFile(List<MultipartFile> mf, Map<String, Object> data)
     {
         return insertAttachFile(mf, data, "");
     }

    List<Map<String, Object>> insertAttachFile(List<MultipartFile> mf, Map<String, Object> data, String bizPath);
    default List<Map<String, Object>> updateAttachFileByRecord(List<MultipartFile> mf, Map<String, Object> data)
    {
        return updateAttachFileByRecord(mf, data, "");
    }
    List<Map<String, Object>> updateAttachFileByRecord(List<MultipartFile> mf, Map<String, Object> sendInfo, String bizPath);
    List<Map<String, Object>> deleteAttachFileByRecordDelYN(Map<String, Object> sendInfo);
    List<Map<String, Object>> deleteAttachFileByRecord(Map<String, Object> sendInfo);
    List<Map<String, Object>> deleteAttachFile(Map<String, Object> sendInfo);
}

