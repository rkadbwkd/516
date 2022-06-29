/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @name PushUtil
 */

public class PushUtil {
    private static final Logger logger = LoggerFactory.getLogger(PushUtil.class);

    private FileOutputStream fos;

    public boolean sendPush(String url, String memberId, String title, String message) {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
 
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
 
            conn.setRequestMethod("GET");
 
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
 
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
 
            System.out.println(response.toString()); //결과, json결과를 parser하여 처리
 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

}

