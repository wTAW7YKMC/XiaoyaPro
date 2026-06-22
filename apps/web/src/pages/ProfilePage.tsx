import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { ChevronRight, BarChart3, FileText, MessageSquare, AlertTriangle, Info } from 'lucide-react';

export default function ProfilePage() {
  const navigate = useNavigate();
  const { user, currentSchool, logout } = useAuthStore();

  const handleLogout = () => {
    if (confirm('确定要退出登录吗？')) {
      logout();
      navigate('/login');
    }
  };

  const menuItems = [
    {
      icon: BarChart3,
      label: '个人数据看板',
      color: 'bg-[#3B82F6]',
      path: '/dashboard',
    },
    {
      icon: FileText,
      label: '我的报告',
      color: 'bg-[#F59E0B]',
      path: '/reports',
    },
    {
      icon: MessageSquare,
      label: '反馈&建议',
      color: 'bg-[#3B82F6]',
      path: '/feedback',
    },
    {
      icon: AlertTriangle,
      label: '投诉举报',
      color: 'bg-[#F59E0B]',
      path: '/complaint',
    },
    {
      icon: Info,
      label: '关于小雅',
      color: 'bg-[#3B82F6]',
      path: '/about',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#E0F7FA] to-[#E8F5F0]">
      {/* 顶部背景 */}
      <div className="h-32 bg-gradient-to-b from-[#B2EBF2] to-transparent"></div>

      {/* 用户信息卡片 */}
      <div className="px-4 -mt-20">
        <div className="bg-white rounded-2xl shadow-card p-5 mb-6">
          <div className="flex items-center gap-3">
            <div className="relative">
              <div className="w-14 h-14 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-2xl">
                👨‍🎓
              </div>
              {/* 普通用户标签 */}
              <div className="absolute -bottom-1 -right-1 bg-[#FEF3C7] text-[#F59E0B] text-xs px-2 py-0.5 rounded-full font-medium">
                普通用户
              </div>
            </div>
            <div className="flex-1">
              <h3 className="text-lg font-bold text-gray-900">{user?.name || '喻贝贝'}</h3>
              <p className="text-sm text-gray-500">{currentSchool?.name || '武汉理工大学'}</p>
            </div>
            <ChevronRight className="w-5 h-5 text-gray-400" />
          </div>
        </div>

        {/* 功能列表 */}
        <div className="space-y-2 mb-6">
          {menuItems.map((item, index) => {
            const Icon = item.icon;
            return (
              <button
                key={index}
                onClick={() => navigate(item.path)}
                className="w-full bg-white rounded-xl shadow-card p-4 flex items-center gap-3 hover:shadow-float transition-all active:scale-[0.98]"
              >
                <div className={`w-10 h-10 rounded-lg ${item.color} flex items-center justify-center`}>
                  <Icon className="w-5 h-5 text-white" />
                </div>
                <span className="flex-1 text-left text-base text-gray-900">
                  {item.label}
                </span>
                <ChevronRight className="w-5 h-5 text-gray-400" />
              </button>
            );
          })}
        </div>

        {/* 退出登录按钮 */}
        <button
          onClick={handleLogout}
          className="w-[90%] mx-auto py-4 bg-[#10B981] text-white font-bold rounded-lg shadow-lg hover:shadow-xl active:scale-[0.98] transition-all"
        >
          退出登录
        </button>
      </div>
    </div>
  );
}
