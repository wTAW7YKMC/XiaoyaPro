
# 🎯 Trae AI智能体完整复刻「小雅智能助手」项目提示词（基于真实UI截图版）

## 📌 项目概述
你需要使用Trae（AI编程助手）完全复刻教育类App「小雅智能助手」。用户已提供5张真实UI截图，你必须**严格按截图像素级还原**每一个页面。

---

## 🖼️ 真实截图页面清单（必须严格还原）

### 截图1：选择学校页（未登录）
- **顶部背景**：浅青色渐变 + 蓝色系学校建筑插画（左侧有房子图案，右侧有书本元素）
- **标题**："请选择学校"（居中，深灰色）
- **学校列表**：2列网格布局，每个学校项包含：
  - 圆形校徽Logo（约60x60px）
  - 学校名称（居中，小字，深灰色）
  - 学校列表：华中师范大学、武汉理工大学、南开大学、广东第二师范学院、宁夏师范大学、喀什大学
- **底部链接**：
  - "查看更多开课院校 >"（青色 #10B981）
  - "其他用户登录 >"（青色 #10B981）
- **整体背景**：浅青色/薄荷绿渐变

### 截图2：登录页
- **顶部导航**："选择学校"（左侧，带建筑图标，青色）
- **Logo区域**：
  - 图标：蓝色建筑/房子图标（简约线条风格）
  - 文字："理工智课"（深蓝色，粗体）
- **当前学校显示**："武汉理工大学"（带小房子图标，浅灰背景圆角条）
- **输入框1**："请输入手机号/账号"
  - 左侧：用户图标（灰色）
  - 样式：圆角矩形，浅灰边框，白色背景
- **输入框2**："请输入密码"
  - 左侧：锁图标（灰色）
  - 右侧：眼睛图标（显示/隐藏密码切换）
- **登录按钮**："登录"
  - 背景：青绿色渐变 #10B981 → #059669
  - 文字：白色，粗体
  - 圆角：约8px
  - 宽度：100%
- **链接**："忘记密码 >"（右侧，青色）
- **分隔线**："其他登录方式"（灰色文字，两侧灰色横线）
- **第三方登录**："统一身份认证"（盾牌图标，青色，居中）
- **底部协议**："登录即表示同意平台《用户服务协议》和《隐私政策》"
  - 左侧：圆形勾选框（已勾选，青色填充 + 白色对勾）
  - 协议文字：灰色，协议名称为青色可点击

### 截图3：首页/工作台（登录后）
- **顶部状态栏**：时间、信号、WiFi、电量（系统默认样式）
- **顶部背景**：浅青色渐变（从上往下渐变变浅）
- **右上角**："个人空间"（青色文字 + 右箭头，可点击）
- **欢迎区域**：
  - 左侧："HI 很高兴见到你！"（青色文字，带3D风格插画 - 书本/文件堆叠图案）
- **用户信息卡片**（白色圆角大卡片，有轻微阴影）：
  - **第一行**：
    - 左侧：圆形头像（卡通男生戴学士帽，约50x50px）
    - 中间："喻贝贝"（用户名，18px粗体，深灰黑 #1F2937）
    - 右侧：右箭头 >（灰色）
  - **第二行**："武汉理工大学"（14px，灰色 #6B7280）
  - **第三行（统计区）**：
    - 左侧："学习课程"（小字灰色） + "61"（大数字，粗体，深灰黑，约24px）
    - 右侧："待完成任务"（小字灰色） + "2"（大数字，粗体，深灰黑）
    - 中间用细竖线分隔
- **功能入口网格**（2x2，白色圆角卡片，间距约12px）：
  1. **我的课程**：彩色书本/文件夹图标（蓝黄渐变）+ "我的课程"文字
  2. **我的文档**：彩色文档图标（蓝绿渐变）+ "我的文档"文字
  3. **任务提醒**：彩色地图/定位图标（蓝紫渐变）+ "任务提醒"文字
  4. **发现**：彩色消息/对话图标（蓝橙渐变），**右下角有小雅AI机器人头像角标** + "发现"文字
  - 每个卡片：白色背景，圆角约16px，图标约40x40px，文字14px居中
