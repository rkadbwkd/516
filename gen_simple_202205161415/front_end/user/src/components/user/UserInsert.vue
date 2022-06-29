<!--
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
-->

<template>
  <div class="container">
     <div class="q-pa-md" style="padding-top:100px" >
      <label>User's Name</label> <q-input type="text" v-model="userData.user_name"></q-input><br>
      <label>User's Age </label>  <q-input type="text" v-model="userData.user_age"></q-input><br>
      <label>User's Sex </label> <q-input type="text" v-model="userData.user_sex"></q-input> <br><br><br>
      <q-btn style="background: #FF0080; color: white" label="취소" @click="moveToUserList()"></q-btn>
      <q-btn style="background: #FF0080; color: white" label="추가" @click="insertUser()"></q-btn>

    
    </div>
      <div>
        
      </div>
  </div>
</template>

<script>

import console from "console";
import { checkRV } from "@/utils/checkRV.js";
import { API } from "@/api";

export default {
  name: 'UserInsert',
  data() {
    return {
      urlPaths: {},
      userData: {
        user_no: "",
        user_name: "",
        user_reg_date: "",
        user_use_yn: "",
        user_age: "",
        user_sex: ""
      },
      model : "",
    };
  },
  computed: {},
  watch: {},
  async created() {},
  mounted() {},
  methods: {
    async insertUser() {
 

      API.userAPI
        .insertUser(this.urlPaths, this.userData)
        .then(response => {
          if (response.status == 200) {
            alert("정상 등록되었습니다");
            this.moveToUserList();
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    // Move Function
    moveToUserList() {
      this.$router.push({
        name: "userList"
      });
    },

    // Check Validation
    checkRVUser() {
      if (
        !checkRV.checkRV(
          this.user.user_no,
          true,
          "checkAll",
          "사용자 seq 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.user.user_name,
          true,
          "checkStrLen",
          "사용자명 형식을 확인하세요.",
          0
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.user.user_reg_date,
          false,
          "checkAll",
          "등록일 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.user.user_use_yn,
          false,
          "checkStrLen",
          "사용여부 형식을 확인하세요.",
          1
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.user.user_age,
          true,
          "checkNumberOnly",
          "나이 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.user.sex,
          true,
          "checkStrLen",
          "성별 형식을 확인하세요.",
          0
        )
      )
        return false;

      return true;
    }
  }
};
</script>

<style lang="scss" scoped></style>