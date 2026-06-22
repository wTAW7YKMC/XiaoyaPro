package com.xiaoya.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.getcapacitor.BridgeActivity;

/**
 * 主页面Activity
 * 继承Capacitor的BridgeActivity，并添加悬浮窗功能
 * 功能：
 * 1. 保留原有的Capacitor Web视图功能
 * 2. 显示"我的课程"入口按钮
 * 3. 请求悬浮窗权限
 * 4. 启动AI助手悬浮窗服务
 */
public class MainActivity extends BridgeActivity {

    // 悬浮窗权限请求码
    private static final int REQUEST_CODE_FLOATING_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加"我的课程"入口按钮到WebView上方
        addMyCoursesButton();

        // 初始化后检查并启动悬浮窗服务
        checkAndStartFloatingService();
    }

    /**
     * 在WebView容器上方添加"我的课程"按钮
     * 用户点击后可跳转到原生课程列表页面
     */
    private void addMyCoursesButton() {
        // 获取主布局容器
        FrameLayout mainLayout = findViewById(android.R.id.content);
        
        if (mainLayout != null) {
            // 创建按钮
            Button btnMyCourses = new Button(this);
            btnMyCourses.setText("📖 我的课程");
            btnMyCourses.setAllCaps(false);
            
            // 设置按钮样式（绿色背景，白色文字）
            btnMyCourses.setBackgroundColor(0xFF4CAF50);
            btnMyCourses.setTextColor(0xFFFFFFFF);
            btnMyCourses.setTextSize(14f);
            btnMyCourses.setPadding(40, 16, 40, 16);
            
            // 设置点击事件 - 跳转到课程列表页面
            btnMyCourses.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MyCoursesActivity.class);
                startActivity(intent);
                
                // 添加从右向左滑入的过渡动画
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
            
            // 创建布局参数（顶部居中）
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = 100;  // 距离顶部100dp
            params.gravity = Gravity.CENTER_HORIZONTAL;
            
            // 将按钮添加到布局中
            mainLayout.addView(btnMyCourses, params);
        }
    }

    /**
     * 检查悬浮窗权限并启动服务
     * Android 6.0+ 需要用户手动授权悬浮窗权限
     */
    private void checkAndStartFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有悬浮窗权限
            if (!Settings.canDrawOverlays(this)) {
                // 没有权限，跳转到设置页面让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_FLOATING_PERMISSION);
            } else {
                // 已有权限，直接启动悬浮窗服务
                startFloatingService();
            }
        } else {
            // Android 6.0以下不需要运行时权限，直接启动
            startFloatingService();
        }
    }

    /**
     * 启动悬浮窗服务
     */
    private void startFloatingService() {
        Intent serviceIntent = new Intent(this, FloatingViewService.class);
        startService(serviceIntent);
    }

    /**
     * 处理权限请求结果回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FLOATING_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 再次检查是否授权成功
                if (Settings.canDrawOverlays(this)) {
                    // 用户已授权，启动悬浮窗服务
                    startFloatingService();
                }
                // 如果用户拒绝授权，不显示悬浮窗（不影响主应用使用）
            }
        }
    }
}
