/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.crud.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import com.anylogic.demo.common.exception.AnyXException;
import com.anylogic.demo.crud.user.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.anylogic.demo.common.model.ResultListVO;



@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserMapper userMapper;

    private FastDateFormat frm = FastDateFormat.getInstance("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    public ResultListVO selectUser(Map<String, Object> param){
        log.debug("User selectUser" + param.toString());;

        ResultListVO resultListVO = new ResultListVO();
        try {
            resultListVO.setRows(userMapper.selectUser(param));

        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }
        return resultListVO;
    }


    @Override
    public Map<String, Object> getUser(Map<String, Object> param){
        log.debug("User getUser" + param.toString());;
        Map<String, Object> recvData = new HashMap<String, Object>();

        try {
            recvData = userMapper.getUser(param);
            
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }
        return recvData;
    }


    @Override
    public Map<String, Object> deleteUser(Map<String, Object> param){

        Map<String, Object> resUserDel = new HashMap<String, Object>();

        try {

            resUserDel.put("deleteResult", userMapper.deleteUser(param));
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }


        return resUserDel;
    }

    @Override
    public Map<String, Object> insertUser(Map<String, Object> param){
        log.debug("User insertUser" + param.toString());;
        Map<String, Object> retUserIns = new HashMap<String, Object>();

        try {
            retUserIns.put("insertResult", userMapper.insertUser(param));
        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }


        return retUserIns;
    }


    @Override
    public Map<String, Object> updateUser(Map<String, Object> param){
        log.debug("User updateUser" + param.toString());;
        Map<String, Object> retUserUpd = new HashMap<String, Object>();

        try {

            retUserUpd.put("updateUserResult", userMapper.updateUser(param));

        }
        catch(Exception e){
            throw new AnyXException(e.getMessage());
        }


        return retUserUpd;
    }


}
