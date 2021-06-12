<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
          <el-button icon="el-icon-back" size="mini"  @click="$router.go(-1)">返回</el-button>
          <h1>所有角色</h1>
        </div>
        <el-form
          :inline="true"
          :model="search"
          size="mini"
          class="demo-form-inline"
        >
          <el-form-item label="名称">
            <el-input
              v-model="search.name"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
            <el-button icon="el-icon-refresh-left" size="mini" @click="fetch"
              >刷新</el-button
            >
          </el-form-item>
        </el-form>
        <el-button type="primary" size="mini" @click="showDialog = true"
          >添加</el-button
        >
        <el-button type="danger" size="mini" @click="delMultiple">删除</el-button>
      </el-card>
    </el-row>
    <el-row>
      <el-col>
        <el-table ref="multipleTable" border :data="data.items" stripe @selection-change="tableSelector = $event">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="roleId" label="ID"> </el-table-column>
          <el-table-column prop="name" label="名称" width="120">
          </el-table-column>
          <el-table-column label="添加日期">
            <template slot-scope="{ row }">{{
              new Date(row.createTime).format("yyyy-MM-dd")
            }}</template>
          </el-table-column>
          <el-table-column label="权限列表" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <el-popover
                :title="per.title"
                width="200"
                v-for="per in row.permissions"
                :key="per.permissionsId"
                trigger="click"
              >
              {{per.comment}}
              <el-button  type="text" style="margin-right:10px" size="mini"  slot="reference">{{ per.title }}</el-button>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            label="描述"
            prop="comment"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            fixed="right"
            width="300"
            label="操作"
            align="center"
          >
            <template slot-scope="{ row }">
              <el-button type="text">设置权限</el-button>
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                circle
                @click="del(row.roleId)"
              ></el-button>
              <el-button
                type="primary"
                icon="el-icon-edit"
                size="mini"
                circle
                @click="edit(row)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-card>
          <el-pagination
            style="float: right; margin-bottom: 10px"
            background
            :page-size="data.limit"
            layout="sizes,prev, pager, next"
            :total="data.total"
          >
          </el-pagination>
        </el-card>
      </el-col>

      <!-- 添加角色====start -->
      <el-dialog title="添加角色" :visible.sync="showDialog" width="60%">
        <el-form ref="form" :model="role" label-width="100px" size="medium">
          <el-form-item label="角色昵称" required>
            <el-input v-model="role.name"></el-input>
          </el-form-item>
          <el-form-item label="描述" required>
            <el-input
              type="textarea"
              v-model="role.comment"
              placeholder="输入描述信息"
            ></el-input>
          </el-form-item>
          <el-form-item label="权限选择" required>
            <h-selector
              action="permissions"
              :multiple="true"
              v-model="role.permissions"
              :visible.sync="selectPermission"
            >
              <div slot-scope="item">
                {{ item.data.title }}
              </div>
              <el-button
              type="primary"
                size="mini"
                @click="selectPermission = true"
                slot="active"
                >选择</el-button>
                <div slot="result">
                  <el-tag v-for="per in role.permissions" :key="per.permisssionsId">{{per.title}}</el-tag>
                </div>
            </h-selector>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 添加角色====end -->
    </el-row>
  </div>
</template>
<script>
import hSelector from "../../components/Selecter";
export default {
  data() {
    return {
      data: {},
      showDialog: false,
      role: {},
      selectPermission: false,
      roles: [],
      search: {},
      c_role: {},
      tableSelector:[]
    };
  },
  components: {
    hSelector,
  },
  methods: {
    async delMultiple(){
      if(this.tableSelector.length < 1){
        this.$message("至少选择一项，请选择！");
        return ;
      }
      await this.$confirm(`确定将${this.tableSelector.map(i => i.name)}删除?`,"提示！",{type:"error"});
      for (const item of this.tableSelector) {
        await this.$axios.delete(`role/${item.roleId}`);
        await this.fetch();
      }
    },
    async fetch() {
      const res = await this.$axios.get(`role?page=1&limit=10`);
      this.data = res.data.data;
    },
    async onSearch() {
      const res = await this.$axios.get(
        `role?page=1&limit=10&q=name,${this.search.name || " "}`
      );
      this.data = res.data.data;
    },
    async submit() {
      await this.$axios.post("role", this.role);
      this.showDialog = false;
      await this.fetch();
    },
    async del(id) {
      await this.$confirm("确定删除？？");
      await this.$axios.delete(`role/${id}`);
      await this.fetch();
    },
  },
  created() {
    this.fetch();
  },
};
</script>