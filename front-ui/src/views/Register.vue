<template>
  <div class="register-container">
    <el-form :model="registerForm" ref="registerFormRef" :rules="rules" label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="registerForm.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="registerForm.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { register } from '@/api/auth';

const registerForm = ref({
  username: '',
  password: ''
});

const registerFormRef = ref(null);
const router = useRouter();

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
};

const onSubmit = async () => {
  try {
    await registerFormRef.value.validate();
    await register(registerForm.value);
    ElMessage.success('注册成功');
    router.push('/login');
  } catch (error) {
    ElMessage.error('注册失败');
  }
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fff;
}
</style> 