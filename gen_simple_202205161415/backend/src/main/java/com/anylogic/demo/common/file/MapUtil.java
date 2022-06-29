/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class MapUtil {

    private static final Logger log = LoggerFactory.getLogger(MapUtil.class);
    
    public List<Map<String, Object>> getParameterMap(MultipartHttpServletRequest mRequest) {
        log.info("getParameterMap()  {}", mRequest.getParameterMap());
        
        Map<String, String[]> paramMap = mRequest.getParameterMap();
        
        int colCount = 0;
        for ( Map.Entry<String, String[]> elem : paramMap.entrySet() ) 
        {
            colCount = elem.getValue().length;
            break;
        }
        
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        int i = 0;
        while (i != colCount) 
        {
            Map<String, Object> tMap = new HashMap<String, Object>();
            for ( Map.Entry<String, String[]> elem : paramMap.entrySet() ) 
            {
                tMap.put(elem.getKey(), elem.getValue()[i]);
            }
            data.add(tMap);
            i++;
        }
    
        log.info("getParameterMap().data  {}",data);
        
        return data;
    }
}

