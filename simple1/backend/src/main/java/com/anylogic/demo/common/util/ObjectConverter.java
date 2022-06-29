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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter {

    public static String toJSONString(Object obj) throws JsonProcessingException {
        return ObjectConverter.toJSONString(obj, Include.NON_NULL);
    }

    public static String toJSONString(Object obj, Include include) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(include);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static JSONObject toJSON(Object obj) throws JsonProcessingException, ParseException {
        return ObjectConverter.toJSON(obj, Include.NON_NULL);
    }

    public static JSONObject toJSON(Object obj, Include include) throws JsonProcessingException, ParseException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(include);

        String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        jsonStr = jsonStr.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("\\t", "");

        JSONObject json = (JSONObject)new JSONParser().parse(jsonStr);

        return json;
    }

    public static JSONObject toJSON(String jsonStr) throws JsonProcessingException, ParseException {
        return ObjectConverter.toJSON(jsonStr, Include.NON_NULL);
    }

    public static JSONObject toJSON(String jsonInputStr, Include include) throws JsonProcessingException, ParseException {

        JSONObject orgJson = (JSONObject)new JSONParser().parse(jsonInputStr);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(include);

        jsonInputStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(orgJson);

        JSONObject newJson = (JSONObject)new JSONParser().parse(jsonInputStr);

        return newJson;
    }
    
    public static Map<String, Object> toMap(Object obj){
        try {
            ///Field[] fields = obj.getClass().getFields(); //private field는 나오지 않음.
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            for(int i=0; i<=fields.length-1;i++){
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }
            return resultMap;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static JSONArray convertListToJson(List<HashMap<String, Object>> bankCdList) {

        JSONArray jsonArray = new JSONArray();

        for (Map<String, Object> map : bankCdList) {

            jsonArray.add(convertMapToJson(map));

        }

        return jsonArray;

    }
    // map 을 json 형태로 변형

    @SuppressWarnings({ "unchecked" })

    public static JSONObject convertMapToJson(Map<String, Object> map) {

        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            String key = entry.getKey();

            Object value = entry.getValue();

            // json.addProperty(key, value);

            json.put(key, value);

        }

        return json;

    }


    
}

