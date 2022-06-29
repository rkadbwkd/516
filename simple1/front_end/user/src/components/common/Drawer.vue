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
    <transition name="fade">
      <div v-show="drawerFlagProp" class="drawer">
        <div role="banner">
          <nav class="drawer-nav" role="navigation">
            <ul class="drawer-menu">
              <div>
                <div class="drawer-header">
                  <h4 class="all-category">전체 카테고리</h4>
                  <button class="drawer-close" @click="toggleDrawer">X</button>
                </div>
                <ul
                  v-for="(category, categoryIndex) in categories"
                  :key="categoryIndex"
                >
                  <li class="big-category">
                    {{ category.bigCategory.code_name }}
                  </li>
                  <ul
                    v-for="(smallCategory,
                    categoryIndex) in category.smallCategories"
                    :key="categoryIndex"
                  >
                    <li class="small-category">
                      {{ smallCategory.code_name }}
                    </li>
                  </ul>
                </ul>
              </div>
            </ul>
          </nav>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { API } from "../../api/index";
import console from "console";

export default {
  components: {},
  data() {
    return {
      codeList: [],
      condCode: { code: 100 }
    };
  },
  props: {
    drawerFlagProp: {
      type: Boolean
    }
  },
  computed: {
    categories() {
      let categories = [];
      let category = {};

      this.codeList.forEach(codeInfo => {
        if (codeInfo.code % 10 === 0) {
          category.bigCategory = codeInfo;
          categories.push({ ...category });
        }
      });

      for (let i = 0; i < categories.length; i++) {
        let smallCategories = [];
        this.codeList.forEach(codeInfo => {
          if (categories[i].bigCategory.code === codeInfo.code_type_id) {
            smallCategories.push(codeInfo);
          }
        });
        categories[i].smallCategories = smallCategories;
      }

      return categories;
    }
  },
  created() {
    this.searchCodeByQuery();
  },
  methods: {
    toggleDrawer() {
      if (this.drawerFlagProp === true) {
        this.drawerFlagProp = false;
      }
    },
    async searchCodeByQuery() {
      await API.codeAPI
        .selectCode(this.condCode)
        .then(response => {
          if (response.status == 200) {
            this.codeList = response.data.data;
          } else {
            alert("카테고리 검색을 실패했습니다");
          }
        })
        .catch(error => {
          console.log(error);
          alert("카테고리 검색을 실패했습니다");
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

nav {
  border-right: solid thin;

  .drawer-header {
    display: flex;

    .all-category {
      margin-top: 5px;
    }

    .drawer-close {
      width: 30px;
      height: 30px;
      margin-left: 20%;
    }
  }

  .drawer-menu {
    margin: 15px;

    .big-category {
      margin-top: 20px;
      font-size: 20px;
    }

    .small-category {
      margin-top: 10px;
      font-size: 16px;
    }
  }
}
</style>


