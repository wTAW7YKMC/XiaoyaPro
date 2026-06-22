# 小雅智能助手 - 开发指南

## 📖 项目简介

小雅智能助手是一款教育类AI应用，采用现代化的全栈技术架构，集成了AI大模型能力，为学生提供智能化的学习体验。

## 🏗️ 技术架构

### 前端技术栈
- **框架**: React 18 + TypeScript
- **构建工具**: Vite 5
- **样式**: Tailwind CSS 3
- **状态管理**: Zustand
- **路由**: React Router v6
- **动画**: Framer Motion
- **图标**: Lucide React
- **HTTP客户端**: Axios
- **实时通信**: Socket.io-client

### 后端技术栈
- **运行时**: Node.js + TypeScript
- **框架**: Express.js
- **ORM**: Prisma
- **数据库**: PostgreSQL 15
- **缓存**: Redis 7
- **对象存储**: MinIO
- **认证**: JWT (JSON Web Token)
- **实时通信**: Socket.io
- **AI集成**: OpenAI API / 通义千问

### 开发工具
- **包管理**: npm workspaces (Monorepo)
- **容器化**: Docker + Docker Compose
- **代码规范**: ESLint + Prettier
- **类型检查**: TypeScript 5

## 📁 项目结构

```
xiaoya-pro/
├── apps/
│   ├── web/                    # 前端应用
│   │   ├── src/
│   │   │   ├── components/     # 通用组件
│   │   │   ├── pages/          # 页面组件
│   │   │   ├── hooks/          # 自定义Hooks
│   │   │   ├── stores/         # Zustand状态管理
│   │   │   ├── services/       # API服务
│   │   │   ├── utils/          # 工具函数
│   │   │   ├── types/          # TypeScript类型
│   │   │   └── assets/         # 静态资源
│   │   ├── public/             # 公共资源
│   │   └── index.html          # 入口HTML
│   │
│   └── server/                 # 后端服务
│       ├── src/
│       │   ├── routes/         # 路由定义
│       │   ├── controllers/    # 控制器
│       │   ├── services/       # 业务逻辑
│       │   ├── middleware/     # 中间件
│       │   ├── utils/          # 工具函数
│       │   └── types/          # TypeScript类型
│       └── prisma/
│           ├── schema.prisma   # 数据库Schema
│           ├── migrations/     # 数据库迁移
│           └── seed.ts         # 种子数据
│
├── packages/
│   └── shared/                 # 共享代码包
│       └── src/
│           └── index.ts        # 共享类型和工具
│
├── docker-compose.yml          # Docker配置
├── package.json                # 根package.json
├── tsconfig.json               # TypeScript配置
└── .env.example                # 环境变量模板
```

## 🚀 快速开始

### 1. 环境要求
- Node.js >= 18.0.0
- npm >= 9.0.0
- Docker Desktop（用于运行数据库和缓存）

### 2. 安装依赖

```bash
# 安装所有工作区依赖
npm install
```

### 3. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件，配置数据库连接等信息
```

### 4. 启动基础设施

```bash
# 启动 PostgreSQL、Redis、MinIO
docker-compose up -d

# 检查服务状态
docker-compose ps
```

### 5. 初始化数据库

```bash
# 生成Prisma客户端
npm run db:generate

# 运行数据库迁移
npm run db:migrate

# 填充种子数据
npm run db:seed
```

### 6. 启动开发服务器

```bash
# 同时启动前端和后端
npm run dev

