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

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//import com.wordnik.swagger.annotations.ApiModel;
//import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
public class PageVO {
    @ApiModelProperty(required = true, value = "Total contents count", dataType = "totalCount.class")
    private int totalCount;

    @SuppressWarnings("rawtypes")
    @ApiModelProperty(required = true, value = "List of contents")
    private List lists;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @SuppressWarnings("rawtypes")
    public List getLists() {
        return lists;
    }

    @SuppressWarnings("rawtypes")
    public void setLists(List lists) {
        this.lists = lists;
    }


}
