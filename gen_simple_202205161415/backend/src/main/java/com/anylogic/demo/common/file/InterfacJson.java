/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

package com.anylogic.demo.common.file;

import com.anylogic.demo.common.util.ObjectConverter;
import org.json.simple.JSONObject;

/**
 * <PRE>
 *  ClassName : InterfacJson
 * </PRE>
 * @version : 1.0
 * @date    : 2015. 6. 9. 오후 5:09:21
 * @author  : jun
 * @brief   :
 */

public class InterfacJson {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JSONObject convertJson(){
        JSONObject body = null;
        try {

            if(result == null || "".equals(result)) {
                body = null;
            }else {
                body = ObjectConverter.toJSON(this.result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return body;
    }



}

