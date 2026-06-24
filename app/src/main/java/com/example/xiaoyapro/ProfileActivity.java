package com.example.xiaoyapro;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

/**
 * 个人中心/我的页面（截图5）
 * 功能：展示用户信息、功能菜单列表、退出登录
 * UI特点：
 * - 顶部浅青色渐变背景
 * - 用户信息卡片：头像（带"普通用户"黄色标签角标）、用户名、学校名、右箭头
 * - 5个白色圆角菜单项：
 *   1. 个人数据看板（蓝色）
 *   2. 我的报告（黄色）
 *   3. 反馈&建议（蓝色）
 *   4. 投诉举报（橙色）
 *   5. 关于小雅（蓝色）
 * - 青绿色退出登录按钮
 */
public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserSchool;
    private AppCompatButton btnLogout;

    // 菜单项数据（与Web端保持一致）
    private final int[][] menuItems = {
            // {图标资源(Emoji), 名称, 背景颜色}
            {R.string.icon_dashboard, R.string.label_dashboard, R.color.color_secondary},           // 个人数据看板 - 蓝色
            {R.string.icon_report, R.string.label_report, R.color.color_accent_orange},              // 我的报告 - 黄色/橙色
            {R.string.icon_feedback, R.string.label_feedback, R.color.color_secondary},               // 反馈&建议 - 蓝色
            {R.string.icon_complaint, R.string.label_complaint, R.color.color_accent_orange},         // 投诉举报 - 橙色
            {R.string.icon_about, R.string.label_about, R.color.color_secondary}                      // 关于小雅 - 蓝色
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        loadUserInfo();
        setupMenuItems();
        setupClickListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserSchool = findViewById(R.id.tv_user_school);
        btnLogout = findViewById(R.id.btn_logout);
    }

    /**
     * 加载用户信息到界面
     */
    private void loadUserInfo() {
        // 从全局变量获取用户信息
        tvUserName.setText(AppGlobals.currentUserName);
        tvUserSchool.setText(AppGlobals.currentSchoolName);
    }

    /**
     * 设置所有菜单项的数据和样式
     */
    private void setupMenuItems() {
        // 获取5个菜单项的CardView引用
        int[] menuIds = {
                R.id.menu_dashboard,
                R.id.menu_report,
                R.id.menu_feedback,
                R.id.menu_complaint,
                R.id.menu_about
        };

        for (int i = 0; i < menuIds.length; i++) {
            CardView cardView = findViewById(menuIds[i]);
            if (cardView != null) {
                // 获取内部组件
                TextView tvIcon = cardView.findViewById(R.id.tv_icon);
                TextView tvName = cardView.findViewById(R.id.tv_menu_name);
                View iconBg = cardView.findViewById(R.id.icon_background);

                if (tvIcon != null && tvName != null && iconBg != null) {
                    // 设置图标
                    String iconStr = getString(menuItems[i][0]);
                    tvIcon.setText(iconStr);

                    // 设置名称
                    String nameStr = getString(menuItems[i][1]);
                    tvName.setText(nameStr);

                    // 设置圆角方形背景色
                    int bgColor = getResources().getColor(menuItems[i][2], getTheme());
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setCornerRadius(10f); // 圆角10dp对应的像素值
                    drawable.setColor(bgColor);
                    iconBg.setBackground(drawable);
                }

                // 设置点击事件（通过tag传递位置信息）
                final int position = i;
                cardView.setTag(position);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleMenuItemClick(position);
                    }
                });
            }
        }
    }

    /**
     * 处理菜单项点击事件
     * @param position 点击的菜单项位置（0-4）
     */
    private void handleMenuItemClick(int position) {
        switch (position) {
            case 0: // 个人数据看板
                showToast("个人数据看板功能开发中");
                break;
            case 1: // 我的报告
                showToast("我的报告功能开发中");
                break;
            case 2: // 反馈&建议
                showToast("反馈与建议功能开发中");
                break;
            case 3: // 投诉举报
                showToast("投诉举报功能开发中");
                break;
            case 4: // 关于小雅
                showToast("关于小雅功能开发中");
                break;
            default:
                break;
        }
    }

    /**
     * 设置其他点击事件监听器
     */
    private void setupClickListeners() {
        // 退出登录按钮
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmDialog();
            }
        });
    }

    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage(getString(R.string.msg_logout_confirm))
                .setPositiveButton("确定", (dialog, which) -> {
                    // 执行退出登录操作
                    performLogout();
                })
                .setNegativeButton("取消", null)
                .setCancelable(true)
                .show();
    }

    /**
     * 执行退出登录逻辑
     */
    private void performLogout() {
        // 清除全局用户数据
        AppGlobals.clearUserData();

        // 跳转到选择学校页面（重新开始登录流程）
        Intent intent = new Intent(ProfileActivity.this, SchoolSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 清除任务栈
        startActivity(intent);
        finish(); // 关闭当前页面
    }

    /**
     * 显示Toast提示
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次回到个人中心时刷新用户信息
        loadUserInfo();
    }
}
