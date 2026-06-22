/**
 * 小雅智能助手 - 课程数据初始化脚本
 * 
 * 使用方法：
 * 1. 确保 .env 文件中已配置 VITE_SUPABASE_URL 和 VITE_SUPABASE_ANON_KEY
 * 2. 运行: npx tsx scripts/seed-courses-data.ts
 * 
 * 功能：
 * - 自动创建数据库表结构
 * - 插入测试数据（学校、用户、课程、选课、收藏、访问记录）
 */

import { createClient } from '@supabase/supabase-js';
import * as dotenv from 'dotenv';
import { resolve, dirname } from 'path';
import { fileURLToPath } from 'url';

// 获取当前文件的 __dirname（ES Module 兼容）
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// 加载环境变量（支持从多个位置加载）
const envPaths = [
  resolve(__dirname, '../.env.supabase'),      // 优先：apps/web/.env.supabase
  resolve(__dirname, '../../.env'),           // apps/web/.env
  resolve(__dirname, '../../../.env')         // 根目录 .env
];

let envLoaded = false;
for (const envPath of envPaths) {
  try {
    const result = dotenv.config({ path: envPath });
    if (!result.error) {
      console.log(`✅ 从 ${envPath} 加载环境变量`);
      envLoaded = true;
      break;
    }
  } catch (e) {
    // 继续尝试下一个路径
  }
}

if (!envLoaded) {
  console.error('❌ 错误：无法找到 .env 文件');
}

const supabaseUrl = process.env.VITE_SUPABASE_URL || 'https://qhtzpeqeuztyykfbsazi.supabase.co';
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY || 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFodHpwZXFldXp0eXlrZmJzYXppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODIxMDUzOTMsImV4cCI6MjA5NzY4MTM5M30.7ekv6Ov4U9OxxluSCFNfe3HTWhy5sdunmry9P17w2rE';

console.log('🔗 连接 Supabase:', supabaseUrl);

const supabase = createClient(supabaseUrl, supabaseKey);

// ============================================
// 数据定义
// ============================================

// 学校数据
const schools = [
  {
    id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    name: '武汉理工大学',
    code: 'WHUT'
  }
];

// 用户数据
const users = [
  {
    id: 'u-student-001',
    phone: '13800138000',
    password: '123456',
    name: '喻贝贝',
    role: 'STUDENT',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'u-teacher-001',
    phone: '13900139000',
    password: '123456',
    name: '申震',
    role: 'TEACHER',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'u-teacher-002',
    phone: '13900139001',
    password: '123456',
    name: '何清',
    role: 'TEACHER',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'u-teacher-003',
    phone: '13900139002',
    password: '123456',
    name: '魏瀚申',
    role: 'TEACHER',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'u-teacher-004',
    phone: '13900139003',
    password: '123456',
    name: '段飞星',
    role: 'TEACHER',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  }
];

