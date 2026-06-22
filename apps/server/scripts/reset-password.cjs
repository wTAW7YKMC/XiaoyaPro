// 更新数据库用户密码为 123456
const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

async function main() {
  // 生成新的密码哈希
  const newPassword = await bcrypt.hash('123456', 10);
  console.log('新密码哈希:', newPassword);
  
  // 更新所有用户的密码
  const result = await prisma.user.updateMany({
    data: { password: newPassword }
  });
  
  console.log(`成功更新 ${result.count} 个用户的密码`);
  console.log('现在可以使用以下账号登录:');
  console.log('- 学生: 13800138000 / 123456');
  console.log('- 教师: 13900139000 / 123456');
  console.log('- 管理员: 13700137000 / 123456');
}

main()
  .catch(console.error)
  .finally(() => prisma.$disconnect());
