package com.xiaoya.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 课程列表适配器
 * 
 * 功能：
 * 1. 将课程数据绑定到 RecyclerView 的每个 item
 * 2. 使用 Picasso 加载封面图片
 * 3. 处理点击事件（跳转到课程详情）
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    
    private static final String TAG = "CourseAdapter";
    
    private Context context;
    private List<Course> courseList;
    private boolean showProgress;  // 是否显示进度条（仅"我学的课"显示）
    private OnItemClickListener onItemClickListener;

    /**
     * 点击事件监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(Course course, int position);
    }

    public CourseAdapter(Context context, List<Course> courseList, boolean showProgress) {
        this.context = context;
        this.courseList = courseList;
        this.showProgress = showProgress;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        
        try {
            holder.bind(course, showProgress, position);
            
            // 设置点击事件
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> {
                    Log.d(TAG, "点击课程: " + course.getTitle());
                    onItemClickListener.onItemClick(course, position);
                });
            }
            
        } catch (Exception e) {
            Log.e(TAG, "绑定数据失败: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * 更新数据列表
     */
    public void updateData(List<Course> newList) {
        this.courseList = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder 内部类
     * 缓存视图引用，避免重复 findViewById
     */
    static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;          // 封面图片
        TextView tvTitle;           // 课程标题
        TextView tvSchool;          // 学院名称
        TextView tvTeacher;         // 教师姓名
        TextView tvVisitInfo;       // 访问量信息标签
        TextView tvVisitCount;      // 访问人数
        LinearLayout layoutProgress; // 进度条容器
        ProgressBar progressBar;    // 进度条
        TextView tvProgressText;    // 进度文本

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // 初始化所有视图组件
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSchool = itemView.findViewById(R.id.tv_school);
            tvTeacher = itemView.findViewById(R.id.tv_teacher);
            tvVisitInfo = itemView.findViewById(R.id.tv_visit_info);
            tvVisitCount = itemView.findViewById(R.id.tv_visit_count);
            layoutProgress = itemView.findViewById(R.id.layout_progress);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvProgressText = itemView.findViewById(R.id.tv_progress_text);
        }

        /**
         * 根据位置获取对应的封面占位图资源
         * 为不同课程分配不同的美观渐变背景
         */
        private int getPlaceholderForPosition(int position) {
            switch (position % 3) {
                case 0:
                    return R.drawable.course_cover_1;  // 蓝紫色渐变
                case 1:
                    return R.drawable.course_cover_2;  // 橙色渐变
                case 2:
                    return R.drawable.course_cover_3;  // 青蓝色渐变
                default:
                    return R.drawable.course_cover_placeholder;  // 默认绿色渐变
            }
        }

        /**
         * 绑定数据到视图
         */
        void bind(Course course, boolean showProgress, int position) {
            // 直接使用本地美观渐变封面，不再加载网络图片
            // 为不同课程分配不同的美观渐变封面背景
            int coverRes = getPlaceholderForPosition(position);
            ivCover.setImageResource(coverRes);
            
            // 设置课程标题
            tvTitle.setText(course.getTitle());
            
            // 设置学院信息
            tvSchool.setText("学院: " + course.getSchoolName());
            
            // 设置教师姓名
            tvTeacher.setText(course.getTeacherName());
            
            // 设置访问量信息
            String schoolShortName = course.getSchoolName().length() > 2 
                    ? course.getSchoolName().substring(0, 2) + "..." 
                    : course.getSchoolName();
            tvVisitInfo.setText("访问量:" + schoolShortName);
            tvVisitCount.setText(course.getVisitCount() + "人");
            
            // 根据是否显示进度条来控制可见性
            if (showProgress) {
                layoutProgress.setVisibility(View.VISIBLE);
                progressBar.setProgress(course.getProgress());
                tvProgressText.setText(course.getProgress() + "%");
            } else {
                layoutProgress.setVisibility(View.GONE);
            }
        }
    }
}
