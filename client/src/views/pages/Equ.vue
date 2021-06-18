<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
            <el-button icon="el-icon-back" size="mini"  @click="$router.go(-1)">返回</el-button>
            <h1>所有设备</h1>
        </div>
        <el-form :inline="true" :model="search" size="mini" class="demo-form-inline">
          <el-form-item label="姓名">
            <el-input
              v-model="search.nickName"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item label="ID">
            <el-input
              v-model="search.user_id"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
            <el-button icon="el-icon-refresh-left" size="mini" @click="fetch">刷新</el-button>
          </el-form-item>
        </el-form>
          <el-button type="primary" size="mini" @click="showDialog = true">添加</el-button>
      </el-card>
    </el-row>
    <el-row>
      <el-col>
        <el-card class="box-card">
          <el-row>
            <el-col :span="4" v-for="equ in data.items" :key="equ.userId">
                <h-equ :equ="equ"></h-equ>
            </el-col>
          </el-row>
        </el-card>
        <el-card>
          <el-pagination
              style="float: right; margin-bottom: 10px"
              background
              @current-change="fetch($event,pagination.page_size)"
              @size-change="fetch(pagination.current_page,$event)"
              :page-size="data.limit"
              :page-sizes="[5, 10,20, 30, 40, 50, 100]"
              layout="sizes, prev, pager, next, jumper, ->, total, slot"
              :total="data.total"
          >
          </el-pagination>
        </el-card>
      </el-col>

      <!-- 更改头像 -->
      <h-selector action="image" no_result @enter="changeAvatarHandle" :visible.sync="changeAvatar">
        <div slot-scope="item" >
            <el-image :src="$axios.defaults.baseURL+'/image/get/'+(item.data && item.data.imageId)" fit="cover" style="width:100px;height:50px"></el-image>
        </div>
      </h-selector>
      <!-- 更改头像====end -->

      <!-- 设置角色 -->
      <h-selector action="role" :multiple="true" v-model="c_equ.roles" no_result @enter="grantequRoleHandle" :visible.sync="grantequRole">
        <div slot-scope="item" >
            <span style="font-size:20px">{{item.data.name}}</span>
        </div>
      </h-selector>
      <!-- 设置角色====end -->

      <!-- 添加设备====start -->
      <el-dialog @open="openDialog" title="添加设备" :visible.sync="showDialog" width="60%">
        <el-form ref="form" :model="equ" label-width="80px" size="medium">
          <el-form-item label="设备昵称" required>
            <el-input v-model="equ.nickName"></el-input>
          </el-form-item>
          <el-form-item label="设备类型" required>
            <el-select v-model="equ.equType" placeholder="请选择设备类型">
              <el-option label="管理员" value="0"></el-option>
              <el-option label="普通设备|学生" value="1"></el-option>
              <el-option label="设备" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设备名" required>
            <el-input type="text" v-model="equ.loginName"></el-input>
          </el-form-item>
          <el-form-item label="密码" required>
            <el-input type="password" v-model="equ.password"></el-input>
          </el-form-item>
          <el-form-item label="照片">
            <h-selector action="image" v-model="equ.avatar" :visible.sync="showSelector">
              <div slot-scope="item">
                <div>
                  <el-image :src="$axios.defaults.baseURL+'/image/get/'+(item.data && item.data.imageId)" fit="cover" style="width:100px;height:50px">
                  </el-image>
                </div>
              </div>
              <div slot="result">
                  <el-image :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+(equ.avatar && equ.avatar.imageId)]"
                  :src="$axios.defaults.baseURL+'/image/get/'+(equ.avatar && equ.avatar.imageId)" fit="cover" style="width:100px;height:50px">
                  </el-image>
              </div>
              <el-button slot="active" size="mini" @click="showSelector = true">选择</el-button>
            </h-selector>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 添加设备====end -->
    </el-row>
  </div>
</template>
<script>
import hSelector from "../../components/Selecter"
import hEqu from "../../components/EquItem.vue"
export default {
  data() {
    return {
      data: {},
      showDialog: false,
      equ: {
      },
      pagination:{
        current_page:1,
        page_size:10
      },
      grantequRole:false,
      changeAvatar:false,
      showSelector:false,
      equs: [],
      search:{},
      c_equ:{}
    };
  },
  components:{
    hSelector,
    hEqu
  },
  methods: {
    async fetch(page = 1,limit=10) {
      this.pagination.page_size = limit;
      this.pagination.current_page = page;
      const res = await this.$axios.get(`user?page=${page}&limit=${limit}&q=user_type,2`);
      this.data = res.data.data;
    },

    async onSearch(){
      const res = await this.$axios.get(`user?page=${this.pagination.current_page}&limit=${this.pagination.page_size}&q=nick_name,${this.search.nickName || ' '},user_type,2`);
      this.data = res.data.data;
    },

    async submit() {
      await this.$axios.post("equ", this.equ);
      this.showDialog = false;
      this.fetch();
    },

    async grantequRoleHandle(event){
      await this.$axios.put(`user/grant/${this.c_equ.equId}`,event);
      this.fetch();
    },

    async removeRoleHandle(role,equ){
      await this.$confirm(`确定从 ${equ.nickName} 上移除 ${role.name} 身份？`)
      await this.$axios.put(`user/revoke/${equ.equId}/${role.roleId}`);
      this.fetch();
    },

    async changeAvatarHandle(event){
      await this.$axios.put(`user/${this.c_equ.equId}`,{
        avatar:event
      })
      this.fetch();
    },
    async del(id){
        await this.$confirm("确定删除？？");
        await this.$axios.delete(`equ/${id}`);
        this.fetch();
    },
    async openDialog() {
      const res = await this.$axios.get("role");
      this.roles = res.data.data.items;
    },
  },
  created() {
    this.fetch();
  },
};
</script>