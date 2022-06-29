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

import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @name FileWriter
 *
 * @ver 0.1
 *
 * @date 2015.03.18
 *
 * @author shin.
 */

public class FileWriter {
    private static final Logger logger = LoggerFactory.getLogger(FileWriter.class);

    private FileOutputStream fos;

    public String writeFile(MultipartFile file, String path, String fileName){
        logger.debug("=====================================================================");
        logger.debug("Write File Name: " + fileName);
        logger.debug("=====================================================================");

        try{
            byte fileData[] = file.getBytes();
            fos = new FileOutputStream(path + fileName);
            fos.write(fileData);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (fos != null) { try { fos.close(); } catch (Exception e2) {e2.printStackTrace();}}
        }
        return fileName;
    }
}