- **底部TabBar**（3个Tab，白色背景，顶部细灰线）：
  - **首页**：房子图标（选中状态：青色填充 #10B981，图标下方无文字或有小字"首页"）
  - **消息**：铃铛图标（未选中：灰色描边，**带红色圆点通知** #EF4444）
  - **我的**：人像图标（未选中：灰色描边）
  - TabBar高度：约50px，图标约24px

### 截图4：消息中心
- **顶部标题栏**："消息中心"（居中，粗体，18px） + 右侧更多按钮（三个点 ...）
- **消息分类列表**（白色圆角卡片，每项间距约8px）：
  1. **待办事项**：左侧蓝色圆角方形背景（#3B82F6）+ 白色日历/清单图标 + "待办事项" + 右箭头
  2. **课程消息**：左侧绿色圆角方形背景（#10B981）+ 白色消息图标 + "课程消息" + **红色未读圆点** + 右箭头
  3. **个人消息**：左侧橙色圆角方形背景（#F59E0B）+ 白色用户图标 + "个人消息" + 右箭头
  4. **系统消息**：左侧蓝色圆角方形背景（#3B82F6）+ 白色设置/齿轮图标 + "系统消息" + 右箭头
  5. **群聊**：左侧绿色圆角方形背景（#10B981）+ 白色对话图标 + "群聊" + 右箭头
  6. **私信**：左侧橙色圆角方形背景（#F59E0B）+ 白色邮件图标 + "私信" + 右箭头
  7. **通讯录**：左侧青色圆角方形背景（#06B6D4）+ 白色联系人图标 + "通讯录" + 右箭头
  - 每项样式：白色卡片，圆角约12px，内边距约16px，图标背景约40x40px圆角方形
- **底部TabBar**：同截图3

### 截图5：我的/个人中心
- **顶部背景**：浅青色渐变（同首页）
- **用户信息区**（白色圆角卡片）：
  - 左侧：圆形头像（卡通男生戴学士帽，约50x50px，**带"普通用户"黄色标签角标** #FEF3C7）
  - 中间：
    - "喻贝贝"（18px粗体，深灰黑）
    - "武汉理工大学"（14px，灰色）
  - 右侧：右箭头 >（灰色）
- **功能列表**（白色圆角卡片，每项间距约8px）：
  1. **个人数据看板**：左侧蓝色圆角方形背景 + 白色图表/数据图标 + "个人数据看板" + 右箭头
  2. **我的报告**：左侧黄色圆角方形背景（#F59E0B）+ 白色文档图标 + "我的报告" + 右箭头
  3. **反馈&建议**：左侧蓝色圆角方形背景 + 白色对话图标 + "反馈&建议" + 右箭头
  4. **投诉举报**：左侧橙色圆角方形背景 + 白色警告图标 + "投诉举报" + 右箭头
  5. **关于小雅**：左侧蓝色圆角方形背景 + 白色信息图标 + "关于小雅" + 右箭头
  - 每项样式：同消息列表项
- **退出登录按钮**：
  - 背景：青绿色 #10B981
  - 文字："退出登录"，白色，粗体，居中
  - 圆角：约8px
  - 宽度：约90%，居中
  - 与上方卡片间距：约16px
- **底部TabBar**：同截图3，当前选中"我的"Tab（人像图标青色填充）

---

## 🎨 UI设计规范（严格按截图提取）

