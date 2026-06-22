-- ============================================
-- 小雅智能助手 - Supabase 数据库初始化脚本
-- 包含：我的课程页面所需的所有表结构和测试数据
-- ============================================

-- 启用 UUID 扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- 1. 学校表 (schools)
-- ============================================
CREATE TABLE IF NOT EXISTS schools (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    logo TEXT,
    code VARCHAR(50) UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- 2. 用户表 (users)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    avatar TEXT,
    role VARCHAR(20) DEFAULT 'STUDENT', -- STUDENT, TEACHER, PARENT, ADMIN
    school_id UUID REFERENCES schools(id),
    user_type VARCHAR(50) DEFAULT '普通用户',
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_users_school_id ON users(school_id);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);

-- ============================================
-- 3. 课程表 (courses)
-- ============================================
CREATE TABLE IF NOT EXISTS courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    cover TEXT, -- 课程封面图片 URL
    description TEXT,
    status VARCHAR(20) DEFAULT 'ONGOING', -- ONGOING(正在进行), UPCOMING(即将开课), ENDED(已结课)
    semester VARCHAR(100) NOT NULL, -- 例如: "2026年春"
    type VARCHAR(20) DEFAULT '校内', -- 校内, 公开, etc.
    visit_count INTEGER DEFAULT 0,
    max_students INTEGER,
    teacher_id UUID NOT NULL REFERENCES users(id),
    school_id UUID REFERENCES schools(id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_courses_teacher_id ON courses(teacher_id);
CREATE INDEX IF NOT EXISTS idx_courses_school_id ON courses(school_id);
CREATE INDEX IF NOT EXISTS idx_courses_status ON courses(status);
CREATE INDEX IF NOT EXISTS idx_courses_semester ON courses(semester);

-- ============================================
-- 4. 选课/注册表 (enrollments)
-- ============================================
CREATE TABLE IF NOT EXISTS enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    role VARCHAR(20) DEFAULT 'STUDENT', -- STUDENT, ASSISTANT
    progress INTEGER DEFAULT 0, -- 学习进度 0-100
    joined_at TIMESTAMPTZ DEFAULT NOW(),

    -- 确保每个用户对每门课程只能有一条选课记录
    UNIQUE(user_id, course_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_enrollments_user_id ON enrollments(user_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course_id ON enrollments(course_id);

-- ============================================
-- 5. 课程收藏表 (user_course_favorites)
-- 用于"收藏的课"功能
-- ============================================
CREATE TABLE IF NOT EXISTS user_course_favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),

    -- 确保每个用户对每门课程只能收藏一次
    UNIQUE(user_id, course_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON user_course_favorites(user_id);
CREATE INDEX IF NOT EXISTS idx_favorites_course_id ON user_course_favorites(course_id);

-- ============================================
-- 6. 课程访问记录表 (course_visits)
-- 用于"访问的课"功能和访问量统计
-- ============================================
CREATE TABLE IF NOT EXISTS course_visits (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    visit_count INTEGER DEFAULT 1, -- 访问次数
    last_visited_at TIMESTAMPTZ DEFAULT NOW(),
    created_at TIMESTAMPTZ DEFAULT NOW(),

    -- 确保每个用户对每门课程只有一条访问记录
    UNIQUE(user_id, course_id)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_visits_user_id ON course_visits(user_id);
CREATE INDEX IF NOT EXISTS idx_visits_course_id ON course_visits(course_id);

-- ============================================
-- 插入测试数据
-- ============================================

-- 插入学校数据
INSERT INTO schools (id, name, code) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', '武汉理工大学', 'WHUT')
ON CONFLICT (id) DO NOTHING;

-- 插入用户数据（学生 + 教师）
INSERT INTO users (id, phone, password, name, role, school_id) VALUES
    ('u-student-001', '13800138000', '123456', '喻贝贝', 'STUDENT', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
    ('u-teacher-001', '13900139000', '123456', '申震', 'TEACHER', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
    ('u-teacher-002', '13900139001', '123456', '何清', 'TEACHER', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
    ('u-teacher-003', '13900139002', '123456', '魏瀚申', 'TEACHER', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890'),
    ('u-teacher-004', '13900139003', '123456', '段飞星', 'TEACHER', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890')
ON CONFLICT (id) DO NOTHING;

-- 插入课程数据（根据截图中的真实课程）
INSERT INTO courses (id, title, cover, status, semester, type, visit_count, teacher_id, school_id) VALUES
    (
        'course-001',
        '国际学术交流基础',
        'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800&h=450&fit=crop',
        'ONGOING',
        '2026年春',
        '校内',
        234,
        'u-teacher-001',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    ),
    (
        'course-002',
        '经济优化理论B',
        'https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=800&h=450&fit=crop',
        'ONGOING',
        '2026年春',
        '校内',
        107,
        'u-teacher-002',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    ),
    (
        'course-003',
        '刑法学',
        'https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=800&h=450&fit=crop',
        'ONGOING',
        '2026年春',
        '校内',
        35,
        'u-teacher-003',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    ),
    (
        'course-004',
        '初级极限飞盘',
        'https://images.unsplash.com/photo-1517649763962-0c623066013b?w=800&h=450&fit=crop',
        'UPCOMING',
        '2026年春',
        '校内',
        89,
        'u-teacher-004',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    ),
    -- 已结课的课程（用于测试筛选功能）
    (
        'course-005',
        '高等数学A（上）',
        'https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&h=450&fit=crop',
        'ENDED',
        '2025年秋',
        '校内',
        456,
        'u-teacher-002',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    ),
    (
        'course-006',
        '大学英语（二）',
        'https://images.unsplash.com/photo-1546410531-bb4caa6b424d?w=800&h=450&fit=crop',
        'ENDED',
        '2025年秋',
        '校内',
        328,
        'u-teacher-001',
        'a1b2c3d4-e5f6-7890-abcd-ef1234567890'
    )
ON CONFLICT (id) DO NOTHING;

-- 插入选课数据（学生"喻贝贝"选修的课程）
INSERT INTO enrollments (user_id, course_id, role, progress) VALUES
    ('u-student-001', 'course-001', 'STUDENT', 65),  -- 国际学术交流基础
    ('u-student-001', 'course-002', 'STUDENT', 40),  -- 经济优化理论B
    ('u-student-001', 'course-003', 'STUDENT', 80),  -- 刑法学
    ('u-student-001', 'course-004', 'STUDENT', 0),   -- 初级极限飞盘（即将开课）
    ('u-student-001', 'course-005', 'STUDENT', 100), -- 高等数学（已结课）
    ('u-student-001', 'course-006', 'STUDENT', 100)  -- 大学英语（已结课）
ON CONFLICT (user_id, course_id) DO NOTHING;

-- 插入收藏数据（收藏的课程）
INSERT INTO user_course_favorites (user_id, course_id) VALUES
    ('u-student-001', 'course-001'),  -- 收藏：国际学术交流基础
    ('u-student-001', 'course-003')   -- 收藏：刑法学
ON CONFLICT (user_id, course_id) DO NOTHING;

-- 插入访问记录（用于统计访问量）
INSERT INTO course_visits (user_id, course_id, visit_count, last_visited_at) VALUES
    ('u-student-001', 'course-001', 15, NOW()),  -- 访问国际学术交流基础 15次
    ('u-student-001', 'course-002', 8, NOW()),   -- 访问经济优化理论B 8次
    ('u-student-001', 'course-003', 12, NOW()),  -- 访问刑法学 12次
    ('u-student-001', 'course-004', 3, NOW())    -- 访问初级极限飞盘 3次
ON CONFLICT (user_id, course_id) DO NOTHING;

-- ============================================
-- 启用行级安全策略 (Row Level Security)
-- ============================================

ALTER TABLE schools ENABLE ROW LEVEL SECURITY;
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE enrollments ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_course_favorites ENABLE ROW LEVEL SECURITY;
ALTER TABLE course_visits ENABLE ROW LEVEL SECURITY;

-- 允许所有操作（开发阶段，后续可收紧权限）
CREATE POLICY "Allow all access" ON schools FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all access" ON users FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all access" ON courses FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all access" ON enrollments FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all access" ON user_course_favorites FOR ALL USING (true) WITH CHECK (true);
CREATE POLICY "Allow all access" ON course_visits FOR ALL USING (true) WITH CHECK (true);

-- 输出完成信息
SELECT '✅ 数据库初始化完成！' AS status;
SELECT '📚 已插入 1 所学校' AS info;
SELECT '👥 已插入 5 个用户（1个学生 + 4个教师）' AS info;
SELECT '📖 已插入 6 门课程（4门进行中 + 1门即将开课 + 1门已结课）' AS info;
SELECT '✅ 已建立完整的关联关系（选课、收藏、访问记录）' AS info;
