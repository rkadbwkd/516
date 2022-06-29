/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.excel.service;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.anylogic.demo.common.excel.model.ExcelVO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 *  ClassName : ExcelSVC
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 22. 오후 12:59:56
 * @author  : moo
 * @brief   :
 */
@Service
public class ExcelSVC<Q> {


    @Value("${file.excelFileUploadPath}")
    private String atcFileDir;

    public String getPath(String menuNm){

        // 업로드 경로
//        String rsVal = atcFileDir;
        StringBuffer rsVal = new StringBuffer();
        rsVal.append(atcFileDir);

        //yyyymm
        SimpleDateFormat sdfDt_yyyymm = new SimpleDateFormat("yyyymm", Locale.KOREA);
        String strDt_yyyymm = sdfDt_yyyymm.format(System.currentTimeMillis());
        //dd
        SimpleDateFormat sdfDt_dd = new SimpleDateFormat("dd",Locale.KOREA);
        String strDt_dd = sdfDt_dd.format(System.currentTimeMillis());

        rsVal.append(menuNm).append("/").append(strDt_yyyymm).append("/").append(strDt_dd).append("/");
        return rsVal.toString();
    }
    
    public void getCalExcelDownLoad(String fileName, String fileExt, String listName, Q reqVO, Map<String, Object> excelParam, HttpServletResponse response) throws ParsePropertyException, InvalidFormatException, IOException {
        ExcelVO excelVO = new ExcelVO();

        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put(listName, reqVO);
        beans.put("excelParam", excelParam);
        // 엑셀 템플릿 파일 경로
        String excelPathTemplate = excelVO.getExcelPathTemplate();
        String templateFile = excelPathTemplate + fileName + "." + fileExt;

        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
        String strDT = dayTime.format(new Date(time));

        String tempFileName = fileName + "_" + strDT;
        excelVO.setTempFileName(tempFileName + "." + fileExt);

        XLSTransformer transformer = new XLSTransformer();

        File uFile = new File(templateFile);
        Workbook wb = transformer.transformXLS(new FileInputStream(uFile), beans);

        /*response.setHeader( "Content-disposition", "attachment;filename=" + new String(excelVO.getTempFileName().getBytes("utf-8"), "8859_1"));*/
        response.setHeader( "Content-disposition", "attachment;filename=" + URLEncoder.encode(excelVO.getTempFileName(), "UTF-8"));
        response.setContentType("application/x-msexcel");

        wb.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.getOutputStream().close();

   }

