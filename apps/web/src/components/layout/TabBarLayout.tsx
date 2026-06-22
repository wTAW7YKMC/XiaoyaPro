import { Outlet } from 'react-router-dom';
import TabBar from './TabBar';

export default function TabBarLayout() {
  return (
    <div className="min-h-screen bg-[#E8F5F0] flex flex-col">
      {/* 页面内容区 */}
      <div className="flex-1 overflow-y-auto pb-16">
        <Outlet />
      </div>

      {/* 底部TabBar */}
      <TabBar />
    </div>
  );
}
