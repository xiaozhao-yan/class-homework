# 基于 TensorFlow Lite 实现的 Android 花卉识别应用

## 📌 项目简介

本教程详细介绍如何在 Android 设备上运用 TensorFlow Lite 进行图像识别。通过本教程，你将掌握：

- 使用 TensorFlow Lite Model Maker 训练自定义图像分类器
- 在 Android Studio 中导入训练好的 TensorFlow Lite 模型
- 结合 CameraX 库实现实时相机预览
- 利用手机 GPU 加速模型运行
- 在 Android 应用中展示识别结果

**最终效果**：应用能够实时识别 5 种花卉（雏菊、蒲公英、玫瑰、向日葵、郁金香），并在屏幕上显示识别结果和置信度。

> **📸 截图位置**：
<img width="2559" height="1530" alt="run" src="https://github.com/user-attachments/assets/6875a767-343d-451e-9aa0-1fa7cb69c0e8" />
<img width="1260" height="2800" alt="测试" src="https://github.com/user-attachments/assets/14c7b285-2f3d-45ce-b34c-6ec9b8fcf5e7" />




---

## 📁 项目结构

```
TFLClassify/
├── finish/                    # 已完成项目（参考实现）
├── start/                     # 实践项目（需要完成的模块）
│   ├── src/main/
│   │   ├── java/org/tensorflow/lite/examples/classification/
│   │   │   ├── MainActivity.kt      # 主界面代码
│   │   │   ├── ui/                   # UI 组件
│   │   │   ├── util/                 # 工具类
│   │   │   └── viewmodel/            # ViewModel
│   │   └── res/                      # 资源文件
│   └── build.gradle                  # 模块构建配置
├── build.gradle                 # 项目构建配置
└── README.md                    # 项目说明文档
```

---

## 🖥️ 预备工作

### 开发环境要求

| 工具 | 版本要求 |
|------|----------|
| Android Studio | 4.1 以上 |
| Android SDK | API 21+ |
| JDK | 11 或 17 |
| 物理手机 | 支持开发者选项和 USB 调试 |
| TensorFlow Lite | 2.17.0 |

### 获取初始代码

**方式一：使用 Git 克隆**

```bash
git clone https://github.com/hoitab/TFLClassify.git
```

**方式二：直接下载 ZIP 包**

