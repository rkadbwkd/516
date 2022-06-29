/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import axios from "axios";
import { makeQueryString } from "../utils/query.js";

const config = {
  baseUrl: "http://localhost:8580/crud/user/"
};

const userAPIDFN = {
  userAPI: (APIName, paths, conditions) => {
    return userAPI[APIName](paths, conditions);
  }
};

const userAPI = {
  selectUser: (paths, conditions) => {
    return axios.get(
      `${config.baseUrl}selectUser/${makeQueryString(conditions)}`
    );
  },
  getUser: (paths, conditions) => {
    return axios.get(
      `${config.baseUrl}getUser/${makeQueryString(conditions)}`
    );
  },
  deleteUser: (paths, userPostData) => {
    return axios.post(`${config.baseUrl}deleteUser/`, userPostData);
  },
  insertUser: (paths, userPostData) => {
    return axios.post(`${config.baseUrl}insertUser/`, userPostData);
  },
  updateUser: (paths, userPostData) => {
    return axios.post(`${config.baseUrl}updateUser/`, userPostData);
    
  }
};

export { userAPIDFN, userAPI };
