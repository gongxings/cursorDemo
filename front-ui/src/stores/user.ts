import { defineStore } from 'pinia';
import { logout } from '@/api/user';
import { ElMessage } from 'element-plus';
import router from '@/router';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  actions: {
    async logoutAction() {
      try {
        await logout();
        this.resetState();
        router.push('/login');
        ElMessage.success('退出登录成功');
      } catch (error) {
        console.error('退出登录失败:', error);
        ElMessage.error('退出登录失败');
      }
    },
    resetState() {
      this.token = '';
      this.userInfo = null;
      localStorage.removeItem('token');
    }
  }
}); 