```css
/* === 色彩系统 === */
--color-bg-primary: linear-gradient(180deg, #E0F7FA 0%, #E8F5F0 100%);
--color-bg-page: #E8F5F0;
--color-primary: #10B981;        /* 主青绿色 */
--color-primary-dark: #059669;     /* 按钮按下 */
--color-primary-light: #D1FAE5;    /* 浅绿背景 */
--color-secondary: #3B82F6;        /* 次蓝色 */
--color-accent-orange: #F59E0B;    /* 橙色强调 */
--color-accent-cyan: #06B6D4;      /* 青色强调 */
--color-card: #FFFFFF;               /* 卡片白色 */
--color-text-primary: #1F2937;     /* 主文字深灰黑 */
--color-text-secondary: #6B7280;   /* 次要文字灰色 */
--color-text-link: #10B981;        /* 链接文字青色 */
--color-border: #E5E7EB;           /* 边框浅灰 */
--color-red-dot: #EF4444;          /* 红点提醒 */
--color-tag-bg: #FEF3C7;           /* 标签背景浅黄 */

/* === 字体 === */
font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Arial, sans-serif;
--font-size-xs: 12px;
--font-size-sm: 14px;
--font-size-base: 16px;
--font-size-lg: 18px;
--font-size-xl: 24px;
--font-weight-normal: 400;
--font-weight-medium: 500;
--font-weight-bold: 700;

/* === 圆角 === */
--radius-sm: 8px;     /* 按钮 */
--radius-md: 12px;    /* 列表项 */
--radius-lg: 16px;    /* 大卡片 */
--radius-xl: 20px;    /* 功能入口卡片 */
--radius-full: 9999px; /* 圆形 */

/* === 阴影 === */
--shadow-card: 0 2px 8px rgba(0, 0, 0, 0.06);
--shadow-float: 0 4px 16px rgba(0, 0, 0, 0.08);

/* === 间距 === */
--spacing-xs: 4px;
--spacing-sm: 8px;
--spacing-md: 12px;
--spacing-lg: 16px;
--spacing-xl: 20px;
--spacing-2xl: 24px;

/* === 布局 === */
--page-padding: 16px;
--card-padding: 16px;
--icon-size-sm: 20px;   /* 列表图标 */
--icon-size-md: 24px;   /* TabBar图标 */
--icon-size-lg: 40px;   /* 功能入口图标 */
--avatar-size: 50px;    /* 头像尺寸 */
```

---

## 🏗️ 技术栈要求

```
前端：React 18 + TypeScript + Vite + Tailwind CSS + React Router v6 + Zustand
移动端：viewport适配 + 底部安全区 + 触摸优化（按钮最小44x44pt）
图标：Lucide React（基础图标）+ 自定义SVG彩色图标（功能入口）
动画：Framer Motion（页面切换、卡片动效）
后端：Node.js + Express + TypeScript
数据库：PostgreSQL（主库）+ Redis（缓存/会话/消息未读）
ORM：Prisma
AI大模型：OpenAI API / 通义千问 / 文心一言（小雅AI助手）
文件存储：MinIO（文档、图片、头像）
实时通信：Socket.io（消息推送、课堂互动）
```

---

## 👥 用户角色体系

| 角色 | 权限 |
|------|------|
| 学生 | 选课、学习、提交作业、参与讨论、查看成绩、记笔记、课程表 |
| 教师 | 创建课程、布置作业、批改、考勤、分组、数据分析、AI备课 |
| 家长 | 查看孩子学习进度、成绩、与教师沟通 |
| 管理员 | 用户管理、课程审核、数据统计、系统配置 |

---

## 🗄️ 数据库核心关联关系（必须体现）

