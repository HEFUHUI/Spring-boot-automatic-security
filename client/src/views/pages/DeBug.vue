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
            <json-viewer :value="msg" v-for="(msg,index) in msgs" :key="msg+index" boxed :expand-depth="0" copyable>
            </json-viewer>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="12">
        <h-online-users :users="users" v-if="showOnlineUser"></h-online-users>
      </el-col>
    </el-row>
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
      showOnlineUser: true,
      isConnected: false,
      msgs: [],
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
      this.$ws.send(JSON.stringify({
        code: 104,
        data: this.input
      }))
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
        data = d;
      }
      switch (data.code) {
        case 214:
          this.users = [];
          for (let key in data.data) {
            this.users.push(data.data[key])
          }
          this.refresh();
          break;
        case 100:
          try {
            this.msgs.push(JSON.parse(data.data))
          }catch (e) {
            this.msgs.push(data.data);
          }
          break;
        case 1:
          this.users.push(data.data)
              break;
        case 2:
          this.users = this.users.filter(item=>item.userId!==data.data.userId)
      }
    })
    if(this.userInfo.user.userType === 0){
      this.fetch();
    }
  },
}
</script>

<style scoped>

</style>