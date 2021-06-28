import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: {
      permissions: [],
      roles: [],
      user:{
        avatar:{}
      },
    },
    readyState:0,
  },
  mutations: {
    login(state, userInfo) {
      state.userInfo = userInfo;
    },
    readyStateChange(state,readyState){
      state.readyState = readyState;
    }
  },
  actions: {
  },
  getters:{
    token(){
      return sessionStorage.getItem("sessionsID")
    },
    userInfo(state){
      return state.userInfo;
    },
    readyState(state){
      return state.readyState;
    }
  },
  modules: {
  }
})
