package com.xiaoya.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的课程页面 - 完全复刻截图设计
 * 
 * 功能特性：
 * ✅ 三个Tab切换（我学的课/收藏的课/访问的课）
 * ✅ 状态筛选（正在进行/即将开课/已结课）- 仅我学的课显示
 * ✅ 学校筛选下拉菜单
 * ✅ 课程卡片列表（封面、课程名、学院、教师、访问量、进度条）
 * ✅ 从Supabase真实读取数据
 * ✅ 加载状态和空状态提示
 */
public class MyCoursesActivity extends AppCompatActivity {
    
    private static final String TAG = "MyCoursesActivity";
    
    // 当前用户ID（实际应用中应从登录状态获取，这里使用测试用户ID）
    private static final String TEST_USER_ID = "u-student-001";
    
    // ========== 视图组件引用 ==========
    
    // 顶部导航栏
    ImageButton btnBack, btnSearch, btnAdd;
    
    // Tab 切换组件
    LinearLayout tabLearning, tabFavorites, tabVisited;
    TextView tvTabLearning, tvTabFavorites, tvTabVisited;
    View indicatorLearning, indicatorFavorites, indicatorVisited;
    
    // 状态筛选栏
    LinearLayout layoutStatusFilter;
    TextView tvStatusOngoing, tvStatusUpcoming, tvStatusEnded;
    ImageButton btnFilter;
    
    // 二级筛选栏
    Spinner spinnerSchools;
    TextView tvFilterAll;
    
    // 列表组件
    RecyclerView rvCourses;
    
    // 加载和空状态提示
    ProgressBar progressBarLoading;
    LinearLayout layoutEmpty;
    TextView tvEmptyHint;
    
    // ========== 数据和适配器 ==========
    List<Course> courseList = new ArrayList<>();
    CourseAdapter adapter;
    
