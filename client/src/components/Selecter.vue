<template>
  <div class="selector">
    <el-dialog
      :visible.sync="visible"
      :destroy-on-close="true"
      width="60%"
      :close-on-click-modal="false"
      :append-to-body="true"
    >
      <el-row :gutter="10">
       <el-col :span="6">
         <el-button type="primary" @click="fetch">刷新</el-button>
       </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-checkbox-group v-if="multiple" v-model="selectds">
            <el-button v-for="item in source" style="margin:2px" :key="item.id">
              <el-checkbox-button :label="item" >
                <slot :data="item"></slot>
              </el-checkbox-button>
            </el-button>
          </el-checkbox-group>
          <el-radio-group v-else v-model="selectds">
            <el-button v-for="item in source" :key="item.id" style="margin: 5px">
              <el-radio-button  :label="item">
                  <slot :data="item"></slot>
              </el-radio-button>
            </el-button>
          </el-radio-group>
          <el-row>
            <el-col :span="24">
                <el-pagination
                  style="padding:5px;text-align:right"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="pagination.currentPage"
                  :background="true"
                  :page-sizes="[5,12,15,20,25,35,100,200]"
                  :page-size="pagination.page_size"
                  layout="sizes, prev, pager, next, jumper, ->, total, slot"
                  :total="pagination.total"
                ></el-pagination>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <span slot="footer">
        <el-button @click="exit">取 消</el-button>
        <el-button type="primary" @click="enter">确 定</el-button>
      </span>
    </el-dialog>
    <template v-if="!not_result">
      <slot v-if="checked" name="result"></slot>
    </template>
    <div>
      <slot name="active"></slot>
    </div>
  </div>
</template>
<script>
export default {
  name:"Selector",
  model: {
    prop: "checked"
  },
  props: ["visible", "checked", "multiple", "not_result","action","title"],
  data() {
    return {
      pagination: {
        currentPage: 1,
        total: 7,
        page_size: 12
      },
      selectds:[],
      source: []
    };
  },
  methods: {
    exit(){
      this.$emit('update:visible', false);
    },
    async fetch() {
      let res = (
        await this.$axios.get(`${this.action}${/\?=*/.test(this.action) ? "&" : "?"}limit=${this.pagination.page_size}&page=${this.pagination.currentPage}`)
      ).data;
      this.source = res.data.items
      this.pagination.total = res.data.total;
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page;
      this.fetch(page);
    },
    async handleSizeChange(size) {
      this.pagination.page_size = size;
      this.fetch(this.pagination.currentPage);
    },
    async enter() {
      this.$emit('input',this.selectds)
      this.$emit("update:visible", false);
      this.$emit("enter",this.selectds)
    }
  },
  created() {
    this.fetch(1);
  }
};
</script>