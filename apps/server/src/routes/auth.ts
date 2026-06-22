import { Router, Request, Response } from 'express';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import { prisma } from '../index.js';
import { authMiddleware, AuthRequest } from '../middleware/auth.js';

const router = Router();

// 登录
router.post('/login', async (req: Request, res: Response) => {
  try {
    const { account, password, schoolId } = req.body;
    
    if (!account || !password) {
      return res.status(400).json({
        success: false,
        error: '账号和密码不能为空',
      });
    }
    
    // 查找用户（支持手机号或邮箱登录）
    const user = await prisma.user.findFirst({
      where: {
        OR: [
          { phone: account },
          { email: account },
        ],
        ...(schoolId ? { schoolId } : {}),
      },
      include: {
        school: true,
      },
    });
    
    if (!user) {
      return res.status(401).json({
        success: false,
        error: '账号或密码错误',
      });
    }
    
    // 验证密码
    const isValidPassword = await bcrypt.compare(password, user.password);
    
    if (!isValidPassword) {
      return res.status(401).json({
        success: false,
        error: '账号或密码错误',
      });
    }
    
    // 生成JWT Token
    const accessToken = jwt.sign(
      {
        id: user.id,
        phone: user.phone,
        role: user.role,
      },
      process.env.JWT_SECRET || 'secret',
      { expiresIn: '7d' }
    );
    
    const refreshToken = jwt.sign(
      { id: user.id },
      process.env.JWT_REFRESH_SECRET || 'refresh-secret',
      { expiresIn: '30d' }
    );
    
    // 返回用户信息
    res.json({
      success: true,
      data: {
        user: {
          id: user.id,
          phone: user.phone,
          name: user.name,
          avatar: user.avatar,
          role: user.role,
          schoolId: user.schoolId,
          userType: user.userType,
          school: user.school,
        },
        accessToken,
        refreshToken,
      },
    });
  } catch (error) {
    console.error('登录错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 获取当前用户信息
router.get('/me', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const user = await prisma.user.findUnique({
      where: { id: req.user!.id },
      include: {
        school: true,
        learnCourses: {
          include: {
            course: true,
          },
        },
      },
    });
    
    if (!user) {
      return res.status(404).json({
        success: false,
        error: '用户不存在',
      });
    }
    
    res.json({
      success: true,
      data: user,
    });
  } catch (error) {
    console.error('获取用户信息错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 刷新Token
router.post('/refresh', async (req: Request, res: Response) => {
  try {
    const { refreshToken } = req.body;
    
    if (!refreshToken) {
      return res.status(400).json({
        success: false,
        error: '未提供刷新令牌',
      });
    }
    
    const decoded = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET || 'refresh-secret') as any;
    
    const user = await prisma.user.findUnique({
      where: { id: decoded.id },
    });
    
    if (!user) {
      return res.status(404).json({
        success: false,
        error: '用户不存在',
      });
    }
    
    const newAccessToken = jwt.sign(
      {
        id: user.id,
        phone: user.phone,
        role: user.role,
      },
      process.env.JWT_SECRET || 'secret',
      { expiresIn: '7d' }
    );
    
    const newRefreshToken = jwt.sign(
      { id: user.id },
      process.env.JWT_REFRESH_SECRET || 'refresh-secret',
      { expiresIn: '30d' }
    );
    
    res.json({
      success: true,
      data: {
        accessToken: newAccessToken,
        refreshToken: newRefreshToken,
      },
    });
  } catch (error) {
    console.error('刷新Token错误:', error);
    res.status(401).json({
      success: false,
      error: '无效的刷新令牌',
    });
  }
});

// 登出
router.post('/logout', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    // 在实际应用中，可以将token加入黑名单
    res.json({
      success: true,
      message: '登出成功',
    });
  } catch (error) {
    console.error('登出错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

export default router;
