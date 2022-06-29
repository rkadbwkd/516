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
      selectUserData: {},
      selectUserCondi: {
        user_no: "",
      }
    };
  },
  computed: {},
  watch: {},
  async created() {
    this.params = this.$route.params;    // to do    // params
  },
  async mounted() {
    await this.selectUser();
  },
  methods: {
    // eslint-disable-next-line
    async selectUser() {
      this.selectUserCondi.user_no = this.userNo;


      try {
        const response = await API.userAPI.selectUser(this.urlPaths, this.selectUserCondi);
        this.selectUserData = response.data.data;

        console.log(response);
      } catch (error) {
        console.log(error);
      }
    },
    async updateUser() {
      if (this.checkRVUpdateUser() == false) {
        return;
      }

      var userFormData = new FormData();

      var requiredData = {
        formData: this.selectUserData
      };
      userFormData.append("data", JSON.stringify(requiredData));

      API.userAPI
        .updateUser(this.urlPaths, userFormData)
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
          this.selectUserData.user_no,
          true,
          "checkAll",
          "사용자 seq 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.selectUserData.user_name,
          true,
          "checkStrLen",
          "사용자명 형식을 확인하세요.",
          0
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.selectUserData.user_reg_date,
          false,
          "checkAll",
          "등록일 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.selectUserData.user_use_yn,
          false,
          "checkStrLen",
          "사용여부 형식을 확인하세요.",
          1
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.selectUserData.user_age,
          true,
          "checkNumberOnly",
          "나이 형식을 확인하세요."
        )
      )
        return false;
      if (
        !checkRV.checkRV(
          this.selectUserData.sex,
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
