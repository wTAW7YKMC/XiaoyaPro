package com.xiaoya.app;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;

/**
 * 系统帮助 / AI对话界面Activity
 * 功能：
 * 1. 显示系统帮助欢迎页面（企鹅头像 + 常见问题 + 启动网页会话入口）
 * 2. 支持用户点击常见问题自动提问
 * 3. 支持用户输入消息并发送
 * 4. 显示AI的回复
 */
public class ChatActivity extends AppCompatActivity {

    // UI组件
    private ImageButton btnBack;           // 返回按钮
    private ScrollView scrollViewMessages; // 消息滚动区域
    private LinearLayout layoutMessages;   // 消息容器
    private EditText editMessage;          // 输入框

    // 顶部欢迎区相关视图
    private TextView tvChatTime;           // 聊天时间
    private LinearLayout btnWebSessionTop; // 欢迎卡片里的启动网页会话按钮

    // 常见问题卡片
    private LinearLayout cardFaq;          // 常见问题卡片容器
    private TextView tagCommon, tagTeachingActivity, tagCourseManage, tagTeaching;
    private TextView faqItem1, faqItem2, faqItem3, faqItem4, faqItem5, faqItem6;
    private TextView btnViewMore;          // 查看更多

    // 底部输入区
    private LinearLayout btnWebSessionBottom; // 底部启动网页会话按钮
    private LinearLayout rootLayout;            // 根布局，用于键盘弹出时手动补偿padding

    // DeepSeek API服务实例
    private DeepSeekService deepSeekService;

    // 是否正在等待AI回复（防止重复发送）
    private boolean isWaitingForResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 初始化UI组件
        initViews();

        // 初始化DeepSeek服务
        deepSeekService = new DeepSeekService();

        // 设置事件监听器
        setupListeners();

