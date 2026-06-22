import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { signIn, signUp } from '../services/supabase';
import { Building2, User, Lock, Eye, EyeOff, ChevronLeft, Shield, Zap, Mail } from 'lucide-react';

export default function LoginPage() {
  const navigate = useNavigate();
  const { currentSchool, setUser, setSchool } = useAuthStore();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isRegister, setIsRegister] = useState(false); // 切换登录/注册
  const [loading, setLoading] = useState(false);

  // 处理登录/注册
  const handleSubmit = async () => {
    if (!email || !password) {
      alert('请输入邮箱和密码');
      return;
    }

    if (isRegister && !name.trim()) {
      alert('请输入姓名');
      return;
    }

    if (password.length < 6) {
      alert('密码至少6位');
      return;
    }

    setLoading(true);

    try {
        if (isRegister) {
          // 注册
          const data = await signUp(email, password, name.trim());

          if (data.user) {
            // 注册成功后直接构建用户对象并登录（Supabase 默认已创建 session）
            const user = {
              id: data.user.id,
              email: data.user.email,
              name: data.user.user_metadata?.name || name.trim(),
              avatar: data.user.user_metadata?.avatar,
              role: data.user.user_metadata?.role || 'STUDENT',
              userType: '普通用户',
            };

            if (!currentSchool) {
              setSchool({ id: 'default', name: '武汉理工大学' });
            }

            setUser(user);
            navigate('/home');
            return;
          }
        } else {
        // 登录 - 直连 Supabase 云端
        const data = await signIn(email, password);

        if (data.user) {
          // 构建用户对象
          const user = {
            id: data.user.id,
            email: data.user.email,
            name: data.user.user_metadata?.name || email.split('@')[0],
            avatar: data.user.user_metadata?.avatar,
            role: data.user.user_metadata?.role || 'STUDENT',
            userType: '普通用户',
          };

          // 设置默认学校（如果还没有）
          if (!currentSchool) {
            setSchool({ id: 'default', name: '武汉理工大学' });
          }

          setUser(user);
          navigate('/home');
        }
      }
    } catch (error: any) {
      console.error(isRegister ? '注册失败:' : '登录失败:', error);
      alert(error?.message || (isRegister ? '注册失败' : '登录失败，请检查账号密码'));
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    navigate('/school-select');
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#E0F7FA] to-[#E8F5F0]">
      {/* 顶部导航 */}
      <div className="flex items-center px-4 py-4">
        <button onClick={handleBack} className="flex items-center text-[#10B981]">
          <ChevronLeft className="w-5 h-5" />
          <Building2 className="w-5 h-5 ml-1" />
          <span className="ml-1 text-sm">选择学校</span>
        </button>
      </div>

      {/* 主内容区 */}
      <div className="px-6 pt-8">
        {/* Logo区域 */}
        <div className="flex flex-col items-center mb-8">
          <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-blue-400 to-blue-600 flex items-center justify-center mb-3">
            <Building2 className="w-12 h-12 text-white" />
          </div>
          <h1 className="text-2xl font-bold text-blue-900">理工智课</h1>
          <p className="text-sm text-gray-500 mt-1">
            {isRegister ? '创建新账号' : '欢迎回来'}
          </p>
        </div>

        {/* 当前学校 */}
        {currentSchool && (
          <div className="flex items-center justify-center gap-2 bg-gray-100 rounded-full px-4 py-2 mb-6 mx-auto w-fit">
            <Building2 className="w-4 h-4 text-gray-600" />
            <span className="text-sm text-gray-700">{currentSchool.name}</span>
          </div>
        )}

        {/* 输入框 */}
        <div className="space-y-4 mb-6">
          {/* 姓名（仅注册时显示） */}
          {isRegister && (
            <div className="relative">
              <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="请输入姓名"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full pl-12 pr-4 py-4 bg-white border border-gray-200 rounded-xl text-base focus:outline-none focus:border-[#10B981]"
              />
            </div>
          )}

          {/* 邮箱输入框 */}
          <div className="relative">
            <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="email"
              placeholder="请输入邮箱"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full pl-12 pr-4 py-4 bg-white border border-gray-200 rounded-xl text-base focus:outline-none focus:border-[#10B981]"
            />
          </div>

          {/* 密码输入框 */}
          <div className="relative">
            <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="请输入密码（至少6位）"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full pl-12 pr-12 py-4 bg-white border border-gray-200 rounded-xl text-base focus:outline-none focus:border-[#10B981]"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-4 top-1/2 -translate-y-1/2"
            >
              {showPassword ? <EyeOff className="w-5 h-5 text-gray-400" /> : <Eye className="w-5 h-5 text-gray-400" />}
            </button>
          </div>
        </div>

        {/* 登录/注册按钮 */}
        <button
          onClick={handleSubmit}
          disabled={loading}
          className="w-full py-4 bg-gradient-to-r from-[#10B981] to-[#059669] text-white font-bold rounded-lg shadow-lg hover:shadow-xl active:scale-[0.98] transition-all disabled:opacity-50 mb-3"
        >
          {loading ? '处理中...' : (isRegister ? '注册' : '登录')}
        </button>

        {/* 切换登录/注册 */}
        <button
          onClick={() => setIsRegister(!isRegister)}
          className="w-full py-3 text-[#10B981] text-sm font-medium"
        >
          {isRegister ? '已有账号？去登录' : '没有账号？立即注册'}
        </button>

        {/* 分隔线 */}
        <div className="flex items-center gap-3 my-6">
          <div className="flex-1 h-px bg-gray-300"></div>
          <span className="text-xs text-gray-500">云端认证</span>
          <div className="flex-1 h-px bg-gray-300"></div>
        </div>

        {/* 云端标识 */}
        <div className="flex justify-center">
          <div className="flex items-center gap-2 px-4 py-2 bg-green-50 rounded-full">
            <Shield className="w-4 h-4 text-green-600" />
            <span className="text-xs text-green-700 font-medium">Supabase 云端安全认证</span>
          </div>
        </div>
      </div>
    </div>
  );
}
