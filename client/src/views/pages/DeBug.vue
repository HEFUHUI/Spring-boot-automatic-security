<template>
  <div>
    <el-row>
      <el-button size="mini" :plain="true" type="info" @click="fetch" icon="el-icon-refresh">refresh</el-button>
    </el-row>
    <br>
    <el-row>
      <el-col :span="12">
        <el-form ref="form" label-width="80px">
          <el-form-item label="广播">
            <el-input v-model="input" placeholder="请输入消息!"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="send">发送</el-button>
          </el-form-item>
          <el-form-item>
            <div>
              <json-viewer v-for="(msg,index) in msgs" :key="msg+index" :value="msg" :expand-depth="4" copyable sort>
              </json-viewer>
            </div>
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
import {GET_INFO, SEND_MESSAGE} from "@/plugins/Commen"

export default {
  name: "DeBug.vue",
  data() {
    return {
      drawer: false,
      input: "",
      result: "",
      showOnlineUser: false,
      isConnected: false,
      msgs: [],
      socketInfo: {
        target: {}
      },
      users: []
    }
  },
  computed: {},
  components: {
    hOnlineUsers
  },
  methods: {
    send() {
      this.$ws.send(JSON.stringify({
        code:111,
        data:this.input
      }))
    },
    async fetch() {
      this.$ws.send(JSON.stringify({
        code: 100,
        data: {
          type: 'all'
        }
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
        case GET_INFO + 1:
          this.users = [];
          for (let key in data.data) {
            this.users.push(data.data[key])
          }
          this.refresh();
          break;
        case SEND_MESSAGE + 1:
          this.msgs.push(data.data)
          break;
        default:
          this.msgs.push(d.data);
          break;

      }
    })
  },
  created() {
    this.fetch()
  }
}
</script>

<style scoped>

</style>