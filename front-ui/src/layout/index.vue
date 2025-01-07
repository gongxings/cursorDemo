<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container">
      <div class="logo">测试管理系统</div>
      <el-menu
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        :router="true"
      >
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" base-path="/" />
      </el-menu>
    </div>

    <!-- 主要内容区 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <div class="navbar">
        <el-breadcrumb separator="/" class="breadcrumb-left">
          <el-breadcrumb-item v-for="(breadcrumb, index) in breadcrumbs" :key="index">
            {{ breadcrumb }}
          </el-breadcrumb-item>
        </el-breadcrumb>
        <div class="right-menu">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              <el-icon><user /></el-icon>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <!-- 内容区 -->
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { useUserStore } from '@/stores/user';
import SidebarItem from './components/SidebarItem.vue';
import { ArrowDown, User } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const routes = computed(() => {
  return router.options.routes.filter(route => !route.meta?.hidden);
});

const activeMenu = computed(() => {
  return route.path;
});

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logoutAction();
  });
};

const handleProfile = () => {
  router.push('/profile');
};

const breadcrumbs = computed(() => {
  return route.matched.map(r => r.meta.title || r.name);
});
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
}

.sidebar-container {
  width: 210px;
  height: 100%;
  background: #304156;
  position: fixed;
  left: 0;
  z-index: 1001;

  .logo {
    height: 50px;
    line-height: 50px;
    text-align: center;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
  }
}

.main-container {
  min-height: 100%;
  margin-left: 210px;
  position: relative;
  flex: 1;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;

  .right-menu {
    .el-dropdown-link {
      cursor: pointer;
      color: #666;
      
      &:hover {
        color: #409EFF;
      }
    }
  }
}

.app-main {
  padding: 20px;
  min-height: calc(100vh - 50px);
  background: #f0f2f5;
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin: 20px 0;
  text-align: center;
}

.user-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 8px;
}

.breadcrumb-left {
  flex: 1;
  display: flex;
  align-items: center;
}
</style> 