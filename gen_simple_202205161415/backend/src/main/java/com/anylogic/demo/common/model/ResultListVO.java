/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
/**
 * VO 객체가 아닌 List를 리턴하는 경우 Page 정보와 같이 리턴
 */

public class ResultListVO implements Serializable{

    private static final long serialVersionUID = 1953666707942320442L;
    private int total = 0;
    private int page = 1;
    private int rowNum = 10;
    private int maxPage = 0;
    private String authToken;

    private List<Object> rows;
    
    private Map<String, Object> baseData;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Object> getRows() {
        return rows;
    }


    /**
     * @return the maxPage
     */
    public int getMaxPage() {
        return maxPage;
    }

    /**
     * @param maxPage the maxPage to set
     */
    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setRows(List<Object> rows) {

        try{
            Object obj = null;
            if(rows != null  && rows.size() > 0){
                obj =  rows.get(0);
        
//            for(Object obj : rows){
                for(Field field : obj.getClass().getSuperclass().getDeclaredFields()){
                    field.setAccessible(true);
                    switch(field.getName()){
                        case "pageNum" :
                            setPage((Integer)field.get(obj));
                            break;
                        case "pagePerCnt" :
                            break;
                        case "maxViewPage" :
                            setMaxPage((Integer)field.get(obj));
                            break;
                        case "totalCnt" :
                            setTotal((Integer)field.get(obj));
                            break;
                        case "authToken" : 
                            setAuthToken((String)field.get(obj));
                        default:
                            break;
                    }
                }
//                break;
//            }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        this.rows = rows;
    }

    public Map<String, Object> getBaseData() {
        return baseData;
    }

    public void setBaseData(Map<String, Object> baseData) {
        this.baseData = baseData;
    }

    public void setAuthToken(String authToken) {
        // TODO Auto-generated method stub
        this.authToken = authToken;
    }
    
    public String getAuthToken() {
        return this.authToken;
    }

}
                
