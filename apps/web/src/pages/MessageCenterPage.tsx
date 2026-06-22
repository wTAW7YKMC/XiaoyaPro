import { useEffect, useState } from 'react';
import { messageAPI } from '../services/api';
import { ChevronRight, Calendar, MessageSquare, User, Settings, Users, Mail, Contact } from 'lucide-react';

interface MessageCategory {
  id: string;
  name: string;
  icon: any;
  color: string;
  unread?: number;
}

export default function MessageCenterPage() {
  const [categories, setCategories] = useState<MessageCategory[]>([]);

  useEffect(() => {
    loadMessageCategories();
  }, []);

  const loadMessageCategories = async () => {
    try {
      const response = await messageAPI.getUnreadCount();
      const unreadByCategory = response.data?.byCategory || {};

      const messageCategories = [
        {
          id: 'todo',
          name: '待办事项',
          icon: Calendar,
          color: 'bg-[#3B82F6]',
          unread: unreadByCategory['待办事项'] || 0,
        },
        {
          id: 'course',
          name: '课程消息',
          icon: MessageSquare,
          color: 'bg-[#10B981]',
          unread: unreadByCategory['课程消息'] || 0,
        },
        {
          id: 'personal',
          name: '个人消息',
          icon: User,
          color: 'bg-[#F59E0B]',
          unread: unreadByCategory['个人消息'] || 0,
        },
        {
          id: 'system',
          name: '系统消息',
          icon: Settings,
          color: 'bg-[#3B82F6]',
          unread: unreadByCategory['系统消息'] || 0,
        },
        {
          id: 'group',
          name: '群聊',
          icon: Users,
          color: 'bg-[#10B981]',
          unread: unreadByCategory['群聊'] || 0,
        },
        {
          id: 'private',
          name: '私信',
          icon: Mail,
          color: 'bg-[#F59E0B]',
          unread: unreadByCategory['私信'] || 0,
        },
        {
          id: 'contacts',
          name: '通讯录',
          icon: Contact,
          color: 'bg-[#06B6D4]',
          unread: 0,
        },
      ];

      setCategories(messageCategories);
    } catch (error) {
      console.error('加载消息分类失败:', error);
    }
  };

  return (
    <div className="min-h-screen bg-[#E8F5F0]">
      {/* 顶部标题栏 */}
      <div className="bg-white px-4 py-4 flex items-center justify-between shadow-sm">
        <h1 className="text-lg font-bold text-gray-900">消息中心</h1>
        <button className="text-gray-500">
          <span className="text-xl">⋯</span>
        </button>
      </div>

      {/* 消息分类列表 */}
      <div className="px-4 py-4 space-y-2">
        {categories.map((category) => {
          const Icon = category.icon;
          return (
            <button
              key={category.id}
              className="w-full bg-white rounded-xl shadow-card p-4 flex items-center gap-3 hover:shadow-float transition-all active:scale-[0.98]"
            >
              <div className={`w-10 h-10 rounded-lg ${category.color} flex items-center justify-center`}>
                <Icon className="w-5 h-5 text-white" />
              </div>
              <span className="flex-1 text-left text-base text-gray-900">
                {category.name}
              </span>
              {category.unread && category.unread > 0 && (
                <span className="w-2 h-2 rounded-full bg-[#EF4444]"></span>
              )}
              <ChevronRight className="w-5 h-5 text-gray-400" />
            </button>
          );
        })}
      </div>
    </div>
  );
}