```prisma
// ========== 核心模型定义 ==========

model User {
  id            String   @id @default(uuid())
  phone         String   @unique
  email         String?  @unique
  password      String   // bcrypt加密
  name          String
  avatar        String?  // 头像URL
  role          Role     @default(STUDENT)
  schoolId      String?
  school        School?  @relation(fields: [schoolId], references: [id])
  userType      String   @default("普通用户") // 普通用户/VIP/管理员
  createdAt     DateTime @default(now())
  updatedAt     DateTime @updatedAt

  // === 1:N 关联 ===
  teachCourses    Course[]       @relation("TeacherCourses")
  submissions   Submission[]
  notes         Note[]
  messages      Message[]       @relation("UserMessages")
  attendances   Attendance[]
  grades        Grade[]
  aiConversations AIConversation[]

  // === N:M 关联 ===
  learnCourses  Enrollment[]
  groups        GroupMember[]
  discussions   Discussion[]
}

model School {
  id        String   @id @default(uuid())
  name      String
  logo      String?  // 校徽URL
  code      String   @unique
  createdAt DateTime @default(now())

  users     User[]
  courses   Course[]
}

model Course {
  id          String   @id @default(uuid())
  title       String
  cover       String?  // 课程封面
  description String?  @db.Text
  status      CourseStatus @default(ONGOING)
  semester    String
  type        String   @default("校内") // 校内/校外/自主开课
  visitCount  Int      @default(0)
  maxStudents Int?
  createdAt   DateTime @default(now())
  updatedAt   DateTime @updatedAt

  // === 关联关系 ===
  teacherId   String
  teacher     User     @relation("TeacherCourses", fields: [teacherId], references: [id])
  schoolId    String?
  school      School?  @relation(fields: [schoolId], references: [id])

  // === 1:N 子表 ===
  assignments   Assignment[]
  documents     Document[]
  discussions   Discussion[]
  groups        Group[]
  enrollments   Enrollment[]
  attendances   Attendance[]
  grades        Grade[]
  chapters      Chapter[] // 课程章节（备授课）
}

model Enrollment {
  id        String   @id @default(uuid())
  userId    String
  courseId  String
  role      EnrollRole @default(STUDENT)
  progress  Int      @default(0) // 学习进度百分比
  joinedAt  DateTime @default(now())

  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)

  @@unique([userId, courseId])
}

model Assignment {
  id          String   @id @default(uuid())
  title       String
  type        AssignmentType @default(HOMEWORK)
  content     String   @db.Text
  deadline    DateTime?
  maxScore    Int      @default(100)
  status      String   @default("DRAFT")
  aiReviewEnabled Boolean @default(false)
  createdAt   DateTime @default(now())

  teacherId   String
  teacher     User     @relation(fields: [teacherId], references: [id])
  courseId    String
  course      Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)

  submissions Submission[]
}

model Submission {
  id          String   @id @default(uuid())
  content     String   @db.Text
  attachments String[]
  score       Int?
  feedback    String?  @db.Text
  aiReview    String?  @db.Text // AI批改结果JSON
  status      SubmissionStatus @default(SUBMITTED)
  submittedAt DateTime @default(now())
  gradedAt    DateTime?

  studentId   String
  student     User     @relation(fields: [studentId], references: [id])
  assignmentId String
  assignment  Assignment @relation(fields: [assignmentId], references: [id], onDelete: Cascade)
}

model Group {
  id          String   @id @default(uuid())
  name        String
  type        String   @default("协作学习分组")
  maxMembers  Int      @default(6)
  createdAt   DateTime @default(now())

  courseId    String
  course      Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)
  members     GroupMember[]
}

model GroupMember {
  id        String   @id @default(uuid())
  role      String   @default("member") // leader/member
  joinedAt  DateTime @default(now())

  userId    String
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
  groupId   String
  group     Group    @relation(fields: [groupId], references: [id], onDelete: Cascade)

  @@unique([userId, groupId])
}

model Chapter {
  id        String   @id @default(uuid())
  title     String
  order     Int
  visible   Boolean  @default(true)
  createdAt DateTime @default(now())

  courseId  String
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)
  materials Material[]
}

model Material {
  id        String   @id @default(uuid())
  name      String
  url       String
  type      String   // PDF/DOC/VIDEO/IMAGE/LINK
  size      Int?
  visible   Boolean  @default(true)
  uploadedAt DateTime @default(now())

  chapterId String
  chapter   Chapter  @relation(fields: [chapterId], references: [id], onDelete: Cascade)
}

model Document {
  id        String   @id @default(uuid())
  name      String
  url       String
  type      String
  size      Int?
  createdAt DateTime @default(now())

  courseId  String
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)
}

model Discussion {
  id        String   @id @default(uuid())
  title     String
  content   String   @db.Text
  aiSummary String?  @db.Text
  createdAt DateTime @default(now())

  authorId  String
  author    User     @relation(fields: [authorId], references: [id])
  courseId  String
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)
}

model Attendance {
  id        String   @id @default(uuid())
  date      DateTime
  status    AttendanceStatus @default(PRESENT)
  method    String   @default("manual")

  userId    String
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
  courseId  String
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)

  @@unique([userId, courseId, date])
}

model Grade {
  id        String   @id @default(uuid())
  type      String   // 平时/期中/期末/总评
  score     Int
  comment   String?
  createdAt DateTime @default(now())

  studentId String
  student   User     @relation(fields: [studentId], references: [id], onDelete: Cascade)
  courseId  String
  course    Course   @relation(fields: [courseId], references: [id], onDelete: Cascade)
}

model Note {
  id        String   @id @default(uuid())
  title     String
  content   String   @db.Text
  courseId  String?
  tags      String[]
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  userId    String
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
}

model Message {
  id        String   @id @default(uuid())
  type      MessageType @default(SYSTEM)
  category  String   // 待办事项/课程消息/个人消息/系统消息/群聊/私信
  title     String
  content   String   @db.Text
  isRead    Boolean  @default(false)
  senderId  String?
  createdAt DateTime @default(now())

  userId    String
  user      User     @relation("UserMessages", fields: [userId], references: [id], onDelete: Cascade)
}

model AIConversation {
  id        String   @id @default(uuid())
  sessionId String   @unique
  title     String   @default("新对话")
  messages  Json     // [{role, content, timestamp, attachments?}]
  context   Json?    // {courseId, userRole, preferences}
  createdAt DateTime @default(now())
  updatedAt DateTime @updatedAt

  userId    String
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
}

enum Role { STUDENT TEACHER PARENT ADMIN }
enum CourseStatus { ONGOING UPCOMING ENDED }
enum AssignmentType { HOMEWORK QUIZ TEST PROJECT }
enum SubmissionStatus { SUBMITTED GRADING GRADED RETURNED }
enum AttendanceStatus { PRESENT ABSENT LATE LEAVE }
enum MessageType { SYSTEM COURSE CHAT }
enum EnrollRole { STUDENT ASSISTANT }
```

