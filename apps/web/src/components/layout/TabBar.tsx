import { useLocation, useNavigate } from 'react-router-dom';
import { Home, Bell, User } from 'lucide-react';
import { cn } from '../../utils/helpers';
import { useEffect, useState } from 'react';
import { messageAPI } from '../../services/api';

export default function TabBar() {
  const location = useLocation();
  const navigate = useNavigate();
  const [hasUnread, setHasUnread] = useState(false);

  useEffect(() => {
    loadUnreadStatus();
  }, []);

  const loadUnreadStatus = async () => {
    try {
      const response = await messageAPI.getUnreadCount();
      if (response.success) {
        setHasUnread((response.data.total || 0) > 0);
      }
    } catch (error) {
      console.error('加载未读消息状态失败:', error);
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
      showDot: hasUnread,
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
                {'showDot' in tab && tab.showDot && (
                  <span className="absolute -top-0.5 -right-0.5 w-2.5 h-2.5 bg-[#EF4444] rounded-full border border-white"></span>
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
