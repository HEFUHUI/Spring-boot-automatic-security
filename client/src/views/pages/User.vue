<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
            <el-button icon="el-icon-back" size="mini" @click="$router.go(-1)">返回</el-button>
            <h1>所有用户</h1>
        </div>
        <el-form :inline="true" :model="search" size="mini" class="demo-form-inline">
          <el-form-item label="姓名">
            <el-input
              v-model="search.nickName"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户类型">
            <el-select v-model="search.userType" @change="onSearch" placeholder="请选择用户类型">
              <el-option label="管理员" value="0"></el-option>
              <el-option label="普通用户|学生" value="1"></el-option>
              <el-option label="设备" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
            <el-button icon="el-icon-refresh-left" size="mini" @click="fetch(1)">刷新</el-button>
          </el-form-item>
        </el-form>
          <el-button type="primary" size="mini" @click="showDialog = true">添加</el-button>
          <el-button type="danger" size="mini" @click="delMultiple">删除</el-button>
      </el-card>
    </el-row>
    <el-row>
      <el-col>
        <el-table ref="multipleTable" border :data="data.items" stripe @selection-change="tableSelector = $event">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="userId" label="ID">
          </el-table-column>
          <el-table-column prop="nickName" label="姓名" width="120">
          </el-table-column>
          <el-table-column
            prop="loginName"
            label="账户名称"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="状态">
            <template slot-scope="{ row }">
              {{ row.online ? '在线' : '离线' }}
            </template>
          </el-table-column>
          <el-table-column label="添加日期">
            <template slot-scope="{ row }">{{ new Date(row.createTime).format("yyyy-MM-dd") }}</template>
          </el-table-column>
          <el-table-column label="用户照片">
            <template slot-scope="{ row }">
              <el-image style="width:100px;height:50px" 
              :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+(row.avatar && row.avatar.imageId)]"
              :src="$axios.defaults.baseURL+'/image/get/'+(row.avatar && row.avatar.imageId)"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="角色列表">
            <template slot-scope="{ row }">
              <div v-for="role in row.roles" :key="role.roleId">
                <el-tag type="warning" size="mini" closable @close="removeRoleHandle(role,row)">{{role.name}}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="用户类型">
            <template slot-scope="{ row }">
              <el-tag type="primary" v-if="row.userType===0">
                管理员
              </el-tag>
              <el-tag type="primary" v-if="row.userType===1">
                普通用户|学生
              </el-tag>
              <el-tag type="primary" v-if="row.userType===2">
                设备
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" width="300" label="操作" align="center">
            <template slot-scope="{ row }">
              <el-button type="text" @click="c_user = row;changeAvatar = true">更改头像</el-button>
              <el-button type="text" @click="c_user = row;grantUserRole = true">设置角色</el-button>
              <el-button type="text" v-if="row.userType === 1" @click="c_user = row;setClassed = true">设置班级</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" circle @click="del(row.userId)"></el-button>
               <el-button type="primary" icon="el-icon-edit" size="mini" circle @click="edit(row)"></el-button>
            </template>
          </el-table-column>
        </el-table>
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

      <!-- 设置班级 -->
      <h-selector action="class" multiple v-model="c_user.tclass" no_result @enter="setClassedConfirm" :visible.sync="setClassed">
        <div slot-scope="item" >
          <span style="font-size:20px">{{item.data.name}}</span>
        </div>
      </h-selector>
      <!-- 设置班级====end -->

      <!-- 更改头像 -->
      <h-selector action="image" no_result @enter="changeAvatarHandle" :visible.sync="changeAvatar">
        <div slot-scope="item" >
            <el-image :src="$axios.defaults.baseURL+'/image/get/'+(item.data && item.data.imageId)" fit="cover" style="width:100px;height:50px">
            </el-image>
        </div>
      </h-selector>
      <!-- 更改头像====end -->

      <!-- 设置角色 -->
      <h-selector action="role" multiple v-model="c_user.roles" no_result @enter="grantUserRoleHandle" :visible.sync="grantUserRole">
        <div slot-scope="item" >
            <span style="font-size:20px">{{item.data.name}}</span>
        </div>
      </h-selector>
      <!-- 设置角色====end -->

      <!-- 添加用户====start -->
      <el-dialog @open="openDialog" title="添加用户" :visible.sync="showDialog" width="60%">
        <el-form ref="form" :model="user" label-width="100px" size="medium">
          <el-form-item label="用户昵称" required>
            <el-input v-model="user.nickName"></el-input>
          </el-form-item>
          <el-form-item label="用户类型" required>
            <el-select v-model="user.userType" placeholder="请选择用户类型">
              <el-option label="管理员" value="0"></el-option>
              <el-option label="普通用户|学生" value="1"></el-option>
              <el-option label="设备" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户名" required>
            <el-input type="text" v-model="user.loginName"></el-input>
          </el-form-item>
          <el-form-item label="密码" required>
            <el-input type="password" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item label="照片">
            <h-selector action="image" v-model="user.avatar" :visible.sync="showSelector">
              <div slot-scope="item">
                <div>
                  <el-image :src="$axios.defaults.baseURL+'/image/get/'+(item.data && item.data.imageId)" fit="cover" style="width:100px;height:50px">
                  </el-image>
                </div>
              </div>
              <div slot="result">
                  <el-image :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+(user.avatar && user.avatar.imageId)]"
                  :src="$axios.defaults.baseURL+'/image/get/'+(user.avatar && user.avatar.imageId)" fit="cover" style="width:100px;height:50px">
                  </el-image>
              </div>
              <el-button slot="active" size="mini" @click="showSelector = true">选择</el-button>
            </h-selector>
          </el-form-item>
          <el-form-item label="用户角色">
            <el-checkbox-group v-model="user.roles">
              <el-button v-for="role in roles" :key="role.roleId">
                <el-checkbox-button :label="role">
                  <span style="font-size:20px">{{role.name}}</span>
                </el-checkbox-button>
              </el-button>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 添加用户====end -->
    </el-row>
  </div>
