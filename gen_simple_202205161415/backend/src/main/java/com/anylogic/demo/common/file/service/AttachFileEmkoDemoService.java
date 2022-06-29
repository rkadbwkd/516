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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anylogic.demo.common.Const;
import com.anylogic.demo.common.excel.service.ExcelSVC;
import com.anylogic.demo.common.exception.AnyXException;
import com.anylogic.demo.common.file.mapper.AttachFileEmkoDemoMapper;
import com.anylogic.demo.common.util.PropUtil;
import com.anylogic.demo.common.util.excel.ExcelRead;
import com.anylogic.demo.common.util.excel.ExcelReadOption;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Transactional
public class AttachFileEmkoDemoService implements AttachFileService {

    @Autowired
    private AttachFileEmkoDemoMapper attachFileMapper;

    @Autowired
    private ExcelSVC excelSVC;

    private static final Logger logger = LoggerFactory.getLogger(AttachFileEmkoDemoService.class);

    @Override
    public List<Map<String, Object>> selectAttachFileByRecord(Map<String, Object> parameter)
    {
        return attachFileMapper.selectAttachFileByRecord(parameter);
    }

    @Override
    public Map<String, Object> getAttachFile(Map<String, Object> parameter)
    {
        return attachFileMapper.getAttachFile(parameter);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED)
    public List<Map<String, Object>> insertAttachFile(List<MultipartFile> mf, Map<String, Object> data, String bizPath)
    {
        if (mf == null || mf.isEmpty() || mf.size() <= 0) {
            logger.warn("noParam list");
            return null;
        }

        String absolutePath = new File("").getAbsolutePath();

        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();
        int failCnt = -1;

        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get ( cal.YEAR );
        int month = cal.get ( cal.MONTH ) + 1 ;
        int date = cal.get ( cal.DATE ) ;

        File dir = null;

        List<Map<String, Object>> newFileList = new ArrayList<Map<String, Object>>();
        newFileList = (List<Map<String, Object>>)data.get("newAddFile");

        List<File> files = new ArrayList<>();

        try {
            for (int i = 0; i < mf.size(); i++) {
                MultipartFile multipartFile = mf.get(i);

                if (multipartFile != null && !multipartFile.isEmpty()) {
                    UUID uuid = UUID.randomUUID();

                    int pos = multipartFile.getOriginalFilename().lastIndexOf(".");
                    String ext = multipartFile.getOriginalFilename().substring(pos); // 이거는 . 포함 확장자만 ex) .jpg

                    String saveMiddlePath = "";
                    if(bizPath.equalsIgnoreCase("") || bizPath == null){
                        saveMiddlePath = ( bizPath == null ? "" : bizPath );
                    }
                    else
                    {
                        saveMiddlePath = data.get("table_name").toString() + year + "/" + month + "/" + date + "/";
                    }

                    String fileUrl = "http://localhost:8580" + "/file_upload" + saveMiddlePath + "/" + uuid.toString() + ext;
                    String saveFullPath = absolutePath + "/src/main/webapp/public" + "/file_upload" + saveMiddlePath + "/" + uuid.toString() + ext;

                    dir = new File(saveFullPath);

                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    File f = new File(saveFullPath);
                    files.add(f);
                    multipartFile.transferTo(f);
                    Map<String, Object> insertFileParam = new HashMap<String, Object>(data);

                    insertFileParam.put("file_name", uuid.toString() + ext);
                    insertFileParam.put("file_path", saveFullPath);
                    insertFileParam.put("file_origin_name", multipartFile.getOriginalFilename());
                    insertFileParam.put("comments", (String)newFileList.get(i).getOrDefault("comments", ""));
                    insertFileParam.put("file_url", fileUrl);

                    logger.info("insertUserFileMulti.insertFileParam()  {}", insertFileParam);
                    int insertResult = attachFileMapper.insertAttachFile(insertFileParam);
                    insertFileParam.put("fileInsertResult", insertResult);

                    rtnMap.add(insertFileParam);
                } // if
            } // for
        }
        catch(Exception e){
            File tf = null;
            for(int i=0; i<files.size(); i++){
                files.get(i).delete();
            }
            throw new AnyXException(e.getMessage());
        }
        return rtnMap;
    }


    @Override
    public List<Map<String, Object>> updateAttachFileByRecord(List<MultipartFile> mf, Map<String, Object> sendInfo, String bizPath)
   // ,  String atcFileId, HttpServletRequest request, HttpServletResponse response)
    {
        List<Map<String, Object>> delFileList = new ArrayList<Map<String, Object>>();
        delFileList = (List<Map<String, Object>>)sendInfo.get("listDelFile");
        for(Map<String, Object> delFile : delFileList){
            this.deleteAttachFile(delFile);
        }

        // 파라메터 없으면 null 반환
        if (mf == null || mf.isEmpty() || mf.size() <= 0) {
            logger.warn("noParam list");
            return null;
        }

        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();

        rtnMap.addAll(this.insertAttachFile(mf, sendInfo, bizPath));
        return rtnMap;
    }

