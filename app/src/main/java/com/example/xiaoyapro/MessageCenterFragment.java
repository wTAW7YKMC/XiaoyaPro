package com.example.xiaoyapro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 消息中心Fragment
 * 用于在MainActivity的TabBar中显示消息中心内容
 * 加载activity_message_center.xml布局文件
 */
public class MessageCenterFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载消息中心布局文件
        rootView = inflater.inflate(R.layout.activity_message_center, container, false);

        // 初始化消息分类列表
        initMessageCategories();

        return rootView;
    }

    /**
     * 初始化消息分类项（复用MessageCenterActivity的逻辑）
     */
    private void initMessageCategories() {
        if (rootView == null) return;

        // TODO: 初始化7个消息分类项的数据和样式
        // （与MessageCenterActivity中的initMessageCategories()方法逻辑相同）
        // 这里简化处理，实际项目中应该完整实现

        // 设置更多按钮点击事件
        android.widget.TextView btnMore = rootView.findViewById(R.id.btn_more);
        if (btnMore != null) {
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.widget.Toast.makeText(getContext(), "更多功能开发中", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
