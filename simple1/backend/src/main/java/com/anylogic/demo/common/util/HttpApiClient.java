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

import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpApiClient {
    
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(HttpApiClient.class);
    
    public String sendApi(HttpMethod method ,String interfaceUrl, Map<String, Object> reqVO) {
        
        ResponseEntity<byte[]> res = null;        
        String resultData = "";
        
        RestTemplate restTemplate = getRestTemplate(MediaType.APPLICATION_JSON);
        
        //HttpHeaders headers = getAuthorizationHeaders(com.anylogic.base.remote.Constants._PREFIX_JWT_AFTER);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> entitiry = null;
        
        if (!HttpMethod.DELETE.equals(method)) {
            entitiry = new HttpEntity<Map<String, Object>>(reqVO, headers);
        } else {
            entitiry = new HttpEntity<Map<String, Object>>(null, headers);
        }
        
        res = restTemplate.exchange(interfaceUrl, method, entitiry, byte[].class);
        
        resultData = new String(res.getBody());
        
        return resultData;
        
    }
    
public String sendApiLogin(HttpMethod method ,String interfaceUrl, Map<String, Object> reqVO) {
        
        ResponseEntity<byte[]> res = null;        
        String resultData = "";
        
        RestTemplate restTemplate = getRestTemplate(MediaType.APPLICATION_JSON);
        
        //HttpHeaders headers = getAuthorizationHeaders(com.anylogic.base.remote.Constants._PREFIX_JWT_AFTER);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> entitiry = null;
        
        if (!HttpMethod.DELETE.equals(method)) {
            entitiry = new HttpEntity<Map<String, Object>>(reqVO, headers);
        } else {
            entitiry = new HttpEntity<Map<String, Object>>(null, headers);
        }
        
        res = restTemplate.exchange(interfaceUrl, method, entitiry, byte[].class);
        
        resultData = res.getStatusCode().toString();
        
        return resultData;
        
    }
    
    private RestTemplate getRestTemplate(MediaType mediaType) {

        /*RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);*/
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); 
        factory.setReadTimeout(20000); // 읽기시간초과, ms 
        factory.setConnectTimeout(10000); // 연결시간초과, ms 
        HttpClient httpClient = HttpClientBuilder.create() 
                .setMaxConnTotal(100) // connection pool 적용 
                .setMaxConnPerRoute(5) // connection pool 적용 
                .build(); 
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅 
        RestTemplate restTemplate = new RestTemplate(factory);


        return restTemplate;
    }

}


