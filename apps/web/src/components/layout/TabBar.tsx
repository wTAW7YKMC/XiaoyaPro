import { useLocation, useNavigate } from 'react-router-dom';
import { Home, Bell, User } from 'lucide-react';
import { cn } from '../../utils/helpers';
import { useEffect, useState } from 'react';
import { messageAPI } from '../../services/api';

export default function TabBar() {
  const location = useLocation();
  const navigate = useNavigate();
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    loadUnreadCount();
  }, []);

  const loadUnreadCount = async () => {
    try {
      const response = await messageAPI.getUnreadCount();
      if (response.success) {
        setUnreadCount(response.data.total);
      }
    } catch (error) {
      console.error('加载未读消息数量失败:', error);
    }
  };

  const tabs = [
    {
      path: '/home',
      icon: Home,
      label: '首页',
    },
    {
      path: '/messages',
      icon: Bell,
      label: '消息',
      badge: unreadCount,
    },
    {
      path: '/profile',
      icon: User,
      label: '我的',
    },
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 safe-area-bottom">
      <div className="flex items-center justify-around h-14">
        {tabs.map((tab) => {
          const isActive = location.pathname === tab.path;
          const Icon = tab.icon;

          return (
            <button
              key={tab.path}
              onClick={() => navigate(tab.path)}
              className="flex flex-col items-center justify-center gap-0.5 flex-1 h-full relative"
            >
              <div className="relative">
                <Icon
                  className={cn(
                    'w-6 h-6 transition-colors',
                    isActive ? 'text-[#10B981]' : 'text-gray-400'
                  )}
                  fill={isActive ? 'currentColor' : 'none'}
                  strokeWidth={isActive ? 2.5 : 2}
                />
                {tab.badge && tab.badge > 0 && (
                  <span className="absolute -top-1 -right-1 min-w-[16px] h-4 px-1 bg-[#EF4444] text-white text-xs rounded-full flex items-center justify-center">
                    {tab.badge > 99 ? '99+' : tab.badge}
                  </span>
                )}
              </div>
              <span
                className={cn(
                  'text-xs transition-colors',
                  isActive ? 'text-[#10B981]' : 'text-gray-400'
                )}
              >
                {tab.label}
              </span>
            </button>
          );
        })}
      </div>
    </div>
  );
}
