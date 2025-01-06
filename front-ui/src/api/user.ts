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

export interface UserData {
  id: number;
  username: string;
  role: string;
}

export function getUserList() {
  return request<UserData[]>({
    url: '/api/users',
    method: 'get'
  });
}

export function getUserById(userId: number) {
  return request<UserData>({
    url: `/api/users/${userId}`,
    method: 'get'
  });
}

export function createUser(data: UserData) {
  return request({
    url: '/api/users',
    method: 'post',
    data
  });
}

export function updateUser(userId: number, data: UserData) {
  return request({
    url: `/api/users/${userId}`,
    method: 'put',
    data
  });
}

export function deleteUser(userId: number) {
  return request({
    url: `/api/users/${userId}`,
    method: 'delete'
  });
}

export function resetPassword(userId: number) {
  return request({
    url: `/api/users/${userId}/reset-password`,
    method: 'post'
  });
} 