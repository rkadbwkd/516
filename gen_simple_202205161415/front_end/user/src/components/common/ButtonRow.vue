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
  <div class="row button-row mt-4">
    <div class="col-6 d-flex justify-content-start">
      <slot name="leftSide"></slot>
    </div>
    <div class="col-6 d-flex justify-content-end">
      <slot name="rightSide"></slot>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss">
.button-row-default {
  cursor: pointer;
  width: 100px;
  height: 30px;
  font-size: 15px;
  align-items: center;
  justify-content: center;
  color: white;
  border: 1px solid rgb(135, 204, 223);
  background-color: rgb(133, 206, 226);
  border-radius: 5px;
  margin: 5px;
}

.button-row-default:hover {
  background-color: #41bef0;
}
</style>


