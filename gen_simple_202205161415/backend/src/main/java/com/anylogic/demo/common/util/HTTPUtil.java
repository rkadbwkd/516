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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;




/**
 * <PRE>
 *  ClassName : HTTPUtil
 * </PRE>
 * @version : 1.0
 * @date    : 2013. 10. 28. 오후 6:43:32
 * @author  : 추병조
 * @brief   : HTTP 관련 유틸
 */
public class HTTPUtil
{
    /** 로거클래스 */
    private static Logger logger = LoggerFactory.getLogger(HTTPUtil.class);

    private static Map<String, String> defaultHeaders = new HashMap<String, String>();

    static
    {
        //소문자로 Key 모두 잡을 것
        defaultHeaders.put("content-type", "application/json");
    }

    private static void updateDefaultHeaders(Map<String, String> headers)
    {
        //소문자로 모두 변환
        Map<String, String> checkHeaders = new HashMap<String, String>();
        if(headers != null && headers.size() != 0)
        {
            Set<String> keys = headers.keySet();
            for(String key : keys)
            {
                String value = headers.get(key);
                checkHeaders.put(key.toLowerCase(), value);
            }
        }

        //소문자기반 기본값 체크
        Set<String> defaultKeys = defaultHeaders.keySet();
        for(String defaultKey : defaultKeys)
        {
            String value = checkHeaders.get(defaultKey);
            //기본값이 없을 경우 입력
            if(value == null)
            {
                String defaultValue = defaultHeaders.get(defaultKey);
                logger.debug("HTTP Add Default Header: Key = "+defaultKey+", Value = "+defaultValue);
                headers.put(defaultKey, defaultValue);
            }
        }
    }

