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
import { API } from "@/api";

export default {
  name: 'UserDetail',
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
    
    this.selectUserCondi.user_no = this.$route.params;    // to do    // params
    //console.log(this.params);
  },
  async mounted() {
    await this.selectUser();
  },
  methods: {
    // eslint-disable-next-line
    async selectUser() {
      this.selectUserCondi.user_no = this.userNo;


      try {
        const response = await API.userAPI.selectUser(this.selectUserCondi);
        this.selectUserData = response.data.data;

        console.log(response);
      } catch (error) {
        console.log(error);
      }
    },
    async deleteUser() {
      const confirmDelete = confirm("삭제하시겠습니까?");
      if (confirmDelete == false) {
        return;
      }

      var delUserParams = [];

      delUserParams.push({
        delObj: {
          user_no: this.selectUserCondi.user_no
        }
      });

      var postData = {
          user_no: this.selectUserCondi.user_no
      };

      await API.userAPI
        .deleteUser(this.urlPaths, postData)
        .then(response => {
          if (response.status == 200) {
            alert("정상 삭제되었습니다");
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

    // Move Function
    moveToUserUpdate(item) {
      this.$router.push({
        name: "userUpdate",
        params: {
          item
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped></style>
