// Supabase 客户端 - 前端直连云端数据库
import { createClient } from '@supabase/supabase-js';

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL;
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY;

if (!supabaseUrl || !supabaseAnonKey || supabaseAnonKey.includes('这里')) {
  console.error('❌ Supabase 配置缺失！请检查 .env 文件');
}

export const supabase = createClient(supabaseUrl, supabaseAnonKey);

// ========== Auth 认证方法 ==========

/** 邮箱密码注册 */
export const signUp = async (email: string, password: string, name: string) => {
  const { data, error } = await supabase.auth.signUp({
    email,
    password,
    options: {
      data: { name },
    },
  });
  if (error) throw error;
  return data;
};

/** 邮箱密码登录 */
export const signIn = async (email: string, password: string) => {
  const { data, error } = await supabase.auth.signInWithPassword({
    email,
    password,
  });
  if (error) throw error;
  return data;
};

/** 登出 */
export const signOut = async () => {
  const { error } = await supabase.auth.signOut();
  if (error) throw error;
};

/** 获取当前用户 */
export const getCurrentUser = async () => {
  const { data: { user } } = await supabase.auth.getUser();
  return user;
};

/** 获取当前 Session */
export const getSession = async () => {
  const { data: { session } } = await supabase.auth.getSession();
  return session;
};

/** 监听登录状态变化 */
export const onAuthStateChange = (callback: (event: string, session: any) => void) => {
  return supabase.auth.onAuthStateChange(callback);
};
