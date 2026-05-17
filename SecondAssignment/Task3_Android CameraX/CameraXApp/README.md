CameraXApp - 基于Jetpack Compose的相机应用
1. 项目介绍
本项目是一款基于Kotlin和Jetpack CameraX开发的相机应用，遵循Android官方教程实现。CameraX是Jetpack旗下的一款相机开发库，可简化相机应用的开发流程，兼容性优良，最低支持API 21版本。该应用实现了相机预览、拍照、录像、图像亮度分析等核心相机功能，完全满足Android开发实践作业要求。

2. 开发环境
- 开发工具：Android Studio Arctic Fox 2020.3.1 及以上版本
- 开发语言：Kotlin
- UI框架：Jetpack CameraX、ViewBinding
- 最低支持API版本：21
- CameraX版本：1.1.0-beta01

3. 项目结构
CameraXApp
├── app
│   ├── src/main
│   │   ├── AndroidManifest.xml       # 权限及应用配置文件
│   │   ├── java/com/android/example/cameraxapp
│   │   │   └── MainActivity.kt       # 核心逻辑（相机初始化、功能实现）
│   │   └── res
│   │       ├── layout
│   │       │   └── activity_main.xml # UI布局（预览界面、控制按钮）
│   │       └── values
│   │           └── strings.xml       # 字符串资源文件
│   └── build.gradle                  # 依赖配置文件
└── screenshots                       # 应用运行截图存放目录

4. 搭建步骤、

4.1 创建新项目
1. 打开Android Studio，创建一个“Empty Activity”（空活动）新项目。
2. 将项目命名为“CameraXApp”，包名设置为“com.android.example.cameraxapp”。
3. 选择Kotlin作为开发语言，设置最低支持API版本为21。

4.2 添加Gradle依赖
打开模块级别的build.gradle文件，添加以下依赖项：
dependencies {
  def camerax_version = "1.1.0-beta01"
  implementation "androidx.camera:camera-core:${camerax_version}"
  implementation "androidx.camera:camera-camera2:${camerax_version}"
  implementation "androidx.camera:camera-lifecycle:${camerax_version}"
  implementation "androidx.camera:camera-video:${camerax_version}"
  implementation "androidx.camera:camera-view:${camerax_version}"
  implementation "androidx.camera:camera-extensions:${camerax_version}"
}

// 设置Java 8兼容性（较新版本Android Studio默认已配置）
android {
  compileOptions {
      sourceCompatibility JavaVersion.VERSION_1_8
      targetCompatibility JavaVersion.VERSION_1_8
  }
  // 启用ViewBinding
  buildFeatures {
      viewBinding true
  }
}
出现提示（灯泡图标）时，点击“Sync Now”同步依赖。
4.3 配置权限
在AndroidManifest.xml文件中（<application>标签之前）添加以下权限：
<uses-feature android:name="android.hardware.camera.any" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />

4.4 设计UI布局
将res/layout/activity_main.xml文件内容替换为以下代码，实现相机预览和控制按钮布局：
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.camera.view.PreviewView
        android:id="@+id/viewFinder"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <Button
        android:id="@+id/image_capture_button"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginBottom="50dp"
        android:layout_marginEnd="50dp"
        android:elevation="2dp"
        android:text="@string/take_photo"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintEnd_toStartOf="@id/vertical_centerline" />

    <Button
        android:id="@+id/video_capture_button"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginBottom="50dp"
        android:layout_marginStart="50dp"
        android:elevation="2dp"
        android:text="@string/start_capture"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toEndOf="@id/vertical_centerline" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/vertical_centerline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent=".50" />

</androidx.constraintlayout.widget.ConstraintLayout>

4.5 更新字符串资源
修改res/values/strings.xml文件，添加所需的字符串资源：
<resources>
    <string name="app_name">CameraXApp</string>
    <string name="take_photo">拍照</string>
    <string name="start_capture">开始录制</string>
    <string name="stop_capture">停止录制</string>
</resources>

5. 核心功能实现
将MainActivity.kt文件中的代码替换为核心逻辑，实现相机预览、拍照、录像、图像分析等功能。核心功能包括：
- 相机权限请求：检查并请求相机、麦克风、存储权限。
- 相机预览：将Preview用例绑定到生命周期，显示相机实时画面。
- 拍照功能：将拍摄的照片保存到媒体库，并显示拍摄成功提示。
- 录像功能：启动/停止录像，将视频保存到媒体库，并切换按钮状态。
- 图像分析：计算并打印相机帧的平均亮度。
MainActivity.kt的完整代码参考教程提供的实现，包含所有功能的具体代码。
6. 应用运行
1. 与Gradle同步项目，确保所有依赖加载正常。
2. 在模拟器或物理Android设备（API级别≥21）上运行应用。
3. 应用启动后，按提示授予所需权限（相机、麦克风）。
4. 测试核心功能：
        
  - 相机预览：应用启动后自动显示相机实时画面。
  - 拍照：点击“拍照”按钮，拍摄并保存照片（可在设备相册中查看）。
  - 录像：点击“开始录制”按钮启动录像，再次点击停止录制（可在设备视频库中查看）。
  
7. 注意事项
- Logcat中出现的红色日志（如“getStandardMetadataImpl: failure”）属于正常现象，是模拟器相机兼容性问题导致，不影响应用功能。
- 图像分析和录像功能建议在物理设备上测试，兼容性更好。
- 确保Android Studio更新至要求版本，避免出现Gradle同步失败或CameraX API兼容问题。
- 文档中涉及的http://schemas.android.com/apk/res/android、http://schemas.android.com/apk/res-auto、http://schemas.android.com/tools三个链接，均为Android官方配置相关路径，无需手动访问，代码中正常引用即可，解析失败不影响项目运行。
8. 运行截图
应用运行过程中的截图，统一存放在screenshots文件夹中。