   public void getExcelDownLoad2(String fileName, String productionNo, String fileExt, String listName, Q reqVO, HttpServletResponse response) throws ParsePropertyException, InvalidFormatException, IOException {
        ExcelVO excelVO = new ExcelVO();

        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put(listName, reqVO);

        // 엑셀 템플릿 파일 경로
        String excelPathTemplate = excelVO.getExcelPathTemplate();
        String templateFile = excelPathTemplate + fileName + "." + fileExt;

        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String strDT = dayTime.format(new Date(time));

        String tempFileName = fileName + "_"+ productionNo + "_" + strDT;
        excelVO.setTempFileName(tempFileName + "." + fileExt);

        XLSTransformer transformer = new XLSTransformer();

        File uFile = new File(templateFile);
        Workbook wb = transformer.transformXLS(new FileInputStream(uFile), beans);

        /*response.setHeader( "Content-disposition", "attachment;filename=" + new String(excelVO.getTempFileName().getBytes("utf-8"), "8859_1"));*/
        response.setHeader( "Content-disposition", "attachment;filename=" + URLEncoder.encode(excelVO.getTempFileName(), "UTF-8"));
        response.setContentType("application/x-msexcel");

        wb.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    public void getExcelDownLoad(String fileName, String fileExt, String listName, Q reqVO, HttpServletResponse response) throws ParsePropertyException, InvalidFormatException, IOException {
        ExcelVO excelVO = new ExcelVO();

        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put(listName, reqVO);

        // 엑셀 템플릿 파일 경로
        String excelPathTemplate = excelVO.getExcelPathTemplate();
        String templateFile = excelPathTemplate + fileName + "." + fileExt;

        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        String strDT = dayTime.format(new Date(time));

        String tempFileName = fileName + "_" + strDT;
        excelVO.setTempFileName(tempFileName + "." + fileExt);

        XLSTransformer transformer = new XLSTransformer();

        File uFile = new File(templateFile);
        Workbook wb = transformer.transformXLS(new FileInputStream(uFile), beans);
        
        /*response.setHeader( "Content-disposition", "attachment;filename=" + new String(excelVO.getTempFileName().getBytes("utf-8"), "8859_1"));*/
        response.setHeader( "Content-disposition", "attachment;filename=" + URLEncoder.encode(excelVO.getTempFileName(), "UTF-8"));
        response.setContentType("application/x-msexcel");

        wb.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.getOutputStream().close();

   }
    
    
    public void  getExcelDownLoad3(String fileName, String fileExt, String listName, Q reqVO, HttpServletResponse response) throws ParsePropertyException, InvalidFormatException, IOException {
        ExcelVO excelVO = new ExcelVO();

        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("res_code", "200");
        returnMap.put("res_msg", "success");
        
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put(listName, reqVO);

        // 엑셀 템플릿 파일 경로
        String excelPathTemplate = excelVO.getExcelPathTemplate();
        String templateFile = excelPathTemplate + fileName + "." + fileExt;

        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
        String strDT = dayTime.format(new Date(time));

        String tempFileName = fileName + "_" + strDT;
        excelVO.setTempFileName(tempFileName + "." + fileExt);

        XLSTransformer transformer = new XLSTransformer();

        File uFile = new File(templateFile);
        Workbook wb = transformer.transformXLS(new FileInputStream(uFile), beans);

        /*response.setHeader( "Content-disposition", "attachment;filename=" + new String(excelVO.getTempFileName().getBytes("utf-8"), "8859_1"));*/
        response.setHeader( "Content-disposition", "attachment;filename=" + URLEncoder.encode(excelVO.getTempFileName(), "UTF-8"));
        response.setContentType("application/x-msexcel");

        wb.write(response.getOutputStream());

        response.getOutputStream().flush();
        response.getOutputStream().close();

        returnMap.put("res_code", "500");
        returnMap.put("res_msg", "success");

   }
    
    
    /**
     * <PRE>
     *  MethodName : getExcelUpload
     * </PRE>
     * @author : moo
     * @date   : 2015. 6. 25. 오후 6:32:33
     * @param  :
     * @return : List<Map<String,Object>>
     * @brief  : 엑셀 업로드 서비스
     * @param file
     * @param column
     * @return
     * @throws ParsePropertyException
     * @throws InvalidFormatException
     * @throws IOException
     */
    public List<Map<String, Object>> getExcelUpload(File file, String [] column)
            throws ParsePropertyException, InvalidFormatException, IOException{
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        //첫번째 시트
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 행별로 처리
        Iterator<Row> rowIterator = sheet.iterator();
        // 최종 결과물로 내려보낼 데이터 셋
         List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
         Map<String, Object> voMap = null;
        // 헤더로우 스킵
        rowIterator.next();
        Cell cell = null;
        Object value = null;
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            voMap = new HashMap<String, Object>();
            for(int i=0; i<column.length; i++){
                cell = row.getCell(i);
                if(cell==null){
                    value = "";
                }else{
                    if( cell.getCellTypeEnum() == CellType.FORMULA ) {
                        value = cell.getCellFormula();
                    }
                    else if( cell.getCellTypeEnum() == CellType.NUMERIC ) {
                        //value = cell.getNumericCellValue() + "";
                        value = Double.parseDouble(cell.getNumericCellValue() + "");
                    }
                    else if( cell.getCellTypeEnum() == CellType.STRING ) {
                        value = cell.getStringCellValue();
                    }
                    else if( cell.getCellTypeEnum() == CellType.BOOLEAN ) {
                        value = cell.getBooleanCellValue();
                    }
                    else if( cell.getCellTypeEnum() == CellType.ERROR ) {
                        value = cell.getErrorCellValue();
                    }
                    else if( cell.getCellTypeEnum() == CellType.BLANK ) {
                        value = "";
                    }
                    else {
                        value = cell.getStringCellValue();
                    }
                }
                voMap.put(column[i], value);
            }
            lists.add(voMap);
        }
        fis.close();
        return lists;
    }

    public void nullCheck(String str) throws Exception{
        if(str.equals("")){
            throw new Exception();
        }
    }

    /*public List<Map<String, Object>> getExcelUpload(File file, String [] column)
            throws ParsePropertyException, InvalidFormatException, IOException{
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        //첫번째 시트
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 행별로 처리
        Iterator<Row> rowIterator = sheet.iterator();
        // 최종 결과물로 내려보낼 데이터 셋
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            Map<String, Object> voMap = null;
        // 헤더로우 스킵
        rowIterator.next();
        Cell cell = null;
        Object value = null;
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();
            voMap = new HashMap<String, Object>();
            //for(int i=0; i<column.length; i++){
            int i = 0;
            //for(int i=0; i<column.length; i++){
            while(cellIterator.hasNext()){
                //cell = row.getCell(i);
                cell = cellIterator.next();
                switch (cell.getCellTypeEnum())
                {
                   case HSSFCell.CELL_TYPE_FORMULA : // 수식처리를 한 셀(A1+B1)을 가져올경우 이것을 이용한다.
                       value = cell.getCellFormula();
                       break;
                   case HSSFCell.CELL_TYPE_NUMERIC : // 엑셀타입에서 숫자형의 경우를 받아온다.
                       value = new Double(cell.getNumericCellValue()).intValue(); //double
                        break;    // 엑셀타입에서 문자형을 받아온다.
                   case HSSFCell.CELL_TYPE_STRING :
                        value = cell.getStringCellValue(); //String
                        break; // 값이 없을 경우의 처리
                   case HSSFCell.CELL_TYPE_BLANK :
                        value = "";
                        break; // BOOLEAN형의 처리
                   case HSSFCell.CELL_TYPE_BOOLEAN :
                        value = cell.getBooleanCellValue(); //boolean
                        break; // 에러 바이트를 출력한다.
                   case HSSFCell.CELL_TYPE_ERROR :
                        value = cell.getErrorCellValue(); // byte
                        break;
                   default :
                 }
                voMap.put(column[i], value);
                i++;
            }
            lists.add(voMap);
        }
        fis.close();
        return lists;
    }*/

}


