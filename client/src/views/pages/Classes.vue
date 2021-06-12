<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
          <el-button icon="el-icon-back" size="mini"  @click="$router.go(-1)">返回</el-button>
          <h1>所有班级</h1>
        </div>
        <el-form :inline="true" :model="search" size="mini" class="demo-form-inline">
          <el-form-item label="班级名称">
            <el-input
                v-model="search.name"
                placeholder="请输入关键字"
            ></el-input>
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
          <el-table-column prop="classId" label="ID">
          </el-table-column>
          <el-table-column
              prop="name"
              label="账户名称"
              show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="添加日期">
            <template slot-scope="{ row }">{{ new Date(row.createTime).format("yyyy-MM-dd") }}</template>
          </el-table-column>
          <el-table-column label="班级成员">
            <template slot-scope="{ row }">
              <el-button type="text" size="mini" v-for="stu in row.users" :key="stu.userId">
                {{ stu.nickName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="comment" label="上课状态">
            <template slot-scope="{row}">
              <el-tag type="success">{{ row.isWorking ? "上课中" : "未在上课" }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="comment" label="备注"></el-table-column>
          <el-table-column fixed="right" width="300" label="操作" align="center">
            <template slot-scope="{ row }">
              <el-button type="warning" size="mini" @click="updateState(row)">切换状态</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" circle @click="del(row.classId)"></el-button>
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
      <!-- 添加班级====start -->
      <el-dialog @open="openDialog" title="添加班级" :visible.sync="showDialog" width="60%">
        <el-form ref="form" :model="tClass" label-width="100px" size="medium">
          <el-form-item label="班级名称" required>
            <el-input v-model="tClass.name" placeholder="输入班级名称"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="tClass.comment" placeholder="输入备注" type="textarea"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showDialog = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 添加班级====end -->
    </el-row>
  </div>
</template>
<script>
// import hSelector from "../../components/Selecter"
export default {
  data() {
    return {
      data: {},
      showDialog: false,
      tClass: {
        users:[],
        comment:"",
        name:""
      },
      selectStu:false,
      pagination:{
        current_page:1,
        page_size:10
      },
      grantUserRole:false,
      changeAvatar:false,
      showSelector:false,
      roles: [],
      search:{},
      c_user:{},
      tableSelector:[]
    };
  },
  components:{
    // hSelector
  },
  methods: {
    async fetch(page = this.pagination.current_page,limit=this.pagination.page_size) {
      this.pagination.page_size = limit;
      this.pagination.current_page = page;
      const res = await this.$axios.get(`class?page=${page}&limit=${limit}`);
      this.data = res.data.data;
    },
    async updateState(row){
      await this.$axios.put(`class/updateState/${row.classId}/${!row.isWorking}`)
      await this.fetch();
    },
    async delMultiple(){
      if(this.tableSelector.length < 1){
        this.$message("至少选择一项，请选择！");
        return ;
      }
      await this.$confirm(`确定将${this.tableSelector.map(i=>i.name)}删除?`,"提示！",{type:"error"});
      for (const item of this.tableSelector) {
        await this.$axios.delete(`class/${item.classId}`);
        await this.fetch()
      }
    },
    async onSearch(){
      const res = await this.$axios.get(`class?page=${this.pagination.current_page}&limit=${this.pagination.page_size}&q=name,${this.search.name}`);
      this.data = res.data.data;
    },
    async submit() {
      await this.$axios.post("class", this.tClass);
      this.showDialog = false;
      await this.fetch();
    },
    async del(id){
      await this.$confirm("确定删除？？");
      await this.$axios.delete(`class/${id}`);
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