<template>
  <el-container>
    <el-header>
      <div class="logo">{{'校园安防'}}</div>

      <div class="right">
        <el-popover
            placement="top"
            trigger="click">
            <el-button type="primary" @click="logout">退出登录</el-button>
          <div slot="reference" class="right-body">
            <div>
              <el-image class="avatar" fit="cover"
                        :src="$axios.defaults.baseURL+'/image/get/'+(userInfo.user.avatar&&userInfo.user.avatar.imageId)"></el-image>
            </div>
            <div>
              <span v-text="userInfo.user && userInfo.user.nickName">管理员</span>
              <br>
              <span v-text="userInfo.user && userInfo.user.userId">123</span>
            </div>
          </div>
        </el-popover>
      </div>
    </el-header>
    <el-container>
      <el-aside :style="{ height: height + 'px', width: 'auto' }">
        <el-menu
          router
          :collapse="isCollapse"
          default-active="2"
          class="el-menu-vertical-demo"
        >
          <el-menu-item @click="isCollapse = !isCollapse">
            <i class="el-icon-menu"></i>
            <span slot="title">{{ isCollapse ? "展开" : "收起" }}</span>
          </el-menu-item>
          <template v-for="(item,index) in menus">
            <el-submenu
              :index="'1-'+index"
              :key="item.title"
              :title="item.title"
              v-if="item.children"
            >
              <template slot="title">
                <i :class="item.icon"></i>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item-group>
                <template slot="title">{{ item.title }}</template>
                <el-menu-item
                  :index="child.link"
                  v-for="child in item.children"
                  :key="item.title + child.title"
                  >{{ child.title }}</el-menu-item
                >
              </el-menu-item-group>
            </el-submenu>
            <el-menu-item v-else :index="item.link" :key="item.title">
              <i :class="item.icon"></i>
              <span slot="title">{{ item.title }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <el-main>
        <!-- name="el-zoom-in-center"-->
        <transition mode="out-in" name="slide-fade">
          <router-view></router-view>
        </transition>
      </el-main>
    </el-container>

    <el-drawer :visible.sync="showDrawer" size="60%">
        <h-online-users :users="users"></h-online-users>
    </el-drawer>
    <el-dialog
        title="已接收消息"
        :visible.sync="messageDialog"
        width="60%">
      <h-char :msgs="msgs"></h-char>
    </el-dialog>
    <div class="fixed-box">
      <div><el-button type="info" icon="el-icon-user-solid" @click="showDrawer = true" circle></el-button></div>
      <div>
        <el-badge :value="unread" :hidden="unread < 1">
          <el-button type="primary" icon="el-icon-message" @click="messageDialog = true;unread = 0" circle></el-button>
        </el-badge>
      </div>
    </div>
    <el-footer>
      <div>
        @校园安防系统-2021
      </div>
    </el-footer>
  </el-container>
</template>
<script>
import hOnlineUsers from "../components/OnlineUsers";
import hChar from "../components/Chat";
import {GET_INFO} from "../plugins/Commen";
export default {
  data() {
    return {
      height: window.innerHeight - 60,
      isCollapse: true,
      showDrawer:false,
      messageDialog:false,
      unread:0,
      users:[],
      msgs:[],
      menus: [
        { title: "首页", link: "/", icon: "el-icon-s-home" },
        {
          title: "用户管理",
          children: [
            { title: "所有用户", link: "/user", icon: "el-icon-user-solid" },
            { title: "用户角色", link: "/role", icon: "el-icon-s-custom" },
            { title: "权限管理", link: "/permissions", icon: "el-icon-cpu" },
          ],
          icon: "el-icon-user-solid",
        },
        {
          title: "教务管理",
          children: [
            { title: "学生管理", link: "/student", icon: "el-icon-user-solid" },
            { title: "班级管理", link: "/classes", icon: "el-icon-user-solid" },
          ],
          icon: "el-icon-school",
        },
        {
          title:"安防系统",
          children:[
            { title: "设备管理", link: "/equ" },
          ],
          icon:"el-icon-cpu"
        },
        {
          title:"系统设置",
          children:[
            { title: "图片管理", link: "/image" },
            { title: "系统日志", link: "/logging" },
          ],
          icon:"el-icon-setting"
        }
      ],
    };
  },
  components:{
    hOnlineUsers,
    hChar
  },
  methods:{
    async logout(){
      await this.$confirm('确定退出登录吗？？');
      await this.$ws.close();
      await this.$axios.get("user/logout");
      await this.$router.push("/login");
    },
    async fetch(){
      const res = await this.$axios.get("user/me");
      this.$store.commit("login",res.data.data);
    },

  },
  mounted() {
    this.$eventBus.$on("ws-message",d=>{
      const data = JSON.parse(d.data);
      switch (data.code){
        case GET_INFO+1:
          this.users = [];
          for(let key in data.data){
            this.users.push(data.data[key])
          }
          break;
        case 311:
          if(!this.messageDialog){
            this.unread++;
          }
          this.msgs.push(data);
          break;
      }
    })
  },
  computed:{
    userInfo(){
      return this.$store.getters.userInfo;
    },
    readyState(){
      return this.$store.getters.readyState;
    }
  },
  created() {
    // document.onkeydown = (e)=>{
    //   this.$message(`${e.key}`)
    //  e.preventDefault()
    // }
    this.fetch();
  }
};
</script>
<style scoped>
.el-header {
  background-color: #409eff;
  color: #fff;
  line-height: 60px;
}
.el-header{
  position: fixed;
  width: 100vw;
  z-index: 100;
  top:0;
}
.el-header .logo{
  float: left;
}

.el-header .right{
  cursor: pointer;
  user-select: none;
  width: 260px;
  box-sizing: border-box;
  height: 100%;
  float: right;
}
.el-header .right-body{
  display: flex;
  line-height: 30px;
}
.el-header .right-body .el-image{
  margin-right: 7px;
}
.el-footer{
  position: fixed;
  width: 100vw;
  height: 30px !important;
  bottom: 0;
  text-align: center;
}
.el-aside {
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
  color: #fff;
  margin-top: 60px;
}
.el-main{
  margin-top: 60px;
  overflow: auto;
  height: 90vh;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
}

.slide-fade-enter-active {
  transition: all .8s ease;
}
.slide-fade-leave-active {
  transition: all .1s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to {
  transform: translateY(100px) scale(0.9);
  opacity: 0;
}
.fixed-box{
  position: fixed;
  bottom: 200px;
  z-index: 100000;
  right: 50px;
  padding: 10px;
  background: rgba(221, 221, 221, 0.33);
  border-radius: 10px;
}
.fixed-box div{
  margin-top: 5px;
}
</style>