package com.example.xiaoyapro;

/**
 * 全局应用变量类
 * 用于在各个Activity之间共享用户信息
 * 注意：实际生产环境应该使用SharedPreferences、Room数据库或ViewModel+LiveData
 */
public class AppGlobals {
    // 当前登录用户的手机号
    public static String currentUserPhone = "";

    // 当前登录用户的姓名
    public static String currentUserName = "";

    // 当前选择的学校名称
    public static String currentSchoolName = "武汉理工大学";

    // 用户角色（学生/教师/家长/管理员）
    public static String userRole = "STUDENT";

    // 用户类型（普通用户/VIP/管理员）
    public static String userType = "普通用户";

    // 学习课程数量
    public static int courseCount = 61;

    // 待完成任务数量
    public static int taskCount = 2;

    /**
     * 清除所有用户数据（退出登录时调用）
     */
    public static void clearUserData() {
        currentUserPhone = "";
        currentUserName = "";
        currentSchoolName = "武汉理工大学";
        userRole = "STUDENT";
        userType = "普通用户";
        courseCount = 61;
        taskCount = 2;
    }

    /**
     * 判断是否已登录
     */
    public static boolean isLoggedIn() {
        return !currentUserPhone.isEmpty();
    }
}