    public static HttpResponse doAction(HttpRequest httpRequest) throws Exception
    {
        HttpURLConnection nowHttpURLConnection = null;
        DataOutputStream nowDataOutputStream = null;
        InputStreamReader nowInputStreamReader = null;
        BufferedReader nowBufferedReader = null;


        String url = httpRequest.getUrl();
        byte[] bdyPackt = httpRequest.getBdyPackt();
        Map<String, String> headers = httpRequest.getHeaders();
        String method = httpRequest.getMethod();
        Integer timeout = httpRequest.getTimeout();

        try
        {
            URL nowURL = null;
            nowURL = new URL(url);
            if(url.startsWith("https"))
            {
//                SSLContext sslContext = null;
//                String keyStoreFilePath = httpRequest.getKeyStoreFilePath();
//                String trustStoreFilePath = httpRequest.getTrustStoreFilePath();
//                String keyStorePassword = httpRequest.getKeyStorePassword();
//                String trustStorePassword = httpRequest.getKeyStorePassword();
//                String sslContextPassword = httpRequest.getSslContextPassword();
//
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection)nowURL.openConnection();
//                //사설인증서
//                if(keyStoreFilePath != null)
//                {
//                    sslContext = SSLContextGenerator.getSSLContext(keyStoreFilePath, trustStoreFilePath, keyStorePassword, trustStorePassword, sslContextPassword);
//                    //System.out.println("SSL provider is: " + sslContext.getProvider());
//                    httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
//                }
//
//                httpsURLConnection.setHostnameVerifier(new HostnameVerifier()
//                {
//                    @Override
//                    public boolean verify(String paramString, SSLSession paramSSLSession)
//                    {
//                        return true;
//                    }
//                 });
//                nowHttpURLConnection = httpsURLConnection;
                throw new Exception("HTTPS Not supported");
            }
            else
            {
                nowHttpURLConnection = (HttpURLConnection)nowURL.openConnection();
            }

            nowHttpURLConnection.setDefaultUseCaches(false);
            nowHttpURLConnection.setDoInput(true);
            nowHttpURLConnection.setDoOutput(true);
            if("POST".equals(method))
            {
                nowHttpURLConnection.setRequestMethod("POST");
            }
            else if("GET".equals(method))
            {
                nowHttpURLConnection.setRequestMethod("GET");
            }
            else if("PUT".equals(method))
            {
                nowHttpURLConnection.setRequestMethod("PUT");
            }
            else if("DELETE".equals(method))
            {
                nowHttpURLConnection.setRequestMethod("DELETE");
            }
            else
            {
                nowHttpURLConnection.setRequestMethod("POST");
            }

            if(timeout != null)
            {
                nowHttpURLConnection.setConnectTimeout(timeout);
                nowHttpURLConnection.setReadTimeout(timeout);
            }

            String requestMethod = nowHttpURLConnection.getRequestMethod();
            //logger.debug("RequestMethod:"+nowHttpURLConnection.getRequestMethod());

            //기본값 셋팅
            if(headers == null)
            {
                headers = new HashMap<String, String>();
            }
            updateDefaultHeaders(headers);

            //HTTP Header Set
            Set<String> keys = headers.keySet();
            for(String key : keys)
            {
                String value = headers.get(key);
                logger.debug("HTTP Header: Key = "+key+", Value = "+value);
                nowHttpURLConnection.setRequestProperty(key, value);
            }

            HttpResponse httpResponse = new HttpResponse();

            if("DELETE".equals(method) || "GET".equals(method))
            {
                logger.debug(url+", "+requestMethod+" send data: \n"+new String(bdyPackt));
                //nowHttpURLConnection.getResponseCode();
            }
            else
            {
                logger.debug(url+", "+requestMethod+" send data: \n"+new String(bdyPackt));
                nowDataOutputStream = new DataOutputStream(nowHttpURLConnection.getOutputStream());
                nowDataOutputStream.write(bdyPackt);
                nowDataOutputStream.flush();
            }

            int responseCode = nowHttpURLConnection.getResponseCode();

            logger.debug(url+", "+requestMethod+", responseCode = "+responseCode);

            if (HttpStatus.Series.valueOf(responseCode) == HttpStatus.Series.SUCCESSFUL)
            {
                nowInputStreamReader = new InputStreamReader(nowHttpURLConnection.getInputStream());
            }
            else
            {
                nowInputStreamReader = new InputStreamReader(nowHttpURLConnection.getErrorStream());
            }

            //RECIEVE
            nowBufferedReader = new BufferedReader(nowInputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            while (true)
            {
                String temp = nowBufferedReader.readLine();
                if(temp == null)
                    break;
                stringBuffer.append(temp);
                //TODO구현 sss의 종결자 체크해서 무한루프 빠져나오도록
            }


            byte[] rpyPackt = stringBuffer.toString().getBytes();
            httpResponse.setRpyCd(responseCode);
            httpResponse.setRpyPackt(rpyPackt);
            logger.debug(url+", "+requestMethod+" RpyPackt data: \n"+new String(rpyPackt));
            return httpResponse;
        }
        catch(Exception e)
        {
            logger.error("httpRequest error. httpRequest = "+httpRequest, e);
            throw e;
        }
        finally
        {
            if(nowBufferedReader != null)
            {
                nowBufferedReader.close();
            }

            if(nowInputStreamReader != null)
            {
                nowInputStreamReader.close();
            }
            if(nowDataOutputStream != null)
            {
                nowDataOutputStream.close();
            }

            if(nowHttpURLConnection != null)
            {
                nowHttpURLConnection.disconnect();
            }
        }
    }

    public static HttpResponse doGet(HttpRequest httpRequest)
    {
        return null;
    }

    public static HttpResponse doPut(HttpRequest httpRequest)
    {
        return null;
    }

    public static HttpResponse doDelete(HttpRequest httpRequest)
    {
        return null;
    }


    /**
     * HTTP POST방식으로 데이터 전송
     * @param url    : 전송할 목적지(http)
     * @param sendData    : 전송할 데이터
     * @return    : POST response
     * @throws IOException
     */
    public static String doPost(String url, String sendData, int timeout) throws Exception
    {
        byte[] sendByte= new String(sendData).getBytes();
        return doAction(url, "POST", sendByte, timeout);
    }

    /**
     * HTTP POST방식으로 데이터 전송
     * @param url    : 전송할 목적지(http)
     * @param sendData    : 전송할 데이터
     * @return    : POST response
     * @throws IOException
     */
    public static String doPost(String url, String contentType, String sendData, int timeout) throws Exception
    {
        byte[] sendByte= new String(sendData).getBytes();
        return doAction(url, "POST", contentType, null, sendByte, timeout);
    }

    public static String doPost(String url, byte[] sendByte, int timeout) throws Exception
    {
        return doAction(url, "POST", "application/json", null, sendByte, timeout);
    }

