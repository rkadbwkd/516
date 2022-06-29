/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import UserListView from "../views/user/UserListView.vue";
import UserDetailView from "../views/user/UserDetailView.vue";
import UserInsertView from "../views/user/UserInsertView.vue";
import UserUpdateView from "../views/user/UserUpdateView.vue";

const userRoutes = [
  {
    path: "/user/userList",
    name: "userList",
    component: UserListView
  },
  {
    path: "/user/userDetail",
    name: "userDetail",
    component: UserDetailView
  },
  {
    path: "/user/userInsert",
    name: "userInsert",
    component: UserInsertView
  },
  {
    path: "/user/userUpdate",
    name: "userUpdate",
    component: UserUpdateView
  }
];

export { userRoutes };
