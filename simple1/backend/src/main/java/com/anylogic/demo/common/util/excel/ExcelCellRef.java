/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class ExcelCellRef {
    
    private static final Logger log = LoggerFactory.getLogger(ExcelCellRef.class);
    
    /**
     * Cell에 해당하는 Column Name을 가젼온다(A,B,C..)
     * 만약 Cell이 Null이라면 int cellIndex의 값으로
     * Column Name을 가져온다.
     * @param cell
     * @param cellIndex
     * @return
     */
    public static String getName(Cell cell, int cellIndex) {
        int cellNum = 0;
        if(cell != null) {
            cellNum = cell.getColumnIndex();
        }
        else {
            cellNum = cellIndex;
        }
        
        return CellReference.convertNumToColString(cellNum);
    }
    
    public static String getValue(Cell cell) {
        String value = "";
        
        if(cell == null) {
            value = "";
        }
        else {
            if( cell.getCellTypeEnum() == CellType.FORMULA ) {
                value = cell.getCellFormula();
            }
            else if( cell.getCellTypeEnum() == CellType.NUMERIC ) {
                //value = cell.getNumericCellValue() + "";
                value = (int)cell.getNumericCellValue() + "";
            }
            else if( cell.getCellTypeEnum() == CellType.STRING ) {
                value = cell.getStringCellValue();
            }
            else if( cell.getCellTypeEnum() == CellType.BOOLEAN ) {
                value = cell.getBooleanCellValue() + "";
            }
            else if( cell.getCellTypeEnum() == CellType.ERROR ) {
                value = cell.getErrorCellValue() + "";
            }
            else if( cell.getCellTypeEnum() == CellType.BLANK ) {
                value = "";
            }
            else {
                value = cell.getStringCellValue();
            }
        }
        
        return value;
    }
 
}