# 或分别启动
npm run dev:web      # 前端：http://localhost:5173
npm run dev:server   # 后端：http://localhost:3000
```

### 7. 访问应用

- **前端应用**: http://localhost:5173
- **后端API**: http://localhost:3000/api
- **Prisma Studio**: npm run db:studio（数据库可视化工具）
- **MinIO控制台**: http://localhost:9001（用户名/密码：minioadmin/minioadmin）

## 📊 数据库设计

### 核心实体关系

1. **用户(User)** - 学生、教师、家长、管理员
2. **学校(School)** - 学校信息
3. **课程(Course)** - 课程信息，关联教师和学校
4. **选课(Enrollment)** - 学生与课程的多对多关系
5. **作业(Assignment)** - 教师布置的作业
6. **提交(Submission)** - 学生的作业提交
7. **分组(Group)** - 学习分组
8. **章节(Chapter)** - 课程章节
9. **资料(Material)** - 课程资料
10. **文档(Document)** - 课程文档
11. **讨论(Discussion)** - 课程讨论
12. **考勤(Attendance)** - 学生考勤
13. **成绩(Grade)** - 学生成绩
14. **笔记(Note)** - 学习笔记
15. **消息(Message)** - 系统消息
16. **AI对话(AIConversation)** - AI助手对话记录

### 关联关系示例

- **用户 ↔ 学校**: 多对一（多个用户属于一个学校）
- **用户 ↔ 课程**: 多对多（通过Enrollment表）
- **教师 ↔ 课程**: 一对多（一个教师可以教多门课程）
- **课程 ↔ 作业**: 一对多（一门课程可以有多个作业）
- **作业 ↔ 提交**: 一对多（一个作业可以有多个学生提交）

## 🔐 测试账号

数据库初始化后，可以使用以下测试账号登录：

| 角色 | 账号 | 密码 |
|------|------|------|
| 学生 | 13800138000 | 123456 |
| 教师 | 13900139000 | 123456 |
| 管理员 | 13700137000 | 123456 |

## 📱 主要功能模块

### 1. 认证系统
- 选择学校
- 手机号/账号登录
- 统一身份认证
- JWT Token管理

### 2. 首页工作台
- 用户信息展示
- 学习课程统计
- 待完成任务提醒
- 功能入口导航

### 3. 消息中心
- 待办事项
- 课程消息
- 个人消息
- 系统消息
- 群聊/私信
- 通讯录

### 4. 个人中心
- 个人信息管理
- 个人数据看板
- 我的报告
- 反馈与建议
- 关于小雅

### 5. AI助手（小雅）
- 智能问答
- 作业辅导
- AI批改
- 个性化学习路径
- 智能摘要

## 🛠️ 开发规范

### 代码风格
- 使用TypeScript严格模式
- 遵循ESLint配置规范
- 组件使用函数式组件 + Hooks
- 使用Prettier格式化代码

### Git提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构代码
test: 测试相关
chore: 构建/工具链相关
```

## 📝 API文档

后端API采用RESTful风格，所有接口前缀为 `/api`。

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/me` - 获取当前用户信息

### 学校相关
- `GET /api/schools` - 获取学校列表

### 课程相关
- `GET /api/courses` - 获取课程列表
- `GET /api/courses/:id` - 获取课程详情
- `POST /api/courses` - 创建课程（教师）
- `POST /api/courses/:id/enroll` - 选课

### 作业相关
- `GET /api/assignments` - 获取作业列表
- `POST /api/assignments` - 创建作业（教师）
- `POST /api/assignments/:id/submit` - 提交作业（学生）

### 消息相关
- `GET /api/messages` - 获取消息列表
- `PUT /api/messages/:id/read` - 标记消息已读

### AI助手
- `POST /api/ai/chat` - AI对话
- `GET /api/ai/conversations` - 获取对话历史

## 🐳 Docker部署

### 构建镜像

```bash
# 构建前端镜像
docker build -t xiaoya-web -f apps/web/Dockerfile .

# 构建后端镜像
docker build -t xiaoya-server -f apps/server/Dockerfile .
```

### 生产环境部署

```bash
# 启动所有服务
docker-compose -f docker-compose.prod.yml up -d

# 查看日志
docker-compose logs -f
```

## 📚 相关文档

- [React文档](https://react.dev/)
- [TypeScript文档](https://www.typescriptlang.org/)
- [Prisma文档](https://www.prisma.io/docs)
- [Tailwind CSS文档](https://tailwindcss.com/)
- [Express文档](https://expressjs.com/)

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：
1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

## 📄 许可证

本项目仅供学习和研究使用。

## 📞 联系方式

如有问题，请联系开发团队。
