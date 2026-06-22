import { Router, Response } from 'express';
import { prisma } from '../index.js';
import { authMiddleware, AuthRequest } from '../middleware/auth.js';

const router = Router();

// AI对话（模拟）
router.post('/chat', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const { message, conversationId } = req.body;
    const userId = req.user!.id;
    
    // 这里应该调用真实的AI API（如通义千问、文心一言等）
    // 现在返回模拟响应
    
    const aiResponses = [
      '这是一个很好的问题！让我来帮你解答...',
      '根据我的理解，这个问题的答案是...',
      '我建议你可以从以下几个方面来思考...',
      '这个知识点很重要，我来详细解释一下...',
    ];
    
    const randomResponse = aiResponses[Math.floor(Math.random() * aiResponses.length)];
    
    // 保存对话记录
    if (conversationId) {
      const conversation = await prisma.aIConversation.findUnique({
        where: { id: conversationId },
      });
      
      if (conversation) {
        const messages = conversation.messages as any[];
        messages.push(
          { role: 'user', content: message, timestamp: Date.now() },
          { role: 'assistant', content: randomResponse, timestamp: Date.now() }
        );
        
        await prisma.aIConversation.update({
          where: { id: conversationId },
          data: {
            messages,
            updatedAt: new Date(),
          },
        });
      }
    } else {
      // 创建新对话
      await prisma.aIConversation.create({
        data: {
          sessionId: `session_${Date.now()}`,
          title: message.substring(0, 20),
          messages: [
            { role: 'user', content: message, timestamp: Date.now() },
            { role: 'assistant', content: randomResponse, timestamp: Date.now() },
          ],
          userId,
        },
      });
    }
    
    res.json({
      success: true,
      data: {
        response: randomResponse,
        timestamp: new Date().toISOString(),
      },
    });
  } catch (error) {
    console.error('AI对话错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

// 获取对话历史
router.get('/conversations', authMiddleware, async (req: AuthRequest, res: Response) => {
  try {
    const userId = req.user!.id;
    
    const conversations = await prisma.aIConversation.findMany({
      where: { userId },
      orderBy: {
        updatedAt: 'desc',
      },
    });
    
    res.json({
      success: true,
      data: conversations,
    });
  } catch (error) {
    console.error('获取对话历史错误:', error);
    res.status(500).json({
      success: false,
      error: '服务器错误',
    });
  }
});

export default router;
