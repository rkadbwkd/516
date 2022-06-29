/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.excel.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <PRE>
 *  ClassName : ExcelVO
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 22. 오후 12:52:51
 * @author  : moo
 * @brief   :
 */

public class ExcelVO {
    public static HttpServletRequest getCurrentRequest() {
           ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
           HttpServletRequest hsr = sra.getRequest();
           return hsr;
    }
    private String excelPathTemplate = (ExcelVO.getCurrentRequest().getSession().getServletContext().getRealPath("/").endsWith("/") 
                                        || ExcelVO.getCurrentRequest().getSession().getServletContext().getRealPath("/").endsWith("\\")) ? 
                                        ExcelVO.getCurrentRequest().getSession().getServletContext().getRealPath("/") + "excel/template/"
                                        : ExcelVO.getCurrentRequest().getSession().getServletContext().getRealPath("/") + "/excel/template/";

    private String excelPathTemp;

    private String tempFileName;
    /**
     * @return the excelPathTemplate
     */
    public String getExcelPathTemplate() {
        return excelPathTemplate;
    }
    /**
     * @param excelPathTemplate the excelPathTemplate to set
     */
    public void setExcelPathTemplate(String excelPathTemplate) {
        this.excelPathTemplate = excelPathTemplate;
    }
    /**
     * @return the excelPathTemp
     */
    public String getExcelPathTemp() {
        return excelPathTemp;
    }
    /**
     * @param excelPathTemp the excelPathTemp to set
     */
    public void setExcelPathTemp(String excelPathTemp) {
        this.excelPathTemp = excelPathTemp;
    }
    /**
     * @return the tempFileName
     */
    public String getTempFileName() {
        return tempFileName;
    }
    /**
     * @param tempFileName the tempFileName to set
     */
    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }



}



