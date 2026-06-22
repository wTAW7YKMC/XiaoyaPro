// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
}

// 配置所有项目的仓库
allprojects {
    repositories {
        google()  // Google Maven仓库（Android依赖）
        mavenCentral()  // Maven Central仓库（第三方库如OkHttp、Gson）
    }
}