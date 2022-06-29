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
  <h3 >AnyX Views</h3>

  <div class="table">
    <table id="list">
      <thead>
        <tr>
          <th width ="12%">번호</th>
          <th width="15%">메뉴</th>
          <th width="25%">뷰이름</th>
          <th width ="40%">view component</th>
          <th>비고</th>
        </tr>
      </thead>
      <tbody id="contacts">
        <tr v-for="(view, index) in model.views" :key="index" >
          <td>{{index + 1}}</td>
          <td>{{view.menu}}</td>
          <td>{{view.name}}</td>
          <td>
            <router-link :to="`${view.viewName}`">{{view.viewName}}</router-link>
            </td>
          <td>{{view.etc}}</td>
        </tr>
      </tbody>
    </table>
    </div>

  </div>
</template>

<script>
export default {
  name: 'anyxviews',
  components: {
  },
  data() {
    return {
      model : {
        "views": [
        {
            "menu": "user",
            "name": "회원목록",
            "viewName":  "/user/userList" ,
            "etc": ""
        }, 
        {
            "menu": "user",
            "name": "회원상세",
            "viewName":  "/user/userDetail" ,
            "etc": ""
        }, 
        {
            "menu": "user",
            "name": "회원등록",
            "viewName":  "/user/userInsert" ,
            "etc": ""
        }, 
        {
            "menu": "user",
            "name": "회원수정",
            "viewName":  "/user/userUpdate" ,
            "etc": ""
        }, 
        ]
      }
    };
  },
  computed: {},
  watch: {},
  async created() {},
  mounted() {},
  methods: {}
};

</script>

<style lang="scss" scoped>h3 {
  text-align: center;
}
.table {
  text-align: center;
  position: relative;
}
table {
  width: 85%;
  border: 1px solid #444444;
  border-collapse: collapse;
  margin-left: auto;
  margin-right: auto;
}
th, td {
  border: 1px solid #444444;
  padding: 10px;
}
</style>