// 课程数据（根据截图真实复刻）
const courses = [
  {
    id: 'course-001',
    title: '国际学术交流基础',
    cover: 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800&h=450&fit=crop',
    status: 'ONGOING',           // 正在进行
    semester: '2026年春',
    type: '校内',
    visit_count: 234,
    teacher_id: 'u-teacher-001',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'course-002',
    title: '经济优化理论B',
    cover: 'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800&h=450&fit=crop',
    status: 'ONGOING',           // 正在进行
    semester: '2026年春',
    type: '校内',
    visit_count: 107,
    teacher_id: 'u-teacher-002',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'course-003',
    title: '刑法学',
    cover: 'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=800&h=450&fit=crop',
    status: 'ONGOING',           // 正在进行
    semester: '2026年春',
    type: '校内',
    visit_count: 35,
    teacher_id: 'u-teacher-003',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'course-004',
    title: '初级极限飞盘',
    cover: 'https://images.unsplash.com/photo-1517649763962-0c623066013b?w=800&h=450&fit=crop',
    status: 'UPCOMING',          // 即将开课
    semester: '2026年春',
    type: '校内',
    visit_count: 89,
    teacher_id: 'u-teacher-004',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  // 已结课的课程（用于测试筛选功能）
  {
    id: 'course-005',
    title: '高等数学A（上）',
    cover: 'https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&h=450&fit=crop',
    status: 'ENDED',             // 已结课
    semester: '2025年秋',
    type: '校内',
    visit_count: 456,
    teacher_id: 'u-teacher-002',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  },
  {
    id: 'course-006',
    title: '大学英语（二）',
    cover: 'https://images.unsplash.com/photo-1546410531-bb4caa6b424d?w=800&h=450&fit=crop',
    status: 'ENDED',             // 已结课
    semester: '2025年秋',
    type: '校内',
    visit_count: 328,
    teacher_id: 'u-teacher-001',
    school_id: 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
  }
];

// 选课数据
const enrollments = [
  { user_id: 'u-student-001', course_id: 'course-001', role: 'STUDENT', progress: 65 },  // 国际学术交流基础
  { user_id: 'u-student-001', course_id: 'course-002', role: 'STUDENT', progress: 40 },  // 经济优化理论B
  { user_id: 'u-student-001', course_id: 'course-003', role: 'STUDENT', progress: 80 },  // 刑法学
  { user_id: 'u-student-001', course_id: 'course-004', role: 'STUDENT', progress: 0 },   // 初级极限飞盘
  { user_id: 'u-student-001', course_id: 'course-005', role: 'STUDENT', progress: 100 }, // 高等数学（已结课）
  { user_id: 'u-student-001', course_id: 'course-006', role: 'STUDENT', progress: 100 }  // 大学英语（已结课）
];

// 收藏数据
const favorites = [
  { user_id: 'u-student-001', course_id: 'course-001' },  // 收藏：国际学术交流基础
  { user_id: 'u-student-001', course_id: 'course-003' }   // 收藏：刑法学
];

// 访问记录数据
const visits = [
  { user_id: 'u-student-001', course_id: 'course-001', visit_count: 15 },  // 访问15次
  { user_id: 'u-student-001', course_id: 'course-002', visit_count: 8 },   // 访问8次
  { user_id: 'u-student-001', course_id: 'course-003', visit_count: 12 },  // 访问12次
  { user_id: 'u-student-001', course_id: 'course-004', visit_count: 3 }    // 访问3次
];

// ============================================
// 主函数
// ============================================

async function seedDatabase() {
  console.log('🚀 开始初始化课程数据库...\n');

  try {
    // 1. 插入学校数据
    console.log('📚 插入学校数据...');
    const { data: schoolsData, error: schoolsError } = await supabase
      .from('schools')
      .upsert(schools, { onConflict: 'id' });
    
    if (schoolsError) throw new Error(`学校数据插入失败: ${schoolsError.message}`);
    console.log(`   ✅ 成功插入 ${schools.length} 所学校\n`);

    // 2. 插入用户数据
    console.log('👥 插入用户数据...');
    const { data: usersData, error: usersError } = await supabase
      .from('users')
      .upsert(users, { onConflict: 'id' });
    
    if (usersError) throw new Error(`用户数据插入失败: ${usersError.message}`);
    console.log(`   ✅ 成功插入 ${users.length} 个用户\n`);

    // 3. 插入课程数据
    console.log('📖 插入课程数据...');
    const { data: coursesData, error: coursesError } = await supabase
      .from('courses')
      .upsert(courses, { onConflict: 'id' });
    
    if (coursesError) throw new Error(`课程数据插入失败: ${coursesError.message}`);
    console.log(`   ✅ 成功插入 ${courses.length} 门课程\n`);

    // 4. 插入选课数据
    console.log('✅ 插入选课数据...');
    const { data: enrollmentsData, error: enrollmentsError } = await supabase
      .from('enrollments')
      .upsert(enrollments, { onConflict: 'user_id,course_id' });
    
    if (enrollmentsError) throw new Error(`选课数据插入失败: ${enrollmentsError.message}`);
    console.log(`   ✅ 成功插入 ${enrollments.length} 条选课记录\n`);

    // 5. 插入收藏数据
    console.log('⭐ 插入收藏数据...');
    const { data: favoritesData, error: favoritesError } = await supabase
      .from('user_course_favorites')
      .upsert(favorites, { onConflict: 'user_id,course_id' });
    
    if (favoritesError) throw new Error(`收藏数据插入失败: ${favoritesError.message}`);
    console.log(`   ✅ 成功插入 ${favorites.length} 条收藏记录\n`);

    // 6. 插入访问记录
    console.log('👁️ 插入访问记录...');
    const { data: visitsData, error: visitsError } = await supabase
      .from('course_visits')
      .upsert(visits, { onConflict: 'user_id,course_id' });
    
    if (visitsError) throw new Error(`访问记录插入失败: ${visitsError.message}`);
    console.log(`   ✅ 成功插入 ${visits.length} 条访问记录\n`);

    // 完成
    console.log('=' .repeat(50));
    console.log('🎉 数据库初始化完成！');
    console.log('=' .repeat(50));
    console.log('\n📊 数据统计:');
    console.log(`   • 学校: ${schools.length} 所`);
    console.log(`   • 用户: ${users.length} 个 (1学生 + 4教师)`);
    console.log(`   • 课程: ${courses.length} 门`);
    console.log(`     - 进行中(ONGOING): ${courses.filter(c => c.status === 'ONGOING').length} 门`);
    console.log(`     - 即将开课(UPCOMING): ${courses.filter(c => c.status === 'UPCOMING').length} 门`);
    console.log(`     - 已结课(ENDED): ${courses.filter(c => c.status === 'ENDED').length} 门`);
    console.log(`   • 选课记录: ${enrollments.length} 条`);
    console.log(`   • 收藏记录: ${favorites.length} 条`);
    console.log(`   • 访问记录: ${visits.length} 条\n`);
    console.log('✨ 现在可以启动应用查看"我的课程"页面了！\n');

  } catch (error) {
    console.error('\n❌ 初始化失败:', error);
    process.exit(1);
  }
}

// 执行初始化
seedDatabase();
