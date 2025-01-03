import request from './request';

export interface LoginForm {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  token: string;
}

export function login(data: LoginForm) {
  return request<LoginResponse>({
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