package com.xiaoya.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * AI对话界面Activity
 * 功能：
 * 1. 显示与DeepSeek AI的聊天记录
 * 2. 支持用户输入消息并发送
 * 3. 显示AI的回复（带打字机效果）
 * 4. 支持清空对话历史
 * 5. 快捷问题标签功能
 */
public class ChatActivity extends AppCompatActivity {

    // UI组件
    private ImageButton btnBack;           // 返回按钮
    private TextView btnClear;             // 清空对话按钮
    private ScrollView scrollViewMessages; // 消息滚动区域
    private LinearLayout layoutMessages;   // 消息容器
    private EditText editMessage;          // 输入框
    private ImageButton btnSend;           // 发送按钮

    // 快捷标签
    private TextView tagCommon, tagStudy, tagHomework, tagCourse;

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
    }

    /**
     * 初始化UI组件引用
     */
    private void initViews() {
        // 获取UI组件引用
        btnBack = findViewById(R.id.btn_back);
        btnClear = findViewById(R.id.btn_clear);
        scrollViewMessages = findViewById(R.id.scroll_view_messages);
        layoutMessages = findViewById(R.id.layout_messages);
        editMessage = findViewById(R.id.edit_message);
        btnSend = findViewById(R.id.btn_send);

        // 获取快捷标签引用
        tagCommon = findViewById(R.id.tag_common);
        tagStudy = findViewById(R.id.tag_study);
        tagHomework = findViewById(R.id.tag_homework);
        tagCourse = findViewById(R.id.tag_course);
    }

    /**
     * 设置所有事件监听器
     */
    private void setupListeners() {
        // 返回按钮 - 关闭当前页面
        btnBack.setOnClickListener(v -> finish());

        // 清空按钮 - 清空对话历史
        btnClear.setOnClickListener(v -> clearConversation());

        // 发送按钮 - 发送用户消息
        btnSend.setOnClickListener(v -> sendMessage());

        // 输入框回车键监听（支持回车发送）
        editMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });

        // 快捷标签点击事件
        setupTagListeners();
    }

    /**
     * 设置快捷标签点击事件
     */
    private void setupTagListeners() {
        View.OnClickListener tagClickListener = v -> {
            // 获取标签文本作为快捷问题
            String question = ((TextView) v).getText().toString();

            // 根据标签设置预设问题
            String presetQuestion = getPresetQuestion(question);

            if (presetQuestion != null) {
                editMessage.setText(presetQuestion);
                sendMessage();  // 自动发送
            }
        };

        tagCommon.setOnClickListener(tagClickListener);
        tagStudy.setOnClickListener(tagClickListener);
        tagHomework.setOnClickListener(tagClickListener);
        tagCourse.setOnClickListener(tagClickListener);
    }

    /**
     * 根据标签获取预设问题
     *
     * @param tag 标签名称
     * @return 预设问题文本
     */
    private String getPresetQuestion(String tag) {
        switch (tag) {
            case "常见问题":
                return "你能够帮我做什么？";
            case "学习辅导":
                return "请帮我讲解一下高等数学中的导数概念";
            case "作业帮助":
                return "我需要帮助完成英语作业，能给我一些写作建议吗？";
            case "课程咨询":
                return "如何选择适合自己的课程？";
            default:
                return null;
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

        // 添加用户消息到界面
        addUserMessage(message);

        // 清空输入框
        editMessage.setText("");

        // 隐藏键盘
        hideKeyboard();

        // 设置等待状态
        isWaitingForResponse = true;
        updateSendButtonState(false);  // 禁用发送按钮

        // 调用DeepSeek API发送消息
        deepSeekService.sendMessage(message, new DeepSeekService.OnResponseListener() {
            @Override
            public void onSuccess(String response) {
                // 成功：显示AI回复
                runOnUiThread(() -> {
                    addAssistantMessage(response);
                    isWaitingForResponse = false;
                    updateSendButtonState(true);  // 恢复发送按钮
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                // 失败：显示错误信息
                runOnUiThread(() -> {
                    addErrorMessage(errorMessage);
                    isWaitingForResponse = false;
                    updateSendButtonState(true);  // 恢复发送按钮
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

        // 创建内部布局（水平方向）
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setPadding(12, 12, 12, 12);

        // 用户头像
        ImageView userAvatar = new ImageView(this);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(40, 40);
        userAvatar.setLayoutParams(avatarParams);
        userAvatar.setImageResource(android.R.drawable.ic_menu_myplaces);
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
        LinearLayout.LayoutParams avatarParams2 = new LinearLayout.LayoutParams(40, 40);
        aiAvatar.setLayoutParams(avatarParams2);
        aiAvatar.setImageResource(android.R.drawable.ic_dialog_info);
        aiAvatar.setBackgroundResource(R.drawable.ai_avatar_bg);
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
     * 清空对话历史
     */
    private void clearConversation() {
        // 清空服务端的对话历史
        deepSeekService.clearConversation();

        // 清空界面上的所有消息（保留欢迎消息）
        int childCount = layoutMessages.getChildCount();
        if (childCount > 1) {
            layoutMessages.removeViews(1, childCount - 1);  // 移除除欢迎卡片外的所有消息
        }
    }

    /**
     * 更新发送按钮状态
     *
     * @param enabled 是否可用
     */
    private void updateSendButtonState(boolean enabled) {
        btnSend.setEnabled(enabled);
        btnSend.setAlpha(enabled ? 1.0f : 0.5f);
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
