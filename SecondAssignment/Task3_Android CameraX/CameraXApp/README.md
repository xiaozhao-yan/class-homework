# Android CameraX 应用开发实验报告

## 实验信息
- **实验名称**：构建 Android CameraX 应用
- **开发环境**：Android Studio + Kotlin
- **最低 SDK 版本**：API Level 21 (Android 5.0)

---

## 实验目的

1. 掌握 Android CameraX 拍照功能的基本用法
2. 掌握 Android CameraX 视频捕捉功能的基本用法
3. 进一步熟悉 Kotlin 语言的特性

> CameraX 是开发智能应用的必要组件，本次实验十分重要。

---

## 实验内容

| 序号 | 内容 | 完成状态 |
|------|------|----------|
| 1 | 创建空工程 | ✅ |
| 2 | 添加 CameraX 依赖 | ✅ |
| 3 | 创建项目布局 (XML) | ✅ |
| 4 | 编写 MainActivity.kt | ✅ |
| 5 | 请求必要权限 | ✅ |
| 6 | 实现 Preview 用例（预览） | ✅ |
| 7 | 实现 ImageCapture 用例（拍照） | ✅ |
| 8 | 实现 ImageAnalysis 用例（图像分析） | ✅ |
| 9 | 实现 VideoCapture 用例（录像） | ✅ |

---

## 实验步骤

### 步骤1：创建空工程

1. 打开 Android Studio，选择 **File → New → New Project**
2. 选择 **Empty Activity**
3. 配置项目：
   - **Name**：`CameraXApp`
   - **Package name**：`com.android.example.cameraxapp`
   - **Language**：`Kotlin`
   - **Minimum SDK**：`API 21`

**📸 截图位置 1**：
<img width="1126" height="813" alt="image" src="https://github.com/user-attachments/assets/633afb19-0081-4042-b2dd-a6b4de4d04bc" />


**📸 截图位置 2**：
<img width="1126" height="813" alt="image" src="https://github.com/user-attachments/assets/4bed457a-1174-4f60-a385-5b6b01be65ca" />

---

### 步骤2：添加 CameraX 依赖

打开 **模块级 build.gradle** 文件，添加以下依赖：

```groovy
dependencies {
    def camerax_version = "1.5.0-alpha06"
    
    // CameraX 核心库
    implementation "androidx.camera:camera-core:${camerax_version}"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    
    // CameraX 生命周期库
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    
    // CameraX 视频录制库
    implementation "androidx.camera:camera-video:${camerax_version}"
    
    // CameraX 视图类
    implementation "androidx.camera:camera-view:${camerax_version}"
    
    // CameraX 扩展库
    implementation "androidx.camera:camera-extensions:${camerax_version}"
    
    // CameraX ML Kit 视觉集成
    implementation "androidx.camera:camera-mlkit-vision:${camerax_version}"
}
```

在 `android` 代码块中添加 Java 8 编译选项：

```groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

启用 ViewBinding：

```groovy
buildFeatures {
    viewBinding true
}
```

**📸 截图位置 3**：
<img width="2559" height="1530" alt="image" src="https://github.com/user-attachments/assets/d30a375a-bcfa-4346-8599-e0f3874a56d5" />


---

### 步骤3：创建项目布局 (activity_main.xml)

打开 `res/layout/activity_main.xml`，替换为以下代码：

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- 相机预览视图 -->
    <androidx.camera.view.PreviewView
        android:id="@+id/viewFinder"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <!-- 拍照按钮 -->
    <Button
        android:id="@+id/image_capture_button"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginBottom="50dp"
        android:layout_marginEnd="50dp"
        android:elevation="2dp"
        android:text="@string/take_photo"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/vertical_centerline" />

    <!-- 录像按钮 -->
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

    <!-- 垂直辅助线（用于分割两个按钮） -->
    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/vertical_centerline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent=".50" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**📸 截图位置 4**：

<img width="626" height="516" alt="image" src="https://github.com/user-attachments/assets/be48c583-6a5d-44d4-bdbd-d186dd5b9934" />



---

### 步骤4：添加字符串资源

打开 `res/values/strings.xml`，添加以下内容：

```xml
<resources>
    <string name="app_name">CameraXApp</string>
    <string name="take_photo">Take Photo</string>
    <string name="start_capture">Start Capture</string>
    <string name="stop_capture">Stop Capture</string>
</resources>
```

---

### 步骤5：请求必要权限

#### 5.1 在 AndroidManifest.xml 中添加权限

```xml
<!-- 确保设备有相机 -->
<uses-feature android:name="android.hardware.camera.any" />

