package com.xiaoya.app;

/**
 * 课程数据模型类
 * 对应 Supabase 数据库中的 courses 表结构
 */
public class Course {
    private String id;                    // 课程ID
    private String title;                 // 课程标题
    private String cover;                 // 封面图片URL
    private String status;                // 状态: ONGOING(进行中), UPCOMING(即将开课), ENDED(已结课)
    private String semester;              // 学期，例如: "2026年春"
    private String type;                  // 类型: 校内, 公开, etc.
    private int visitCount;               // 访问量
    private String teacherId;             // 教师ID
    private String schoolId;              // 学校ID
    private Teacher teacher;              // 教师信息
    private School school;               // 学校信息
    private int progress;                 // 学习进度 (0-100)，仅我学的课有此字段

    /**
     * 教师信息内部类
     */
    public static class Teacher {
        private String id;
        private String name;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    /**
     * 学校信息内部类
     */
    public static class School {
        private String id;
        private String name;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // ========== Getter & Setter 方法 ==========

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getVisitCount() { return visitCount; }
    public void setVisitCount(int visitCount) { this.visitCount = visitCount; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    /**
     * 获取状态显示文本
     */
    public String getStatusText() {
        if (status == null) return "未知";
        switch (status) {
            case "ONGOING":
                return "正在进行";
            case "UPCOMING":
                return "即将开课";
            case "ENDED":
                return "已结课";
            default:
                return "未知";
        }
    }

    /**
     * 获取教师姓名（带空值保护）
     */
    public String getTeacherName() {
        if (teacher != null && teacher.getName() != null) {
            return teacher.getName();
        }
        return "未知教师";
    }

    /**
     * 获取学校名称（带空值保护）
     */
    public String getSchoolName() {
        if (school != null && school.getName() != null) {
            return school.getName();
        }
        return "未知学院";
    }
}
