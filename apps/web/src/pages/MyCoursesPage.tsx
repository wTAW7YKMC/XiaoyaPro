/**
 * 我的课程页面 - 完全复刻截图设计
 * 
 * 功能特性：
 * ✅ 三个Tab切换（我学的课/收藏的课/访问的课）
 * ✅ 状态筛选（正在进行/即将开课/已结课）
 * ✅ 学校筛选下拉
 * ✅ 课程卡片列表（封面、课程名、学院、教师、访问量）
 * ✅ 从Supabase真实读取数据
 */

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  ArrowLeft, 
  Search, 
  Plus, 
  Filter, 
  ChevronDown,
  Users,
  Eye,
  BookOpen
} from 'lucide-react';
import { supabase } from '../services/supabase';
import { useAuthStore } from '../stores/authStore';

// ============================================
// 类型定义
// ============================================

interface Course {
  id: string;
  title: string;
  cover: string | null;
  status: 'ONGOING' | 'UPCOMING' | 'ENDED';
  semester: string;
  type: string;
  visit_count: number;
  teacher_id: string;
  school_id: string;
  teacher?: {
    id: string;
    name: string;
  };
  school?: {
    id: string;
    name: string;
  };
  progress?: number; // 选课进度
}

interface Enrollment {
  id: string;
  user_id: string;
  course_id: string;
  role: string;
  progress: number;
}

interface Favorite {
  id: string;
  user_id: string;
  course_id: string;
}

interface VisitRecord {
  id: string;
  user_id: string;
  course_id: string;
  visit_count: number;
  last_visited_at: string;
}

// Tab类型
type TabType = 'learning' | 'favorites' | 'visited';

// 状态筛选类型
type StatusFilter = 'all' | 'ONGOING' | 'UPCOMING' | 'ENDED';

