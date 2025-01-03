import { defineStore } from 'pinia';
import { login, getUserInfo, logout } from '@/api/user';
import { ref } from 'vue';

export const useUserStore = defineStore('user', () => {
  const token = ref('');
  const userInfo = ref<any>(null);

  // 登录
  async function loginAction(loginForm: { username: string; password: string }) {
    try {
      const res = await login(loginForm);
      if (res.success && res.token) {
        token.value = res.token;
        return res;
      }
      throw new Error(res.message || '登录失败');
    } catch (error) {
      throw error;
    }
  }

  // 获取用户信息
  async function getUserInfoAction() {
    try {
      const res = await getUserInfo();
      if (res.success) {
        userInfo.value = res.data;
      }
      return res;
    } catch (error) {
      throw error;
    }
  }

  // 登出
  async function logoutAction() {
    try {
      await logout();
      resetState();
    } catch (error) {
      throw error;
    }
  }

  // 重置状态
  function resetState() {
    token.value = '';
    userInfo.value = null;
  }

  return {
    token,
    userInfo,
    loginAction,
    getUserInfoAction,
    logoutAction,
    resetState
  };
}, {
  persist: true // 启用持久化存储
}); 