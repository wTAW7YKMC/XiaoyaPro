package com.example.xiaoyapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 主Activity（带TabBar的主容器）
 * 功能：
 * - 作为登录后的主界面入口
 * - 管理底部TabBar（首页、消息、我的）的切换
 * - 通过Fragment管理三个主要页面内容
 *
 * 页面流程：
 * SchoolSelectActivity -> LoginActivity -> MainActivity（当前）
 */
public class MainActivity extends AppCompatActivity {

    // Tab位置常量
    private static final int TAB_HOME = 0;
    private static final int TAB_MESSAGES = 1;
    private static final int TAB_PROFILE = 2;

    // 当前选中的Tab
    private int currentTab = TAB_HOME;

    // TabBar组件引用
    private LinearLayout tabHome;
    private LinearLayout tabMessages;
    private LinearLayout tabProfile;

    // Tab图标和背景组件
    private View bgTabHome, bgTabMessages, bgTabProfile;
    private TextView iconTabHome, iconTabMessages, iconTabProfile;
    private View redDotMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 检查是否已登录（防止未登录用户直接进入此页面）
        if (!checkLoginStatus()) {
            redirectToLogin();
            return;
        }

        initViews();
        setupTabBar();
        // 默认显示首页
        switchTab(TAB_HOME);
    }

    /**
     * 检查登录状态
     * @return 是否已登录
     */
    private boolean checkLoginStatus() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("is_logged_in", false)) {
            return true;
        }
        return AppGlobals.isLoggedIn();
    }

    /**
     * 重定向到登录页面
     */
    private void redirectToLogin() {
        Intent intent = new Intent(this, SchoolSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        // Tab栏按钮
        tabHome = findViewById(R.id.tab_home);
        tabMessages = findViewById(R.id.tab_messages);
        tabProfile = findViewById(R.id.tab_profile);

        // Tab背景
        bgTabHome = findViewById(R.id.bg_tab_home);
        bgTabMessages = findViewById(R.id.bg_tab_messages);
        bgTabProfile = findViewById(R.id.bg_tab_profile);

        // Tab图标
        iconTabHome = findViewById(R.id.icon_tab_home);
        iconTabMessages = findViewById(R.id.icon_tab_messages);
        iconTabProfile = findViewById(R.id.icon_tab_profile);

        // 消息红点
        redDotMessages = findViewById(R.id.red_dot_messages);
    }

    /**
     * 设置TabBar点击事件
     */
    private void setupTabBar() {
        // 首页Tab点击
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTab(TAB_HOME);
            }
        });

        // 消息Tab点击
        tabMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTab(TAB_MESSAGES);

                // TODO: 实际项目中应该检查是否有未读消息来决定是否隐藏红点
                // 这里暂时不隐藏红点（模拟有未读消息）
                // redDotMessages.setVisibility(View.GONE);
            }
        });

        // 我的Tab点击
        tabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTab(TAB_PROFILE);
            }
        });
    }

    /**
     * 切换Tab
     * @param tab 要切换到的Tab位置（TAB_HOME/TAB_MESSAGES/TAB_PROFILE）
     */
    private void switchTab(int tab) {
        if (currentTab == tab) {
            return; // 已经是当前Tab，无需切换
        }

        currentTab = tab;

        // 更新所有Tab的UI状态
        updateTabUI();

        // 切换Fragment内容
        switchFragment(tab);
    }

    /**
     * 更新所有Tab的UI状态（选中/未选中）
     */
    private void updateTabUI() {
        // 重置所有Tab为未选中状态
        resetAllTabs();

        // 设置当前选中的Tab为高亮状态
        switch (currentTab) {
            case TAB_HOME:
                bgTabHome.setVisibility(View.VISIBLE);
                iconTabHome.setAlpha(1.0f); // 完全不透明
                break;

            case TAB_MESSAGES:
                bgTabMessages.setVisibility(View.VISIBLE);
                iconTabMessages.setAlpha(1.0f); // 完全不透明
                break;

            case TAB_PROFILE:
                bgTabProfile.setVisibility(View.VISIBLE);
                iconTabProfile.setAlpha(1.0f); // 完全不透明
                break;
        }
    }

    /**
     * 重置所有Tab为未选中状态
     */
    private void resetAllTabs() {
        // 隐藏所有背景
        bgTabHome.setVisibility(View.INVISIBLE);
        bgTabMessages.setVisibility(View.INVISIBLE);
        bgTabProfile.setVisibility(View.INVISIBLE);

        // 设置所有图标为半透明（未选中状态）
        iconTabHome.setAlpha(0.6f);
        iconTabMessages.setAlpha(0.6f);
        iconTabProfile.setAlpha(0.6f);
    }

    /**
     * 根据Tab切换对应的Fragment
     * @param tab Tab位置
     */
    private void switchFragment(int tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 隐藏所有Fragment（如果存在的话）
        hideAllFragments(transaction);

        // 显示目标Fragment
        String tag = getFragmentTag(tab);
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            // Fragment不存在，创建新的
            fragment = createFragment(tab);
            transaction.add(R.id.fragment_container, fragment, tag);
        } else {
            // Fragment已存在，显示它
            transaction.show(fragment);
        }

        transaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有Fragment
     */
    private void hideAllFragments(FragmentTransaction transaction) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment homeFrag = fm.findFragmentByTag(getFragmentTag(TAB_HOME));
        if (homeFrag != null) transaction.hide(homeFrag);

        Fragment msgFrag = fm.findFragmentByTag(getFragmentTag(TAB_MESSAGES));
        if (msgFrag != null) transaction.hide(msgFrag);

        Fragment profileFrag = fm.findFragmentByTag(getFragmentTag(TAB_PROFILE));
        if (profileFrag != null) transaction.hide(profileFrag);
    }

    /**
     * 创建对应位置的Fragment实例
     * @param tab Tab位置
     * @return Fragment实例
     */
    private Fragment createFragment(int tab) {
        switch (tab) {
            case TAB_HOME:
                return new HomeFragment();
            case TAB_MESSAGES:
                return new MessageCenterFragment();
            case TAB_PROFILE:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    /**
     * 获取Fragment的标签名称
     * @param tab Tab位置
     * @return 标签字符串
     */
    private String getFragmentTag(int tab) {
        switch (tab) {
            case TAB_HOME:
                return "HOME_FRAGMENT";
            case TAB_MESSAGES:
                return "MESSAGES_FRAGMENT";
            case TAB_PROFILE:
                return "PROFILE_FRAGMENT";
            default:
                return "UNKNOWN_FRAGMENT";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次回到MainActivity时刷新Tab状态
        if (currentTab != TAB_HOME) {
            updateTabUI();
        }
    }

    /**
     * 处理物理返回键（可选：双击返回退出应用）
     */
    private long lastBackPressTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) {
            // 如果距离上次按返回键超过2秒，提示再按一次退出
            android.widget.Toast.makeText(this, "再按一次退出应用", android.widget.Toast.LENGTH_SHORT).show();
            lastBackPressTime = currentTime;
        } else {
            // 2秒内再次按返回键，退出应用
            super.onBackPressed();
        }
    }
}
