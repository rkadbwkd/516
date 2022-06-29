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
 <div class="q-pa-md" style="padding-top:100px" >
  
  <!-- child {{ pagenation}}  -->
    
    <q-markup-table >
      <thead>
        <tr>
          <th class="text-left">user_no</th>
          <th class="text-right">user_name</th>
          <th class="text-right">user_age</th>
                   
         
        </tr>
      </thead>

      <tbody>
        <tr>
          <td class="text-left">{{getUserData.user_no}}</td>
          <td class="text-right">{{getUserData.user_name}}</td>
          <td class="text-right">{{getUserData.user_age}}</td>
          
       
       </tr>      
      </tbody>
    </q-markup-table>

    <div class="q-pa-md q-gutter-sm">
         <q-btn @click="moveToUserUpdate()" color="white" text-color="black" label="수정"></q-btn>
         <q-btn @click="moveToUserList()" color="white" text-color="black" label="취소"></q-btn>
         <q-btn @click="deleteUser()" color="white" text-color="black" label="삭제"></q-btn>
    </div>
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
      getUserData: {},
      getUserCondi: {
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
    await this.getUser();
  },
  methods: {
    // eslint-disable-next-line
    async getUser() {
      this.getUserCondi.user_no = this.params.item;
      console.log(this.getUserCondi.user_no);


      try {
        const response = await API.userAPI.getUser(this.urlPaths, this.getUserCondi);
        this.getUserData = response.data.data;

        console.log(this.getUserData);
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
          user_no: this.getUserCondi.user_no
        }
      });

      var postData = {
          user_no: this.getUserCondi.user_no
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
    moveToUserUpdate() {
      this.$router.push({
        name: "userUpdate",
        params: {
          user_no : this.params.item
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped></style>
