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
  <div id="pop-up">
    <div class="contents container">
      <div
        @click="closePopUp()"
        class="close-btn row d-flex justify-content-end"
      >
        X
      </div>
      <slot name="contents"></slot>
      <div class="row">
        <div class="btn-wrapper col-6">
          <button @click="confirmPopUp()" class="btn pop-up-btn">확인</button>
        </div>
        <div class="btn-wrapper col-6">
          <button @click="closePopUp()" class="btn pop-up-btn">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    body() {
      return document.querySelector("body");
    }
  },
  async created() {
    this.body.style.overflow = "hidden";
  },
  methods: {
    confirmPopUp() {
      this.body.style.overflow = "unset";
      this.$emit("confirm:popUp");
    },
    closePopUp() {
      this.body.style.overflow = "unset";
      this.$emit("close:popUp");
    }
  }
};
</script>

<style lang="scss" scoped>
#pop-up {
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgba(30, 30, 30, 0.5);
  width: 100vw;
  height: 100vh;
  z-index: 9999;
}

.contents {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #dddddd;
  border-radius: 5px;
  background-color: white;
  padding: 10px 20px;
}

.close-btn {
  font-size: 25px;
  cursor: pointer;
  margin: 2px 0;
  border-bottom: 1px solid lightgray;
}

.btn-wrapper {
  padding: 0 2px;
}

.pop-up-btn {
  width: 100%;
  background-color: black;
  color: white;
}

.pop-up-btn:hover {
  background-color: gray;
}
</style>


