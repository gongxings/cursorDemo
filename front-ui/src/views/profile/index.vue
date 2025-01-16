<template>
  <div class="profile">
    <el-card>
      <h2>个人中心</h2>
      <el-form :model="userInfo" label-width="100px" disabled>
        <el-form-item label="用户名">
          <el-input v-model="userInfo.username" />
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="userInfo.role" />
        </el-form-item>
        <!-- 其他用户信息字段 -->
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
const userInfo = computed(() => userStore.userInfo);
</script>

<style scoped>
.profile {
  padding: 20px;
}
</style> 