</template>
<script>
import hSelector from "../../components/Selecter"
export default {
  data() {
    return {
      data: {},
      showDialog: false,
      user: {
        nickName: "",
        password: "",
        roles: [],
      },
      pagination:{
        current_page:1,
        page_size:10
      },
      setClassed:false,
      grantUserRole:false,
      changeAvatar:false,
      showSelector:false,
      roles: [],
      search:{
        userId:""
      },
      c_user:{},
      tableSelector:[]
    };
  },
  components:{
    hSelector
  },
  methods: {
    async fetch(page = 1,limit=10) {
      this.pagination.page_size = limit;
      this.pagination.current_page = page;
      const res = await this.$axios.get(`user?page=${page}&limit=${limit}`);
      this.data = res.data.data;
      this.search = {};
    },
    async onSearch(){
      const res = await this.$axios.get(`user?page=${this.pagination.current_page}&limit=${this.pagination.page_size}&q=nick_name,${this.search.nickName || ' '},user_type,${this.search.userType}`);
      this.data = res.data.data;
    },
    async delMultiple(){
      if(this.tableSelector.length < 1){
        this.$message("至少选择一项，请选择！");
        return ;
      }
      await this.$confirm(`确定将${this.tableSelector.map(i => i.nickName)}删除?`,"提示！",{type:"error"});
      for (const item of this.tableSelector) {
        await this.$axios.delete(`user/${item.userId}`);
        await this.fetch();
      }
    },
    async setClassedConfirm(e){
      await this.$axios.put(`user/setClass/${this.c_user.userId}/${e.classId}`);
      await this.fetch();
    },
    async submit() {
      await this.$axios.post("user", this.user);
      this.showDialog = false;
      await this.fetch();
    },
    async grantUserRoleHandle(event){
      await this.$axios.put(`user/grant/${this.c_user.userId}`,event);
      await this.fetch();
    },
    async removeRoleHandle(role,user){
      await this.$confirm(`确定从 ${user.nickName} 上移除 ${role.name} 身份？`)
      await this.$axios.put(`user/revoke/${user.userId}/${role.roleId}`);
      await this.fetch();
    },
    async changeAvatarHandle(event){
      await this.$axios.put(`user/${this.c_user.userId}`,{
        avatar:event
      })
      await this.fetch();
    },
    async del(id){
        await this.$confirm("确定删除？？");
        await this.$axios.delete(`user/${id}`);
        await this.fetch();
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