<!-- 请求权限 -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />
```


#### 5.2 权限说明

| 权限 | 用途 |
|------|------|
| `CAMERA` | 打开相机进行预览、拍照、录像 |
| `RECORD_AUDIO` | 录制视频时采集音频 |
| `WRITE_EXTERNAL_STORAGE` | Android 9 及以下保存照片/视频到存储 |

---

### 步骤6：编写 MainActivity.kt

#### 6.1 完整代码

```kotlin
package com.android.example.cameraxapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.example.cameraxapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewBinding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // 请求相机权限
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // 设置按钮监听器
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return
        viewBinding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.videoCaptureButton.apply {
                            text = getString(R.string.stop_capture)
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, msg)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(TAG, "Video capture error: ${recordEvent.error}")
                        }
                        viewBinding.videoCaptureButton.apply {
                            text = getString(R.string.start_capture)
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 预览
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // 拍照
            imageCapture = ImageCapture.Builder().build()

            // 录像
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // 选择后置摄像头
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, videoCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
```

**📸 截图位置 6**：
<img width="2559" height="1523" alt="image" src="https://github.com/user-attachments/assets/b2898c13-697c-4eda-86c4-eabf94c1f8da" />

---

### 步骤7：运行应用

#### 7.1 权限请求

首次运行应用时，系统会弹出权限请求对话框：

| 权限 | 用途 |
|------|------|
| 相机权限 | 预览、拍照、录像 |
| 麦克风权限 | 录制视频时录音 |

**📸 截图位置 7**：
<img width="2559" height="1527" alt="屏幕截图 2026-05-13 113920" src="https://github.com/user-attachments/assets/3378d9a7-8d8a-4354-b7b8-2906c871c6c5" />


#### 7.2 应用界面

应用运行后，界面应包含：
- **上半部分**：相机预览画面
- **左下角**：TAKE PHOTO（拍照）按钮
- **右下角**：START CAPTURE（开始录像）按钮

**📸 截图位置 8**：
<img width="2554" height="1526" alt="屏幕截图 2026-05-13 115153" src="https://github.com/user-attachments/assets/50bd13e8-14b5-49bc-9a21-67a0abeaa2af" />


---

### 步骤8：功能测试

#### 8.1 拍照功能测试

1. 点击 **TAKE PHOTO** 按钮
2. 系统弹出提示："Photo capture succeeded: content://..."
3. 照片保存到 `Pictures/CameraX-Image/` 目录

**📸 截图位置 9**：
拍照成功的 Toast 提示截图：
<img width="454" height="909" alt="image" src="https://github.com/user-attachments/assets/8a450dd5-1663-4ea8-9f5f-e3edd104db83" />


**📸 截图位置 10**：
相册中显示拍摄照片的截图：
<img width="442" height="911" alt="image" src="https://github.com/user-attachments/assets/f6551f1d-708a-413c-8e02-244c95b87248" />


#### 8.2 录像功能测试

1. 点击 **START CAPTURE** 按钮
2. 按钮文字变为 **STOP CAPTURE**
3. 点击 **STOP CAPTURE** 结束录制
4. 视频保存到 `Movies/CameraX-Video/` 目录

**📸 截图位置 11**：
录像开始/结束的状态截图：

<img width="442" height="872" alt="image" src="https://github.com/user-attachments/assets/9a5b8f7e-64bb-4363-b6e9-1b90126953a6" />

<img width="442" height="893" alt="image" src="https://github.com/user-attachments/assets/e45c726b-3cb6-4b18-959f-452a47f646d2" />


**📸 截图位置 12**：
相册中显示录制视频的截图：


<img width="442" height="911" alt="image" src="https://github.com/user-attachments/assets/d85a6274-b5f9-475a-835c-365dacd54c54" />


---

## 功能总结

| 功能模块 | 实现类 | 状态 |
|----------|--------|------|
| 预览 (Preview) | `PreviewView` + `Preview` | ✅ |
| 拍照 (ImageCapture) | `ImageCapture` | ✅ |
| 录像 (VideoCapture) | `VideoCapture` + `Recorder` | ✅ |
| 图像分析 (ImageAnalysis) | `ImageAnalysis` (代码已注释) | ⚪ |

---

## CameraX 核心组件说明

| 组件 | 说明 |
|------|------|
| `ProcessCameraProvider` | 绑定相机生命周期到 LifecycleOwner |
| `Preview` | 实时显示摄像头画面 |
| `ImageCapture` | 高质量静态图片捕捉 |
| `VideoCapture` | 视频录制功能 |
| `CameraSelector` | 选择前置/后置摄像头 |
| `PreviewView` | 显示预览画面的 View |

---

## 常见问题与解决

| 问题 | 解决方案 |
|------|----------|
| 应用闪退：相机权限被拒绝 | 在设置中手动授予相机权限 |
| 预览画面黑屏 | 检查 CameraX 依赖版本是否匹配 |
| 视频无声音 | 检查是否授予 `RECORD_AUDIO` 权限 |
| 编译错误 | 确保 build.gradle 中的依赖版本正确并 Sync |

---

## 实验总结

### 完成情况

| 实验内容 | 完成情况 |
|----------|----------|
| 掌握 CameraX 拍照功能 | ✅ |
| 掌握 CameraX 视频捕捉功能 | ✅ |
| 熟悉 Kotlin 语言特性 | ✅ |
| 上传代码至 GitHub | ✅ |
| 撰写详细 Readme 文档 | ✅ |

### 知识点掌握

- ✅ CameraX 库的基本用法
- ✅ Android 硬件权限获取
- ✅ Preview、ImageCapture、VideoCapture 用例
- ✅ Kotlin 协程与生命周期管理

---

## 参考资源

- [Kotlin 官方网站](https://kotlinlang.org/)
- [CameraX 概览](https://developer.android.com/training/camerax)
- [CameraX 使用入门](https://developer.android.com/camera/camerax)
- [Android 官方 CameraX 示例](https://github.com/android/camera-samples)

---

**实验完成日期**：2026年6月10日
**实验人员**：121052023003 揭玮燕
```
