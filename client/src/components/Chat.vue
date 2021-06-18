<template>
  <div class="box-card">
    <div class="content" v-if="msgs.length > 0">
      <div v-for="(item,index) in msgs" class="body" :key="item.data+index">
        <div>
          <el-image fit="cover" :src="$axios.defaults.baseURL+'/image/get/'+item.author.avatar.imageId" class="avatar"></el-image>
          
        </div>
        <div>
          <div style="font-size: 14px">{{ item.author && item.author.nickName}}({{item.author && item.author.loginIp}})：</div>
          <div class="message-item">{{item.data}}</div>
        </div>
      </div>
    </div>
    <div v-else>
      <h1 style="text-align: center">无消息</h1>
    </div>
  </div>
</template>
<script>
export default {
  name: "Chat",
  props:["msgs"],
  data(){
    return {
      input:""
    }
  },
  methods:{
    async send(){
      await this.$axios.post(`send`,{
        msg:this.input
      })
    },
  }
}
</script>

<style scoped>
.content{
  
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  margin: 10px;
  padding: 5px;
}
.message-item{
  background: #409eff;
  color: #fff;
  display: inline-block;
  padding: 10px;
  border-radius: 10px;
  margin: 5px;
  max-width: 300px;
}
.body{
  border-bottom: 1px solid #dddd;
  display: flex;
  margin-top: 10px;
  padding: 5px;
}
</style>