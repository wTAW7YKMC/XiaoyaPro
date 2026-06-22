import { PrismaClient } from '@prisma/client';
import bcrypt from 'bcryptjs';

const prisma = new PrismaClient();

// 使用字符串代替枚举导入（兼容性问题）
const Role = {
  STUDENT: 'STUDENT',
  TEACHER: 'TEACHER',
  ADMIN: 'ADMIN',
};

async function main() {
  console.log('🌱 开始填充数据库...');

  // 1. 创建学校
  const schools = await Promise.all([
    prisma.school.create({
      data: {
        name: '华中师范大学',
        code: 'CCNU',
        logo: 'https://www.ccnu.edu.cn/images/logo.png',
      },
    }),
    prisma.school.create({
      data: {
        name: '武汉理工大学',
        code: 'WUT',
        logo: 'https://www.whut.edu.cn/images/logo.png',
      },
    }),
    prisma.school.create({
      data: {
        name: '南开大学',
        code: 'NKU',
        logo: 'https://www.nankai.edu.cn/images/logo.png',
      },
    }),
    prisma.school.create({
      data: {
        name: '广东第二师范学院',
        code: 'GDEI',
        logo: 'https://www.gdei.edu.cn/images/logo.png',
      },
    }),
    prisma.school.create({
      data: {
        name: '宁夏师范大学',
        code: 'NXNU',
        logo: 'https://www.nxnu.edu.cn/images/logo.png',
      },
    }),
    prisma.school.create({
      data: {
        name: '喀什大学',
        code: 'KSU',
        logo: 'https://www.ksu.edu.cn/images/logo.png',
      },
    }),
  ]);

  console.log(`✅ 创建了 ${schools.length} 所学校`);

  // 2. 创建用户（密码统一为：123456）
  const hashedPassword = await bcrypt.hash('123456', 10);

  const student = await prisma.user.create({
    data: {
      phone: '13800138000',
      name: '喻贝贝',
      password: hashedPassword,
      role: Role.STUDENT,
      schoolId: schools[1].id, // 武汉理工大学
      userType: '普通用户',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=student1',
    },
  });

  const teacher = await prisma.user.create({
    data: {
      phone: '13900139000',
      name: '张老师',
      password: hashedPassword,
      role: Role.TEACHER,
      schoolId: schools[1].id,
      userType: '普通用户',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=teacher1',
    },
  });

  const admin = await prisma.user.create({
    data: {
      phone: '13700137000',
      name: '管理员',
      password: hashedPassword,
      role: Role.ADMIN,
      schoolId: schools[1].id,
      userType: '管理员',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin1',
    },
  });

  console.log('✅ 创建了用户（学生、教师、管理员）');

  // 3. 创建课程
  const course1 = await prisma.course.create({
    data: {
      title: '高等数学',
      description: '大学高等数学课程，包括微积分、线性代数等内容',
      semester: '2024春季',
      type: '校内',
      status: 'ONGOING',
      teacherId: teacher.id,
      schoolId: schools[1].id,
      cover: 'https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=400',
    },
  });

  const course2 = await prisma.course.create({
    data: {
      title: '大学英语',
      description: '大学英语课程，提升英语听说读写能力',
      semester: '2024春季',
      type: '校内',
      status: 'ONGOING',
      teacherId: teacher.id,
      schoolId: schools[1].id,
      cover: 'https://images.unsplash.com/photo-1546410531-bb4caa69424d?w=400',
    },
  });

  const course3 = await prisma.course.create({
    data: {
      title: '计算机程序设计',
      description: 'Python编程基础与实践',
      semester: '2024春季',
      type: '校内',
      status: 'ONGOING',
      teacherId: teacher.id,
      schoolId: schools[1].id,
      cover: 'https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=400',
    },
  });

  console.log('✅ 创建了3门课程');

  // 4. 学生选课（N:M关联）
  await prisma.enrollment.createMany({
    data: [
      { userId: student.id, courseId: course1.id, role: 'STUDENT', progress: 65 },
      { userId: student.id, courseId: course2.id, role: 'STUDENT', progress: 40 },
      { userId: student.id, courseId: course3.id, role: 'STUDENT', progress: 80 },
    ],
  });

  console.log('✅ 学生选课完成');

  // 5. 创建作业
  const assignment1 = await prisma.assignment.create({
    data: {
      title: '第一章课后习题',
      type: 'HOMEWORK',
      content: '完成教材第一章的所有习题，包括微积分基础计算',
      deadline: new Date('2024-12-31'),
      maxScore: 100,
      status: 'PUBLISHED',
      teacherId: teacher.id,
      courseId: course1.id,
    },
  });

  const assignment2 = await prisma.assignment.create({
    data: {
      title: '英语作文：My Future Plan',
      type: 'HOMEWORK',
      content: '写一篇不少于300字的英语作文，描述你的未来规划',
      deadline: new Date('2024-12-25'),
      maxScore: 100,
      status: 'PUBLISHED',
      teacherId: teacher.id,
      courseId: course2.id,
    },
  });

  console.log('✅ 创建了2个作业');

  // 6. 创建消息
  await prisma.message.createMany({
    data: [
      {
        type: 'SYSTEM',
        category: '待办事项',
        title: '作业提交提醒',
        content: '您有2个作业即将到期，请及时完成提交',
        userId: student.id,
        isRead: false,
      },
      {
        type: 'COURSE',
        category: '课程消息',
        title: '新课程资料已上传',
        content: '《高等数学》第三章课件已更新',
        userId: student.id,
        isRead: false,
      },
      {
        type: 'SYSTEM',
        category: '系统消息',
        title: '系统维护通知',
        content: '系统将于本周六凌晨2点进行维护，预计持续2小时',
        userId: student.id,
        isRead: true,
      },
    ],
  });

  console.log('✅ 创建了消息');

  // 7. 创建考勤记录
  await prisma.attendance.create({
    data: {
      date: new Date('2024-12-20'),
      status: 'PRESENT',
      method: 'manual',
      userId: student.id,
      courseId: course1.id,
    },
  });

  console.log('✅ 创建了考勤记录');

  // 8. 创建成绩
  await prisma.grade.create({
    data: {
      type: '平时',
      score: 85,
      comment: '表现良好',
      studentId: student.id,
      courseId: course1.id,
    },
  });

  console.log('✅ 创建了成绩记录');

  // 9. 创建笔记
  await prisma.note.create({
    data: {
      title: '高等数学第一章笔记',
      content: '极限的定义和性质...\n连续函数的概念...\n导数的计算方法...',
      courseId: course1.id,
      tags: ['高数', '极限', '导数'],
      userId: student.id,
    },
  });

  console.log('✅ 创建了笔记');

  console.log('\n🎉 数据库填充完成！');
  console.log('\n📋 测试账号信息：');
  console.log('学生账号：13800138000 / 123456');
  console.log('教师账号：13900139000 / 123456');
  console.log('管理员账号：13700137000 / 123456');
}

main()
  .catch((e) => {
    console.error('❌ 填充数据库失败:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
