plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.xiaoyapro"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.xiaoyapro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 添加CardView和RecyclerView依赖（用于卡片布局和列表）
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 添加GradientDrawable支持
    implementation("androidx.core:core-ktx:1.12.0")

    // OkHttp网络请求库
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Gson解析库
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}