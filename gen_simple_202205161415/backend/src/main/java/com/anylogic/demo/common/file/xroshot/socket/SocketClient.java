/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.common.file.xroshot.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.anylogic.demo.common.file.xroshot.exception.BatchProcessingException;
import com.anylogic.demo.common.file.xroshot.common.ErrorCode;
import com.anylogic.demo.common.file.xroshot.common.LoggerMessage;
import com.anylogic.demo.common.file.xroshot.common.ServiceProviderInfo;
import com.anylogic.demo.common.file.xroshot.common.XroshotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @class SocketClient.java
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
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private static String serverHostName;
    private static int serverPort;
    private static String msgCharSet;
    private static int maxMsgSize = 1024 * 10;

    public static void setServerInfo(ServiceProviderInfo spInfo) {
        serverHostName = spInfo.getMasServerIp();
        serverPort = spInfo.getMasServerPort();
        msgCharSet = spInfo.getMasServerCharSet();
    }

    public static Socket openSocket(String hostName, int port) throws BatchProcessingException {
        String logTitle = "openSocket";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(XroshotUtil.isEmptyString(hostName) || port == 0) {
            String hostNameVal = null;
            if(hostName == null) hostNameVal = "";

            logMsg = "호스트 명 또는 포트 값이 없습니다.(host : " + hostNameVal + ", port : " + port + ")";
            errorCode = ErrorCode.NULL_ERROR;

            //logger.info("xroshot host : " + hostNameVal + ", port : " + port);
            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        Socket clientSocket = null;

        try {
            clientSocket = new Socket(hostName, port);
            clientSocket.setSoTimeout(60000);
        }
        catch (UnknownHostException e) {
            logMsg = "지정된 호스트 명/포트에 접속 할 수 없습니다.(host : " + hostName + ", port : " + port + ")";
            errorCode = ErrorCode.SQL_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch (IOException e) {
            logMsg = "서버 접속 중 I/O 예외가 발생하였습니다.";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(clientSocket == null || clientSocket.isClosed()) {
            logMsg = "소켓을 생성하지 못했습니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return clientSocket;
    }

    public static String requestNormalCommand(String requestMsg) throws BatchProcessingException {
        String logTitle = "requestNormalCommand";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        Socket socket = null;

        try {
            socket = SocketClient.openSocket(serverHostName, serverPort);
        }
        catch (BatchProcessingException e) {
            logMsg = e.getMessage();
            errorCode = e.getErrorCode();

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            closeSocket(socket);

            throw e;
        }
        catch(Exception e) {
            logMsg = "서버 접속 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            closeSocket(socket);

            throw new BatchProcessingException(logMsg, errorCode);
        }

        if(socket == null || socket.isClosed()) {
            logMsg = "Socket open에 실패하였습니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            closeSocket(socket);

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String responseMsg = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            // request
            os = socket.getOutputStream();
            os.write(requestMsg.getBytes(msgCharSet));
            os.write('\0');
            os.flush();

            // response
            is = socket.getInputStream();
            byte[] arr = new byte[maxMsgSize];
            is.read(arr);

            responseMsg = new String(arr, msgCharSet);
            responseMsg = responseMsg.substring(0, responseMsg.indexOf('\0'));
        }
        catch (IOException e) {
            logMsg = "서버에 메세지 전송 중 I/O 예외가 발생하였습니다.";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch(Exception e) {
            logMsg = "서버 메세지 전송 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.MAS_SERVER_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        finally {
            try {
                if(os != null) {
                    os.close();
                    os = null;
                }

                if(is != null) {
                    is.close();
                    is = null;
                }
            }
            catch(IOException e) {
                logMsg = "스트림을 닫는 중 I/O 예외가 발생하였습니다.";
                errorCode = ErrorCode.IO_ERROR;

                logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));
            }

            closeSocket(socket);
        }

        return responseMsg;
    }

    public static String requestConnectionCommand(Socket socket, String requestMsg) throws BatchProcessingException {
        String logTitle = "requestConnectionCommand";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(socket == null || socket.isClosed()) {
            logMsg = "소켓이 null이거나 이미 닫혔습니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        String responseMsg = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            // request
            os = socket.getOutputStream();
            os.write(requestMsg.getBytes(msgCharSet));
            os.write('\0');
            os.flush();

            // response
            is = socket.getInputStream();
            byte[] arr = new byte[maxMsgSize];
            is.read(arr);

            responseMsg = new String(arr, msgCharSet);
            responseMsg = responseMsg.substring(0, responseMsg.indexOf('\0'));
        }
        catch (IOException e) {
            logMsg = "서버에 메세지 전송 중 I/O 예외가 발생하였습니다.";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch(Exception e) {
            logMsg = "서버 메세지 전송 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.MAS_SERVER_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return responseMsg;
    }

    public static void requestCommand(Socket socket, String requestMsg) throws BatchProcessingException {
        String logTitle = "requestCommand";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(socket == null || socket.isClosed()) {
            logMsg = "소켓이 null이거나 이미 닫혔습니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        OutputStream os = null;

        try {
            // request
            os = socket.getOutputStream();
            os.write(requestMsg.getBytes(msgCharSet));
            os.write('\0');
            os.flush();
        }
        catch (IOException e) {
            logMsg = "서버에 메세지 전송 중 I/O 예외가 발생하였습니다.";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch(Exception e) {
            logMsg = "서버 메세지 전송 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.MAS_SERVER_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }
    }

    public static String responseCommand(Socket socket) throws BatchProcessingException {
        String logTitle = "responseCommand";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(socket == null){        // PMD : Avoid unnecessary comparisons in boolean expressions
            logMsg = "소켓이 null입니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;
            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));
            throw new BatchProcessingException(logMsg, errorCode);
        }else if(socket.isClosed()) {    // PMD : Avoid unnecessary comparisons in boolean expressions
                logMsg = "소켓이 이미 닫혔습니다.";
                errorCode = ErrorCode.SOKCET_CONNECT_ERROR;
                logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));
                throw new BatchProcessingException(logMsg, errorCode);
        }

        String responseMsg = null;
        InputStream is = null;

        try {
            // response
            is = socket.getInputStream();

            byte[] arr = new byte[maxMsgSize];
            is.read(arr);
            responseMsg = new String(arr, msgCharSet);
        }
        catch(SocketTimeoutException e) {
            logMsg = "서버에 메세지 전송 중 타임 아웃 예외가 발생하였습니다.(" + e.getMessage() + ")";
            errorCode = ErrorCode.SOCKET_TIMEOUT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch (IOException e) {
            logMsg = "서버에 메세지 전송 중 I/O 예외가 발생하였습니다.(" + e.getMessage() + ")";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch(Exception e) {
            logMsg = "서버 메세지 전송 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.MAS_SERVER_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        return responseMsg;
    }

    public static List<String> responseContinueCommand(InputStream is) throws BatchProcessingException {
        String logTitle = "responseContinueCommand";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;

        if(is == null) {
            logMsg = "소켓 스트림이 null 입니다.";
            errorCode = ErrorCode.SOKCET_CONNECT_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        StringBuilder sb = new StringBuilder();
        List<String> responseMsgList = new ArrayList<String>();

        try {
            String serverResponse = null;
            char endChar = '\0';
            int lastIndex = -1;
            byte[] arr = new byte[10];

            while(true) {
                is.read(arr);
                serverResponse = new String(arr, msgCharSet);
                lastIndex = serverResponse.indexOf(endChar);

                if(lastIndex > -1) {
                    break;
                }

                sb.append(serverResponse);
            }

            String msg1 = serverResponse.substring(0, lastIndex);
            String msg2 = serverResponse.substring(lastIndex);
            responseMsgList.add(sb.toString() + msg1);

            msg2 = msg2.trim();
            int startIndex = msg2.indexOf("<?");
            if(startIndex > -1) {
                msg2 = msg2.substring(startIndex);
                responseMsgList.add(msg2);
            }
        }
        catch (IOException e) {
            logMsg = "서버에 메세지 전송 중 I/O 예외가 발생하였습니다.";
            errorCode = ErrorCode.IO_ERROR;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));

            throw new BatchProcessingException(logMsg, errorCode);
        }
        catch(Exception e) {
            logMsg = "서버 메세지 전송 중 알 수 없는 예외가 발생하였습니다.";
            errorCode = ErrorCode.MAS_SERVER_REQUEST_FAIL;

            logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg));

            throw new BatchProcessingException(logMsg, errorCode);
        }

        logger.debug("Start ********************************");
        for (String responseMsg : responseMsgList)
        {
            logger.debug(responseMsg);
        }
        logger.debug("******************************** End");
        return responseMsgList;
    }

    public static void closeSocket(Socket socket) {
        String logTitle = "closeSocket";
        String logMsg = "";
        int errorCode = ErrorCode.RC_OK;
        Socket socketVal = socket;

        if(socket == null){        // PMD : Avoid unnecessary comparisons in boolean expressions
            return;
        }else{
//            if(socketVal != null) {
                if(socketVal.isClosed()) {
                    socketVal = null;
                }
                else {
                    try {
                        OutputStream os = socketVal.getOutputStream();
                        InputStream is = socketVal.getInputStream();

                        if(os != null) {
                            os.close();
                            os = null;
                        }

                        if(is != null) {
                            is.close();
                            is = null;
                        }

                        socketVal.close();
                        socketVal = null;
                    }
                    catch (IOException e) {
                        logMsg = "Socket close 중 I/O 예외가 발생하였습니다.";
                        errorCode = ErrorCode.IO_ERROR;

                        logger.error(LoggerMessage.getErrorMessage(logTitle, errorCode, logMsg, e.getMessage()));
                    }
                }
//            }
        }
    }
}


