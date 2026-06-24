package com.example.xiaoyapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

/**
 * 个人中心/我的Fragment
 * 用于在MainActivity的TabBar中显示个人中心内容
 * 加载activity_profile.xml布局文件
 */
public class ProfileFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载个人中心布局文件
        rootView = inflater.inflate(R.layout.activity_profile, container, false);

        // 初始化用户信息和菜单项
        initProfileContent();

        return rootView;
    }

    /**
     * 初始化个人中心内容（复用ProfileActivity的逻辑）
     */
    private void initProfileContent() {
        if (rootView == null) return;

        // 填充用户信息
        android.widget.TextView tvUserName = rootView.findViewById(R.id.tv_user_name);
        android.widget.TextView tvUserSchool = rootView.findViewById(R.id.tv_user_school);

        if (tvUserName != null) {
            tvUserName.setText(AppGlobals.currentUserName);
        }
        if (tvUserSchool != null) {
            tvUserSchool.setText(AppGlobals.currentSchoolName);
        }

        // 设置退出登录按钮点击事件
        AppCompatButton btnLogout = rootView.findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutConfirmDialog();
                }
            });
        }

        // TODO: 初始化5个菜单项的点击事件（与ProfileActivity类似）
        // 这里简化处理，实际项目中应该完整实现setupMenuItems()方法
    }

    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutConfirmDialog() {
        if (getContext() == null || getActivity() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), SchoolSelectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish(); // 关闭当前Activity
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次Fragment可见时刷新用户信息
        initProfileContent();
    }
}
