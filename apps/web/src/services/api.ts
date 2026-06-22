import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios';
import { useAuthStore } from '../stores/authStore';
import { supabase } from './supabase';

// 创建axios实例
const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:3000/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
apiClient.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    // 从 Supabase 获取 session token
    const { data: { session } } = await supabase.auth.getSession();
    
    if (session?.access_token) {
      config.headers.Authorization = `Bearer ${session.access_token}`;
    }
    
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    return response.data;
  },
  async (error: AxiosError) => {
    const { logout } = useAuthStore.getState();
    
    // 401 未授权
    if (error.response?.status === 401) {
      // 登出并跳转到登录页
      logout();
      window.location.href = '/login';
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;

// API方法
export const authAPI = {
  login: (data: { account: string; password: string; schoolId?: string }): Promise<any> =>
    apiClient.post('/auth/login', data),
  
  logout: (): Promise<any> =>
    apiClient.post('/auth/logout'),
  
  getMe: (): Promise<any> =>
    apiClient.get('/auth/me'),
  
  refresh: (refreshToken: string): Promise<any> =>
    apiClient.post('/auth/refresh', { refreshToken }),
};

export const schoolAPI = {
  getAll: (): Promise<any> =>
    apiClient.get('/schools'),
};

export const courseAPI = {
  getAll: (): Promise<any> =>
    apiClient.get('/courses'),
  
  getById: (id: string): Promise<any> =>
    apiClient.get(`/courses/${id}`),
  
  enroll: (courseId: string): Promise<any> =>
    apiClient.post(`/courses/${courseId}/enroll`),
};

export const messageAPI = {
  getAll: (): Promise<any> =>
    apiClient.get('/messages'),
  
  markAsRead: (id: string): Promise<any> =>
    apiClient.put(`/messages/${id}/read`),
  
  getUnreadCount: (): Promise<any> =>
    apiClient.get('/messages/unread-count'),
};

export const aiAPI = {
  chat: (message: string, conversationId?: string): Promise<any> =>
    apiClient.post('/ai/chat', { message, conversationId }),
  
  getConversations: (): Promise<any> =>
    apiClient.get('/ai/conversations'),
  
  getConversationById: (id: string): Promise<any> =>
    apiClient.get(`/ai/conversations/${id}`),
};
