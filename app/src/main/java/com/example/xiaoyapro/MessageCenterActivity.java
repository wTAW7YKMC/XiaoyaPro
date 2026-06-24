package com.example.xiaoyapro;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * 消息中心页面（截图4）
 * 功能：展示7类消息分类，用户点击进入对应详情页
 * UI特点：
 * - 顶部白色标题栏："消息中心" + 更多按钮（三个点）
 * - 7个白色圆角卡片列表项：
 *   1. 待办事项（蓝色背景 + 日历图标）
 *   2. 课程消息（绿色背景 + 消息图标，**带未读红点**）
 *   3. 个人消息（橙色背景 + 用户图标）
 *   4. 系统消息（蓝色背景 + 设置图标）
 *   5. 群聊（绿色背景 + 对话图标）
 *   6. 私信（橙色背景 + 邮件图标）
 *   7. 通讯录（青色背景 + 联系人图标）
 */
public class MessageCenterActivity extends AppCompatActivity {

    // 消息分类数据（与Web端保持一致）
    private final int[][] messageCategories = {
            // {图标资源(Emoji), 名称, 背景颜色}
            {R.string.icon_todo, R.string.label_todo, R.color.color_secondary},          // 待办事项 - 蓝色
            {R.string.icon_course_msg, R.string.label_course_msg, R.color.color_primary}, // 课程消息 - 青绿色
            {R.string.icon_personal_msg, R.string.label_personal_msg, R.color.color_accent_orange}, // 个人消息 - 橙色
            {R.string.icon_system_msg, R.string.label_system_msg, R.color.color_secondary},         // 系统消息 - 蓝色
            {R.string.icon_group_chat, R.string.label_group_chat, R.color.color_primary},             // 群聊 - 青绿色
            {R.string.icon_private_msg, R.string.label_private_msg, R.color.color_accent_orange},     // 私信 - 橙色
            {R.string.icon_contacts, R.string.label_contacts, R.color.color_accent_cyan}              // 通讯录 - 青色
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);

        initMessageCategories();
        setupClickListeners();
    }

    /**
     * 初始化所有消息分类项的数据和样式
     */
    private void initMessageCategories() {
        // 获取7个分类项的CardView引用
        int[] itemIds = {
                R.id.item_todo,
                R.id.item_course_msg,
                R.id.item_personal_msg,
                R.id.item_system_msg,
                R.id.item_group_chat,
                R.id.item_private_msg,
                R.id.item_contacts
        };

        for (int i = 0; i < itemIds.length; i++) {
            CardView cardView = findViewById(itemIds[i]);
            if (cardView != null) {
                // 获取内部组件
                TextView tvIcon = cardView.findViewById(R.id.tv_icon);
                TextView tvName = cardView.findViewById(R.id.tv_category_name);
                View iconBg = cardView.findViewById(R.id.icon_background);

                if (tvIcon != null && tvName != null && iconBg != null) {
                    // 设置图标
                    String iconStr = getString(messageCategories[i][0]);
                    tvIcon.setText(iconStr);

                    // 设置名称
                    String nameStr = getString(messageCategories[i][1]);
                    tvName.setText(nameStr);

                    // 设置圆角方形背景色
                    int bgColor = getResources().getColor(messageCategories[i][2], getTheme());
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
                        handleCategoryClick(position);
                    }
                });
            }
        }

        // TODO: 实际项目中应该根据API数据动态显示/隐藏红点
        // 这里默认显示课程消息的红点（模拟有未读消息）
        // View redDotCourse = findViewById(R.id.red_dot_course);
        // redDotCourse.setVisibility(View.VISIBLE); // 默认已显示
    }

    /**
     * 处理消息分类点击事件
     * @param position 点击的分类位置（0-6）
     */
    private void handleCategoryClick(int position) {
        switch (position) {
            case 0: // 待办事项
                showToast("待办事项功能开发中");
                break;
            case 1: // 课程消息
                showToast("课程消息功能开发中");
                break;
            case 2: // 个人消息
                showToast("个人消息功能开发中");
                break;
            case 3: // 系统消息
                showToast("系统消息功能开发中");
                break;
            case 4: // 群聊
                showToast("群聊功能开发中");
                break;
            case 5: // 私信
                showToast("私信功能开发中");
                break;
            case 6: // 通讯录
                showToast("通讯录功能开发中");
                break;
            default:
                break;
        }
    }

    /**
     * 设置其他点击事件监听器
     */
    private void setupClickListeners() {
        // 更多按钮（三个点）- 预留功能，如消息设置、清空未读等
        TextView btnMore = findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 实现更多选项菜单（如：全部标为已读、消息设置等）
                showToast("更多功能开发中");
            }
        });
    }

    /**
     * 显示Toast提示
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
