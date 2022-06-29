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
  <div class="row page-row">
    <span class="page-bracket" @click="changePage(1)">
      <h4>
        <small>&lt;&lt;</small>
      </h4>
    </span>
    <span class="page-bracket" @click="changePage(currPageNum - 1)">
      <h4>
        <small>&lt;</small>
      </h4>
    </span>
    <div
      v-for="(pageNum, index) in pageNums"
      :key="index"
      @click="changePage(pageNum)"
      class="page"
      :class="{ active: pageHignlighting(pageNum) }"
    >
      {{ pageNum }}
    </div>
    <span class="page-bracket" @click="changePage(currPageNum + 1)">
      <h4>
        <small>&gt;</small>
      </h4>
    </span>
    <span
      class="page-bracket"
      @click="changePage(parseInt(totalCount / pagePerCnt) + 1)"
    >
      <h4>
        <small>&gt;&gt;</small>
      </h4>
    </span>
  </div>
</template>

<script>
import { conditionBus } from "../../utils/search-condition-bus.js";

export default {
  data() {
    return {
      currPageNum: 1,
      totalCount: 0,
      pagePerCnt: 10,
      startPageNum: 1
    };
  },
  props: {
    totalCntProp: {
      type: Number
    },
    pageNumProp: {
      type: Number
    }
  },
  watch: {
    totalCntProp() {
      this.totalCount = this.totalCntProp;
    },
    pageNumProp() {
      this.currPageNum = this.pageNumProp;
    }
  },
  computed: {
    pageNums() {
      var pageNums = [];

      if (this.totalCount > 10 * this.pagePerCnt) {
        const lastPage = parseInt((this.totalCount - 1) / this.pagePerCnt) + 1;

        pageNums.push(1);

        var startPage, endPage;
        if (this.currPageNum - 3 <= 2) {
          startPage = 2;
          endPage = startPage + 6;
        } else {
          startPage = this.currPageNum - 3;
          endPage = this.currPageNum + 3;
          if (startPage != 2) pageNums.push("...");

          if (endPage + 3 > lastPage - 1) {
            startPage = lastPage - 7;
            endPage = lastPage - 1;
          }
        }

        for (var i = startPage; i <= endPage; i++) {
          pageNums.push(i);
        }

        if (pageNums[pageNums.length - 1] != lastPage - 1) pageNums.push("...");

        pageNums.push(lastPage);
      } else {
        for (var j = 1; j <= (this.totalCount - 1) / this.pagePerCnt + 1; j++) {
          pageNums.push(j);
        }
      }
      return pageNums;
    }
  },
  created() {
    conditionBus.$on("change:conditions", conditions => {
      this.currPageNum = conditions.pageNum;
    });
    conditionBus.$on("searchEvent", searchCondition => {
      this.pagePerCnt = searchCondition.pagePerCnt;
      this.currPageNum = searchCondition.pageNum;
    });
  },
  beforeDestroy() {
    conditionBus.$off("change:conditions", conditions => {
      this.currPageNum = conditions.pageNum;
      this.pageHignlighting(conditions.pageNum);
    });
  },
  methods: {
    changePageNums(conditions) {
      this.totalCount = parseInt(conditions.totalCount);
      this.pagePerCnt = parseInt(conditions.pagePerCnt);
      if (conditions.pageNum) this.currPageNum = parseInt(conditions.pageNum);
      else this.currPageNum = 1;
    },
    pageHignlighting(pageNum) {
      if (pageNum == this.currPageNum) return true;
      else return false;
    },
    changePage(pageNum) {
      if (
        pageNum > 0 &&
        pageNum <= parseInt(this.totalCount / this.pagePerCnt) + 1
      ) {
        this.currPageNum = parseInt(pageNum);
        conditionBus.$emit("change:pageNum", pageNum);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.page-row {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;

  .page {
    width: 20px;
    height: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: bold;
    font-size: 16px;
    margin-right: 15px;
    cursor: pointer;
  }

  .page.active {
    color: rgb(133, 206, 226) !important;
  }
}

.page-bracket {
  color: rgb(150, 150, 150);
  margin-right: 15px;
  cursor: pointer;
}
</style>