export default function MyCoursesPage() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  
  // ========== 状态管理 ==========
  
  // 当前选中的Tab
  const [activeTab, setActiveTab] = useState<TabType>('learning');
  
  // 状态筛选
  const [statusFilter, setStatusFilter] = useState<StatusFilter>('all');
  
  // 学校筛选
  const [schoolFilter, setSchoolFilter] = useState<string>('all');
  const [schools, setSchools] = useState<Array<{id: string, name: string}>>([]);
  
  // 课程数据
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  
  // 统计数据
  const [stats, setStats] = useState({
    ongoing: 0,
    upcoming: 0,
    ended: 0
  });

  // ========== 数据加载 ==========
  
  useEffect(() => {
    if (user) {
      loadSchools();
      loadData();
    }
  }, [user, activeTab, statusFilter, schoolFilter]);

  /** 加载学校列表 */
  const loadSchools = async () => {
    try {
      const { data, error } = await supabase
        .from('schools')
        .select('id, name')
        .order('name');
      
      if (!error && data) {
        setSchools(data);
      }
    } catch (error) {
      console.error('加载学校列表失败:', error);
    }
  };

  /** 根据当前Tab加载数据 */
  const loadData = async () => {
    setLoading(true);
    
    try {
      let courseIds: string[] = [];
      
      switch (activeTab) {
        case 'learning':
          // 我学的课：从enrollments表获取
          await loadEnrolledCourses();
          break;
          
        case 'favorites':
          // 收藏的课：从user_course_favorites表获取
          await loadFavoriteCourses();
          break;
          
        case 'visited':
          // 访问的课：从course_visits表获取
          await loadVisitedCourses();
          break;
      }
    } catch (error) {
      console.error('加载课程数据失败:', error);
      setLoading(false);
    }
  };

  /** 加载我学的课程 */
  const loadEnrolledCourses = async () => {
    if (!user) return;
    
    // 获取选课记录
    const { data: enrollments, error: enrollError } = await supabase
      .from('enrollments')
      .select(`
        course_id,
        progress,
        courses (
          id,
          title,
          cover,
          status,
          semester,
          type,
          visit_count,
          teacher_id,
          school_id,
          teacher:users!courses_teacher_id_fkey (id, name),
          school:schools (id, name)
        )
      `)
      .eq('user_id', user.id);
    
    if (enrollError) throw enrollError;
    
    // 提取课程数据并添加progress字段
    let coursesData: Course[] = [];
    if (enrollments) {
      coursesData = enrollments.map((e: any) => ({
        ...e.courses,
        progress: e.progress
      }));
      
      // 应用状态筛选
      if (statusFilter !== 'all') {
        coursesData = coursesData.filter(c => c.status === statusFilter);
      }
      
      // 应用学校筛选
      if (schoolFilter !== 'all') {
        coursesData = coursesData.filter(c => c.school_id === schoolFilter);
      }
      
      // 计算统计数据
      const allCourses = enrollments.map((e: any) => e.courses);
      setStats({
        ongoing: allCourses.filter((c: Course) => c.status === 'ONGOING').length,
        upcoming: allCourses.filter((c: Course) => c.status === 'UPCOMING').length,
        ended: allCourses.filter((c: Course) => c.status === 'ENDED').length
      });
    }
    
    setCourses(coursesData);
    setLoading(false);
  };

  /** 加载收藏的课程 */
  const loadFavoriteCourses = async () => {
    if (!user) return;
    
    const { data: favorites, error: favError } = await supabase
      .from('user_course_favorites')
      .select(`
        course_id,
        courses (
          id,
          title,
          cover,
          status,
          semester,
          type,
          visit_count,
          teacher_id,
          school_id,
          teacher:users!courses_teacher_id_fkey (id, name),
          school:schools (id, name)
        )
      `)
      .eq('user_id', user.id);
    
    if (favError) throw favError;
    
    let coursesData: Course[] = [];
    if (favorites) {
      coursesData = favorites.map((f: any) => f.courses).filter(Boolean);
      
      // 应用筛选
      if (statusFilter !== 'all') {
        coursesData = coursesData.filter(c => c.status === statusFilter);
      }
      if (schoolFilter !== 'all') {
        coursesData = coursesData.filter(c => c.school_id === schoolFilter);
      }
    }
    
    setCourses(coursesData);
    setStats({ ongoing: 0, upcoming: 0, ended: 0 }); // 收藏不显示统计
    setLoading(false);
  };

  /** 加载访问过的课程 */
  const loadVisitedCourses = async () => {
    if (!user) return;
    
    const { data: visits, error: visitError } = await supabase
      .from('course_visits')
      .select(`
        course_id,
        visit_count,
        last_visited_at,
        courses (
          id,
          title,
          cover,
          status,
          semester,
          type,
          visit_count,
          teacher_id,
          school_id,
          teacher:users!courses_teacher_id_fkey (id, name),
          school:schools (id, name)
        )
      `)
      .eq('user_id', user.id)
      .order('last_visited_at', { ascending: false });
    
    if (visitError) throw visitError;
    
    let coursesData: Course[] = [];
    if (visits) {
      coursesData = visits.map((v: any) => v.courses).filter(Boolean);
      
      // 应用筛选
      if (statusFilter !== 'all') {
        coursesData = coursesData.filter(c => c.status === statusFilter);
      }
      if (schoolFilter !== 'all') {
        coursesData = coursesData.filter(c => c.school_id === schoolFilter);
      }
    }
    
    setCourses(coursesData);
    setStats({ ongoing: 0, upcoming: 0, ended: 0 }); // 访问不显示统计
    setLoading(false);
  };

  // ============================================
  // 渲染函数
  // ============================================

  /** 获取状态文本和颜色 */
  const getStatusInfo = (status: string) => {
    switch (status) {
      case 'ONGOING':
        return { text: '正在进行', color: 'text-green-600' };
      case 'UPCOMING':
        return { text: '即将开课', color: 'text-blue-600' };
      case 'ENDED':
        return { text: '已结课', color: 'text-gray-500' };
      default:
        return { text: '未知', color: 'text-gray-500' };
    }
  };

  /** 渲染课程卡片 */
  const renderCourseCard = (course: Course) => {
    const statusInfo = getStatusInfo(course.status);
    const teacherName = course.teacher?.name || '未知教师';
    const schoolName = course.school?.name || '未知学院';
    
    return (
      <div
        key={course.id}
        onClick={() => navigate(`/course/${course.id}`)}
        className="bg-white rounded-xl shadow-sm p-4 mb-3 active:scale-[0.98] transition-transform cursor-pointer"
      >
        <div className="flex gap-4">
          {/* 左侧封面图 */}
          <div className="relative w-32 h-24 flex-shrink-0 rounded-lg overflow-hidden bg-gradient-to-br from-gray-100 to-gray-200">
            {course.cover ? (
              <>
                <img 
                  src={course.cover} 
                  alt={course.title}
                  className="w-full h-full object-cover"
                />
                {/* 封面底部信息条 */}
                <div className="absolute bottom-0 left-0 right-0 bg-black/50 text-white text-xs px-2 py-1">
                  <div className="font-medium">{course.semester} | {course.type} | 教务开课</div>
                </div>
              </>
            ) : (
              <div className="w-full h-full flex items-center justify-center text-gray-400">
                <BookOpen className="w-8 h-8" />
              </div>
            )}
          </div>

          {/* 右侧信息 */}
          <div className="flex-1 flex flex-col justify-between min-w-0">
            {/* 课程标题 */}
            <h3 className="text-base font-bold text-gray-900 mb-1 truncate">
              {course.title}
            </h3>

            {/* 学院信息 */}
            <p className="text-sm text-gray-500 mb-1 truncate">
              学院: {schoolName}
            </p>

            {/* 教师姓名 */}
            <p className="text-sm text-gray-700 mb-2 truncate">
              {teacherName}
            </p>

            {/* 底部：访问量 */}
            <div className="flex items-center gap-1 text-sm text-gray-400">
              <Eye className="w-4 h-4" />
              <span>访问量:{schoolName.slice(0, 2)}...</span>
              <Users className="w-4 h-4 ml-2" />
              <span>{course.visit_count}人</span>
              
              {/* 进度条（仅我学的课显示） */}
              {activeTab === 'learning' && course.progress !== undefined && (
                <div className="ml-auto flex items-center gap-1">
                  <div className="w-16 h-1.5 bg-gray-200 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-green-500 rounded-full transition-all"
                      style={{ width: `${course.progress}%` }}
                    />
                  </div>
                  <span className="text-xs text-gray-500">{course.progress}%</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  };

  // ============================================
  // 主渲染
  // ============================================

  return (
    <div className="min-h-screen bg-gradient-to-b from-[#E0F7FA] to-[#F5F5F5]">
      {/* ========== 顶部导航栏 ========== */}
      <div className="bg-white sticky top-0 z-10 shadow-sm">
        <div className="flex items-center justify-between px-4 py-3">
          {/* 左侧返回按钮 */}
          <button 
            onClick={() => navigate(-1)}
            className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 active:bg-gray-200"
          >
            <ArrowLeft className="w-6 h-6 text-gray-700" />
          </button>

          {/* 中间标题 */}
          <h1 className="text-lg font-bold text-gray-900">课程</h1>

          {/* 右侧按钮组 */}
          <div className="flex items-center gap-2">
            {/* 搜索按钮 */}
            <button className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 active:bg-gray-200">
              <Search className="w-5 h-5 text-gray-700" />
            </button>
            
            {/* 添加按钮 */}
            <button className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 active:bg-gray-200">
              <Plus className="w-5 h-5 text-gray-700" />
            </button>
          </div>
        </div>

        {/* ========== Tab 切换栏 ========== */}
        <div className="flex border-b border-gray-100">
          {[
            { key: 'learning' as TabType, label: '我学的课' },
            { key: 'favorites' as TabType, label: '收藏的课' },
            { key: 'visited' as TabType, label: '访问的课' }
          ].map(tab => (
            <button
              key={tab.key}
              onClick={() => setActiveTab(tab.key)}
              className={`flex-1 py-3 text-sm font-medium relative transition-colors ${
                activeTab === tab.key 
                  ? 'text-[#10B981]' 
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              {tab.label}
              {/* 下划线指示器 */}
              {activeTab === tab.key && (
                <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-12 h-0.5 bg-[#10B981] rounded-full" />
              )}
            </button>
          ))}
        </div>
      </div>

      {/* ========== 状态筛选栏（仅我学的课显示） ========== */}
      {activeTab === 'learning' && (
        <div className="bg-white px-4 py-3 border-b border-gray-100">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <button
                onClick={() => setStatusFilter('all')}
                className={`text-sm font-medium transition-colors ${
                  statusFilter === 'all' ? 'text-[#10B981]' : 'text-gray-500'
                }`}
              >
                正在进行({stats.ongoing})
              </button>
              <button
                onClick={() => setStatusFilter('UPCOMING')}
                className={`text-sm font-medium transition-colors ${
                  statusFilter === 'UPCOMING' ? 'text-[#10B981]' : 'text-gray-500'
                }`}
              >
                即将开课({stats.upcoming})
              </button>
              <button
                onClick={() => setStatusFilter('ENDED')}
                className={`text-sm font-medium transition-colors ${
                  statusFilter === 'ENDED' ? 'text-[#10B981]' : 'text-gray-500'
                }`}
              >
                已结课({stats.ended})
              </button>
            </div>
            
            {/* 筛选图标 */}
            <button className="p-1">
              <Filter className="w-5 h-5 text-[#10B981]" />
            </button>
          </div>
        </div>
      )}

      {/* ========== 二级筛选栏 ========== */}
      <div className="bg-white px-4 py-2 border-b border-gray-100">
        <div className="flex items-center gap-2">
          {/* 所有课程按钮 */}
          <button
            onClick={() => setSchoolFilter('all')}
            className={`px-4 py-1.5 rounded-full text-sm font-medium border transition-all ${
              schoolFilter === 'all'
                ? 'border-[#10B981] text-[#10B981] bg-[#10B981]/5'
                : 'border-gray-300 text-gray-600 hover:border-gray-400'
            }`}
          >
            所有课程
          </button>

          {/* 学校下拉选择 */}
          <div className="relative">
            <select
              value={schoolFilter}
              onChange={(e) => setSchoolFilter(e.target.value)}
              className="appearance-none px-4 py-1.5 pr-8 rounded-full text-sm font-medium border border-gray-300 text-gray-600 bg-white hover:border-gray-400 focus:border-[#10B981] focus:outline-none cursor-pointer"
            >
              <option value="all">全部学校</option>
              {schools.map(school => (
                <option key={school.id} value={school.id}>
                  {school.name}
                </option>
              ))}
            </select>
            <ChevronDown className="absolute right-2 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
          </div>
        </div>
      </div>

      {/* ========== 课程列表区域 ========== */}
      <div className="px-4 py-4 pb-20">
        {loading ? (
          /* 加载状态 */
          <div className="flex flex-col items-center justify-center py-20">
            <div className="w-12 h-12 border-4 border-[#10B981]/20 border-t-[#10B981] rounded-full animate-spin mb-4" />
            <p className="text-gray-500 text-sm">加载中...</p>
          </div>
        ) : courses.length > 0 ? (
          /* 课程列表 */
          <div>
            {courses.map(renderCourseCard)}
          </div>
        ) : (
          /* 空状态 */
          <div className="flex flex-col items-center justify-center py-20 text-gray-400">
            <BookOpen className="w-16 h-16 mb-4 opacity-30" />
            <p className="text-lg font-medium mb-2">暂无课程</p>
            <p className="text-sm">
              {activeTab === 'learning' && '您还没有选修任何课程'}
              {activeTab === 'favorites' && '您还没有收藏任何课程'}
              {activeTab === 'visited' && '您还没有访问过任何课程'}
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