        // 监听键盘弹出，自动滚动到底部确保输入框可见
        setupKeyboardListener();
    }

    /**
     * 初始化UI组件引用
     */
    private void initViews() {
        // 顶部标题栏和消息区
        btnBack = findViewById(R.id.btn_back);
        scrollViewMessages = findViewById(R.id.scroll_view_messages);
        layoutMessages = findViewById(R.id.layout_messages);
        editMessage = findViewById(R.id.edit_message);

        // 欢迎区
        tvChatTime = findViewById(R.id.tv_chat_time);
        btnWebSessionTop = findViewById(R.id.btn_web_session_top);

        // 常见问题卡片
        cardFaq = findViewById(R.id.card_faq);
        tagCommon = findViewById(R.id.tag_common);
        tagTeachingActivity = findViewById(R.id.tag_teaching_activity);
        tagCourseManage = findViewById(R.id.tag_course_manage);
        tagTeaching = findViewById(R.id.tag_teaching);
        faqItem1 = findViewById(R.id.faq_item_1);
        faqItem2 = findViewById(R.id.faq_item_2);
        faqItem3 = findViewById(R.id.faq_item_3);
        faqItem4 = findViewById(R.id.faq_item_4);
        faqItem5 = findViewById(R.id.faq_item_5);
        faqItem6 = findViewById(R.id.faq_item_6);
        btnViewMore = findViewById(R.id.btn_view_more);

        // 底部输入区
        btnWebSessionBottom = findViewById(R.id.btn_web_session_bottom);
        rootLayout = findViewById(R.id.root_layout);

        // 设置当前时间
        tvChatTime.setText(DateFormat.format("HH:mm", Calendar.getInstance()));
    }

    /**
     * 设置所有事件监听器
     */
    private void setupListeners() {
        // 返回按钮 - 关闭当前页面
        btnBack.setOnClickListener(v -> finish());

        // 输入框回车键监听（支持回车发送）
        editMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });

        // 启动网页会话按钮（顶部和底部功能一致）
        View.OnClickListener webSessionListener = v -> {
            editMessage.setText("转接人工客服");
            sendMessage();
        };
        btnWebSessionTop.setOnClickListener(webSessionListener);
        btnWebSessionBottom.setOnClickListener(webSessionListener);

        // 标签切换
        setupTagListeners();

        // 常见问题点击
        setupFaqListeners();

        // 查看更多
        btnViewMore.setOnClickListener(v -> {
            editMessage.setText("查看更多常见问题");
            sendMessage();
        });
    }

    /**
     * 监听软键盘弹出/收起，自动滚动消息列表到底部，确保输入框始终可见
     * 当 adjustResize 未生效时，手动给根布局设置 paddingBottom 补偿键盘高度
     */
    private void setupKeyboardListener() {
        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect r = new Rect();
            private int originalScreenHeight = 0;

            @Override
            public void onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getHeight();
                int keyboardHeight = screenHeight - r.bottom;

                if (keyboardHeight == 0) {
                    // 键盘收起，记录原始屏幕高度并恢复padding
                    originalScreenHeight = screenHeight;
                    rootLayout.setPadding(0, 0, 0, 0);
                } else {
                    // 键盘弹出
                    if (screenHeight == originalScreenHeight) {
                        // adjustResize 未生效，手动补偿 paddingBottom 将输入框顶到键盘上方
                        rootLayout.setPadding(0, 0, 0, keyboardHeight);
                    }
                    scrollToBottom();
                }
            }
        });
    }

    /**
     * 设置标签切换事件
     */
    private void setupTagListeners() {
        // 当前所有标签统一切换到未选中样式
        View.OnClickListener tagClickListener = v -> {
            resetTagStyles();
            TextView selectedTag = (TextView) v;
            selectedTag.setBackgroundResource(R.drawable.tag_help_selected_bg);
            selectedTag.setTextColor(getResources().getColor(R.color.blue_primary));

            // 根据标签刷新常见问题列表
            updateFaqList(selectedTag.getText().toString());
        };

        tagCommon.setOnClickListener(tagClickListener);
        tagTeachingActivity.setOnClickListener(tagClickListener);
        tagCourseManage.setOnClickListener(tagClickListener);
        tagTeaching.setOnClickListener(tagClickListener);
    }

    /**
     * 重置所有标签为未选中状态
     */
    private void resetTagStyles() {
        TextView[] tags = {tagCommon, tagTeachingActivity, tagCourseManage, tagTeaching};
        for (TextView tag : tags) {
            tag.setBackgroundResource(R.drawable.tag_help_unselected_bg);
            tag.setTextColor(getResources().getColor(R.color.gray_text));
        }
    }

    /**
     * 根据选中标签更新常见问题列表
     *
     * @param tag 标签名称
     */
    private void updateFaqList(String tag) {
        switch (tag) {
            case "教学活动":
                faqItem1.setText("如何发布课堂活动");
                faqItem2.setText("如何管理活动参与名单");
                faqItem3.setText("活动数据如何导出？");
                faqItem4.setText("如何设置活动开放时间");
                faqItem5.setText("如何复制已有活动");
                faqItem6.setText("活动提醒如何开启？");
                break;
            case "课程管理":
                faqItem1.setText("如何创建新课程");
                faqItem2.setText("如何邀请学生加入");
                faqItem3.setText("课程设置在哪里修改？");
                faqItem4.setText("如何删除已归档课程");
                faqItem5.setText("课程资料如何批量上传");
                faqItem6.setText("如何设置课程助教？");
                break;
            case "教学":
                faqItem1.setText("如何创建教学计划");
                faqItem2.setText("教学进度如何查看");
                faqItem3.setText("如何发布教学通知？");
                faqItem4.setText("学生成绩如何录入");
                faqItem5.setText("如何生成教学报告");
                faqItem6.setText("教学互动如何开展？");
                break;
            default: // 常见问题
                faqItem1.setText("如何开启签到");
                faqItem2.setText("如何更改签到状态");
                faqItem3.setText("如何延长任务截止时间？");
                faqItem4.setText("如何设置总评成绩计分项");
                faqItem5.setText("如何开启随机选人/选组");
                faqItem6.setText("如何发布任务？");
                break;
        }
    }

    /**
     * 设置常见问题点击事件
     */
    private void setupFaqListeners() {
        TextView[] faqItems = {faqItem1, faqItem2, faqItem3, faqItem4, faqItem5, faqItem6};
        View.OnClickListener faqClickListener = v -> {
            String question = ((TextView) v).getText().toString();
            editMessage.setText(question);
            sendMessage();
        };

        for (TextView item : faqItems) {
            item.setOnClickListener(faqClickListener);
        }
    }

    /**
     * 发送用户消息
     */
    private void sendMessage() {
        // 获取输入内容
        String message = editMessage.getText().toString().trim();

        // 验证输入不为空
        if (TextUtils.isEmpty(message)) {
            return;
        }

        // 如果正在等待AI回复，不重复发送
        if (isWaitingForResponse) {
            return;
        }

        // 首次发送消息时隐藏常见问题卡片，保留时间、欢迎卡片和聊天记录
        if (cardFaq.getVisibility() == View.VISIBLE) {
            cardFaq.setVisibility(View.GONE);
        }

        // 添加用户消息到界面
        addUserMessage(message);

        // 清空输入框
        editMessage.setText("");

        // 隐藏键盘
        hideKeyboard();

        // 设置等待状态
        isWaitingForResponse = true;

        // 调用DeepSeek API发送消息
        deepSeekService.sendMessage(message, new DeepSeekService.OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                // 成功：显示AI回复
                runOnUiThread(() -> {
                    addAssistantMessage(response);
                    isWaitingForResponse = false;
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // 失败：显示错误信息
                runOnUiThread(() -> {
                    addErrorMessage(errorMessage);
                    isWaitingForResponse = false;
                });
            }
        });
    }

    /**
     * 添加用户消息气泡到界面
     *
     * @param message 用户消息内容
     */
    private void addUserMessage(String message) {
        // 创建消息卡片容器
        CardView messageCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 8, 0, 8);
        messageCard.setLayoutParams(cardParams);
        messageCard.setRadius(16f);
        messageCard.setCardElevation(1f);
        messageCard.setUseCompatPadding(true);
        messageCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));

        // 创建内部布局（水平方向）
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setPadding(12, 12, 12, 12);

        // 用户头像
        ImageView userAvatar = new ImageView(this);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(40, 40);
        userAvatar.setLayoutParams(avatarParams);
        userAvatar.setImageResource(R.drawable.ic_customer_service);
        userAvatar.setBackgroundResource(R.drawable.user_avatar_bg);
        userAvatar.setPadding(8, 8, 8, 8);

        // 用户消息文字
        TextView messageText = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        textParams.setMarginStart(12);
        messageText.setLayoutParams(textParams);
        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        messageText.setTextSize(15);
        messageText.setLineSpacing(6f, 1f);

        // 组装布局
        innerLayout.addView(userAvatar);
        innerLayout.addView(messageText);
        messageCard.addView(innerLayout);
        layoutMessages.addView(messageCard);

        // 滚动到底部
        scrollToBottom();
    }

    /**
     * 添加AI助手回复消息到界面
     *
     * @param message AI回复内容
     */
    private void addAssistantMessage(String message) {
        // 创建消息卡片容器
        CardView messageCard = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 8, 0, 8);
        messageCard.setLayoutParams(cardParams);
        messageCard.setRadius(16f);
        messageCard.setCardElevation(1f);
        messageCard.setUseCompatPadding(true);
        messageCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));

        // 创建内部布局（水平方向）
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setPadding(12, 12, 12, 12);

        // AI头像
        ImageView aiAvatar = new ImageView(this);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(40, 40);
        aiAvatar.setLayoutParams(avatarParams);
        aiAvatar.setImageResource(R.drawable.ic_penguin);
        aiAvatar.setBackgroundResource(R.drawable.floating_ai_button_bg);
        aiAvatar.setPadding(8, 8, 8, 8);

        // AI回复文字
        TextView messageText = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        textParams.setMarginStart(12);
        messageText.setLayoutParams(textParams);
        messageText.setText(message);
        messageText.setTextColor(getResources().getColor(android.R.color.black));
        messageText.setTextSize(15);
        messageText.setLineSpacing(6f, 1f);

        // 组装布局
        innerLayout.addView(aiAvatar);
        innerLayout.addView(messageText);
        messageCard.addView(innerLayout);
        layoutMessages.addView(messageCard);

        // 滚动到底部
        scrollToBottom();
    }

    /**
     * 添加错误消息提示
     *
     * @param error 错误信息
     */
    private void addErrorMessage(String error) {
        // 创建错误提示卡片
        TextView errorText = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        errorText.setLayoutParams(params);
        errorText.setText("⚠️ " + error + "\n请检查网络连接后重试");
        errorText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        errorText.setTextSize(13);
        errorText.setPadding(16, 12, 16, 12);

        layoutMessages.addView(errorText);
        scrollToBottom();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 滚动消息列表到底部
     */
    private void scrollToBottom() {
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity销毁时释放资源
        if (deepSeekService != null) {
            deepSeekService.clearConversation();
        }
    }
}
