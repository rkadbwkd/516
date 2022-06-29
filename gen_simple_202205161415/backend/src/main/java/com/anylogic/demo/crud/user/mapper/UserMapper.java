/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.demo.crud.user.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface UserMapper {


    List<Object> selectUser(Map<String, Object> param) throws RuntimeException;

    Map<String, Object> getUser(Map<String, Object> param) throws RuntimeException;

    int deleteUser(Map<String, Object> param) throws RuntimeException;

    int insertUser(Map<String, Object> param) throws RuntimeException;

    int updateUser(Map<String, Object> param) throws RuntimeException;

}
