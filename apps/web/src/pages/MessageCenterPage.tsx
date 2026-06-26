import { useEffect, useState } from 'react';
import { messageAPI } from '../services/api';
import { ChevronRight, CalendarCheck, BookOpen, User, Settings, MessageCircle, Mail, Contact } from 'lucide-react';

interface MessageCategory {
  id: string;
  name: string;
  icon: any;
  bgColor: string;
  iconColor: string;
  hasUnread?: boolean;
}

// 静态分类数据，不依赖API即可渲染
const defaultCategories: MessageCategory[] = [
  {
    id: 'todo',
    name: '待办事项',
    icon: CalendarCheck,
    bgColor: '#5B8DEF',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
  {
    id: 'course',
    name: '课程消息',
    icon: BookOpen,
    bgColor: '#3DB88C',
    iconColor: '#FFFFFF',
    hasUnread: true,
  },
  {
    id: 'personal',
    name: '个人消息',
    icon: User,
    bgColor: '#E8734A',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
  {
    id: 'system',
    name: '系统消息',
    icon: Settings,
    bgColor: '#7BA7E0',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
  {
    id: 'group',
    name: '群聊',
    icon: MessageCircle,
    bgColor: '#8BC34A',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
  {
    id: 'private',
    name: '私信',
    icon: Mail,
    bgColor: '#D4A76A',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
  {
    id: 'contacts',
    name: '通讯录',
    icon: Contact,
    bgColor: '#26A69A',
    iconColor: '#FFFFFF',
    hasUnread: false,
  },
];

export default function MessageCenterPage() {
  const [categories, setCategories] = useState<MessageCategory[]>(defaultCategories);

  useEffect(() => {
    loadMessageCategories();
  }, []);

  const loadMessageCategories = async () => {
    try {
      const response = await messageAPI.getUnreadCount();
      const unreadByCategory = response.data?.byCategory || {};

      const messageCategories: MessageCategory[] = [
        {
          id: 'todo',
          name: '待办事项',
          icon: CalendarCheck,
          bgColor: '#5B8DEF',
          iconColor: '#FFFFFF',
          hasUnread: false,
        },
        {
          id: 'course',
          name: '课程消息',
          icon: BookOpen,
          bgColor: '#3DB88C',
          iconColor: '#FFFFFF',
          hasUnread: (unreadByCategory['课程消息'] || 0) > 0,
        },
        {
          id: 'personal',
          name: '个人消息',
          icon: User,
          bgColor: '#E8734A',
          iconColor: '#FFFFFF',
          hasUnread: (unreadByCategory['个人消息'] || 0) > 0,
        },
        {
          id: 'system',
          name: '系统消息',
          icon: Settings,
          bgColor: '#7BA7E0',
          iconColor: '#FFFFFF',
          hasUnread: (unreadByCategory['系统消息'] || 0) > 0,
        },
        {
          id: 'group',
          name: '群聊',
          icon: MessageCircle,
          bgColor: '#8BC34A',
          iconColor: '#FFFFFF',
          hasUnread: (unreadByCategory['群聊'] || 0) > 0,
        },
        {
          id: 'private',
          name: '私信',
          icon: Mail,
          bgColor: '#D4A76A',
          iconColor: '#FFFFFF',
          hasUnread: (unreadByCategory['私信'] || 0) > 0,
        },
        {
          id: 'contacts',
          name: '通讯录',
          icon: Contact,
          bgColor: '#26A69A',
          iconColor: '#FFFFFF',
          hasUnread: false,
        },
      ];

      setCategories(messageCategories);
    } catch (error) {
      // API失败时保持默认数据，不影响页面显示
      console.error('加载消息分类失败:', error);
    }
  };

  return (
    <div className="min-h-screen bg-[#D5F0E8] pb-16">
      {/* 顶部标题栏 */}
      <div className="px-4 pt-4 pb-3 flex items-center justify-center relative">
        <h1 className="text-[18px] font-bold text-gray-900">消息中心</h1>
        <button className="absolute right-4 text-gray-600 text-xl">
          <span>&#8943;</span>
        </button>
      </div>

      {/* 消息分类列表 */}
      <div className="px-4 space-y-3">
        {categories.map((category) => {
          const Icon = category.icon;
          return (
            <button
              key={category.id}
              className="w-full bg-white rounded-2xl p-4 flex items-center gap-4 active:opacity-90 transition-opacity"
              style={{ boxShadow: '0 1px 4px rgba(0,0,0,0.04)' }}
            >
              {/* 图标 */}
              <div
                className="w-12 h-12 rounded-xl flex items-center justify-center flex-shrink-0"
                style={{ backgroundColor: category.bgColor }}
              >
                <Icon className="w-6 h-6" style={{ color: category.iconColor }} />
              </div>

              {/* 名称 */}
              <span className="flex-1 text-left text-[16px] text-gray-800 font-medium">
                {category.name}
              </span>

              {/* 未读红点 */}
              {category.hasUnread && (
                <span className="w-2.5 h-2.5 rounded-full bg-[#EF4444] mr-1"></span>
              )}

              {/* 右箭头 */}
              <ChevronRight className="w-5 h-5 text-gray-300" />
            </button>
          );
        })}
      </div>
    </div>
  );
}
