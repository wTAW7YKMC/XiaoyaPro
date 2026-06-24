package com.example.xiaoyapro;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * 首页工作台页面（截图3）
 * 功能：展示用户信息、学习统计、功能入口导航
 * UI特点：
 * - 顶部浅青色渐变背景 + "个人空间"链接
 * - 欢迎区域：书本图标 + "HI 很高兴见到你！"
 * - 用户信息卡片：头像、用户名、学校、统计数据（课程数/任务数）
 * - 2x2功能入口网格：我的课程、我的文档、任务提醒、发现（带AI角标）
 */
public class HomeActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserSchool;
    private TextView tvCourseCount;
    private TextView tvTaskCount;
    private LinearLayout btnPersonalSpace;

    // 功能入口数据（与Web端保持一致）
    private final int[][] functionEntries = {
            // {图标资源(Emoji), 标签, 渐变起始颜色, 渐变结束颜色}
            {R.string.icon_courses, R.string.label_courses, R.color.gradient_blue_400, R.color.gradient_blue_600},      // 我的课程
            {R.string.icon_documents, R.string.label_documents, R.color.gradient_green_400, R.color.gradient_green_600}, // 我的文档
            {R.string.icon_tasks, R.string.label_tasks, R.color.gradient_purple_400, R.color.gradient_purple_600},     // 任务提醒
            {R.string.icon_discover, R.string.label_discover, R.color.gradient_orange_400, R.color.gradient_orange_600} // 发现
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        loadUserInfo();
        setupFunctionEntries();
        setupClickListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserSchool = findViewById(R.id.tv_user_school);
        tvCourseCount = findViewById(R.id.tv_course_count);
        tvTaskCount = findViewById(R.id.tv_task_count);
        btnPersonalSpace = findViewById(R.id.btn_personal_space);
    }

    /**
     * 加载用户信息到界面
     */
    private void loadUserInfo() {
        // 从全局变量获取用户信息
        tvUserName.setText(AppGlobals.currentUserName);
        tvUserSchool.setText(AppGlobals.currentSchoolName);
        tvCourseCount.setText(String.valueOf(AppGlobals.courseCount));
        tvTaskCount.setText(String.valueOf(AppGlobals.taskCount));
    }

    /**
     * 设置功能入口网格的数据和样式
     */
    private void setupFunctionEntries() {
        // 获取4个功能入口的View引用
        // 前3个是include标签引入的CardView，第4个是FrameLayout包裹的
        int[] entryIds = {
                R.id.entry_courses,
                R.id.entry_documents,
                R.id.entry_tasks,
                R.id.entry_discover
        };

        for (int i = 0; i < entryIds.length; i++) {
            View entryView = findViewById(entryIds[i]);
            if (entryView == null) continue;

            // 对于entry_discover（FrameLayout），需要从中找到内部的CardView
            View containerView = entryView;
            if (i == 3 && entryView instanceof android.widget.FrameLayout) {
                // entry_discover是FrameLayout，找到内部的include（CardView）
                containerView = ((android.widget.FrameLayout) entryView).getChildAt(0);
            }

            // 获取内部的TextView组件
            TextView tvIcon = containerView.findViewById(R.id.tv_icon);
            TextView tvLabel = containerView.findViewById(R.id.tv_label);
            View iconBg = containerView.findViewById(R.id.icon_background);

            if (tvIcon != null && tvLabel != null && iconBg != null) {
                // 设置图标（使用字符串资源的Emoji）
                String iconStr = getString(functionEntries[i][0]);
                tvIcon.setText(iconStr);

                // 设置标签文字
                String labelStr = getString(functionEntries[i][1]);
                tvLabel.setText(labelStr);

                // 设置渐变背景
                int startColor = getResources().getColor(functionEntries[i][2], getTheme());
                int endColor = getResources().getColor(functionEntries[i][3], getTheme());
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{startColor, endColor}
                );
                gradientDrawable.setCornerRadius(12f); // 圆角12dp对应的像素值
                iconBg.setBackground(gradientDrawable);
            }

            // 设置点击事件（通过tag传递位置信息）
            final int position = i;
            entryView.setTag(position);
            entryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleFunctionEntryClick(position);
                }
            });
        }
    }

    /**
     * 处理功能入口点击事件
     * @param position 点击的功能入口位置（0-3）
     */
    private void handleFunctionEntryClick(int position) {
        switch (position) {
            case 0: // 我的课程
                Intent intent = new Intent(this, MyCoursesActivity.class);
                startActivity(intent);
                break;
            case 1: // 我的文档
                // TODO: 跳转到我的文档页面
                showToast("我的文档功能开发中");
                break;
            case 2: // 任务提醒
                // TODO: 跳转到任务提醒页面
                showToast("任务提醒功能开发中");
                break;
            case 3: // 发现
                // TODO: 跳转到发现页面（可能包含AI助手入口）
                showToast("发现功能开发中");
                break;
            default:
                break;
        }
    }

    /**
     * 设置其他点击事件监听器
     */
    private void setupClickListeners() {
        // 个人空间按钮
        btnPersonalSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 跳转到个人空间页面（或个人中心Tab）
                showToast("个人空间功能开发中");
            }
        });
    }

    /**
     * 显示Toast提示（简化方法调用）
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次回到首页时刷新用户信息（防止数据过期）
        loadUserInfo();
    }
}
