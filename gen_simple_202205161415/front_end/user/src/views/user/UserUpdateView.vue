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
  <div class="app container">
    <h1>UserUpdateView</h1>
    <user-update> </user-update>
  </div>
</template>

<script>
import UserUpdate from "@/components/user/UserUpdate.vue";

export default {
  name: 'UserUpdateView',
  components: {
    UserUpdate
  },
  data() {
    return {};
  },
  computed: {},
  watch: {},
  async created() {},
  mounted() {},
  methods: {}
};
</script>

<style lang="scss" scoped></style>
