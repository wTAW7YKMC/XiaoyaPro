import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import morgan from 'morgan';
import dotenv from 'dotenv';
import { createServer } from 'http';
import { Server } from 'socket.io';
import { PrismaClient } from '@prisma/client';

// 加载环境变量
dotenv.config();

// 初始化Prisma客户端
const prisma = new PrismaClient();

// 创建Express应用
const app = express();
const httpServer = createServer(app);

// Socket.io配置
const io = new Server(httpServer, {
  cors: {
    origin: process.env.NODE_ENV === 'production' 
      ? 'https://your-domain.com' 
      : 'http://localhost:5173',
    methods: ['GET', 'POST'],
  },
});

// 中间件
app.use(helmet());
app.use(cors({
  origin: [
    'http://localhost:5173',
    'http://10.0.2.2:5173',  // Android 模拟器
    'capacitor://localhost',   // Capacitor Android
    'http://localhost',        // 允许所有本地开发
  ],
  credentials: true,
}));
app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 健康检查
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// API路由（使用静态导入）
import authRoutes from './routes/auth.js';
import schoolRoutes from './routes/schools.js';
import courseRoutes from './routes/courses.js';
import messageRoutes from './routes/messages.js';
import aiRoutes from './routes/ai.js';

app.use('/api/auth', authRoutes);
app.use('/api/schools', schoolRoutes);
app.use('/api/courses', courseRoutes);
app.use('/api/messages', messageRoutes);
app.use('/api/ai', aiRoutes);

// Socket.io连接处理
io.on('connection', (socket) => {
  console.log('用户连接:', socket.id);
  
  socket.on('disconnect', () => {
    console.log('用户断开:', socket.id);
  });
  
  // 加入房间（用于消息推送）
  socket.on('join-room', (roomId: string) => {
    socket.join(roomId);
    console.log(`用户 ${socket.id} 加入房间 ${roomId}`);
  });
  
  // 发送消息
  socket.on('send-message', async (data) => {
    const { roomId, message } = data;
    io.to(roomId).emit('new-message', message);
  });
});

// 错误处理中间件
app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error('错误:', err);
  
  const status = err.status || 500;
  const message = err.message || '服务器内部错误';
  
  res.status(status).json({
    success: false,
    error: message,
  });
});

// 404处理
app.use((req, res) => {
  res.status(404).json({
    success: false,
    error: '接口不存在',
  });
});

// 启动服务器
const PORT = process.env.PORT || 3000;

httpServer.listen(PORT, '0.0.0.0', () => {
  console.log(`
🚀 服务器已启动
📡 API地址: http://localhost:${PORT}
📡 局域网地址: http://0.0.0.0:${PORT} (手机可访问)
🔌 Socket.io已就绪
📊 环境: ${process.env.NODE_ENV || 'development'}
  `);
});

// 优雅关闭
process.on('SIGTERM', async () => {
  console.log('收到SIGTERM信号，开始关闭服务器...');
  await prisma.$disconnect();
  httpServer.close(() => {
    console.log('服务器已关闭');
    process.exit(0);
  });
});

export { app, prisma, io };
