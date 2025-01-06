<template>
  <div class="login-container">
    <div class="login-left">
      <img src="/path/to/rental.svg" alt="租房图表" class="login-image" />
    </div>
    <div class="login-right">
      <el-card class="login-card">
        <template #header>
          <div class="login-header">
            <h2>房产管理系统</h2>
            <p>欢迎登录</p>
          </div>
        </template>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              class="login-button"
              @click="handleLogin"
            >
              登录
            </el-button>
            <el-button
              type="default"
              class="register-button"
              @click="handleRegister"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
}
.login-left {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-image {
  max-width: 100%;
  height: auto;
}
.login-right {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const loading = ref(false)
const loginFormRef = ref<FormInstance>()
const userStore = useUserStore()

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true;
        const res = await userStore.loginAction(loginForm);
        if (res.success) {
          ElMessage.success(res.message || '登录成功');
          await userStore.getUserInfoAction();
          await router.replace('/dashboard');
        }
      } catch (error: any) {
        console.error('登录失败:', error);
        ElMessage.error(error.message || '登录失败');
      } finally {
        loading.value = false;
      }
    }
  });
};

const handleRegister = () => {
  router.push('/register');
};
</script>