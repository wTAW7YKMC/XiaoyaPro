package com.example.xiaoyapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择学校页面（截图1）
 * 功能：显示学校列表网格，用户选择后跳转到登录页
 * UI特点：
 * - 顶部浅青色渐变背景 + 学校建筑装饰图标
 * - 2列网格布局展示学校列表（圆形校徽 + 学校名称）
 * - 底部"查看更多开课院校"和"其他用户登录"链接
 */
public class SchoolSelectActivity extends AppCompatActivity {

    private GridView gridSchools;
    private LinearLayout btnMoreSchools;
    private LinearLayout btnOtherLogin;

    // 模拟学校数据（与Web端保持一致）
    private final String[][] schoolData = {
            {"1", "华中师范大学", "🏛️"},
            {"2", "武汉理工大学", "🏫"},
            {"3", "南开大学", "🎓"},
            {"4", "广东第二师范学院", "📚"},
            {"5", "宁夏师范大学", "🏛️"},
            {"6", "喀什大学", "🏫"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select);

        initViews();
        setupSchoolGrid();
        setupClickListeners();
    }

    /**
     * 初始化视图组件
     */
    private void initViews() {
        gridSchools = findViewById(R.id.grid_schools);
        btnMoreSchools = findViewById(R.id.btn_more_schools);
        btnOtherLogin = findViewById(R.id.btn_other_login);
    }

    /**
     * 设置学校网格列表
     */
    private void setupSchoolGrid() {
        List<Map<String, String>> dataList = new ArrayList<>();

        // 准备数据
        for (String[] school : schoolData) {
            Map<String, String> item = new HashMap<>();
            item.put("id", school[0]);
            item.put("name", school[1]);
            item.put("logo", school[2]);
            dataList.add(item);
        }

        // 创建适配器
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                dataList,
                R.layout.item_school_grid,
                new String[]{"logo", "name"},
                new int[]{R.id.tv_school_logo, R.id.tv_school_name}
        );

        gridSchools.setAdapter(adapter);

        // 设置点击事件
        gridSchools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中的学校信息
                Map<String, String> selectedSchool = (Map<String, String>) parent.getItemAtPosition(position);

                // 跳转到登录页面，传递学校数据
                Intent intent = new Intent(SchoolSelectActivity.this, LoginActivity.class);
                intent.putExtra("school_id", selectedSchool.get("id"));
                intent.putExtra("school_name", selectedSchool.get("name"));
                startActivity(intent);
            }
        });
    }

    /**
     * 设置底部按钮点击事件
     */
    private void setupClickListeners() {
        // 查看更多开课院校（预留功能）
        btnMoreSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 实现查看更多院校功能
                // 可以跳转到完整的学校列表页面或WebView
            }
        });

        // 其他用户登录（预留功能）
        btnOtherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 实现其他用户登录方式
                // 可以是管理员登录、教师登录等特殊入口
            }
        });
    }
}
