/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


// eslint-disable-next-line
const mutations = Object.assign({},
  {
    login (state, item) {
        state.token = item.headers['accesstoken']
        state.id = item.data['id']
        state.role = item.data['role']
        state.email = item.data['email']
        state.nickname = item.data['nickname']
    },
    logout (state) {
        state.token = null
        state.id = null
        state.role = null
        state.email = null
        state.nickname = null
    }
  }
);

export { mutations };
