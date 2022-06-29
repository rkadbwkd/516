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
    <Left> </Left>
    <router-view />
  </div>
</template>

<script>
import Left from "@/components/user/Left.vue";

export default {
  name: 'App',
  components: {
    Left
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

<style lang="scss" global>
  // @import "assets/css/common.css"
  // @import "assets/css/font.css"
</style>
