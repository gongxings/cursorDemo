import request from './request';

export interface LoginData {
  username: string;
  password: string;
}

export interface UserInfo {
  id: number;
  username: string;
  role: string;
  email?: string;
  phone?: string;
}

export function login(data: LoginData) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  });
}

export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  });
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  });
} 