---

## 🤖 AI大模型嵌入方案

### 小雅AI助手功能
1. **智能问答**：基于课程知识库RAG回答学科问题
2. **作业辅导**：拍照搜题、步骤解析、举一反三
3. **AI作业批改**：客观题自动批改，主观题给出评分建议
4. **个性化学习路径**：根据学情数据推荐内容
5. **智能摘要**：自动生成课程讨论区内容摘要
6. **语音识别**：语音输入指令、语音笔记（Web Speech API）
7. **知识图谱**：构建课程知识点关联网络

### AI技术架构
```
用户提问 → 意图识别 → 向量检索(Pinecone/Milvus) → 提示工程 → 大模型API → 流式输出(SSE)
                ↓
         课程上下文注入（当前课程ID、用户角色、学习进度）
```

---

## 🔧 分步实现计划

### Phase 1: 项目初始化与数据库
1. 初始化Monorepo（apps/web + apps/server + packages/shared）
2. 配置TypeScript、ESLint、Prettier、Tailwind
3. Docker Compose：PostgreSQL + Redis + MinIO
4. Prisma Schema（严格按上述模型）+ 迁移 + Seed数据

### Phase 2: 认证体系
1. 注册/登录API（JWT + Refresh Token）
2. 统一身份认证接口（模拟OAuth2）
3. 选择学校页（截图1）+ 登录页（截图2）像素级还原
4. 路由守卫 + 权限控制

### Phase 3: 核心页面UI还原
1. 底部TabBar组件（3个Tab，选中状态）
2. 首页工作台（截图3）：用户信息卡片 + 功能入口网格 + 欢迎插画
3. 消息中心（截图4）：7类消息列表
4. 个人中心（截图5）：功能列表 + 退出登录
5. 所有页面严格按截图配色和布局

### Phase 4: 业务逻辑与API
1. 学校管理API
2. 课程CRUD + 选课/退课
3. 作业管理（布置、提交、批改）
4. 分组管理（创建、加入、退出）
5. 考勤管理
6. 成绩管理
7. 笔记/文档/讨论
8. 消息系统（含未读红点）

### Phase 5: AI功能集成
1. 接入大模型API
2. RAG检索系统（向量数据库 + 文档切片）
3. 小雅AI助手聊天界面
4. AI作业批改
5. 智能推荐

### Phase 6: 实时与优化
1. Socket.io消息推送
2. 文件上传/下载
3. 性能优化（虚拟列表、懒加载）
4. PWA配置

### Phase 7: 测试与部署
1. 单元测试 + E2E测试
2. Docker打包 + Nginx配置
3. CI/CD流水线

---

## ⚠️ 关键约束

1. **截图优先**：用户提供的5张截图是最高设计标准，颜色、间距、圆角、图标位置必须与截图一致
2. **关联关系**：数据库1:N和N:M关系必须通过业务逻辑体现（如删除课程级联删除作业）
3. **角色隔离**：教师/学生看到的同一页面内容不同
4. **数据一致性**：Prisma事务保证原子性
5. **AI上下文**：AI助手识别用户角色和当前课程
6. **移动端体验**：触摸优化，手势支持
7. **离线支持**：核心页面离线缓存

---

## 📋 交付物
- [ ] 完整可运行的前后端代码
- [ ] Prisma迁移文件和Seed脚本
- [ ] Docker Compose配置
- [ ] API文档（Swagger）
- [ ] 数据库ER图
- [ ] 部署文档
