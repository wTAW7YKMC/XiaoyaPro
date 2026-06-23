const { createClient } = require('@supabase/supabase-js');

const supabaseUrl = 'https://qhtzpeqeuztyykfbsazi.supabase.co';
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFodHpwZXFldXp0eXlrZmJzYXppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODIxMDUzOTMsImV4cCI6MjA5NzY4MTM5M30.7ekv6Ov4U9OxxluSCFNfe3HTWhy5sdunmry9P17w2rE';

const supabase = createClient(supabaseUrl, supabaseKey);

async function checkDb() {
  console.log('🔍 检查数据库结构...\n');

  try {
    console.log('📖 Course 表数据（包含所有字段）:');
    const { data: courses, error: coursesError } = await supabase
      .from('Course')
      .select('*');
    
    if (coursesError) {
      console.error('❌ 查询失败:', coursesError);
    } else {
      console.log('✅ 查询成功!');
      console.log('字段:', courses.length > 0 ? Object.keys(courses[0]) : '无数据');
      console.log('数据:', JSON.stringify(courses, null, 2));
    }

    console.log('\n👥 User 表数据:');
    const { data: users, error: usersError } = await supabase
      .from('User')
      .select('*');
    
    if (usersError) {
      console.error('❌ 查询失败:', usersError);
    } else {
      console.log('✅ 查询成功!');
      console.log('字段:', users.length > 0 ? Object.keys(users[0]) : '无数据');
      console.log('数据:', JSON.stringify(users, null, 2));
    }

    console.log('\n📚 School 表数据:');
    const { data: schools, error: schoolsError } = await supabase
      .from('School')
      .select('*');
    
    if (schoolsError) {
      console.error('❌ 查询失败:', schoolsError);
    } else {
      console.log('✅ 查询成功!');
      console.log('字段:', schools.length > 0 ? Object.keys(schools[0]) : '无数据');
      console.log('数据:', JSON.stringify(schools, null, 2));
    }

    console.log('\n🔗 测试关联查询（Course + User）:');
    const { data: joined, error: joinedError } = await supabase
      .from('Course')
      .select(`
        id,
        title,
        teacher_id,
        User!Course_teacherId_fkey (id, name)
      `);
    
    if (joinedError) {
      console.error('❌ 关联查询失败:', joinedError);
    } else {
      console.log('✅ 关联查询成功!');
      console.log('数据:', JSON.stringify(joined, null, 2));
    }

  } catch (error) {
    console.error('\n❌ 检查失败:', error.message || error);
  }
}

checkDb();