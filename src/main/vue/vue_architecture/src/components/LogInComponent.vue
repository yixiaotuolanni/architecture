<template>
  <div class="login-container">
    <h1 class="login-title">登录</h1>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="电话号码" prop="phoneNumber">
        <el-input v-model="ruleForm.phoneNumber"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="ruleForm.password" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loginWithCus('ruleForm')">用户登录</el-button>
        <el-button type="primary" @click="loginWithBus('ruleForm')">商家登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import router from "@/router/index.js";
import {axiosInstance} from "@/main.js";

export default {
  data() {
    return {
      ruleForm: {
        phoneNumber : '',
        password : ''
      },
      rules: {
        phoneNumber : [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { len:11, message: '长度不为11', trigger: 'blur' }
        ],
        password : [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    };
  },
  methods: {
    loginWithCus(formName) {
      axiosInstance.post('/customer/login',{
        phoneNumber : this.ruleForm.phoneNumber,
        password : this.ruleForm.password
      },{
        headers:
          {"Content-Type":"application/x-www-form-urlencoded"}
      }).then(response => {
        // 处理登录成功的响应
        localStorage.setItem("token",response.data)
        router.push('/customer')
      }).catch(error => {
        // 处理登录失败的情况
        // 可以在这里提示用户登录失败的原因
        console.error('登录失败:', error);
      });

    },
    loginWithBus(formName) {
      axiosInstance.post('/business/login',{
        phoneNumber : this.ruleForm.phoneNumber,
        password : this.ruleForm.password
      },{
        headers:
          {"Content-Type":"application/x-www-form-urlencoded"}
      }).then(response => {
        // 处理登录成功的响应
        localStorage.setItem("token",response.data)
        router.push('/business')
      }).catch(error => {
        // 处理登录失败的情况
        // 可以在这里提示用户登录失败的原因
        console.error('登录失败:', error);
      });

    }
  }
}
</script>
<style>
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-title {
  font-weight: bolder;
  font-size: 12px;
  text-align: center;
  justify-content: center;
  margin-bottom: 20px;
}

</style>




