package com.xiaoya.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Supabase 数据库服务类
 * 
 * 功能：
 * 1. 通过 REST API 连接 Supabase 云端数据库
 * 2. 获取"我学的课"、"收藏的课"、"访问的课"数据
 * 3. 支持状态筛选（进行中/即将开课/已结课）
 */
public class SupabaseService {
    
    private static final String TAG = "SupabaseService";
    
    // Supabase 配置信息
    private static final String SUPABASE_URL = "https://qhtzpeqeuztyykfbsazi.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFodHpwZXFldXp0eXlrZmJzYXppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODIxMDUzOTMsImV4cCI6MjA5NzY4MTM5M30.7ekv6Ov4U9OxxluSCFNfe3HTWhy5sdunmry9P17w2rE";
    
    // HTTP 客户端
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
    /**
     * 获取我学的课程列表
     */
    public static void getEnrolledCourses(String userId, CourseCallback callback) {
        try {
            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/rest/v1/enrollments")
                    .get()
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                    .addHeader("Prefer", "return=representation")
                    .addHeader("Select", String.format(
                            "course_id,progress,courses(id,title,cover,status,semester,type,visit_count,teacher_id,school_id," +
                            "teacher:users!courses_teacher_id_fkey(id,name),school:schools(id,name))"))
                    .build();
            
            executeRequest(request, callback);
            
        } catch (Exception e) {
            Log.e(TAG, "构建请求失败: ", e);
            callback.onError("请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取收藏的课程列表
     */
    public static void getFavoriteCourses(String userId, CourseCallback callback) {
        try {
            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/rest/v1/user_course_favorites")
                    .get()
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                    .addHeader("Prefer", "return=representation")
                    .addHeader("Select", String.format(
                            "course_id,courses(id,title,cover,status,semester,type,visit_count,teacher_id,school_id," +
                            "teacher:users!courses_teacher_id_fkey(id,name),school:schools(id,name))"))
                    .build();
            
            executeRequest(request, callback);
            
        } catch (Exception e) {
            Log.e(TAG, "构建请求失败: ", e);
            callback.onError("请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取访问过的课程列表
     */
    public static void getVisitedCourses(String userId, CourseCallback callback) {
        try {
            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/rest/v1/course_visits?user_id=eq." + userId + "&order=last_visited_at.desc")
                    .get()
                    .addHeader("apikey", SUPABASE_ANON_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                    .addHeader("Prefer", "return=representation")
                    .addHeader("Select", String.format(
                            "visit_count,last_visited_at,courses(id,title,cover,status,semester,type,visit_count,teacher_id,school_id," +
                            "teacher:users!courses_teacher_id_fkey(id,name),school:schools(id,name))"))
                    .build();
            
            executeRequest(request, callback);
            
        } catch (Exception e) {
            Log.e(TAG, "构建请求失败: ", e);
            callback.onError("请求失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行 HTTP 请求
     */
    private static void executeRequest(Request request, final CourseCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "网络请求失败: ", e);
                callback.onError("网络错误: " + e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseBody = response.body().string();
                    
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "HTTP 错误: " + response.code());
                        callback.onError("服务器错误: " + response.code());
                        return;
                    }
                    
                    JSONArray jsonArray = new JSONArray(responseBody);
                    List<Course> courseList = parseCourseList(jsonArray);
                    
                    Log.d(TAG, "成功获取 " + courseList.size() + " 门课程");
                    callback.onSuccess(courseList);
                    
                } catch (Exception e) {
                    Log.e(TAG, "解析响应失败: ", e);
                    callback.onError("数据解析失败: " + e.getMessage());
                }
            }
        });
    }
    
    /**
     * 解析课程列表
     */
    private static List<Course> parseCourseList(JSONArray jsonArray) throws Exception {
        List<Course> courseList = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            Course course = new Course();
            
            if (item.has("courses")) {
                JSONObject courseObj = item.getJSONObject("courses");
                parseCourseObject(course, courseObj);
                
                if (item.has("progress")) {
                    course.setProgress(item.getInt("progress"));
                }
            } else {
                parseCourseObject(course, item);
            }
            
            courseList.add(course);
        }
        
        return courseList;
    }
    
    /**
     * 解析单个课程对象
     */
    private static void parseCourseObject(Course course, JSONObject obj) throws Exception {
        course.setId(obj.optString("id"));
        course.setTitle(obj.optString("title"));
        course.setCover(obj.optString("cover"));
        course.setStatus(obj.optString("status"));
        course.setSemester(obj.optString("semester"));
        course.setType(obj.optString("type"));
        course.setVisitCount(obj.optInt("visit_count", 0));
        course.setTeacherId(obj.optString("teacher_id"));
        course.setSchoolId(obj.optString("school_id"));
        
        if (obj.has("teacher") && !obj.isNull("teacher")) {
            JSONObject teacherObj = obj.getJSONObject("teacher");
            Course.Teacher teacher = new Course.Teacher();
            teacher.setId(teacherObj.optString("id"));
            teacher.setName(teacherObj.optString("name"));
            course.setTeacher(teacher);
        }
        
        if (obj.has("school") && !obj.isNull("school")) {
            JSONObject schoolObj = obj.getJSONObject("school");
            Course.School school = new Course.School();
            school.setId(schoolObj.optString("id"));
            school.setName(schoolObj.optString("name"));
            course.setSchool(school);
        }
    }
    
    /**
     * 回调接口
     */
    public interface CourseCallback {
        void onSuccess(List<Course> courses);
        void onError(String errorMessage);
    }
}
