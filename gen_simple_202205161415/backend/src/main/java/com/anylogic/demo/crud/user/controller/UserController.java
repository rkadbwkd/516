/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.crud.user.controller;

import java.util.List;
import java.util.Map;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.anylogic.demo.common.util.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.anylogic.demo.common.model.ResultListVO;
import com.anylogic.demo.common.mvc.message.Messages;
import com.anylogic.demo.crud.user.service.UserService;


@RestController
@RequestMapping("/crud/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/selectUser")
    public ResponseEntity<?> selectUser(@RequestParam Map<String, Object> param) {
        log.debug("User selectUser" + param.toString());
        
        ResultListVO resultListVO = new ResultListVO();
        try {
            resultListVO = userService.selectUser(param);
        } catch (Exception e) { 
            e.printStackTrace();
            return ResponseUtil.responseError(e.getMessage());
        }
        
        return ResponseUtil.responseOk(resultListVO);
    }


    @RequestMapping(value = "/getUser")
    public ResponseEntity<?> getUser(@RequestParam Map<String, Object> param) {
        log.debug("User getUser" + param.toString());
        Map<String, Object> ret = null;
        
        try {
            ret = userService.getUser(param);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseUtil.responseError(e.getMessage());
        }
        
        return ResponseUtil.responseOk(ret);
    }


    @RequestMapping(value = "/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Object> param) throws Exception  {
        log.debug("User deleteUser" + param.toString());


        Map<String, Object> ret = null;
        try {
            ret = userService.deleteUser(param);
        } catch (Exception e) { 
            e.printStackTrace();
            return ResponseUtil.responseError(e.getMessage());
        }
        return ResponseUtil.responseOk(ret);
    }


    @RequestMapping(value = "/insertUser")
    public ResponseEntity<?> insertUser(@RequestBody Map<String, Object> param) throws Exception {
        log.debug("User insertUser" + param.toString());

        Map<String, Object> ret = null;
        try {
            ret = userService.insertUser(param);
        } catch (Exception e) { 
            e.printStackTrace();
            return ResponseUtil.responseError(e.getMessage());
        }

        return ResponseUtil.responseOk(ret);
    }


    @RequestMapping(value = "/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> param) throws Exception {
        log.debug("User updateUser" + param.toString());

        Map<String, Object> ret = null;
        try {
            ret = userService.updateUser(param);
        } catch (Exception e) { 
            e.printStackTrace();
            return ResponseUtil.responseError(e.getMessage());
        }

        return ResponseUtil.responseOk(ret);
    }


}