1. 访问 [GitHub 仓库链接](https://github.com/hoitab/TFLClassify)
2. 点击 `Code` → `Download ZIP`
3. 解压到工作目录

### 运行初始代码

1. 打开 Android Studio，选择 **Open an Existing Project**
2. 选择 `TFLClassify/build.gradle` 打开整个项目

> **📸 截图位置**：
> <img width="2543" height="1526" alt="image" src="https://github.com/user-attachments/assets/51a2df42-6f36-42bb-a554-3c8da0b5c168" />



3. 项目包含两个模块：
   - `finish`：已完成项目（参考实现）
   - `start`：实践项目（需要完成的模块）

4. 手机通过 USB 连接电脑，开启开发者选项和 USB 调试

5. 选择 **`start`** 模块，运行到物理设备（**推荐使用真机，模拟器可能不支持 CameraX**）

6. 授权相机权限，初始界面显示随机数模拟的识别结果
<img width="362" height="703" alt="image" src="https://github.com/user-attachments/assets/d076a6d2-9b3e-4c45-a8aa-407c1b612b3a" />

---

## 📦 向应用中添加 TensorFlow Lite 模型

### 1. 导入模型

1. 确保当前选中的是 **`start`** 模块

> **📸 截图位置**：此处应插入选中 start 模块截图

2. 右键 `start` 模块 → `New` → `Other` → `TensorFlow Lite Model`

> **📸 截图位置**：此处应插入 New → TensorFlow Lite Model 截图

3. 选择训练好的模型文件。本教程使用 `finish` 模块中 `ml` 文件夹下的 `FlowerModel.tflite`

> **📸 截图位置**：此处应插入选择模型文件截图

4. 点击 **Finish**，系统自动下载模型依赖包并添加到 `build.gradle`

5. 导入成功后，可以看到模型摘要信息

> **📸 截图位置**：此处应插入模型导入成功截图

### 2. 检查依赖配置

导入成功后，`build.gradle` 会自动添加以下依赖：

```gradle
dependencies {
    implementation 'org.tensorflow:tensorflow-lite:2.17.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-metadata:0.4.4'
}
```

---

## 📝 代码实现

### 查看 TODO 列表

项目初始代码中包含若干 TODO 项，用于引导完成开发：

1. 点击 `View` → `Tool Windows` → `TODO`
2. 可以对 TODO 列表按模块分组，方便定位

> **📸 截图位置**：此处应插入 TODO 列表截图

### TODO 1：初始化模型

在 `ImageAnalyzer` 类中添加模型实例：

```kotlin
private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
    ImageAnalysis.Analyzer {

    // TODO 1: Add class variable TensorFlow Lite Model
    private val flowerModel = FlowerModel.newInstance(ctx)

    // ... 其他代码
}
```

### TODO 2：图像转换

在 `analyze` 方法中，将 CameraX 的 `ImageProxy` 转换为 `TensorImage`：

```kotlin
override fun analyze(imageProxy: ImageProxy) {
    // TODO 2: Convert Image to Bitmap then to TensorImage
    val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
    
    // ... 其他代码
}
```

### TODO 3：处理识别结果

对模型输出进行处理，按置信度排序并取前 K 个结果：

```kotlin
override fun analyze(imageProxy: ImageProxy) {
    // TODO 3: Process the image using the trained model, sort and pick out the top results
    val outputs = flowerModel.process(tfImage)
        .probabilityAsCategoryList.apply {
            sortByDescending { it.score }  // 按置信度降序排序
        }.take(MAX_RESULT_DISPLAY)          // 取前 MAX_RESULT_DISPLAY 个结果
    
    // ... 其他代码
}
```

### TODO 4：构建识别结果列表

将识别结果添加到 `Recognition` 对象中：

```kotlin
override fun analyze(imageProxy: ImageProxy) {
    // TODO 4: Converting the top probability items into a list of recognitions
    for (output in outputs) {
        items.add(Recognition(output.label, output.score))
    }
    
    // ... 其他代码
}
```

### 删除占位代码

注释或删除原有的随机数模拟代码：

```kotlin
// START - Placeholder code at the start of the codelab. Comment this block of code out.
// for (i in 0..MAX_RESULT_DISPLAY-1){
//     items.add(Recognition("Fake label $i", Random.nextFloat()))
// }
// END - Placeholder code at the start of the codelab. Comment this block of code out.
```

---

## 🚀 运行与测试

### 运行应用

1. 确保手机通过 USB 连接并开启调试模式
2. 在 Android Studio 中选择 **`start`** 模块
3. 点击运行按钮 ▶
4. 在手机上授权相机权限

### 预期效果

- 相机预览画面正常显示
- 对准花卉时，屏幕下方显示识别结果
- 结果显示花卉名称和置信度

| 花卉名称 | 标签 | 预期置信度 |
|----------|------|------------|
| 雏菊 | daisy | >85% |
| 蒲公英 | dandelion | >85% |
| 玫瑰 | roses | >85% |
| 向日葵 | sunflowers | >85% |
| 郁金香 | tulips | >85% |

> **📸 截图位置**：此处应插入最终运行效果截图

---

## 🔧 可选优化：GPU 加速

### 启用 GPU 代理

在 `MainActivity.kt` 中添加 GPU 代理配置：

```kotlin
import org.tensorflow.lite.gpu.GpuDelegate

private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
    ImageAnalysis.Analyzer {

    // 创建 GPU 代理
    private val gpuDelegate = GpuDelegate()
    
    // 配置模型选项
    private val options = Interpreter.Options().addDelegate(gpuDelegate)
    private val flowerModel = FlowerModel.newInstance(ctx, options)
    
    // ... 其他代码
}
```

### 添加依赖

在 `build.gradle` 中添加 GPU 依赖：

```gradle
dependencies {
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.17.0'
}
```

---

## 📊 常见问题与解决

| 问题 | 解决方法 |
|------|----------|
| Gradle 同步失败 | 配置国内镜像源（阿里云、清华源）加速下载 |
| 模型导入失败 | 检查模型文件路径是否正确，确保文件名不含中文 |
| 相机无法打开 | 检查手机权限设置，确认已授权相机权限 |
| 识别结果不准确 | 增加训练数据量或调整训练轮数 |
| 应用卡顿 | 启用 GPU 加速，或降低相机预览分辨率 |
| 编译报错 `FULLY_CONNECTED` | 检查 TensorFlow Lite 版本与模型版本是否匹配 |

---

## 📚 参考资料

- [TensorFlow Lite 官方文档](https://www.tensorflow.org/lite)
- [TensorFlow Lite Model Maker](https://www.tensorflow.org/lite/models/modify/model_maker)
- [CameraX 官方文档](https://developer.android.com/training/camerax)
- [GitHub 项目源码](https://github.com/hoitab/TFLClassify)

---

