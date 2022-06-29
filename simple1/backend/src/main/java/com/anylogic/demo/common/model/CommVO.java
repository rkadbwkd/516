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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <PRE>
 *  ClassName : CommVO
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 5. 22. 오후 3:46:46
 * @author  : jkkim
 * @brief   :
 */

//@JsonInclude(Include.NON_NULL)
public class CommVO {

    @JsonIgnore
    // 현재 페이지
    protected int pageNum = 1;

    @JsonIgnore
    // 페이지 사이즈
    protected int pagePerCnt = 10;

    @JsonIgnore
    // 토탈 갯수
    protected int totalCnt = 0;

    @JsonIgnore
    // 최대 페이지
    protected int maxViewPage = 0;

    @JsonIgnore
    // 역순 번호
    protected int rnum;

    /**
     * @return the pageNum
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum the pageNum to set
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return the pagePerCnt
     */
    public int getPagePerCnt() {
        return pagePerCnt;
    }

    /**
     * @param pagePerCnt the pagePerCnt to set
     */
    public void setPagePerCnt(int pagePerCnt) {
        this.pagePerCnt = pagePerCnt;
    }

    /**
     * @return the totalCnt
     */
    public int getTotalCnt() {
        return totalCnt;
    }

    /**
     * @param totalCnt the totalCnt to set
     */
    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    /**
     * @return the maxViewPage
     */
    public int getMaxViewPage() {
        return maxViewPage;
    }

    /**
     * @param maxViewPage the maxViewPage to set
     */
    public void setMaxViewPage(int maxViewPage) {
        this.maxViewPage = maxViewPage;
    }

    /**
     * @return the rnum
     */
    public int getRnum() {
        return rnum;
    }

    /**
     * @param rnum the rnum to set
     */
    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CommVO [pageNum=" + pageNum + ", pagePerCnt=" + pagePerCnt
                + ", totalCnt=" + totalCnt + ", maxViewPage=" + maxViewPage
                + ", rnum=" + rnum + "]";
    }


}



