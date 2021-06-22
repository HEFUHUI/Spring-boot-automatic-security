"use strict";

import Vue from 'vue';
import axios from "axios";
import { Loading, Message,Notification } from "element-ui"

export let config = {
  host: process.env.VUE_APP_baseURL || "localhost:8081",
  timeout: 8000,
};
const _axios = axios.create({
  baseURL: 'http://'+config.host,
  timeout: config.timeout,
});
_axios.options.withCredentials = true;

let loading;
function startLoading(){
  loading = Loading.service({
    text:"玩命加载中。。。。",
     fullscreen:true
  })
}
function stopLoading(){
  loading.close();
}

_axios.interceptors.request.use(
  function(config) {
    startLoading();
    if(sessionStorage.getItem("sessionsID")){
      config.headers.common['Authorization'] = sessionStorage.getItem("sessionsID")
    }
    return config;
  },
  function(error) {
    stopLoading()
    return Promise.reject(error);
  }
);

_axios.interceptors.response.use(
  function(response) {
    stopLoading();
    if(response.data.code >= 300){
      Message({type:"error",message:response.data.msg,showClose:true})
    }
    if(response.data.code === 204){
      Message({type:"success",message:response.data.msg,showClose:true})
    }
    return response;
  },
  function(error) {
    stopLoading();
    if(error.response.data.status === 401){
      sessionStorage.removeItem("sessionsID")
      Message({
        type:"error",
        message:"登录失效.请重新登录!",
        showClose:true,
      })
    }else{
      Message({
        type:"error",
        message:error.response.data.message || error.response.data.msg,
        showClose:true,
      })
    }
    return Promise.reject(error);
  }
);
let ws;
function connectWS(){
  ws = new WebSocket(`ws://${config.host}/ws?token=${sessionStorage.getItem("sessionsID")}`);
}
connectWS();

const app = new Vue();

Plugin.install = function(Vue) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
    $ws:{
      get(){
        return ws;
      }
    },
    $eventBus:{
      get(){
        return app;
      }
    }
  });
};
Vue.use(Plugin);
ws.onmessage = (data)=>{
  try{
    const res = JSON.parse(data.data);
    if(res.code>=4000){
      Message({type:"error",message:`${res.msg}，状态码：${res.code}`})
    }
  }catch (e){
    console.log(e)
  }
  app.$emit("ws-message",data)
}
ws.onclose = ()=>{
  Notification({type:"error",title:"已断开服务器连接!",duration:0,message:"点击重试！",showClose:true,
    onClick(){
      window.location.reload();
    }})
}
ws.onerror = (err)=>{
  app.$emit("ws-error",err)
}
// ws.onopen = function (){
//   Message({type:"success",message:"连接成功!",showClose:true})
// }
export default Plugin;
