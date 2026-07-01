package com.xiaoya.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 悬浮窗服务 - 提供可自由拖动的AI助手悬浮窗
 * 功能：
 * 1. 显示可拖动的圆形悬浮窗按钮
 * 2. 点击后打开DeepSeek AI对话界面
 * 3. 支持手指自由拖动定位
 */
public class FloatingViewService extends Service {

    // 窗口管理器，用于控制悬浮窗
    private WindowManager windowManager;
    // 悬浮窗视图
    private View floatingView;
    // 悬浮窗布局参数
    private WindowManager.LayoutParams params;

    // 记录触摸位置，用于计算拖动距离
    private float initialTouchX;
    private float initialTouchY;
    private float initialX;
    private float initialY;

    // 是否正在拖动（区分点击和拖动）
    private boolean isMoving = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化悬浮窗
        initFloatingView();
    }

    /**
     * 初始化悬浮窗视图
     * 创建一个圆形的AI助手图标悬浮窗
     */
    private void initFloatingView() {
        // 获取窗口管理器实例
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 创建ImageView作为悬浮窗
        ImageView imageView = new ImageView(this);
        floatingView = imageView;  // 赋值给父类引用

        // 设置AI助手图标为企鹅形象，与页面整体风格保持一致
        imageView.setImageResource(R.drawable.ic_penguin);
        // 设置背景为白色圆形卡片，带轻微阴影
        imageView.setBackgroundResource(R.drawable.floating_ai_button_bg);
        // 设置大小为56dp x 56dp（约112px，贴合参考图的圆形按钮比例）
        int buttonSize = dpToPx(56);
        imageView.setLayoutParams(new WindowManager.LayoutParams(buttonSize, buttonSize));
        // 设置内边距让企鹅图标在圆形内居中显示
        imageView.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        // 设置缩放类型，保证图标完整展示
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        // 设置布局参数
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // 根据Android版本选择窗口类型
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // 不获取焦点，不影响其他操作
                PixelFormat.TRANSLUCENT  // 透明格式
        );

        // 设置初始位置：屏幕右下角
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 600;  // 距离顶部600像素

        // 将悬浮窗添加到窗口
        windowManager.addView(floatingView, params);

        // 设置触摸监听器（实现拖动和点击）
        setupTouchListener();
    }

    /**
     * 设置触摸监听器
     * 实现悬浮窗的自由拖动和点击功能
     */
    private void setupTouchListener() {
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下时记录初始位置
                        isMoving = false;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        initialX = params.x;
                        initialY = params.y;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // 手指移动时计算并更新悬浮窗位置
                        float deltaX = event.getRawX() - initialTouchX;
                        float deltaY = event.getRawY() - initialTouchY;

                        // 如果移动距离超过阈值，判定为拖动操作
                        if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                            isMoving = true;
                            // 更新悬浮窗位置
                            params.x = (int) (initialX + deltaX);
                            params.y = (int) (initialY + deltaY);
                            // 刷新窗口显示
                            windowManager.updateViewLayout(floatingView, params);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        // 手指抬起时判断是点击还是拖动
                        if (!isMoving) {
                            // 如果没有明显移动，视为点击事件
                            onFloatingViewClicked();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 将dp单位转换为像素px
     * 保证悬浮窗按钮在不同屏幕密度下显示大小一致
     */
    private int dpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * 悬浮窗被点击时的处理
     * 打开DeepSeek AI对话界面
     */
    private void onFloatingViewClicked() {
        // 创建Intent跳转到ChatActivity
        Intent intent = new Intent(this, ChatActivity.class);
        // 设置标志位，从Service启动Activity需要此标志
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 服务销毁时移除悬浮窗
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}
