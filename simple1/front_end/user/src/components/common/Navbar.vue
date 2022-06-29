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
    <div>
      <p>제목 : <input class="title" type="text" v-model="data.title"> </p>
        <quill-editor v-model="data.content" style="width: 1150px; height: 500px;"></quill-editor>
      </div>
      
  </div>
</template>

<script>
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import { quillEditor } from 'vue-quill-editor';

export default {
  components: {
    quillEditor,
  },
  data() {
    return {
      data: this.$attrs.data,
    }
  },
  methods: {

  }
}
</script>

<style>
.title {
  width: 1100px; 
  margin-left: 5px;
  border-right: 0px;
  border-left: 0px;
  border-top: 0px;
  border-bottom-color:lightgray;
}
</style>


