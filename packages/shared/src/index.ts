// 共享类型定义
export enum Role {
  STUDENT = 'STUDENT',
  TEACHER = 'TEACHER',
  PARENT = 'PARENT',
  ADMIN = 'ADMIN'
}

export enum CourseStatus {
  ONGOING = 'ONGOING',
  UPCOMING = 'UPCOMING',
  ENDED = 'ENDED'
}

export enum AssignmentType {
  HOMEWORK = 'HOMEWORK',
  QUIZ = 'QUIZ',
  TEST = 'TEST',
  PROJECT = 'PROJECT'
}

export enum SubmissionStatus {
  SUBMITTED = 'SUBMITTED',
  GRADING = 'GRADING',
  GRADED = 'GRADED',
  RETURNED = 'RETURNED'
}

export enum AttendanceStatus {
  PRESENT = 'PRESENT',
  ABSENT = 'ABSENT',
  LATE = 'LATE',
  LEAVE = 'LEAVE'
}

export enum MessageType {
  SYSTEM = 'SYSTEM',
  COURSE = 'COURSE',
  CHAT = 'CHAT'
}

export enum EnrollRole {
  STUDENT = 'STUDENT',
  ASSISTANT = 'ASSISTANT'
}

// API响应类型
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  error?: string;
  message?: string;
}

// 用户相关类型
export interface UserInfo {
  id: string;
  phone: string;
  name: string;
  avatar?: string;
  role: Role;
  schoolId?: string;
  userType: string;
}

export interface LoginRequest {
  account: string;
  password: string;
  schoolId?: string;
}

export interface LoginResponse {
  user: UserInfo;
  accessToken: string;
  refreshToken: string;
}

// 课程相关类型
export interface CourseInfo {
  id: string;
  title: string;
  cover?: string;
  description?: string;
  status: CourseStatus;
  semester: string;
  type: string;
  visitCount: number;
  teacher: {
    id: string;
    name: string;
    avatar?: string;
  };
  school?: {
    id: string;
    name: string;
  };
}

// 消息相关类型
export interface MessageInfo {
  id: string;
  type: MessageType;
  category: string;
  title: string;
  content: string;
  isRead: boolean;
  createdAt: string;
}

// AI对话相关类型
export interface AIMessage {
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp: number;
  attachments?: string[];
}

export interface AIConversationInfo {
  id: string;
  sessionId: string;
  title: string;
  messages: AIMessage[];
  createdAt: string;
  updatedAt: string;
}
