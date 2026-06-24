package com.example.xiaoyapro;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录页面（截图2）
 * 功能：用户账号密码登录，支持统一身份认证
 * UI特点：
 * - 顶部导航栏（返回 + 选择学校）
 * - Logo区域（蓝色建筑图标 + "理工智课"）
 * - 当前学校显示标签
 * - 手机号/账号输入框 + 密码输入框（带显示/隐藏切换）
 * - 青绿色渐变登录按钮
 * - 忘记密码链接
 * - 统一身份认证入口
 * - 底部协议勾选框
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private TextView btnTogglePassword;
    private TextView tvCurrentSchool;
    private CheckBox checkboxAgreement;
    private LinearLayout btnLogin;
    private LinearLayout btnUnifiedAuth;
    private LinearLayout topNavigation;

    // 密码是否可见
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        loadSchoolInfo();
        setupClickListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        tvCurrentSchool = findViewById(R.id.tv_current_school);
        checkboxAgreement = findViewById(R.id.checkbox_agreement);
        btnLogin = findViewById(R.id.btn_login);
        btnUnifiedAuth = findViewById(R.id.btn_unified_auth);
        topNavigation = findViewById(R.id.top_navigation);
    }

    /**
     * 加载从选择学校页面传递过来的学校信息
     */
    private void loadSchoolInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            String schoolName = intent.getStringExtra("school_name");
            if (schoolName != null && !schoolName.isEmpty()) {
                tvCurrentSchool.setText(schoolName);
            }
        }
    }

    /**
     * 设置所有点击事件监听器
     */
    private void setupClickListeners() {
        // 返回按钮（顶部导航）
        topNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回到选择学校页面
            }
        });

        // 密码显示/隐藏切换
        btnTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    // 显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnTogglePassword.setText("🙈");
                } else {
                    // 隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnTogglePassword.setText("👁️");
                }
                // 将光标移动到末尾
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // 登录按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // 统一身份认证按钮
        btnUnifiedAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 实现统一身份认证逻辑（OAuth2等）
                Toast.makeText(LoginActivity.this, "统一身份认证功能开发中", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 处理登录逻辑
     */
    private void handleLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 验证输入
        if (phone.isEmpty()) {
            Toast.makeText(this, "请输入手机号/账号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查是否同意协议
        if (!checkboxAgreement.isChecked()) {
            Toast.makeText(this, "请先同意用户服务协议和隐私政策", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: 实际项目中应该调用后端API进行登录验证
        // 这里使用模拟数据直接跳转到首页

        // 保存用户信息到SharedPreferences或全局变量（简化处理）
        AppGlobals.currentUserPhone = phone;
        AppGlobals.currentUserName = "喻贝贝"; // 模拟用户名
        AppGlobals.currentSchoolName = tvCurrentSchool.getText().toString();

        // 跳转到主页（带TabBar的主界面）
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("is_logged_in", true);
        startActivity(intent);
        finish(); // 关闭登录页面，防止返回
    }

    /**
     * 处理忘记密码（预留功能）
     */
    public void onForgotPassword(View view) {
        // TODO: 实现忘记密码功能（重置密码页面或弹窗）
        Toast.makeText(this, "忘记密码功能开发中", Toast.LENGTH_SHORT).show();
    }
}
