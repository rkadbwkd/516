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
  <!-- <div class="container">
      <div>
        <q-table
          :rows="selectUserList"
          @row-click="check"
         
          
        ></q-table>
      </div>

  </div> -->
  <div>
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
        <tr @click="Movetopage(table_value.user_no)" v-for="(table_value,index) in selectUserList" :key="index" >
          <td class="text-left">{{table_value.user_no}}</td>
          <td class="text-right">{{table_value.user_name}}</td>
          <td class="text-right">{{table_value.user_age}}</td>
          
       
       </tr>      
      </tbody>
    </q-markup-table>
   </div>

   
  <div class="q-pa-lg flex flex-center">
      <!-- 왼쪽 정렬 필요 -->
     <div>
       Total :  {{ totalcount }}

    </div> 
    <div>
        <q-pagination
        v-model="current"
        :max="5"
        direction-links
        boundary-links
        icon-first="skip_previous"
        icon-last="skip_next"
        icon-prev="fast_rewind"
        icon-next="fast_forward"
        
        @click="titleUpdate"
        
        ></q-pagination>
    </div>
    <!-- 오른 정렬 필요 -->
    <div>
        <!-- row :  {{ rowcount }} -->
        <!-- <select @change="rowcountupdate" v-model="row_count">
          <option value=5>5</option>
          <option value=10>10</option>
          <option value=15>15</option>
          <option value=20>20</option> 
        </select> -->
        <div >
  <div class="q-pa-md" style="max-width: 300px">
    <div class="q-gutter-md">
      <q-select  v-model="row_count" :options="options" ></q-select>

    

     
    </div>
  </div>
</div>
        
    </div>


  </div>

</div>
</template>

<script>

import console from "console";
import { formatDate } from "@/utils/date.js";
import { API } from "@/api";

export default {
  name: 'UserList',
  data() {
    return {
      urlPaths: {},
      userName: "",
      sex: "",
      selectUserList: [],
      selectUserCondi: {
        user_name: "",
        sex: "",
        pageNum: 1,
        pagePerCnt: 10,
        sortDirection: "ASC",
        sortItem: "user_no"
      },
      monthsSelectUser: [1, 3, 6],
      selectUserPagePerCnt: [10, 25, 50, 100]
    };
  },
  computed: {
    selectUserCheckedRows() {
      if (
        this.selectUserList != [] &&
        this.selectUserList[0] != undefined
      ) {
        return this.selectUserList[0].totalCnt;
      } else {
        return 0;
      }
    },

  },
  watch: {},
  // async created() {
  //   if (Object.keys(this.$route.query).length == 0) {
  //     this.$router
  //       .replace({
  //         query: this.userCondi
  //       })
  //       .catch(error => {
  //         console.log(error);
  //       });
  //   }
  //   // to do 
  //   // this.searchUserByQuery(user_name, sex);
  //   window.onpopstate = () => {
  //     // to do
  //     // this.searchUserByQuery();
  //   };
  // },
  async mounted() {
    await this.selectUser(1);
  },
  beforeDestroy() {

    window.onpopstate = null;
  },
  methods: {

  Movetopage(user_no){
   // console.log(user_no);
    this.$router.push({
      name : 'userDetail',
      params : {
        key : user_no
      }
    });
  },

    // changeMonth
    changeMonthUser(month) {
      var today = new Date();
      this.userCondi.end_dt = formatDate(today);
      var aMonthBefore = today.setMonth(today.getMonth() - month);
      this.userCondi.start_dt = formatDate(aMonthBefore);
    },
    changeSortSelectUser(header) {
      if (this.selectUserCondi.selectUserSortItem == header) {
        this.selectUserCondi.selectUserSortDirection == "ASC" ? "DESC" : "ASC";
      } else {
        this.selectUserCondi.selectUserSortItem = header;
        this.selectUserCondi.selectUserSortDirection = "ASC";
      }
      this.selectUser(this.selectUserCondi.pageNum);
    },
    selectUserByPageNum(pageNum) {
      this.selectUserCondi.pageNum = pageNum;
      this.$router
        .push({
          query: this.selectUserCondi
        })
        .catch(error => {
          console.log(error);
        });
      this.selectUser(pageNum);
    },
    // eslint-disable-next-line
    async selectUser(pageNum) {

      this.selectUserCondi.pageNum = pageNum;

      this.selectUserCondi.user_name = this.userName;
      this.selectUserCondi.sex = this.sex;

      try {

        const response = await API.userAPI.selectUser(this.urlPaths, this.selectUserCondi);
        this.selectUserList = response.data.data.rows;
        console.log(response);
      } catch (error) {
        console.log(error);
      }
      // this.$emit("event-name", this.SelectUser[0].TOTAL_CNT);
    },
    async selectUserByQuery() {
      this.selectUserCondi = Object.assign({}, this.$route.query);
      try {
        const response = await API.userAPI.selectUser(this.urlPaths, this.selectUserCondi);
        this.selectUserList = response.data.data.rows;
        console.log(response);
      } catch (error) {
        console.log(error);
      }
      // this.$emit("event-name", this.SelectUser[0].TOTAL_CNT);
    },

    // Move Function
    moveToUserDetail(item) {
      this.$router.push({
        name: "userDetail",
        params: {
          item
        }
      });
    },

    // Move Function
    moveToUserInsert() {
      this.$router.push({
        name: "userInsert"
      });
    }
  }
};
</script>

<style lang="scss" scoped></style>
