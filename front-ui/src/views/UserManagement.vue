<template>
  <div class="user-management-container">
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="username" label="用户名" width="180"></el-table-column>
      <el-table-column prop="role" label="角色" width="180">
        <template #default="scope">
          <el-select v-model="scope.row.role" @change="updateUserRole(scope.row.id, scope.row.role)">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="普通用户" value="USER"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="重置密码" width="180">
        <template #default="scope">
          <el-button @click="resetUserPassword(scope.row.id)" type="warning" size="small">重置密码</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button @click="editUser(scope.row)" type="primary" size="small">编辑</el-button>
          <el-button @click="deleteUser(scope.row.id)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button @click="createUser" type="primary" style="margin-top: 20px;">新增用户</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { getUsers, createUser, updateUser, deleteUser, updateUserRoleApi, resetUserPasswordApi } from '@/api/user';

const users = ref([]);

const fetchUsers = async () => {
  try {
    users.value = await getUsers();
  } catch (error) {
    ElMessage.error('获取用户列表失败');
  }
};

const createUser = async () => {
  // Logic to create a new user
};

const editUser = (user) => {
  // Logic to edit a user
};

const deleteUser = async (id) => {
  try {
    await deleteUser(id);
    ElMessage.success('用户删除成功');
    fetchUsers();
  } catch (error) {
    ElMessage.error('删除用户失败');
  }
};

const updateUserRole = async (userId, role) => {
  try {
    await updateUserRoleApi(userId, role);
    ElMessage.success('用户角色更新成功');
  } catch (error) {
    ElMessage.error('更新用户角色失败');
  }
};

const resetUserPassword = async (userId) => {
  try {
    const newPassword = prompt('请输入新密码');
    if (newPassword) {
      await resetUserPasswordApi(userId, newPassword);
      ElMessage.success('用户密码重置成功');
    }
  } catch (error) {
    ElMessage.error('重置用户密码失败');
  }
};

onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
.user-management-container {
  padding: 20px;
}
</style> 