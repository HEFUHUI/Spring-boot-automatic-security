<template>
  <div>
    <el-row>
      <el-button size="mini" :plain="true" type="info" @click="fetch" icon="el-icon-refresh">refresh</el-button>
    </el-row>
    <br>
    <el-row align="center" :gutter="10">
      <el-col  :span="12">
        <el-form ref="form" label-width="80px">
          <el-form-item>
            <el-input v-model="input" placeholder="请输入消息!"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="send">广播</el-button>
          </el-form-item>
          <el-form-item label="接收消息">
            <!-- <div v-for="(msg,index) in msgs" :key="msg+index" >
              {{msg}}
            </div> -->
            <el-button @click="showMsg = true" type="info" size="mini">显示消息</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <h-online-users></h-online-users>
      </el-col>
    </el-row>
    <el-dialog title="所有消息" :visible.sync="showMsg" width="60%">
      <el-button type="warning" size="mini" @click="goBottom">到底部</el-button>
      <div style="overflow: auto;height: 50vh" ref="msg-content">
        <h1></h1>
        <el-card v-for="(msg,index) in msgs" :key="msg+index">
          <span style="font-size: 18px;color: #409EFF;text-shadow: 2px 2px 10px black">{{ ++index }}</span>
          <json-viewer :value="msg" :expand-depth="0" copyable>
          </json-viewer>
        </el-card>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import hOnlineUsers from "../../components/OnlineUsers"

export default {
  name: "DeBug.vue",
  data() {
    return {
      drawer: false,
      input: "",
      result: "",
      isConnected: false,
      msgs: [],
      showMsg:false,
      socketInfo: {
        target: {}
      },
      users: []
    }
  },
  computed: {
    userInfo(){
      return this.$store.getters.userInfo;
    }
  },
  components: {
    hOnlineUsers
  },
  methods: {
    send() {
      this.$ws.send(this.input.toString())
    },
    goBottom(){
      this.$refs["msg-content"].scrollTop = this.$refs["msg-content"].scrollHeight;
    },
    async fetch() {
      this.$ws.send(JSON.stringify({
        code: 204
      }))
    },
    refresh() {
      this.showOnlineUser = false;
      setTimeout(() => {
        this.showOnlineUser = true;
      })
    }
  },
  mounted() {
    this.$eventBus.$on("ws-message", d => {
      let data = "";
      try {
        data = JSON.parse(d.data);
      } catch (e) {
        data = d.data;
      }
      this.msgs.push(data);
    })
    this.fetch();
  },
}
</script>

<style scoped>

</style>