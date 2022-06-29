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

import com.anylogic.demo.common.model.ResultListVO;
import java.util.List;
import java.util.Map;

import com.anylogic.demo.common.mvc.message.Messages;


public interface UserService {


    public ResultListVO selectUser(Map<String, Object> param);

    public Map<String, Object> getUser(Map<String, Object> param);

    public Map<String, Object> deleteUser(Map<String, Object> param);

    public Map<String, Object> insertUser(Map<String, Object> param);

    public Map<String, Object> updateUser(Map<String, Object> param);

}
