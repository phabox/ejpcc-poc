import Vue from "vue";
import Vuex from "vuex";
import draws from "./draws";
import companies from "./companies"
Vue.use(Vuex);
export default new Vuex.Store({
  modules: { draws, companies },
});