    // 当前选中的 Tab 和筛选条件
    String currentTab = "learning";  // 默认显示"我学的课"
    String currentStatusFilter = null;  // 当前状态筛选（null=全部）
    boolean showProgress = true;  // 是否显示进度条
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_courses);
        
        // 设置系统边距（适配刘海屏等）
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_my_courses), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // 初始化视图
        initViews();
        
        // 设置事件监听
        setupListeners();
        
        // 加载课程数据
        loadCoursesData();
    }
    
    /**
     * 初始化所有视图组件的引用
     */
    private void initViews() {
        Log.d(TAG, "初始化视图组件");
        
        // 顶部导航栏
        btnBack = findViewById(R.id.btn_back);
        btnSearch = findViewById(R.id.btn_search);
        btnAdd = findViewById(R.id.btn_add);
        
        // Tab 切换
        tabLearning = findViewById(R.id.tab_learning);
        tabFavorites = findViewById(R.id.tab_favorites);
        tabVisited = findViewById(R.id.tab_visited);
        tvTabLearning = findViewById(R.id.tv_tab_learning);
        tvTabFavorites = findViewById(R.id.tv_tab_favorites);
        tvTabVisited = findViewById(R.id.tv_tab_visited);
        indicatorLearning = findViewById(R.id.indicator_learning);
        indicatorFavorites = findViewById(R.id.indicator_favorites);
        indicatorVisited = findViewById(R.id.indicator_visited);
        
        // 状态筛选栏
        layoutStatusFilter = findViewById(R.id.layout_status_filter);
        tvStatusOngoing = findViewById(R.id.tv_status_ongoing);
        tvStatusUpcoming = findViewById(R.id.tv_status_upcoming);
        tvStatusEnded = findViewById(R.id.tv_status_ended);
        btnFilter = findViewById(R.id.btn_filter);
        
        // 二级筛选
        spinnerSchools = findViewById(R.id.spinner_schools);
        tvFilterAll = findViewById(R.id.tv_filter_all);
        
        // 列表
        rvCourses = findViewById(R.id.rv_courses);
        
        // 加载和空状态
        progressBarLoading = findViewById(R.id.progress_bar_loading);
        layoutEmpty = findViewById(R.id.layout_empty);
        tvEmptyHint = findViewById(R.id.tv_empty_hint);
        
        // 配置 RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCourses.setLayoutManager(layoutManager);
        
        Log.d(TAG, "视图初始化完成");
    }
    
    /**
     * 设置所有事件监听器
     */
    private void setupListeners() {
        Log.d(TAG, "设置事件监听");
        
        // 返回按钮 - 关闭当前页面
        btnBack.setOnClickListener(v -> {
            finish();
        });
        
        // 搜索按钮（暂未实现）
        btnSearch.setOnClickListener(v -> {
            Log.d(TAG, "点击搜索按钮");
        });
        
        // 添加按钮（暂未实现）
        btnAdd.setOnClickListener(v -> {
            Log.d(TAG, "点击添加按钮");
        });
        
        // Tab 切换事件
        setupTabListeners();
        
        // 状态筛选事件
        setupStatusFilterListeners();
        
        // 学校下拉选择事件
        setupSchoolSpinnerListener();
        
        Log.d(TAG, "事件监听设置完成");
    }
    
    /**
     * 设置 Tab 切换监听
     */
    private void setupTabListeners() {
        // 点击"我学的课"
        tabLearning.setOnClickListener(v -> switchTab("learning"));
        
        // 点击"收藏的课"
        tabFavorites.setOnClickListener(v -> switchTab("favorites"));
        
        // 点击"访问的课"
        tabVisited.setOnClickListener(v -> switchTab("visited"));
    }
    
    /**
     * 切换 Tab 页面
     * @param tab 目标 Tab: learning/favorites/visited
     */
    private void switchTab(String tab) {
        if (currentTab.equals(tab)) return;  // 避免重复切换
        
        Log.d(TAG, "切换到 Tab: " + tab);
        
        currentTab = tab;
        
        // 重置所有 Tab 样式为未选中状态
        resetTabStyles();
        
        // 根据当前 Tab 设置选中样式
        switch (tab) {
            case "learning":
                setTabSelected(tvTabLearning, indicatorLearning);
                showProgress = true;  // 我学的课显示进度条
                break;
                
            case "favorites":
                setTabSelected(tvTabFavorites, indicatorFavorites);
                showProgress = false;  // 收藏的课不显示进度条
                break;
                
            case "visited":
                setTabSelected(tvTabVisited, indicatorVisited);
                showProgress = false;  // 访问的课不显示进度条
                break;
        }
        
        // 仅"我学的课"显示状态筛选栏
        if ("learning".equals(tab)) {
            layoutStatusFilter.setVisibility(View.VISIBLE);
        } else {
            layoutStatusFilter.setVisibility(View.GONE);
        }
        
        // 重新加载数据
        loadCoursesData();
    }
    
    /**
     * 重置所有 Tab 为未选中样式
     */
    private void resetTabStyles() {
        // 重置文字颜色为灰色
        tvTabLearning.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvTabFavorites.setTextColor(getResources().getColor(android.R.color.darker_gray));
        tvTabVisited.setTextColor(getResources().getColor(android.R.color.darker_gray));
        
        // 隐藏所有下划线指示器
        indicatorLearning.setVisibility(View.GONE);
        indicatorFavorites.setVisibility(View.GONE);
        indicatorVisited.setVisibility(View.GONE);
    }
    
    /**
     * 设置指定 Tab 为选中样式
     */
    private void setTabSelected(TextView textView, View indicator) {
        // 文字设为主题绿色
        textView.setTextColor(getResources().getColor(R.color.primary_green));
        // 显示绿色下划线
        indicator.setVisibility(View.VISIBLE);
    }
    
    /**
     * 设置状态筛选监听
     */
    private void setupStatusFilterListeners() {
        // 点击"正在进行"
        tvStatusOngoing.setOnClickListener(v -> filterByStatus("ONGOING"));
        
        // 点击"即将开课"
        tvStatusUpcoming.setOnClickListener(v -> filterByStatus("UPCOMING"));
        
        // 点击"已结课"
        tvStatusEnded.setOnClickListener(v -> filterByStatus("ENDED"));
        
        // 筛选图标按钮
        btnFilter.setOnClickListener(v -> {
            Log.d(TAG, "点击筛选按钮");
        });
    }
    
    /**
     * 按状态筛选课程
     * @param status 状态值: ONGOING/UPCOMING/ENDED/null(全部)
     */
    private void filterByStatus(String status) {
        Log.d(TAG, "按状态筛选: " + status);
        
        currentStatusFilter = status;
        
        // 更新筛选按钮样式
        updateStatusFilterUI(status);
        
        // 重新加载并过滤数据
        loadCoursesData();
    }
    
    /**
     * 更新状态筛选 UI 样式
     */
    private void updateStatusFilterUI(String selectedStatus) {
        // 重置所有按钮样式
        resetStatusFilterStyle();
        
        // 设置选中按钮样式
        switch (selectedStatus) {
            case "ONGOING":
                setStatusFilterSelected(tvStatusOngoing);
                break;
            case "UPCOMING":
                setStatusFilterSelected(tvStatusUpcoming);
                break;
            case "ENDED":
                setStatusFilterSelected(tvStatusEnded);
                break;
        }
    }
    
    /**
     * 重置状态筛选按钮样式
     */
    private void resetStatusFilterStyle() {
        tvStatusOngoing.setBackgroundResource(R.drawable.filter_button_normal_bg);
        tvStatusOngoing.setTextColor(getResources().getColor(android.R.color.black));
        
        tvStatusUpcoming.setBackgroundResource(R.drawable.filter_button_normal_bg);
        tvStatusUpcoming.setTextColor(getResources().getColor(android.R.color.black));
        
        tvStatusEnded.setBackgroundResource(R.drawable.filter_button_normal_bg);
        tvStatusEnded.setTextColor(getResources().getColor(android.R.color.black));
    }
    
    /**
     * 设置状态筛选按钮为选中样式
     */
    private void setStatusFilterSelected(TextView textView) {
        textView.setBackgroundResource(R.drawable.filter_button_selected_bg);
        textView.setTextColor(getResources().getColor(android.R.color.white));
    }
    
    /**
     * 设置学校下拉框监听
     */
    private void setupSchoolSpinnerListener() {
        spinnerSchools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSchool = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "选择学校: " + selectedSchool);
                // TODO: 实现按学校筛选逻辑
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选择任何项，不做处理
            }
        });
    }
    
    /**
     * 从 Supabase 加载课程数据
     * 根据 currentTab 和 currentStatusFilter 获取对应数据
     */
    private void loadCoursesData() {
        Log.d(TAG, "开始加载课程数据 - Tab: " + currentTab + ", Status: " + currentStatusFilter);
        
        // 显示加载状态
        showLoading(true);
        showEmpty(false);
        
        SupabaseService.CourseCallback callback = new SupabaseService.CourseCallback() {
            @Override
            public void onSuccess(List<Course> courses) {
                runOnUiThread(() -> {
                    hideLoading();
                    
                    Log.d(TAG, "数据加载成功，共 " + courses.size() + " 门课程");
                    
                    // 如果有状态筛选条件，进行本地过滤
                    if (currentStatusFilter != null && !currentStatusFilter.isEmpty()) {
                        List<Course> filteredList = new ArrayList<>();
                        for (Course course : courses) {
                            if (currentStatusFilter.equals(course.getStatus())) {
                                filteredList.add(course);
                            }
                        }
                        courseList = filteredList;
                    } else {
                        courseList = courses;
                    }
                    
                    // 检查是否有数据
                    if (courseList.isEmpty()) {
                        showEmpty(true);
                        tvEmptyHint.setText("暂无相关课程");
                    } else {
                        showEmpty(false);
                        
                        // 创建或更新适配器
                        adapter = new CourseAdapter(MyCoursesActivity.this, courseList, showProgress);
                        
                        // 设置点击事件
                        adapter.setOnItemClickListener((course, position) -> {
                            Log.d(TAG, "点击课程: " + course.getTitle());
                            // TODO: 跳转到课程详情页面
                        });
                        
                        rvCourses.setAdapter(adapter);
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    hideLoading();
                    showEmpty(true);
                    tvEmptyHint.setText("加载失败，请检查网络连接");
                    Log.e(TAG, "数据加载失败: " + errorMessage);
                });
            }
        };
        
        // 根据 Tab 类型调用不同的 API
        switch (currentTab) {
            case "learning":
                SupabaseService.getEnrolledCourses(TEST_USER_ID, callback);
                break;
                
            case "favorites":
                SupabaseService.getFavoriteCourses(TEST_USER_ID, callback);
                break;
                
            case "visited":
                SupabaseService.getVisitedCourses(TEST_USER_ID, callback);
                break;
                
            default:
                Log.w(TAG, "未知 Tab 类型: " + currentTab);
                callback.onError("未知页面类型");
        }
    }
    
    /**
     * 显示/隐藏加载进度条
     */
    private void showLoading(boolean show) {
        progressBarLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    
    /**
     * 隐藏加载状态
     */
    private void hideLoading() {
        showLoading(false);
    }
    
    /**
     * 显示/隐藏空状态提示
     */
    private void showEmpty(boolean show) {
        layoutEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        rvCourses.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
