<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
            <el-button icon="el-icon-back" size="mini"  @click="$router.go(-1)">返回</el-button>
            <h1>所有权限</h1>
        </div>
        <el-form :inline="true" :model="search" size="mini" class="demo-form-inline">
          <el-form-item label="名称">
            <el-input
              v-model="search.title"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item label="值">
          <el-input
              v-model="search.code"
              placeholder="请输入关键字"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
            <el-button icon="el-icon-refresh-left" size="mini" @click="fetch">刷新</el-button>
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
          <el-table-column prop="permissionsId" label="ID" width="200">
          </el-table-column>
          <el-table-column prop="title" label="名称" width="120">
          </el-table-column>
          <el-table-column prop="code" label="值">
          </el-table-column>
          <el-table-column label="添加日期">
            <template slot-scope="{ row }">{{ new Date(row.createTime).format("yyyy-MM-dd") }}</template>
          </el-table-column>
          <el-table-column label="描述" prop="comment" show-overflow-tooltip></el-table-column>
          <el-table-column fixed="right" width="300" label="操作" align="center">
            <template slot-scope="{ row }">
              <el-button type="danger" icon="el-icon-delete" size="mini" circle @click="del(row.permissionsId)"></el-button>
               <el-button type="primary" icon="el-icon-edit" size="mini" circle @click="edit(row)"></el-button>
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

      <!-- 添加权限====start -->
      <el-dialog title="添加权限" :visible.sync="showDialog" width="60%">
        <el-form ref="form" :model="permission" label-width="100px" size="medium">
          <el-form-item label="权限名称" required>
            <el-input v-model="permission.title"></el-input>
          </el-form-item>
          <el-form-item label="权限值" required>
            <el-input v-model="permission.code"></el-input>
          </el-form-item>
          <el-form-item label="描述" required>
            <el-input type="textarea" v-model="permission.comment" placeholder="输入描述信息"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 添加权限====end -->
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      data: {},
      showDialog: false,
      permission: {},
      selectPermission:false,
      permissions: [],
      search:{},
      c_permission:{},
      tableSelector:[]
    };
  },
  methods: {
    async fetch() {
      const res = await this.$axios.get(`permissions?page=1&limit=10`);
      this.data = res.data.data;
    },
    async onSearch(){
      const res = await this.$axios.get(`permissions?page=1&limit=10&q=title,${this.search.title || ' '},code,${this.search.code}`);
      this.data = res.data.data;
    },
    async submit() {
      await this.$axios.post("permissions", this.permission);
      this.showDialog = false;
      await this.fetch();
    },
    async delMultiple(){
      if(this.tableSelector.length < 1){
        this.$message("至少选择一项，请选择！");
        return ;
      }
      await this.$confirm(`确定将${this.tableSelector.map(i=>i.title)}删除?`,"提示！",{type:"error"});
      for (const item of this.tableSelector) {
        await this.$axios.delete(`permissions/${item.permissionsId}`);
        await this.fetch();
      }
    },
    async del(id){
        await this.$confirm("确定删除？？");
        await this.$axios.delete(`permissions/${id}`);
        await this.fetch();
    },
  },
  created() {
    this.fetch();
  },
};
</script>