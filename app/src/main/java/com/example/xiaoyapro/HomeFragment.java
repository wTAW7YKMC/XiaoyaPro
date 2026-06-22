package com.example.xiaoyapro;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

/**
 * 首页Fragment
 * 用于在MainActivity的TabBar中显示首页工作台内容
 * 加载activity_home.xml布局文件
 */
public class HomeFragment extends Fragment {

    private View rootView;

    // 功能入口数据（与HomeActivity保持一致）
    private final int[][] functionEntries = {
            // {图标资源(Emoji), 标签, 渐变起始颜色, 渐变结束颜色}
            {R.string.icon_courses, R.string.label_courses, R.color.gradient_blue_400, R.color.gradient_blue_600},      // 我的课程
            {R.string.icon_documents, R.string.label_documents, R.color.gradient_green_400, R.color.gradient_green_600}, // 我的文档
            {R.string.icon_tasks, R.string.label_tasks, R.color.gradient_purple_400, R.color.gradient_purple_600},     // 任务提醒
            {R.string.icon_discover, R.string.label_discover, R.color.gradient_orange_400, R.color.gradient_orange_600} // 发现
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载首页布局文件
        rootView = inflater.inflate(R.layout.activity_home, container, false);

        // 初始化首页内容和事件监听器
        initHomeContent();
        setupFunctionEntries();
        setupClickListeners();

        return rootView;
    }

    /**
     * 初始化首页内容（复用HomeActivity的逻辑）
     */
    private void initHomeContent() {
        if (rootView == null) return;

        // 获取用户信息并填充到界面
        TextView tvUserName = rootView.findViewById(R.id.tv_user_name);
        TextView tvUserSchool = rootView.findViewById(R.id.tv_user_school);
        TextView tvCourseCount = rootView.findViewById(R.id.tv_course_count);
        TextView tvTaskCount = rootView.findViewById(R.id.tv_task_count);

        if (tvUserName != null) {
            tvUserName.setText(AppGlobals.currentUserName);
        }
        if (tvUserSchool != null) {
            tvUserSchool.setText(AppGlobals.currentSchoolName);
        }
        if (tvCourseCount != null) {
            tvCourseCount.setText(String.valueOf(AppGlobals.courseCount));
        }
        if (tvTaskCount != null) {
            tvTaskCount.setText(String.valueOf(AppGlobals.taskCount));
        }
    }

    /**
     * 设置功能入口网格的数据和样式
     */
    private void setupFunctionEntries() {
        if (rootView == null) return;

        // 获取4个功能入口的View引用
        // 前3个是include标签引入的CardView，第4个是FrameLayout包裹的
        int[] entryIds = {
                R.id.entry_courses,
                R.id.entry_documents,
                R.id.entry_tasks,
                R.id.entry_discover
        };

        for (int i = 0; i < entryIds.length; i++) {
            View entryView = rootView.findViewById(entryIds[i]);
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
                int startColor = getResources().getColor(functionEntries[i][2], null);
                int endColor = getResources().getColor(functionEntries[i][3], null);
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TL_BR,
                        new int[]{startColor, endColor}
                );
                gradientDrawable.setCornerRadius(12f);
                iconBg.setBackground(gradientDrawable);
            }

            // 设置点击事件
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
        if (getContext() == null) return;
        
        switch (position) {
            case 0: // 我的课程
                android.widget.Toast.makeText(getContext(), "我的课程功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case 1: // 我的文档
                android.widget.Toast.makeText(getContext(), "我的文档功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case 2: // 任务提醒
                android.widget.Toast.makeText(getContext(), "任务提醒功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                break;
            case 3: // 发现
                android.widget.Toast.makeText(getContext(), "发现功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 设置其他点击事件监听器
     */
    private void setupClickListeners() {
        if (rootView == null) return;

        // 个人空间按钮
        View btnPersonalSpace = rootView.findViewById(R.id.btn_personal_space);
        if (btnPersonalSpace != null) {
            btnPersonalSpace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() != null) {
                        android.widget.Toast.makeText(getContext(), "个人空间功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次Fragment可见时刷新数据
        initHomeContent();
    }
}
