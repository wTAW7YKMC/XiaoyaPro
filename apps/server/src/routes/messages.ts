import { Router, Response } from 'express';
import { prisma } from '../index.js';
import { authMiddleware, AuthRequest } from '../middleware/auth.js';

const router = Router();

// 获取消息列表
router.get('/', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const userId = req.user!.id;
    const { category } = req.query;
    
    const where: any = { userId };
    
    if (category) {
      where.category = category;
    }
    
    const messages = await prisma.message.findMany({
      where,
      orderBy: {
        createdAt: 'desc',
      },
    });
    
    res.json({
      success: true,
      data: messages,
    });
  } catch (error) {
    console.error('获取消息列表错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 获取未读消息数量
router.get('/unread-count', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const userId = req.user!.id;
    
    const count = await prisma.message.count({
      where: {
        userId,
        isRead: false,
      },
    });
    
    // 按分类统计
    const byCategory = await prisma.message.groupBy({
      by: ['category'],
      where: {
        userId,
        isRead: false,
      },
      _count: {
        category: true,
      },
    });
    
    res.json({
      success: true,
      data: {
        total: count,
        byCategory: byCategory.reduce((acc, item) => {
          acc[item.category] = item._count.category;
          return acc;
        }, {} as Record<string, number>),
      },
    });
  } catch (error) {
    console.error('获取未读消息数量错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 标记消息已读
router.put('/:id/read', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const { id } = req.params;
    const userId = req.user!.id;
    
    const message = await prisma.message.updateMany({
      where: {
        id,
        userId,
      },
      data: {
        isRead: true,
      },
    });
    
    if (message.count === 0) {
      return res.status(404).json({
        success: false,
        error: '消息不存在',
      });
    }
    
    res.json({
      success: true,
      message: '标记已读成功',
    });
  } catch (error) {
    console.error('标记消息已读错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 标记所有消息已读
router.put('/read-all', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const userId = req.user!.id;
    
    await prisma.message.updateMany({
      where: {
        userId,
        isRead: false,
      },
      data: {
        isRead: true,
      },
    });
    
    res.json({
      success: true,
      message: '全部标记已读成功',
    });
  } catch (error) {
    console.error('标记所有消息已读错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

export default router;
