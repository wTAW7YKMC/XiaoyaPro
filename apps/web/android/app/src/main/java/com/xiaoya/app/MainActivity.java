package com.xiaoya.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.getcapacitor.BridgeActivity;

/**
 * 主页面Activity
 * 继承Capacitor的BridgeActivity，显示Web前端界面
 * 功能：
 * 1. 加载Capacitor Web视图（React前端）
 * 2. 请求悬浮窗权限
 * 3. 启动AI助手悬浮窗服务
 */
public class MainActivity extends BridgeActivity {

    // 悬浮窗权限请求码
    private static final int REQUEST_CODE_FLOATING_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化后检查并启动悬浮窗服务
        checkAndStartFloatingService();
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
