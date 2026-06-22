// 用户状态管理 - 使用 Supabase Auth
import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { supabase, getCurrentUser, signOut as supabaseSignOut } from '../services/supabase';

interface School {
  id: string;
  name: string;
  logo?: string;
}

interface User {
  id: string;
  email?: string;
  phone?: string;
  name: string;
  avatar?: string;
  role: string;
  schoolId?: string;
  userType: string;
}

interface AuthState {
  // 状态
  currentSchool: School | null;
  user: User | null;
  isAuthenticated: boolean;

  // actions
  setSchool: (school: School) => void;
  setUser: (user: User) => void;
  logout: () => Promise<void>;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      // 初始状态
      currentSchool: null,
      user: null,
      isAuthenticated: false,

      // 设置学校
      setSchool: (school) => set({ currentSchool: school }),

      // 设置用户信息（登录成功后调用）
      setUser: (user) =>
        set({
          user,
          isAuthenticated: true,
        }),

      // 登出 - 同时清除 Supabase Session
      logout: async () => {
        try {
          await supabaseSignOut();
        } catch (e) {
          console.error('Supabase 登出失败:', e);
        }
        set({
          user: null,
          isAuthenticated: false,
        });
      },
    }),
    {
      name: 'xiaoya-auth',
    }
  )
);
