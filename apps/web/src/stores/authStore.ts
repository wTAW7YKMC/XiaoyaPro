import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface School {
  id: string;
  name: string;
  logo?: string;
}

interface User {
  id: string;
  phone: string;
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
  accessToken: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  
  //  actions
  setSchool: (school: School) => void;
  setUser: (user: User, accessToken: string, refreshToken: string) => void;
  logout: () => void;
  updateToken: (accessToken: string, refreshToken: string) => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      // 初始状态
      currentSchool: null,
      user: null,
      accessToken: null,
      refreshToken: null,
      isAuthenticated: false,
      
      // 设置学校
      setSchool: (school) => set({ currentSchool: school }),
      
      // 设置用户信息（登录成功）
      setUser: (user, accessToken, refreshToken) =>
        set({
          user,
          accessToken,
          refreshToken,
          isAuthenticated: true,
        }),
      
      // 登出
      logout: () =>
        set({
          user: null,
          accessToken: null,
          refreshToken: null,
          isAuthenticated: false,
        }),
      
      // 更新Token
      updateToken: (accessToken, refreshToken) =>
        set({ accessToken, refreshToken }),
    }),
    {
      name: 'xiaoya-auth', // localStorage key
    }
  )
);
