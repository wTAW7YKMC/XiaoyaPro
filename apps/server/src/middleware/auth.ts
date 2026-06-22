import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';

export interface AuthRequest extends Request {
  user?: {
    id: string;
    phone: string;
    role: string;
  };
}

export const authMiddleware = (req: AuthRequest, res: Response, next: NextFunction) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    
    if (!token) {
      return res.status(401).json({
        success: false,
        error: '未提供认证令牌',
      });
    }
    
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'secret') as any;
    
    req.user = {
      id: decoded.id,
      phone: decoded.phone,
      role: decoded.role,
    };
    
    next();
  } catch (error) {
    return res.status(401).json({
      success: false,
      error: '无效的认证令牌',
    });
  }
};

export const roleMiddleware = (roles: string[]) => {
  return (req: AuthRequest, res: Response, next: NextFunction) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        error: '未认证',
      });
    }
    
    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        error: '权限不足',
      });
    }
    
    next();
  };
};
