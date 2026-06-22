import { Router, Request, Response } from 'express';
import { prisma } from '../index.js';

const router = Router();

// 获取所有学校
router.get('/', async (req: Request, res: Response) => {
  try {
    const schools = await prisma.school.findMany({
      orderBy: {
        name: 'asc',
      },
    });
    
    res.json({
      success: true,
      data: schools,
    });
  } catch (error) {
    console.error('获取学校列表错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 获取单个学校
router.get('/:id', async (req: Request, res: Response) => {
  try {
    const { id } = req.params;
    
    const school = await prisma.school.findUnique({
      where: { id },
      include: {
        courses: true,
        users: {
          select: {
            id: true,
            name: true,
            role: true,
          },
        },
      },
    });
    
    if (!school) {
      return res.status(404).json({
        success: false,
        error: '学校不存在',
      });
    }
    
    res.json({
      success: true,
      data: school,
    });
  } catch (error) {
    console.error('获取学校详情错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

export default router;