    @Override
    public List<Map<String, Object>> deleteAttachFileByRecordDelYN(Map<String, Object> sendInfo)
    {
        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> fileInfos = attachFileMapper.selectAttachFileByRecord(sendInfo);
        try {
            if (fileInfos.size() > 0) {
                for (Map<String, Object> fileInfo : fileInfos)
                {
                    int res = 0;
                    res = attachFileMapper.deleteAttachFileDelYN(fileInfo);
                    fileInfo.put("fileInsertResult", res);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }
        return rtnMap;
    }


    @Override
    public List<Map<String, Object>> deleteAttachFileByRecord(Map<String, Object> sendInfo)
    {
        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();

        List<Map<String, Object>> fileInfos = attachFileMapper.selectAttachFileByRecord(sendInfo);
        try {
            if (fileInfos.size() > 0) {
                for (Map<String, Object> fileInfo : fileInfos)
                {
                    String fileFullPath = fileInfo.get("file_path").toString();
                    File delFile = new File(fileFullPath);
                    delFile.delete();

                    int res = 0;
                    res = attachFileMapper.deleteAttachFile(fileInfo);
                    fileInfo.put("fileInsertResult", res);
                }
            }
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }
        return rtnMap;
    }



    @Override
    public List<Map<String, Object>> deleteAttachFile(Map<String, Object> sendInfo)
    {
        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();

        Map<String, Object> fileInfo = attachFileMapper.getAttachFile(sendInfo);
        try {
            String fileFullPath = fileInfo.get("file_path").toString();
            File delFile = new File(fileFullPath);
            delFile.delete();

            int res = 0;
            res = attachFileMapper.deleteAttachFile(fileInfo);
            fileInfo.put("fileDeleteResult", res);
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }
        return rtnMap;
    }



    public void insertFileDataExcelUpload(File destFile) throws Exception {
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setOutputColumns("A", "B", "C", "D", "E", "F");
        excelReadOption.setStartRow(2);

        List<Map<String, String>> excelContent = ExcelRead.read(excelReadOption);

        for (Map<String, String> article : excelContent) {
            System.out.println(article.get("A"));
            System.out.println(article.get("B"));
            System.out.println(article.get("C"));
            System.out.println(article.get("D"));
            System.out.println(article.get("E"));
            System.out.println(article.get("F"));
        }
    }



    public List<Map<String, Object>> insertExcel (HttpServletRequest request) {
        List<Map<String, Object>> rtnMap = new ArrayList<Map<String, Object>>();
        File file = null;

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> lmf = multipartRequest.getFiles("excelFile");

            if (lmf != null && lmf.isEmpty() == false && lmf.size() > 0) { // if1
                for (int i = 0; i < lmf.size(); i++) { // for1
                    MultipartFile mf = lmf.get(i);
                    if (mf != null && mf.isEmpty() == false) { // if2
                        logger.info("insertExcel().mf  {}", mf);

                        String tempExcelPath = PropUtil.getInstance().getValues("properties/serverpath.properties", "tempExcelPath");

                        int pos = mf.getOriginalFilename().lastIndexOf(".");
                        //String ext = mf.getOriginalFilename().substring(pos + 1); // 이거는 . 없이 확장자만 ex) jpg
                        String ext = mf.getOriginalFilename().substring(pos); // 이거는 . 포함 확장자만 ex) .jpg

                        UUID uuid = UUID.randomUUID();

                        String savePath = tempExcelPath + "\\";
                        String saveFullPath = savePath + uuid.toString() + ext;

                        file = new File(savePath);
                        if (!file.isDirectory()) {
                            file.mkdirs();
                        }

                        mf.transferTo(new File(saveFullPath));

                        Map<String, Object> fileInfo = new HashMap<String, Object>();
                        fileInfo.put("excelPath", saveFullPath);
                        rtnMap.add(fileInfo);
                    } // if2
                } // for1
            } // if1

        }
        catch(Exception e){
            e.printStackTrace();
            throw new AnyXException(e.getMessage());
        }

        return rtnMap;
    }

    public void fileDownload(HttpServletResponse response, Map<String, Object> parameter) {
        Map<String, Object> file = null;
        file = attachFileMapper.getAttachFile(parameter);

        if (file != null && !file.isEmpty()) {
            byte[] fileByte = new byte[0];
            try {
                fileByte = FileUtils.readFileToByteArray(new File((String) file.get("file_path")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            response.setContentType("application/octet-stream");
            response.setContentLength(fileByte.length);
            try {
                response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode((String) file.get("file_origin_name"),"UTF-8")+"\";");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //response.setHeader("Content-Transfer-Encoding", "binary");
            try {
                response.getOutputStream().write(fileByte);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

