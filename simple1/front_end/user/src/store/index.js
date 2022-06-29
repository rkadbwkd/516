/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import {createStore } from "vuex";
import createPersistedState from "vuex-persistedstate"
import { states } from "./states/index.js";
import { getters } from "./getters/index.js";
import { mutations } from "./mutations/index.js";
import { actions } from "./actions/index.js";

export default createStore({
  states,
  getters,
  mutations,
  actions,
  plugins: [createPersistedState()],
  modules: {}
});
