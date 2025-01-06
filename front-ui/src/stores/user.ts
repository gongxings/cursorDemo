import { defineStore } from 'pinia';
import { logout, login, getUserInfo } from '@/api/user';
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
    },
    async loginAction(loginForm: { username: string; password: string }): Promise<{ success: boolean; message: string }> {
      try {
        const { data } = await login(loginForm);
        if (data.success) {
          this.token = data.token;
          localStorage.setItem('token', data.token);
          ElMessage.success('登录成功');
          return { success: true, message: '登录成功' };
        } else {
          ElMessage.error(data.message);
          return { success: false, message: data.message };
        }
      } catch (error) {
        console.error('登录失败:', error);
        ElMessage.error('登录失败');
        return { success: false, message: '登录失败' };
      }
    },
    async getUserInfoAction() {
      try {
        const { data } = await getUserInfo();
        this.userInfo = data;
      } catch (error) {
        console.error('获取用户信息失败:', error);
      }
    }
  }
}); 