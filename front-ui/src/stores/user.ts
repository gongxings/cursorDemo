import { defineStore } from 'pinia';
import { login, getUserInfo, logout } from '@/api/user';
import type { LoginData, UserInfo } from '@/api/user';

interface UserState {
  token: string;
  userInfo: UserInfo | null;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('token') || '',
    userInfo: null
  }),
  
  getters: {
    username: state => state.userInfo?.username,
    role: state => state.userInfo?.role
  },
  
  actions: {
    async login(loginData: LoginData) {
      try {
        const { data } = await login(loginData);
        this.token = data.token;
        localStorage.setItem('token', data.token);
        return data;
      } catch (error) {
        throw error;
      }
    },
    
    async getInfo() {
      try {
        const { data } = await getUserInfo();
        this.userInfo = data;
        return data;
      } catch (error) {
        throw error;
      }
    },
    
    async logout() {
      try {
        await logout();
        this.token = '';
        this.userInfo = null;
        localStorage.removeItem('token');
      } catch (error) {
        throw error;
      }
    }
  }
}); 