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
app.use(cors());
app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// 健康检查
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// API路由
app.use('/api/auth', await import('./routes/auth.js').then(m => m.default));
app.use('/api/schools', await import('./routes/schools.js').then(m => m.default));
app.use('/api/courses', await import('./routes/courses.js').then(m => m.default));
app.use('/api/messages', await import('./routes/messages.js').then(m => m.default));
app.use('/api/ai', await import('./routes/ai.js').then(m => m.default));

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

httpServer.listen(PORT, () => {
  console.log(`
🚀 服务器已启动
📡 API地址: http://localhost:${PORT}
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
