/**
 * 课程详情页面 - 完全复刻截图设计
 * 
 * 页面结构：
 * - 顶部导航栏：返回按钮、课程标题、网格视图图标、全屏图标
 * - 课程信息区域：课程名、教师列表、学期信息、查看记录链接
 * - 功能入口：作业任务、课程工具、分组、我的学情（4个图标）
 * - 课程内容列表：课程导学、教学安排、章节内容等
 */

import { useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, LayoutGrid, Maximize2, Folder } from 'lucide-react';

export default function CourseDetailPage() {
  const navigate = useNavigate();
  const { courseId } = useParams();

  // 课程内容章节数据（根据截图）
  const courseContent = [
    { id: 'intro', title: '课程导学', icon: Folder },
    { id: 'schedule', title: '教学安排2024', icon: null },
    { id: 'homework', title: '学生作业（参考解答）', icon: Folder, tag: '学生线下合作探究' },
    { id: 'survey', title: '课程问卷', icon: Folder },
    { id: 'chapter1', title: '第一章函数', icon: Folder },
    { id: 'chapter2', title: '第二章极限与连续', icon: Folder },
    { id: 'chapter3', title: '第三章导数与微分', icon: Folder },
    { id: 'chapter4', title: '第四章中值定理与导数应用', icon: Folder },
    { id: 'chapter5', title: '第五章不定积分', icon: null },
    { id: 'chapter6', title: '第六章定积分计算', icon: Folder },
  ];

  return (
    <div className="min-h-screen bg-[#E8F5F0]">
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
            {/* 网格视图按钮 */}
            <button className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 active:bg-gray-200">
              <LayoutGrid className="w-5 h-5 text-gray-700" />
            </button>
            
            {/* 全屏按钮 */}
            <button className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 active:bg-gray-200">
              <Maximize2 className="w-5 h-5 text-gray-700" />
            </button>
          </div>
        </div>
      </div>

      {/* ========== 课程信息区域 ========== */}
      <div className="bg-[#D4F5F0] px-4 pt-4 pb-6">
        {/* 课程标题 */}
        <h2 className="text-xl font-bold text-gray-900 mb-2">
          经济数学——微积分（一）
        </h2>

        {/* 教师列表 */}
        <p className="text-sm text-gray-600 mb-4">
          韩华,余旌胡,高足,田书英,周青龙
        </p>

        {/* 学期信息和查看记录 */}
        <div className="flex items-center justify-between">
          <span className="text-xs text-gray-500">
            2024年秋 | 校内公开 | 教务开课
          </span>
          <button className="text-xs text-[#10B981] font-medium">
            查看记录 &gt;
          </button>
        </div>
      </div>

      {/* ========== 功能入口网格 ========== */}
      <div className="bg-white px-4 py-6">
        <div className="grid grid-cols-4 gap-4">
          {/* 作业任务 */}
          <button className="flex flex-col items-center gap-2 active:scale-95 transition-transform">
            <div className="w-12 h-12 rounded-lg bg-[#E0F7FA] flex items-center justify-center">
              <span className="text-2xl text-[#10B981]">✓</span>
            </div>
            <span className="text-xs text-gray-700">作业任务</span>
          </button>

          {/* 课程工具 */}
          <button className="flex flex-col items-center gap-2 active:scale-95 transition-transform">
            <div className="w-12 h-12 rounded-lg bg-[#E0F7FA] flex items-center justify-center">
              <span className="text-2xl text-[#10B981]"></span>
            </div>
            <span className="text-xs text-gray-700">课程工具</span>
          </button>

          {/* 分组 */}
          <button className="flex flex-col items-center gap-2 active:scale-95 transition-transform">
            <div className="w-12 h-12 rounded-lg bg-[#E0F7FA] flex items-center justify-center">
              <span className="text-2xl text-[#10B981]">👥</span>
            </div>
            <span className="text-xs text-gray-700">分组</span>
          </button>

          {/* 我的学情 */}
          <button className="flex flex-col items-center gap-2 active:scale-95 transition-transform">
            <div className="w-12 h-12 rounded-lg bg-[#E0F7FA] flex items-center justify-center">
              <span className="text-2xl text-[#10B981]">📊</span>
            </div>
            <span className="text-xs text-gray-700">我的学情</span>
          </button>
        </div>

        {/* 分页指示器 */}
        <div className="flex justify-center gap-1 mt-4">
          <div className="w-1.5 h-1.5 rounded-full bg-[#10B981]" />
          <div className="w-1.5 h-1.5 rounded-full bg-gray-300" />
        </div>
      </div>

      {/* ========== 课程内容列表 ========== */}
      <div className="bg-white mt-2 px-4 py-4">
        {/* 标题栏 */}
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-base font-bold text-gray-900">课程内容</h3>
          <button className="text-xs text-[#10B981] bg-[#E0F7FA] px-3 py-1 rounded-full">
            答疑
          </button>
        </div>

        {/* 章节列表 */}
        <div className="space-y-1">
          {courseContent.map((item) => (
            <button
              key={item.id}
              className="w-full flex items-center py-3 border-b border-gray-50 last:border-0 active:bg-gray-50 transition-colors"
            >
              {/* 标签（如果有） */}
              {item.tag && (
                <span className="text-xs text-[#F59E0B] bg-[#FEF3C7] px-2 py-1 rounded mr-2 mb-1">
                  {item.tag}
                </span>
              )}

              {/* 文件夹图标 */}
              {item.icon ? (
                <Folder className="w-5 h-5 text-gray-400 mr-3 flex-shrink-0" />
              ) : (
                <div className="w-5 mr-3 flex-shrink-0" />
              )}

              {/* 章节标题 */}
              <span className="text-sm text-gray-700 flex-1 text-left">
                {item.title}
              </span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
