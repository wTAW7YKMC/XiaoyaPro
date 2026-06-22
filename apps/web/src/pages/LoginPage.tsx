import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { authAPI } from '../services/api';
import { Building2, User, Lock, Eye, EyeOff, ChevronLeft, Shield, Zap } from 'lucide-react';

// 开发模式：测试账号数据（用于无数据库时快速开发）
const DEV_TEST_USERS = [
  {
    id: 'dev-student-001',
    phone: '13800138000',
    name: '喻贝贝',
    role: 'STUDENT',
    userType: '普通用户',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=student1',
    schoolId: 'dev-school-001',
    school: { id: 'dev-school-001', name: '武汉理工大学', logo: '' },
  },
  {
    id: 'dev-teacher-001',
    phone: '13900139000',
    name: '张老师',
    role: 'TEACHER',
    userType: '普通用户',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher1',
    schoolId: 'dev-school-001',
    school: { id: 'dev-school-001', name: '武汉理工大学', logo: '' },
  },
];

export default function LoginPage() {
  const navigate = useNavigate();
  const { currentSchool, setUser, setSchool } = useAuthStore();

  const [account, setAccount] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [agreed, setAgreed] = useState(true);
  const [loading, setLoading] = useState(false);

  // 开发模式快速登录（绕过后端API）
  const handleDevLogin = (userIndex: number) => {
    const user = DEV_TEST_USERS[userIndex];
    
    // 设置学校（如果还没选择）
    if (!currentSchool && user.school) {
      setSchool(user.school);
    }
    
    // 模拟 token
    const accessToken = 'dev-access-token-' + Date.now();
    const refreshToken = 'dev-refresh-token-' + Date.now();
    
    // 直接设置用户状态（跳过API调用）
    setUser(user, accessToken, refreshToken);
    navigate('/home');
  };

  const handleLogin = async () => {
    if (!account || !password) {
      alert('请输入账号和密码');
      return;
    }

    if (!agreed) {
      alert('请先同意用户协议和隐私政策');
      return;
    }

    setLoading(true);

    try {
      // 调用登录API
      const response = await authAPI.login({
        account,
        password,
        schoolId: currentSchool?.id,
      });

      if (response.success) {
        const { user, accessToken, refreshToken } = response.data;
        setUser(user, accessToken, refreshToken);
        navigate('/home');
      }
    } catch (error: any) {
      console.error('登录失败:', error);
      alert(error.response?.data?.error || '登录失败，请重试');
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
          {/* 账号输入框 */}
          <div className="relative">
            <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="请输入手机号/账号"
              value={account}
              onChange={(e) => setAccount(e.target.value)}
              className="w-full pl-12 pr-4 py-4 bg-white border border-gray-200 rounded-xl text-base focus:outline-none focus:border-[#10B981] transition-colors"
            />
          </div>

          {/* 密码输入框 */}
          <div className="relative">
            <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type={showPassword ? 'text' : 'password'}
              placeholder="请输入密码"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full pl-12 pr-12 py-4 bg-white border border-gray-200 rounded-xl text-base focus:outline-none focus:border-[#10B981] transition-colors"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-4 top-1/2 -translate-y-1/2"
            >
              {showPassword ? (
                <EyeOff className="w-5 h-5 text-gray-400" />
              ) : (
                <Eye className="w-5 h-5 text-gray-400" />
              )}
            </button>
          </div>
        </div>

        {/* 登录按钮 */}
        <button
          onClick={handleLogin}
          disabled={loading}
          className="w-full py-4 bg-gradient-to-r from-[#10B981] to-[#059669] text-white font-bold rounded-lg shadow-lg hover:shadow-xl active:scale-[0.98] transition-all disabled:opacity-50 disabled:cursor-not-allowed mb-3"
        >
          {loading ? '登录中...' : '登录'}
        </button>

        {/* 开发模式快速入口 */}
        <div className="mb-4 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
          <div className="flex items-center gap-2 mb-2">
            <Zap className="w-4 h-4 text-yellow-600" />
            <span className="text-xs font-semibold text-yellow-700">快速登录（无需密码）</span>
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => handleDevLogin(0)}
              className="flex-1 py-2 bg-blue-500 text-white text-xs rounded-md hover:bg-blue-600 transition-colors"
            >
              学生账号
            </button>
            <button
              onClick={() => handleDevLogin(1)}
              className="flex-1 py-2 bg-purple-500 text-white text-xs rounded-md hover:bg-purple-600 transition-colors"
            >
              教师账号
            </button>
          </div>
        </div>

        {/* 忘记密码 */}
        <div className="text-right mb-8">
          <button className="text-[#10B981] text-sm">忘记密码 &gt;</button>
        </div>

        {/* 分隔线 */}
        <div className="flex items-center gap-3 mb-6">
          <div className="flex-1 h-px bg-gray-300"></div>
          <span className="text-xs text-gray-500">其他登录方式</span>
          <div className="flex-1 h-px bg-gray-300"></div>
        </div>

        {/* 第三方登录 */}
        <div className="flex justify-center mb-8">
          <button className="flex flex-col items-center gap-2">
            <div className="w-12 h-12 rounded-full bg-[#10B981] flex items-center justify-center">
              <Shield className="w-6 h-6 text-white" />
            </div>
            <span className="text-xs text-gray-600">统一身份认证</span>
          </button>
        </div>

        {/* 协议 */}
        <div className="flex items-start gap-2 text-xs text-gray-500">
          <button
            onClick={() => setAgreed(!agreed)}
            className={`flex-shrink-0 w-4 h-4 rounded-full border-2 flex items-center justify-center mt-0.5 ${
              agreed ? 'bg-[#10B981] border-[#10B981]' : 'border-gray-300'
            }`}
          >
            {agreed && <span className="text-white text-xs">✓</span>}
          </button>
          <span>
            登录即表示同意平台
            <button className="text-[#10B981]">《用户服务协议》</button>
            和
            <button className="text-[#10B981]">《隐私政策》</button>
          </span>
        </div>
      </div>
    </div>
  );
}
