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

  <div>
 <div class="q-pa-md" style="padding-top:100px" >


       <div class="q-gutter-md" style="max-width: 300px">


        <q-select filled v-model="sex" :options="options" label="성별" stack-label :dense="dense" :options-dense="denseOpts"></q-select>

         <q-input v-model="userName" label="user_name"></q-input>
      <q-btn style="background: #FF0080; color: white" label="조회" @click="selectUser(1)"></q-btn>
      <q-btn style="background: #FF0080; color: white" label="등록" @click="moveToUserInsert()"></q-btn>

      </div>

  

    
    <q-markup-table >
      <thead>
        <tr>
          <th class="text-left">user_no</th>
          <th class="text-right">user_name</th>
          <th class="text-right">user_age</th>
          <th class="text-right">user_sex</th>
          
                   
         
        </tr>
      </thead>

      <tbody>
        <tr @click="moveToUserDetail(table_value.user_no)" v-for="(table_value,index) in selectUserList" :key="index" >
          <td class="text-left">{{table_value.user_no}}</td>
          <td class="text-right">{{table_value.user_name}}</td>
          <td class="text-right">{{table_value.user_age}}</td>
           <td class="text-right">{{table_value.sex}}</td>
          
       
       </tr>      
      </tbody>


    </q-markup-table>
  </div>


  
   
  <div class="q-pa-lg flex flex-center">
   
    <div>
        <q-pagination
        v-model="current"
        :max="rowCount"
        direction-links
        boundary-links
        icon-first="skip_previous"
        icon-last="skip_next"
        icon-prev="fast_rewind"
        icon-next="fast_forward"
        
        @click="selectUserByPageNum(current)"
        
       ></q-pagination>
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
              options: [
        '전체','M', 'F', 
      ],

      rowCount : 0,
      urlPaths: {},
      userName: "",
      sex: "",
      selectUserList: [],
      selectUserCondi: {
        user_name: "",
        sex: "",
        pageNum: 1,
        pagePerCnt: 5,
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

  // Movetopage(user_no){
  //  // console.log(user_no);
  //   this.$router.push({
  //     name : 'userDetail',
  //     params : {
  //       key : user_no
  //     }
  //   });
  // },

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
    
     // alert(this.current);
      this.selectUserCondi.pageNum = this.current;
      console.log(this.selectUserCondi.pageNum);
      // this.$router
      //   .push({
      //     query: this.selectUserCondi
      //   })
      //   .catch(error => {
      //     console.log(error);
      //   });
      this.selectUser(pageNum);
    },
    // eslint-disable-next-line
    async selectUser(pageNum) {

      

      this.selectUserCondi.pageNum = pageNum;

      this.selectUserCondi.user_name = this.userName;

       if(this.sex == '전체')
      {
        this.sex= "";
      }
      this.selectUserCondi.sex = this.sex;


  
      console.log(this.selectUserCondi);

      try {

        const response = await API.userAPI.selectUser(this.urlPaths, this.selectUserCondi);
        this.selectUserList = response.data.data.rows;


        // 현재 페이징 카운트 // 

        this.rowCount = response.data.data.rows[0].totalCnt;

        if (parseInt(this.rowCount) % (this.selectUserCondi.pagePerCnt) !== 0)
          this.rowCount = parseInt(this.rowCount/(this.selectUserCondi.pagePerCnt) + 1);
        else{
           this.rowCount = parseInt(this.rowCount/(this.selectUserCondi.pagePerCnt));
        }

        console.log(this.rowCount);

         
        



        //
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
