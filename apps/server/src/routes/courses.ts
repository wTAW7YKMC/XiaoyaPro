import { Router, Response } from 'express';
import { prisma } from '../index.js';
import { authMiddleware, AuthRequest } from '../middleware/auth.js';

const router = Router();

// 获取课程列表（需要登录）
router.get('/', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const userId = req.user!.id;
    
    // 获取用户学习的课程
    const enrollments = await prisma.enrollment.findMany({
      where: { userId },
      include: {
        course: {
          include: {
            teacher: {
              select: {
                id: true,
                name: true,
                avatar: true,
              },
            },
            school: true,
          },
        },
      },
    });
    
    const courses = enrollments.map(e => ({
      ...e.course,
      progress: e.progress,
      enrollmentId: e.id,
    }));
    
    res.json({
      success: true,
      data: courses,
    });
  } catch (error) {
    console.error('获取课程列表错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 获取课程详情
router.get('/:id', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const { id } = req.params;
    const userId = req.user!.id;
    
    const course = await prisma.course.findUnique({
      where: { id },
      include: {
        teacher: {
          select: {
            id: true,
            name: true,
            avatar: true,
          },
        },
        school: true,
        chapters: {
          where: { visible: true },
          orderBy: { order: 'asc' },
          include: {
            materials: {
              where: { visible: true },
            },
          },
        },
        assignments: {
          orderBy: { createdAt: 'desc' },
        },
        enrollments: {
          where: { userId },
        },
      },
    });
    
    if (!course) {
      return res.status(404).json({
        success: false,
        error: '课程不存在',
      });
    }
    
    res.json({
      success: true,
      data: course,
    });
  } catch (error) {
    console.error('获取课程详情错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 选课
router.post('/:id/enroll', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const courseId = req.params.id;
    const userId = req.user!.id;
    
    // 检查是否已选课
    const existing = await prisma.enrollment.findUnique({
      where: {
        userId_courseId: {
          userId,
          courseId,
        },
      },
    });
    
    if (existing) {
      return res.status(400).json({
        success: false,
        error: '已选过该课程',
      });
    }
    
    // 创建选课记录
    const enrollment = await prisma.enrollment.create({
      data: {
        userId,
        courseId,
        role: 'STUDENT',
        progress: 0,
      },
    });
    
    res.json({
      success: true,
      data: enrollment,
      message: '选课成功',
    });
  } catch (error) {
    console.error('选课错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

export default router;
