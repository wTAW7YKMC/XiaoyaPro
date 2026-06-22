import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { courseAPI } from '../services/api';
import { ChevronRight, BookOpen, FileText, Bell, Compass } from 'lucide-react';

interface Course {
  id: string;
  title: string;
  progress: number;
}

export default function HomePage() {
  const navigate = useNavigate();
  const { user, currentSchool } = useAuthStore();
  const [courses, setCourses] = useState<Course[]>([]);
  const [taskCount, setTaskCount] = useState(2);

  useEffect(() => {
    loadCourses();
  }, []);

  const loadCourses = async () => {
    try {
      const response = await courseAPI.getAll();
      if (response.success) {
        setCourses(response.data);
      }
    } catch (error) {
      console.error('加载课程失败:', error);
      // 使用模拟数据
      setCourses([
        { id: '1', title: '高等数学', progress: 65 },
        { id: '2', title: '大学英语', progress: 40 },
        { id: '3', title: '计算机程序设计', progress: 80 },
      ]);
    }
  };

  const functionEntries = [
    {
      icon: BookOpen,
      label: '我的课程',
      gradient: 'from-blue-400 to-blue-600',
      path: '/courses',
    },
    {
      icon: FileText,
      label: '我的文档',
      gradient: 'from-green-400 to-green-600',
      path: '/documents',
    },
    {
      icon: Bell,
      label: '任务提醒',
      gradient: 'from-purple-400 to-purple-600',
      path: '/tasks',
    },
    {
      icon: Compass,
      label: '发现',
      gradient: 'from-orange-400 to-orange-600',
      path: '/discover',
      badge: true,
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#E0F7FA] to-[#E8F5F0]">
      {/* 顶部区域 */}
      <div className="px-4 pt-4 pb-6">
        {/* 右上角个人空间 */}
        <div className="flex justify-end mb-4">
          <button className="flex items-center gap-1 text-[#10B981] text-sm">
            <span>个人空间</span>
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>

        {/* 欢迎区域 */}
        <div className="flex items-center gap-3 mb-6">
          <div className="text-4xl">📚</div>
          <h2 className="text-xl font-bold text-[#10B981]">HI 很高兴见到你！</h2>
        </div>
      </div>

      {/* 用户信息卡片 */}
      <div className="px-4 -mt-2">
        <div className="bg-white rounded-2xl shadow-card p-5 mb-6">
          {/* 用户信息 */}
          <div className="flex items-center gap-3 mb-4">
            <div className="relative">
              <div className="w-14 h-14 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-2xl">
                👨‍🎓
              </div>
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2">
                <h3 className="text-lg font-bold text-gray-900">{user?.name || '喻贝贝'}</h3>
                <ChevronRight className="w-5 h-5 text-gray-400" />
              </div>
              <p className="text-sm text-gray-500">{currentSchool?.name || '武汉理工大学'}</p>
            </div>
          </div>

          {/* 统计数据 */}
          <div className="flex items-center justify-around pt-4 border-t border-gray-100">
            <div className="text-center">
              <p className="text-xs text-gray-500 mb-1">学习课程</p>
              <p className="text-2xl font-bold text-gray-900">{courses.length || 61}</p>
            </div>
            <div className="w-px h-10 bg-gray-200"></div>
            <div className="text-center">
              <p className="text-xs text-gray-500 mb-1">待完成任务</p>
              <p className="text-2xl font-bold text-gray-900">{taskCount}</p>
            </div>
          </div>
        </div>

        {/* 功能入口网格 */}
        <div className="grid grid-cols-2 gap-3 mb-6">
          {functionEntries.map((entry, index) => {
            const Icon = entry.icon;
            return (
              <button
                key={index}
                onClick={() => navigate(entry.path)}
                className="bg-white rounded-2xl shadow-card p-5 flex flex-col items-center gap-3 hover:shadow-float transition-all active:scale-95 relative"
              >
                <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${entry.gradient} flex items-center justify-center`}>
                  <Icon className="w-6 h-6 text-white" />
                </div>
                <span className="text-sm text-gray-700">{entry.label}</span>
                
                {/* AI角标 */}
                {entry.badge && (
                  <div className="absolute bottom-2 right-2 w-8 h-8 rounded-full bg-gradient-to-br from-blue-400 to-purple-500 flex items-center justify-center text-xs">
                    🤖
                  </div>
                )}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
