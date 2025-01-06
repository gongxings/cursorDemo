<template>
  <div class="login-container">
      <div class="login-content">
        <div class="login-left">
          <img src="/public/rental.svg" alt="租房图表" class="login-image" />
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
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
  position: relative;
  overflow: hidden;
}
.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}
.background-svg {
  width: 100%;
  height: 100%;
  opacity: 0.3;
}
.login-content {
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
  max-width: 800px;
  width: 100%;
}
.login-left {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20px;
}
.login-image {
  max-width: 100%;
  height: auto;
  margin-right: 20px;
}
.login-right {
  flex: 1;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 20px;
}
.login-card {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.login-header {
  text-align: center;
  color: #00796b;
}
.login-header h2 {
  margin: 0;
  font-size: 26px;
  font-weight: bold;
}
.login-header p {
  margin: 5px 0 0;
  font-size: 16px;
  color: #004d40;
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