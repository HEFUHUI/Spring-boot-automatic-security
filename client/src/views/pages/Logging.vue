<template>
  <div>
    <el-row>
      <el-card class="box-card" style="margin-bottom: 10px">
        <div slot="header">
          <el-button icon="el-icon-back" size="mini"  @click="$router.go(-1)">返回</el-button>
          <h1>系统日志</h1>
        </div>
        <el-form :inline="true" :model="search" size="mini" class="demo-form-inline">
          <el-form-item label="结果">
              <el-select v-model="search.logOutcome" placeholder="选择结果">
                <el-option label="成功" value="true"></el-option>
                <el-option label="失败" value="false"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="日志类型">
            <el-select v-model="search.logType" placeholder="选择日志类型">
              <el-option label="错误日志" value="1100"></el-option>
              <el-option label="警告日志" value="1101"></el-option>
              <el-option label="用户登录" value="1000"></el-option>
              <el-option label="用户退出登录" value="1001"></el-option>
              <el-option label="人脸识别" value="1002"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">查询</el-button>
            <el-button icon="el-icon-refresh-left" size="mini" @click="fetch(pagination.current_page,pagination.page_size)">刷新</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row>
      <el-col>
        <el-table ref="multipleTable" border :data="data.items" stripe>
          <el-table-column prop="logId" label="ID" width="50" align="center">
          </el-table-column>
          <el-table-column label="日志类型" width="120">
            <template slot-scope="{row}">
              {{row.logType}}
            </template>
          </el-table-column>
          <el-table-column
              prop="logUser"
              label="关联用户|设备">
            <template slot-scope="{row}">
              <el-link @click="$router.push('/monitor/'+row.user.userId)" v-if="row.user.userType === 0" type="primary">{{ row.user.nickName }}</el-link>
              <el-link @click="$router.push('/monitor/'+row.user.userId)" v-if="row.user.userType === 1" type="primary">{{ row.user.nickName }}</el-link>
              <el-link @click="$router.push('/monitor/'+row.user.userId)" v-if="row.user.userType === 2" type="primary">{{ row.user.nickName }}</el-link>
            </template>
          </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="{ row }">
              {{ row.logOutcome ? '成功' : '失败' }}
            </template>
          </el-table-column>
          <el-table-column label="添加日期">
            <template slot-scope="{ row }">{{ new Date(row.logCreateTime).format("yyyy-MM-dd HH:mm:ss") }}</template>
          </el-table-column>
          <el-table-column prop="logComment" label="描述" show-overflow-tooltip>
          </el-table-column>
        </el-table>
        <el-card>
          <el-pagination
              style="float: right; margin-bottom: 10px"
              background
              @current-change="fetch($event,pagination.page_size)"
              @size-change="fetch(pagination.current_page,$event)"
              :page-size="data.limit"
              :page-sizes="[5,10, 20, 30, 40, 50, 100]"
              layout="sizes, prev, pager, next, jumper, ->, total, slot"
              :total="data.total"
          >
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      data: {},
      search:{
        logOutcome:"",
        logType:""
      },
      pagination:{
        current_page:1,
        page_size:10
      }
    };
  },
  methods: {
    async fetch(page = 1,limit = 10) {
      this.pagination.current_page = page;
      this.pagination.page_size = limit;
      const res = await this.$axios.get(`logging?page=${page}&limit=${limit}`);
      this.data = res.data.data;
    },
    async onSearch(){
      const res = await this.$axios.get(`logging?page=1&limit=10&q=log_outcome,${this.search.logOutcome || ' '},log_type,${this.search.logType}`);
      this.data = res.data.data;
    },
  },
  created() {
    this.fetch();
  },
};
</script>