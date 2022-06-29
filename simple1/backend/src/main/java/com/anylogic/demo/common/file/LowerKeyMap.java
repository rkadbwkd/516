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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LowerKeyMap {

    public static Map changeToLowerMapKey (Map map) {
        Map<String, Object> origin = map;
        Map<String, Object> temp = new HashMap<String, Object>();
        
        Set<String> set = origin.keySet();
        Iterator<String> e = set.iterator();
        
        while (e.hasNext()) {
            String key = e.next();
            Object value = (Object) origin.get(key);
            temp.put(key.toLowerCase(), value);
        }
        origin = null;
        return temp;
    }
}
