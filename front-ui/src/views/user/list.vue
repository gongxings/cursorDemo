<template>
  <div class="user-list">
    <el-card class="operation-bar">
      <el-button type="primary" @click="handleAdd">添加用户</el-button>
    </el-card>

    <el-card>
      <el-table :data="userList" style="width: 100%">
        <el-table-column prop="username" label="用户名" width="180"></el-table-column>
        <el-table-column prop="role" label="角色" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="text" size="small" @click="handleResetPassword(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { getUserList, deleteUser, resetPassword } from '@/api/user';
import type { UserData } from '@/api/user';

const router = useRouter();
const userList = ref<UserData[]>([]);

const fetchUserList = async () => {
  const { data } = await getUserList();
  userList.value = data;
};

const handleAdd = () => {
  router.push('/user/add');
};

const handleEdit = (user: UserData) => {
  router.push(`/user/edit/${user.id}`);
};

const handleDelete = (user: UserData) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteUser(user.id!);
    ElMessage.success('删除成功');
    fetchUserList();
  }).catch(() => {});
};

const handleResetPassword = (user: UserData) => {
  ElMessageBox.confirm('确定要重置该用户的密码吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await resetPassword(user.id!);
    ElMessage.success('密码已重置为初始密码');
  }).catch(() => {});
};

onMounted(() => {
  fetchUserList();
});
</script>

<style scoped>
.operation-bar {
  margin-bottom: 20px;
}
</style> 