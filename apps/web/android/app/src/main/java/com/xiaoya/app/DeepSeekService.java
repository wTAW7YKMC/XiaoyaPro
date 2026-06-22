package com.xiaoya.app;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * DeepSeek API服务类
 * 功能：
 * 1. 封装DeepSeek大模型API的调用逻辑
 * 2. 支持多轮对话（维护上下文）
 * 3. 异步请求，不阻塞主线程
 * 4. 提供回调接口处理响应
 */
public class DeepSeekService {

    // DeepSeek API地址（使用官方API端点）
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    // API密钥（用户提供）
    private static final String API_KEY = "sk-378fb243297e475cb55c4fac1e77c866";

    // HTTP客户端实例（配置超时时间）
    private OkHttpClient client;
    // JSON解析器
    private Gson gson;
    // 主线程Handler，用于回调到UI线程
    private Handler mainHandler;

    // 对话历史记录（用于多轮对话上下文）
    private List<ChatMessage> conversationHistory;

    /**
     * 聊天消息数据模型
     */
    public static class ChatMessage {
        public String role;      // 角色：system/user/assistant
        public String content;   // 消息内容

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    /**
     * API响应回调接口
     */
    public interface OnResponseListener {
        void onSuccess(String response);      // 成功回调
        void onFailure(String errorMessage);  // 失败回调
    }

    /**
     * 构造函数
     * 初始化HTTP客户端、JSON解析器和对话历史
     */
    public DeepSeekService() {
        // 配置OkHttp客户端（设置连接和读写超时）
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)   // 连接超时30秒
                .readTimeout(60, TimeUnit.SECONDS)       // 读取超时60秒（AI生成可能较慢）
                .writeTimeout(30, TimeUnit.SECONDS)      // 写入超时30秒
                .build();

        // 初始化Gson解析器
        gson = new Gson();

        // 获取主线程Handler
        mainHandler = new Handler(Looper.getMainLooper());

        // 初始化对话历史列表
        conversationHistory = new ArrayList<>();

        // 添加系统提示词（定义AI助手的角色和行为）
        addSystemMessage();
    }

    /**
     * 添加系统提示词
     * 设置AI助手为"小雅智能助手"的教育类AI助手角色
     */
    private void addSystemMessage() {
        String systemPrompt = "你是小雅智能助手，一款教育类AI应用的核心AI助手。你的主要功能包括：\n" +
                "1. 智能问答 - 回答学生关于学习的问题\n" +
                "2. 作业辅导 - 帮助学生理解和完成作业\n" +
                "3. 学习建议 - 提供个性化学习建议\n" +
                "4. 知识解答 - 解答各学科知识点\n\n" +
                "请用友好、专业、耐心的语气回复学生。如果问题与教育无关，请礼貌地引导学生回到学习话题。";

        conversationHistory.add(new ChatMessage("system", systemPrompt));
    }

    /**
     * 发送消息给DeepSeek API
     *
     * @param userMessage 用户输入的消息
     * @param listener    响应回调监听器
     */
    public void sendMessage(final String userMessage, final OnResponseListener listener) {
        // 将用户消息添加到对话历史
        conversationHistory.add(new ChatMessage("user", userMessage));

        // 构建请求体JSON
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "deepseek-chat");  // 使用DeepSeek聊天模型

        // 将对话历史转换为JSON数组
        JsonArray messagesArray = new JsonArray();
        for (ChatMessage msg : conversationHistory) {
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("role", msg.role);
            messageObj.addProperty("content", msg.content);
            messagesArray.add(messageObj);
        }
        requestBody.add("messages", messagesArray);

        // 其他参数配置
        requestBody.addProperty("temperature", 0.7);      // 温度参数（0-1，越高越随机）
        requestBody.addProperty("max_tokens", 2000);      // 最大生成token数

        // 创建HTTP请求
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                gson.toJson(requestBody)
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)  // API密钥认证
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // 异步发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败回调
                mainHandler.post(() -> listener.onFailure("网络错误: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        // HTTP请求失败（非200状态码）
                        String errorBody = response.body() != null ? response.body().string() : "未知错误";
                        mainHandler.post(() -> listener.onFailure("API错误: " + response.code()));
                        return;
                    }

                    // 解析成功响应
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

                    // 提取AI回复内容
                    if (jsonResponse.has("choices")) {
                        JsonArray choices = jsonResponse.getAsJsonArray("choices");
                        if (choices.size() > 0) {
                            JsonObject choice = choices.get(0).getAsJsonObject();
                            String assistantMessage = choice.getAsJsonObject("message").get("content").getAsString();

                            // 将AI回复添加到对话历史
                            conversationHistory.add(new ChatMessage("assistant", assistantMessage));

                            // 回调成功结果到UI线程
                            mainHandler.post(() -> listener.onSuccess(assistantMessage));
                        } else {
                            mainHandler.post(() -> listener.onFailure("未收到有效回复"));
                        }
                    } else {
                        mainHandler.post(() -> listener.onFailure("API返回格式异常"));
                    }
                } catch (Exception e) {
                    // JSON解析异常
                    mainHandler.post(() -> listener.onFailure("数据解析错误: " + e.getMessage()));
                } finally {
                    // 关闭响应体
                    response.close();
                }
            }
        });
    }

    /**
     * 清空对话历史
     * 用于开始新的对话会话
     */
    public void clearConversation() {
        conversationHistory.clear();
        addSystemMessage();  // 重新添加系统提示词
    }

    /**
     * 获取当前对话历史长度
     */
    public int getConversationLength() {
        return conversationHistory.size();
    }
}