    /**
     * HTTP 방식으로 데이터 전송
     * @param url    : 전송할 목적지(http)
     * @param sendData    : 전송할 데이터
     * @return    : POST response
     * @throws IOException
     */
    public static String doAction(String url, String method, String sendData, int timeout) throws Exception
    {
        byte[] sendByte= new String(sendData).getBytes();
        return doAction(url, method, sendByte, timeout);
    }

    /**
     * HTTP 방식으로 데이터 전송
     * @param url    : 전송할 목적지(http)
     * @param sendData    : 전송할 데이터
     * @return    : POST response
     * @throws IOException
     */
    public static String doAction(String url, String method, String contentType, String sendData, int timeout) throws Exception
    {
        byte[] sendByte= new String(sendData).getBytes();
        return doAction(url, method, contentType, null, sendByte, timeout);
    }

    public static String doAction(String url, String method, byte[] sendByte, int timeout) throws Exception
    {
        return doAction(url, method, "application/json", null, sendByte, timeout);
    }

    /**
     * HTTP 방식으로 데이터 전송
     * @param url    : 전송할 목적지(http)
     * @param sendData    : 전송할 데이터
     * @return    : POST response
     * @throws IOException
     */
    public static String doAction(String url, String method, String contentType, Map<String, String> headers, byte[] sendByte, int timeout) throws Exception
    {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.setMethod(method);
        if(contentType != null)
        {
            if(headers == null)
            {
                headers = new HashMap<String, String>();
            }
            headers.put("content-type", contentType);
        }
        httpRequest.setHeaders(headers);
        httpRequest.setBdyPackt(sendByte);
        httpRequest.setTimeout(timeout);

        HttpResponse httpResponse = HTTPUtil.doAction(httpRequest);
        if(httpResponse == null || httpResponse.getRpyPackt() == null)
        {
            return null;
        }
        return new String(httpResponse.getRpyPackt());
    }




    public static boolean isHTTPURL(String url)
    {
        try {
            URL nowURL = new URL(url);
            //http가 아닐 경우 false
            if(!nowURL.getProtocol().equals("http"))
            {
                return false;
            }
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    public static class HttpRequest
    {
        /** URL */
        private String url;
        /** method */
        private String method;
        /** HTTP 헤더 */
        private Map<String, String> headers;
        /** 바디패킷 */
        private byte[] bdyPackt;
        /** 타임아웃 */
        private Integer timeout;

        /** 키저장파일경로 */
        private String keyStoreFilePath;
        /** 트러스트저장파일경로 */
        private String trustStoreFilePath;
        /** 키저장비밀번호 */
        private String keyStorePassword;
        /** 트러스트저장비밀번호 */
        private String trustStorePassword;
        /** SSL컨택스트비밀번호 */
        private String sslContextPassword;
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public Map<String, String> getHeaders() {
            return headers;
        }
        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }
        public byte[] getBdyPackt() {
            return bdyPackt;
        }
        public void setBdyPackt(byte[] bdyPackt) {
            this.bdyPackt = bdyPackt;
        }
        public Integer getTimeout() {
            return timeout;
        }
        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
        public String getMethod() {
            return method;
        }
        public void setMethod(String method) {
            this.method = method;
        }
        public String getKeyStoreFilePath() {
            return keyStoreFilePath;
        }
        public void setKeyStoreFilePath(String keyStoreFilePath) {
            this.keyStoreFilePath = keyStoreFilePath;
        }
        public String getTrustStoreFilePath() {
            return trustStoreFilePath;
        }
        public void setTrustStoreFilePath(String trustStoreFilePath) {
            this.trustStoreFilePath = trustStoreFilePath;
        }
        public String getKeyStorePassword() {
            return keyStorePassword;
        }
        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }
        public String getTrustStorePassword() {
            return trustStorePassword;
        }
        public void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }
        public String getSslContextPassword() {
            return sslContextPassword;
        }
        public void setSslContextPassword(String sslContextPassword) {
            this.sslContextPassword = sslContextPassword;
        }
    }

    public static class HttpResponse
    {
        /** 응답코드 */
        private Integer rpyCd;
        /** 바디패킷 */
        private byte[] rpyPackt;
        public Integer getRpyCd() {
            return rpyCd;
        }
        public void setRpyCd(Integer rpyCd) {
            this.rpyCd = rpyCd;
        }
        public byte[] getRpyPackt() {
            return rpyPackt;
        }
        public void setRpyPackt(byte[] rpyPackt) {
            this.rpyPackt = rpyPackt;
        }
    }
}

