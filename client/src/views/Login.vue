<template>
  <div>
    <el-row justify="center" type="flex">
      <el-col :span="10">
        <h1 style="text-align: center">用户登录</h1>
        <el-form
          :model="input"
          status-icon
          :rules="rules"
          ref="input"
          label-width="100px"
          class="demo-input"
        >
          <el-form-item label="用户名" prop="userName">
            <el-input
              type="text"
              placeholder="请输入用户名！"
              v-model="input.userName"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              type="password"
              placeholder="请输入密码！"
              v-model="input.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
<!--          <el-form-item label="验证码" prop="verificationCode">-->
<!--            <el-input-->
<!--              v-model.number="input.verificationCode"-->
<!--              placeholder="请输入下方验证码！"-->
<!--            ></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item-->
<!--            ><el-image-->
<!--              @click="refresh"-->
<!--              v-if="verfCode"-->
<!--              :src="$axios.defaults.baseURL + '/auth/ver'"-->
<!--            ></el-image-->
<!--          ></el-form-item>-->
          <el-form-item>
            <el-button type="primary" @click="submitForm">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      input: {
        userName: "",
        password: "",
        verificationCode: "",
      },
      rules: {
        username: [
          { required: true, message: "用户名不能为空!", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空!", trigger: "blur" },
        ],
        verificationCode: [
          { required: true, message: "验证码不能为空！", trigger: "blur" },
        ],
      },
      verfCode: true,
    };
  },
  methods: {
    submitForm() {
      this.$refs.input.validate(async (value) => {
        if (value) {
          const res = await this.$axios.post("auth/login-json", this.input);
          if (res.data.code !== 200) {
            this.$message({ type: "error", message: res.data.msg });
          } else {
            sessionStorage.setItem("sessionsID", res.data.msg);
            this.$store.commit("login", res.data.data);
            window.location.href = "/"
          }
        } else {
          this.$message({ type: "error", message: "请输入！" });
          return false;
        }
      });
    },
    resetForm() {
      this.input = {};
    },
    refresh() {
      this.verfCode = false;
      setTimeout(() => {
        this.verfCode = true;
      });
    },
  },
  created(){
    document.onkeypress = e=>{
      if(e.code === "Enter"){
        this.submitForm();
      }
    }
  }
};
</script>
