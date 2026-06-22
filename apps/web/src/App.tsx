import { HashRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './stores/authStore';
import SchoolSelectPage from './pages/SchoolSelectPage';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import MessageCenterPage from './pages/MessageCenterPage';
import ProfilePage from './pages/ProfilePage';
import MyCoursesPage from './pages/MyCoursesPage';  // 新增：我的课程页面
import TabBarLayout from './components/layout/TabBarLayout';

function App() {
  const { isAuthenticated, currentSchool } = useAuthStore();

  return (
    <HashRouter>
      <Routes>
        {/* 未登录路由 */}
        <Route path="/school-select" element={<SchoolSelectPage />} />
        <Route path="/login" element={<LoginPage />} />
        
        {/* 登录后路由（带TabBar） */}
        <Route element={<TabBarLayout />}>
          <Route path="/home" element={<HomePage />} />
          <Route path="/messages" element={<MessageCenterPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/courses" element={<MyCoursesPage />} />  {/* 新增：我的课程 */}
        </Route>
        
        {/* 默认路由 */}
        <Route
          path="/"
          element={
            isAuthenticated
              ? <Navigate to="/home" replace />
              : <Navigate to="/school-select" replace />
          }
        />
        
        {/* 404 */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </HashRouter>
  );
}

export default App;
