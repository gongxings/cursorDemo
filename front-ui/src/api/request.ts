import axios, { AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';
import { useUserStore } from '@/stores/user';

// 定义通用响应格式
interface BaseResponse<T = any> {
  success: boolean;
  message: string;
  token?: string;
  data?: T;
  [key: string]: any;
}

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    
    // 如果是登录请求，不需要token
    if (config.url === '/auth/login') {
      return config;
    }

    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
      return config;
    } else {
      // 非登录请求且没有token时跳转到登录页
      router.replace('/login');
      return Promise.reject('No token found');
    }
  },
  error => {
    console.error('请求错误：', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse<BaseResponse>) => {
    const res = response.data;
    
    if (!res.success) {
      ElMessage.error(res.message || '请求失败');
      if (response.status === 401) {
        const userStore = useUserStore();
        userStore.resetState();
        localStorage.removeItem('token');
        router.replace('/login');
      }
      return Promise.reject(new Error(res.message || '请求失败'));
    }
    return Promise.resolve(res);
  },
  error => {
    console.error('响应错误：', error);
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录');
      const userStore = useUserStore();
      userStore.resetState();
      localStorage.removeItem('token');
      router.replace('/login');
    } else {
      const message = error.response?.data?.message || '请求失败';
      ElMessage.error(message);
    }
    return Promise.reject(error);
  }
);

export default service; 