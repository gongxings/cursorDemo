<template>
  <el-card>
    <el-form :model="userForm" ref="userFormRef" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="userForm.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="userForm.role" placeholder="请选择角色">
          <el-option label="管理员" value="ADMIN"></el-option>
          <el-option label="普通用户" value="USER"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { createUser, updateUser, getUserById } from '@/api/user';
import type { UserData } from '@/api/user';

const router = useRouter();
const route = useRoute();
const userFormRef = ref();
const userForm = reactive<UserData>({
  username: '',
  role: 'USER'
});

const isEdit = route.name === 'UserEdit';

const fetchUser = async (id: number) => {
  const { data } = await getUserById(id);
  Object.assign(userForm, data);
};

const handleSubmit = async () => {
  if (!userFormRef.value) return;
  await userFormRef.value.validate();
  if (isEdit) {
    await updateUser(userForm.id!, userForm);
    ElMessage.success('用户更新成功');
  } else {
    await createUser(userForm);
    ElMessage.success('用户创建成功');
  }
  router.push('/user/list');
};

const handleCancel = () => {
  router.push('/user/list');
};

onMounted(() => {
  if (isEdit) {
    fetchUser(Number(route.params.id));
  }
});
</script> 