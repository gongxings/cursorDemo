<template>
  <div class="login-container">
    <div class="login-background"></div>
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
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

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
</script>

<style lang="scss" scoped>
.login-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: hidden;
  background-color: #f0f2f5;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    45deg,
    #1a237e,
    #0d47a1,
    #01579b,
    #006064,
    #004d40
  );
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
  z-index: 0;

  &::before,
  &::after {
    content: '';
    position: absolute;
    left: -50%;
    top: -50%;
    right: -50%;
    bottom: -50%;
    background: linear-gradient(
      0deg,
      transparent 24%,
      rgba(255, 255, 255, 0.05) 25%,
      rgba(255, 255, 255, 0.05) 26%,
      transparent 27%,
      transparent 74%,
      rgba(255, 255, 255, 0.05) 75%,
      rgba(255, 255, 255, 0.05) 76%,
      transparent 77%,
      transparent
    ),
    linear-gradient(
      90deg,
      transparent 24%,
      rgba(255, 255, 255, 0.05) 25%,
      rgba(255, 255, 255, 0.05) 26%,
      transparent 27%,
      transparent 74%,
      rgba(255, 255, 255, 0.05) 75%,
      rgba(255, 255, 255, 0.05) 76%,
      transparent 77%,
      transparent
    );
    background-size: 50px 50px;
  }

  &::after {
    background-position: 25px 25px;
    animation: patternMove 20s linear infinite;
  }
}

.login-card {
  position: relative;
  width: 420px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  z-index: 1;
  
  .login-header {
    text-align: center;
    color: #1a237e;
    
    h2 {
      margin: 0;
      font-size: 24px;
      font-weight: 600;
    }
    
    p {
      margin: 10px 0 0;
      font-size: 16px;
      color: #666;
    }
  }
  
  .login-form {
    padding: 20px;
    
    .el-input {
      height: 40px;
      
      :deep(.el-input__wrapper) {
        background: rgba(255, 255, 255, 0.8);
      }
    }
    
    .login-button {
      width: 100%;
      height: 40px;
      font-size: 16px;
    }
  }
}

@keyframes gradientBG {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes patternMove {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(50px, 50px);
  }
}

// 响应式设计
@media screen and (max-width: 576px) {
  .login-card {
    width: 90%;
    margin: 0 20px;
  }
}
</style>