<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>在线用户列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="fetch">刷新</el-button>
      </div>
      <el-table
          :border="false"
          :data="userList"
          style="width: 100%">
        <el-table-column
            label="头像">
          <template slot-scope="{row}">
            <el-image :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+(row.data.avatar && row.data.avatar.imageId)]" :src="$axios.defaults.baseURL+'/image/get/'+(row.data.avatar && row.data.avatar.imageId)" class="avatar"></el-image>
          </template>
        </el-table-column>
        <el-table-column
            prop="data.nickName"
            label="昵称"
            width="180">
        </el-table-column>
        <el-table-column
            prop="data.userId"
            label="用户ID">
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" icon="el-icon-s-promotion" @click="current_user = row.data;dialogVisible = true">发送消息</el-button>
            <el-button type="warning" size="mini" icon="el-icon-delete" @click="current_user = row.data;dialogVisible = true">下线用户</el-button>
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
  props: ["users"],
  data() {
    return {
      input:"",
      userList: [],
      dialogVisible:false,
      current_user:{},
    }
  },
  destroyed() {
    this.userList = [];
  },
  methods:{
    send(){
      this.$ws.send(JSON.stringify({
        code:110,
        data:{
          destination:this.current_user.userId,
          data:this.input
        }
      }))
    },
    fetch(){
      this.userList = [];
      this.users.forEach(async id => {
        if(id !== this.userInfo.user.userId){
          const res = await this.$axios.get(`user/${id}`);
          this.userList.push(res.data)
        }
      })
    }
  },
  computed:{
    userInfo(){
      return this.$store.getters.userInfo;
    }
  },
  created() {
    this.fetch();
  }
}
</script>

<style scoped>

</style>