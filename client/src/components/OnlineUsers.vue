<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>在线用户列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="fetch">刷新</el-button>
      </div>
      <el-table
          :border="false"
          :data="users"
          style="width: 100%">
        <el-table-column
            label="头像">
          <template slot-scope="{row}">
            <el-image :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+(row.avatar && row.avatar.imageId)]" :src="$axios.defaults.baseURL+'/image/get/'+(row.avatar && row.avatar.imageId)" class="avatar"></el-image>
          </template>
        </el-table-column>
        <el-table-column
            prop="nickName"
            label="昵称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="loginIp"
            label="登录IP">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="{row}">
              <template v-if="row.sessionId !== sessionId">
                <el-button type="primary" size="mini" icon="el-icon-s-promotion" @click="current_user = row;dialogVisible = true"></el-button>
            </template>
            <template v-else>
              <el-button type="primary" disabled size="mini">自己</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog
        :append-to-body="true"
        title="提示"
        :visible.sync="dialogVisible"
        width="30%">
      <el-input type="textarea" v-model="input" placeholder="输入消息内容"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="send">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "OnlineUsers",
  data() {
    return {
      input:"",
      dialogVisible:false,
      current_user:{},
      users:[]
    }
  },
  destroyed() {
    this.userList = [];
  },
  methods:{
    send(){
      this.$ws.send(JSON.stringify({
        code:301,
        destination:this.current_user.sessionId,
        data:this.input
      }))
      this.dialogVisible = false;
    },
    async fetch() {
      this.$ws.send(JSON.stringify({
        code: 204
      }))
    },
  },
  computed:{
    sessionId(){
      return sessionStorage.getItem("sessionsID");
    },
    userInfo(){
      return this.$store.getters.userInfo;
    }
  },
  mounted() {
    this.$eventBus.$on("ws-message",msg=>{
      try {
        let data = JSON.parse(msg.data);
        switch (data.code) {
          case 214:
            this.users = [];
            for (let key in data.data) {
              this.users.push(data.data[key])
            }
            this.refresh();
            break;
          case 1:
            this.users.push(data.data)
                break;
          case 2:
            this.users = this.users.filter(item=>item.sessionId!==data.data.sessionId);
            break;
          }
      } catch (e) {
        console.log(e);
      }
    })
    this.fetch();
  }
}
</script>

<style scoped>

</style>