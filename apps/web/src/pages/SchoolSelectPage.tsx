import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { schoolAPI } from '../services/api';
import { Building2, ChevronRight } from 'lucide-react';

interface School {
  id: string;
  name: string;
  logo?: string;
}

export default function SchoolSelectPage() {
  const navigate = useNavigate();
  const { setSchool } = useAuthStore();
  const [schools, setSchools] = useState<School[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadSchools();
  }, []);

  const loadSchools = async () => {
    try {
      // 模拟数据（实际应该调用API）
      const mockSchools = [
        { id: '1', name: '华中师范大学', logo: '🏛️' },
        { id: '2', name: '武汉理工大学', logo: '🏫' },
        { id: '3', name: '南开大学', logo: '🎓' },
        { id: '4', name: '广东第二师范学院', logo: '📚' },
        { id: '5', name: '宁夏师范大学', logo: '🏛️' },
        { id: '6', name: '喀什大学', logo: '🏫' },
      ];
      
      // 实际API调用
      // const response = await schoolAPI.getAll();
      // setSchools(response.data);
      
      setSchools(mockSchools);
    } catch (error) {
      console.error('加载学校列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectSchool = (school: School) => {
    setSchool(school);
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#E0F7FA] to-[#E8F5F0]">
      {/* 顶部背景装饰 */}
      <div className="relative h-48 bg-gradient-to-b from-[#B2EBF2] to-transparent overflow-hidden">
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="text-6xl opacity-20">🏛️ 📚</div>
        </div>
      </div>

      {/* 主内容区 */}
      <div className="px-4 -mt-16">
        {/* 标题 */}
        <h1 className="text-center text-xl font-bold text-gray-800 mb-6">
          请选择学校
        </h1>

        {/* 学校列表网格 */}
        {loading ? (
          <div className="text-center text-gray-500 py-12">加载中...</div>
        ) : (
          <div className="grid grid-cols-2 gap-4 mb-8">
            {schools.map((school) => (
              <button
                key={school.id}
                onClick={() => handleSelectSchool(school)}
                className="flex flex-col items-center p-4 bg-white rounded-2xl shadow-card hover:shadow-float transition-all active:scale-95"
              >
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-100 to-green-100 flex items-center justify-center text-3xl mb-2">
                  {school.logo || '🏛️'}
                </div>
                <span className="text-sm text-gray-700 text-center leading-tight">
                  {school.name}
                </span>
              </button>
            ))}
          </div>
        )}

        {/* 底部链接 */}
        <div className="space-y-3 text-center pb-8">
          <button className="text-[#10B981] text-sm flex items-center justify-center gap-1 mx-auto">
            查看更多开课院校
            <ChevronRight className="w-4 h-4" />
          </button>
          <button className="text-[#10B981] text-sm flex items-center justify-center gap-1 mx-auto">
            其他用户登录
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
}
