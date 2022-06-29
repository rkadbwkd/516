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


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {

    public static String nvl(String input) {
        if ( input == null ) 
            return "";
        else return input.trim();
    }
    
    //string 따옴표 제거
    public static String removeDoubleQuotes(String input) {

        StringBuilder sb = new StringBuilder();

        char[] tab = input.toCharArray();
        for (char current : tab) {
            if (current != '"')
                sb.append(current);
        }

        return sb.toString();
    }

    //기본 filieName
    public static String getDefaultFileName(String fileName, String fileExt) {
        if ( fileName == null ) 
            return "Dime_CRM";
        else return fileName.trim();
    }
    
    //numberformat
    public static String numberFormatter( Integer integer) {
        return  integer.toString();
    }
    
    
    //replace
    public static String replace(String target, String  from_str, String to_str) {
        if (target != null) {
            target.replaceAll(from_str, to_str);
        }
        return target;
    }


    
    public static String pathEndSlashRemove(String input) {
        return input != null ? input.replaceAll("(\\\\|/)+$", "") : input;
    }

    public static boolean urlMatches(String requestUri, String url) {

        if (isEmpty(requestUri) || isEmpty(url)) return false;

        return requestUri.contains(url.replaceAll("^/(.+)/.+$", "$1"));
    }

    public static boolean matches(String regex, CharSequence source) {
        return Pattern.compile(regex).matcher(source).matches();
    }

    public static String replaceAll(String source, String regex, String replacement) {
        return source.replaceAll(regex, replacement);
    }

    public static String getArticleBody(String source, String title, String subTitle) {
        return source.replace(title + System.getProperty("line.separator"), "").replace(subTitle.replace(title + System.getProperty("line.separator"), ""), "");
    }

    public static String bodyToHtmlbody(String input) {
        return input.replaceAll("\n", "<br>");
    }

    public static String getParamSort(String input) {
        if (isEmpty(input)) return "";

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String a : input.split("\\&")) {
            String[] b = a.split("\\=");
            String key = null, value = null;
            if (b.length == 0) {
                continue;
            }
            if (b.length == 1) {
                key = b[0];
                value = "";
            }
            else if (b.length > 1) {
                key = b[0];
                value = b[1];
            }

            if (map.get(key) == null) {
                List<String> values = new ArrayList<String>();
                values.add(value);
                map.put(key, values);
            }
            else {
                map.get(key).add(value);
            }
        }
        StringBuilder rst = new StringBuilder();
        for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            Collections.sort(map.get(key));
            for (String value : map.get(key)) {
                rst.append(key + "=" + value + "&");
            }
        }

        if (rst.length() > 0) rst.delete(rst.length() - 1, rst.length());

        return rst.toString();
    }

    /**
     * null 이거나 빈문자열 인지 체크
     * added 2018.01.28 by 이수철
     * @param obj Object
     * @return null 이거나 객체가 비어 있음
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true; 
        else if (obj instanceof List) {
            return (obj == null || ((List<?>) obj).isEmpty());
        }
        else if (obj instanceof Set) {
            return ((Set<?>) obj).isEmpty();
        }
        else if (obj.getClass().isArray()) {
            return ((Object[]) obj).length==0;
        }
        else if (obj instanceof String) {
            return obj.toString().length()==0;
        }
        else {
            return false;
        }
    }

    public static boolean isTrimEmpty(Object obj) {
        return (obj == null || obj.toString().trim().length() == 0);
    }

    /**
     * 기본문자열에서 비교문자열이 있는지 체크 한다.
     * added 2018.01.03 by 이수철
     * @param baseStr 기본문자열
     * @param inStrs 비교문자
     * @return 존재유무
     */
    public static boolean in(Object baseStr, Object... inStrs) {
        if (baseStr == null) return false;
        for (Object inStr : inStrs) {
            if (baseStr.equals(inStr)) return true;
        }
        return false;
    }

    public static boolean in(Object baseStr, String[] inStrs) {
        if (baseStr == null) return false;
        for (String inStr : inStrs) {
            if (baseStr.equals(inStr)) return true;
        }
        return false;
    }

    /**
     * 채울문자열로 채워진 문자열을 구한다.
     * added 2018.01.24 by 이수철
     * @param fObj 채울문자열
     * @param len 채울길이
     * @return 채워진문자열
     */
    public static String fillStr(Object fObj, int len) {
        String sReturn = "";
        String fStr = fObj.toString();
        if (len < 0) return "";
        for (int i = 0; i < len; i += fStr.length()) {
            sReturn += fStr;
        }
        return sReturn.substring(0, len);
    }

    public static String fillRight(Object oBase, Object fObj, int len) {
        return (oBase.toString() + fillStr(fObj, len - oBase.toString().getBytes().length));
    }

    public static String fillLeft(Object oBase, Object fObj, int len) {
        return (fillStr(fObj, len - oBase.toString().getBytes().length) + oBase);
    }

    public static String strTo(Object baseStr, Object fromStr, Object nullTo) {
        if(baseStr==null) return null;
        if(baseStr.equals(fromStr)) return nullTo.toString();
        else return baseStr.toString();
    }

    /**
     * null 이거나 빈문자열 인 경우 다른문자로 대체한다. (toNotNull)
     * added 2018.01.10 by 이수철
     * @param obj 입력문자열
     * @param nullTo 대체문자
     * @return 처리문자
     */
    public static String toNN(Object obj) {
        return toNN(obj, "");
    }

    public static String toNN(Object obj, Object nullTo) {
        try {
            return (obj == null || obj.equals("")) ? nullTo.toString() : obj.toString();
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public static String prefix(String str, String prefix) {
        String rtnStr = "";
        if (!isEmpty(str)) {
            
            if (!isEmpty(prefix)) {
                rtnStr = prefix + str;
            } else {
                rtnStr = str;
            }
        }
        return rtnStr;
    } 
    
    public static String suffix(String str, String suffix) {
        String rtnStr = "";
        if (!isEmpty(str)) {
            
            if (!isEmpty(suffix)) {
                rtnStr = str + suffix;
            } else {
                rtnStr = str;
            }
        }
        return rtnStr;
    } 
    
    public static String toNumFormat(int num) {
          DecimalFormat df = new DecimalFormat("#,###");
          return df.format(num);
         }
    
}

