<template>
  <div>
    <el-row>
      <el-col>
        <el-card class="box-card" style="margin-bottom: 10px">
          <div slot="header">
            <h1>
              <el-button
                style="margin-right: 10px"
                @click="$router.go(-1)"
                icon="el-icon-back"
                size="small"
                >返回</el-button
              >
              <span>所有图片</span>
            </h1>
          </div>
          <el-form :model="search" size="mini" class="demo-form-inline">
            <el-form-item>
              <el-input
                v-model="search.alias"
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
          <el-upload
            multiple
            :on-error="uploadError"
            :show-file-list="false"
            :on-success="uploadSuccess"
            :headers="{ Authorization: $store.getters.token }"
            drag
            :action="$axios.defaults.baseURL + '/image/upload'"
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或
              <em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              <slot name="tip"></slot>
            </div>
          </el-upload>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <el-row>
          <el-card style="padding: 10px; margin: 10px 0"
            ><el-col
              :span="3"
              v-for="image in data.items"
              :key="image.image_id"
            >
                <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);padding:10px">
                  <el-image
                    :preview-src-list="[$axios.defaults.baseURL+'/image/get/'+image.imageId]"
                    style="height:100px"
                    fit="cover"
                    :src="$axios.defaults.baseURL+'/image/get/'+image.imageId"
                    :alt="image.alias"
                  />
                  <el-button size="mini" type="danger" @click="del(image.imageId)">删除</el-button>
                </div>
            </el-col>
          </el-card>
        </el-row>
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
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      data: {},
      user: {
        nickName: "",
        password: "",
        role: [],
      },
      showSelector: false,
      roles: [],
      search: {},
    };
  },
  methods: {
    async fetch() {
      const res = await this.$axios.get(`image?page=1&limit=10`);
      this.data = res.data.data;
    },
    async onSearch() {
      const res = await this.$axios.get(
        `image?page=1&limit=10&q=alias,${this.search.alias || " "}`
      );
      this.data = res.data.data;
    },
    uploadError(err){
        this.$message({type:"error",message:JSON.parse(err.message).msg,showClose:true})
        this.fetch();
    },
    uploadSuccess(res){
      this.$message({type:"success",message:JSON.parse(res).msg,showClose:true})
      this.fetch();
    },
    async del(id){
        this.$confirm("确定删除？？").then(async ()=>{
            await this.$axios.delete(`image/${id}`)
            this.fetch();
        })
    },
  },
  created() {
    this.fetch();
  },
};
</script>