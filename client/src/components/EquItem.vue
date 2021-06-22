<template>
  <el-card :body-style="{ padding: '0px' }">
    <el-image
        :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+equ.avatar.imageId]"
        fit="cover"
        :src="$axios.defaults.baseURL+'/image/get/'+equ.avatar.imageId" style="width: 100%;height: 200px" class="image">
    </el-image>
    <div style="padding: 14px;">
      <span style="font-weight: bold;font-size: 20px">{{ equ.nickName }}</span>
      <p><time class="time">添加时间：{{ new Date(equ.createTime).format("yyyy-MM-dd") }}</time></p>
      <el-button type="success" size="mini" v-if="isOnline">在线</el-button>
      <el-button type="warning" size="mini" @click="fetch" v-else>离线</el-button>
    </div>
    <div style="padding: 10px">
      <el-button size="mini" type="info" :disabled="!isOnline" @click="$router.push(`/monitor/${source.sessionId}`)">监控页面</el-button>
      <el-button type="danger" size="mini" class="button" disabled>下线设备</el-button>
    </div>
  </el-card>
</template>
<script>
export default {
  props:["equ"],
  data(){
    return {
      source:{},
      isOnline:false
    }
  },
  methods:{
    fetch(){
      this.$ws.send(JSON.stringify({
        code:206,
        data:this.equ.userId
      }))
    }
  },
  mounted(){
    this.$eventBus.$on("ws-message",msg=>{
      try {
        const data = JSON.parse(msg.data)
        console.log(data.code)
        if(data.code === 216){
          this.isOnline = true;
          this.source = data.data;
        }else if(data.code === 1){
          if(data.data.userId === this.equ.userId){
            this.isOnline = true;
            this.source = data.data;
          }
        }else if(data.code===2){
          if(data.data.userId === this.equ.userId){
            this.isOnline = false;
            this.source = {};
          }
        }
      }catch (e) {
        console.log(e)
      }
    })
    this.fetch();
  }
}
</script>