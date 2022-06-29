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
      <label>User's Name</label> <q-input type="text" v-model="getUserData.user_name"></q-input><br>
      <label>User's Age </label>  <q-input type="text" v-model="getUserData.user_age"></q-input><br>
      <label>User's Sex </label> <q-input type="text" v-model="getUserData.sex"></q-input> <br><br><br>
      <q-btn style="background: #FF0080; color: white" label="취소" @click="moveToUserList()"></q-btn>
      <q-btn style="background: #FF0080; color: white" label="수정" @click="updateUser()"></q-btn>
    </div>
  
  </div>
</template>

<script>

import console from "console";
import { checkRV } from "@/utils/checkRV.js";
import { API } from "@/api";

export default {
  name: 'UserUpdate',
  data() {
    return {
      params: {},
      urlPaths: {},
      userNo: "",
      getUserData: {},
      getUserCondi: {
        user_no: "",
      },
      userData: {
        user_no: "",
        user_name: "",
        user_reg_date: "",
        user_use_yn: "",
        user_age: "",
        sex: ""
      }
    };
  },
  computed: {},
  watch: {},
  async created() {
    this.params = this.$route.params;    // to do    // params
  },
  async mounted() {
    await this.getUser();
  },
  methods: {
    // eslint-disable-next-line
    async getUser() {
      this.getUserCondi.user_no = this.params.user_no;


      try {
        const response = await API.userAPI.getUser( this.urlPaths,this.getUserCondi);
        this.getUserData = response.data.data;

        console.log(response);
      } catch (error) {
        console.log(error);
      }
    },
    async updateUser() {
      // if (this.checkRVUser() == false) {
      //   return;
      // }

      // var userFormData = new FormData();

      // var requiredData = {
      //   formData: this.getUserData
      // };
      // userFormData.append("data", JSON.stringify(requiredData));

      API.userAPI
        .updateUser(this.urlPaths, this.getUserData)
        .then(response => {
          if (response.status == 200) {
            alert("정상 갱신되었습니다");
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
          this.getUserData.user_name,
          true,
          "checkStrLen",
          "사용자명 형식을 확인하세요.",
          0
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.getUserData.user_reg_date,
          false,
          "checkAll",
          "등록일 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.getUserData.user_use_yn,
          false,
          "checkStrLen",
          "사용여부 형식을 확인하세요.",
          1
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.getUserData.user_age,
          true,
          "checkNumberOnly",
          "나이 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.getUserData.sex,
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
