/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anylogic.demo.common.file.service.AttachFileEmkoDemoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/anyx/common/file/emkodemo")
public class AttachFileEmkoDemoController {

    private static final Logger logger = LoggerFactory.getLogger(AttachFileEmkoDemoController.class);

    @Autowired
    private AttachFileEmkoDemoService attachFileService;


    @RequestMapping(value = "/downloadImageFile")
    public void attachImage(String uploadPath, String orgFilename, String mimeTypeParam, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("download...");
            logger.debug("파일 정보 : {} - {} - {}", uploadPath, orgFilename);
        }

        OutputStream outputStream = response.getOutputStream();
        if (StringUtils.isEmpty(uploadPath) || StringUtils.isEmpty(orgFilename) || StringUtils.isEmpty(mimeTypeParam)) {
            String errorMessage = "다운로드 파일 정보가 존재하지 않습니다.";
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            if (logger.isDebugEnabled()) logger.debug(errorMessage);
            return;
        }

        String base64Image = "";
        File file = new File(uploadPath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.encodeBase64String(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }

        response.setContentType(mimeTypeParam);
        response.getOutputStream().write(base64Image.getBytes());
    }



   /* @RequestMapping(value = "/getFileData", method = RequestMethod.GET)
    public ResultListVO getFileData(@RequestParam Map<String, Object> parameter) {

        ResultListVO resultListVO = new ResultListVO();
        resultListVO.setRows(attachFileService.getFileData(parameter));

        return resultListVO;
    }*/


    //    @RequestMapping(value = "/fileDownload")
//    public void download(String uploadPath,  String orgFilename, String mimeTypeParam, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
//        if(logger.isDebugEnabled()) {
//            logger.debug("download...");
//            logger.debug("파일 정보 : {} - {} - {}", uploadPath,  orgFilename);
//        }
//
//        OutputStream outputStream = response.getOutputStream();
//        if(StringUtils.isEmpty(uploadPath) || StringUtils.isEmpty(orgFilename)  || StringUtils.isEmpty(mimeTypeParam)) {
//            String errorMessage = "다운로드 파일 정보가 존재하지 않습니다.";
//            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
//            outputStream.close();
//            if(logger.isDebugEnabled()) logger.debug(errorMessage);
//
//            return;
//        }
//
//        if(logger.isDebugEnabled()) logger.debug(uploadPath);
//        File file = new File(uploadPath);
//
//        if(!file.exists()) {
//            String errorMessage = "다운로드 파일이 존재하지 않습니다.";
//            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
//            outputStream.close();
//
//            if(logger.isDebugEnabled()) logger.debug(errorMessage);
//            return;
//        }
//
//        if(logger.isDebugEnabled()) logger.debug(mimeTypeParam);
//
//        response.setContentType(mimeTypeParam);
//        response.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(orgFilename, "UTF-8") + "\"");
//        response.setContentLength((int) file.length());
//
//        int comma = orgFilename.indexOf(".");
//        String fileExt = orgFilename.substring(comma, orgFilename.length());
//
//        response.getOutputStream().write(readFile2(uploadPath));
//    }
    @RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
    public void fileDownload(@RequestParam Map<String, Object> parameter, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {

        String uploadPath = parameter.get("uploadPath").toString();
        uploadPath = new File(uploadPath).getCanonicalPath();
        String orgFilename = parameter.get("orgFilename").toString();
//       String confirmStr = orgFilename.substring(0,orgFilename.lastIndexOf("."));
        //
        //
//       if(confirmStr.contains(".") || confirmStr.contains("/")|| confirmStr.contains("\\") || confirmStr.contains("%")) {
//          return;
//       }

        System.out.println(uploadPath);
        System.out.println(orgFilename);

        String mimeTypeParam = "application/octet-stream";
        if (logger.isDebugEnabled()) {
            logger.debug("download...");
            logger.debug("파일 정보 : {} - {} - {}", uploadPath, orgFilename);
        }

        OutputStream outputStream = response.getOutputStream();
   /*      if(StringUtils.isEmpty(uploadPath) || StringUtils.isEmpty(orgFilename)  || StringUtils.isEmpty(mimeTypeParam)) {
             String errorMessage = "다운로드 파일 정보가 존재하지 않습니다.";
             outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
             outputStream.close();
             if(logger.isDebugEnabled()) logger.debug(errorMessage);

             return;
         }*/

        if (logger.isDebugEnabled()) logger.debug(uploadPath);
        File file = new File(uploadPath);

        if (!file.exists()) {
            String errorMessage = "다운로드 파일이 존재하지 않습니다.";
            throw new RuntimeException();
            //outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            //outputStream.close();

            // if(logger.isDebugEnabled()) logger.debug(errorMessage);
            // return;
        }

        if (logger.isDebugEnabled()) logger.debug(mimeTypeParam);

        response.setContentType(mimeTypeParam);
        response.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(orgFilename, "UTF-8") + "\"");
        response.setContentLength((int) file.length());

        int comma = orgFilename.indexOf(".");
        String fileExt = orgFilename.substring(comma, orgFilename.length());

        response.getOutputStream().write(readFile2(uploadPath));
    }

    //외부연동용 fileDownload
    //실파일명을 파라미터로 받아 다운로드 되도록 구현
    @RequestMapping(value = "/fileDownloadByFilename")
    public void downloadFileByFileName(String file_name, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("download...");
            logger.debug("파일 정보 : {} - {} - {}", file_name);
        }

        OutputStream outputStream = response.getOutputStream();
        if (StringUtils.isEmpty(file_name)) {
            String errorMessage = "다운로드 파일 정보가 존재하지 않습니다.";
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            if (logger.isDebugEnabled()) logger.debug(errorMessage);

            return;
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("file_name", file_name);

        //첨부파일에서 파일명이 있는지 확인한다
        List<Map<String, Object>> resultList = attachFileService.selectAttachFileByRecord(paramMap);

        if (resultList == null || resultList.size() <= 0) {
            logger.error(">>>>>>  fileDownloadByFileName : file_name = " + file_name + " 이 없습니다.");
            return;
        }

        String orgFilename = (String) resultList.get(0).get("file_origin_name");
        String uploadPath = (String) resultList.get(0).get("file_path");

        if (logger.isDebugEnabled()) logger.debug(file_name);
        File file = new File(uploadPath);

        if (!file.exists()) {
            String errorMessage = "다운로드 파일이 존재하지 않습니다.";
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();

            if (logger.isDebugEnabled()) logger.debug(errorMessage);
            return;
        }


        String mimeTypeParam = "application/octet-stream";

        response.setContentType(mimeTypeParam);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(orgFilename, "UTF-8") + "\"");
        //response.setHeader("Content-Transfer-Encoding", "binary" );
        response.setContentLength((int) file.length());

        int comma = orgFilename.indexOf(".");
        String fileExt = orgFilename.substring(comma, orgFilename.length());

        response.getOutputStream().write(readFile2(uploadPath));
    }

    public static byte[] readFile2(String fileName) {
        FileInputStream fis = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(fileName);
            data = new byte[fis.available()];
            fis.read(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                ;
            }
        }
        return data;
    }

    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        logger.debug("파일업로드 실행");
        // return attachFileService.attachFile(file);
        return false;
    }
}

