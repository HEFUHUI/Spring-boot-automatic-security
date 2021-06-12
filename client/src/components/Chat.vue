<template>
  <div class="box-card">
    <div class="content" v-if="msgs.length > 0">
      <div v-for="(item,index) in msgs" :key="item.data+index">
        <el-image fit="cover" :src="$axios.defaults.baseURL+'/image/get/'+item.author.avatar.imageId" class="avatar"></el-image>
        <div style="font-size: 17px">{{ item.author.nickName}}：</div>
        <div class="message-item">{{item.data}}</div>
      </div>
    </div>
    <div v-else>
      <h1 style="text-align: center">无消息</h1>
    </div>
    <el-form ref="form" :inline="true" label-width="80px">
      <el-form-item label="消息">
        <el-input v-model="input" placeholder="输入消息内容!"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="medium" @click="send">发送</el-button>
      </el-form-item>
    </el-form>
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
}
</style>