/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.demo.common.mail.vo;

import java.io.Serializable;

/**
 * <PRE>
 *  ClassName : FindPwVO
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 26. 오후 2:21:44
 * @author  : jun
 * @brief   :
 */

public class FindPwVO implements Serializable{
    private static final long serialVersionUID = -7534685903622346106L;

    private String password;
    private String transDate;

    public FindPwVO(){
        super();
    }

    public FindPwVO(String password, String transDate){
        super();
        this.password = password;
        this.transDate = transDate;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the transDate
     */
    public String getTransDate() {
        return transDate;
    }

    /**
     * @param transDate the transDate to set
     */
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FindPwVO [password=" + password + ", transDate=" + transDate
                + "]";
    }

}

