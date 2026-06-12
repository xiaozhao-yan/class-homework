# 任务三：面向 AI 应用的 Compose 布局

## 📌 项目简介

本项目是一个基于 **Jetpack Compose** 构建的 AI 应用界面原型，展示了 LiteRT AI Demo 的核心布局。项目实现了相机预览区、识别结果展示区以及功能按钮区，为后续集成 TensorFlow Lite 模型进行图像识别提供完整的 UI 框架。

### 主要功能

- 📷 **相机预览区**：预留 CameraX 相机预览位置
- 📊 **结果展示区**：显示模型名称、识别结果、置信度、推理时间
- 🎯 **功能按钮区**：支持拍照识别、相册导入、切换模型、清空结果
- 🎨 **主题支持**：亮色/暗色模式自动切换，Android 12+ 动态颜色

---

## 🖥️ 开发环境

| 工具 | 版本 |
|------|------|
| Android Studio | 4.1+ |
| Kotlin | 1.9+ |
| Jetpack Compose | BOM 2024.02.00+ |
| 最低 SDK | API 21 |
| 目标 SDK | API 33 |

---

## 📁 项目结构

```
AICompose/
├── app/
│   └── src/main/
│       ├── java/com/example/aicompose/
│       │   ├── MainActivity.kt              # 主界面代码
│       │   └── ui/theme/
│       │       ├── Color.kt                 # 颜色定义
│       │       ├── Theme.kt                 # 主题配置
│       │       └── Type.kt                  # 字体排版
│       └── res/                             # 资源文件
├── build.gradle.kts                         # 项目级构建配置
├── settings.gradle.kts                      # 项目设置
└── README.md                                # 项目说明
```

---

## 🎨 主题配置

### 颜色定义（Color.kt）

```kotlin
package com.example.aicompose.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
```

### 主题配色方案

| 颜色名称 | 亮色模式 | 暗色模式 | 用途 |
|----------|----------|----------|------|
| Primary | `Purple40` (#6650a4) | `Purple80` (#D0BCFF) | 主要主题色 |
| Secondary | `PurpleGrey40` (#625b71) | `PurpleGrey80` (#CCC2DC) | 次要主题色 |
| Tertiary | `Pink40` (#7D5260) | `Pink80` (#EFB8C8) | 强调色 |

### 主题配置（Theme.kt）

```kotlin
package com.example.aicompose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AIComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Android 12+ 支持动态颜色（跟随系统壁纸颜色）
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) 
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

| 特性 | 说明 |
|------|------|
| 动态颜色 | Android 12+ 支持跟随系统壁纸自动调整主题色 |
| 暗色模式 | 自动检测系统暗色模式设置 |
| 自定义主题 | 亮色/暗色模式使用不同的配色方案 |

### 字体排版（Type.kt）

```kotlin
package com.example.aicompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
```

> **📸 截图位置**：
> <img width="2559" height="1528" alt="image" src="https://github.com/user-attachments/assets/41c5dba1-2b11-4230-b079-524f554cd5d7" />


---

## 📝 主界面代码（MainActivity.kt）

### 整体布局结构

使用 `Column` 垂直组织页面，从上到下依次排列：

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIComposeTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    TopBar()
                    CameraPreviewArea()
                    ResultArea()
                    ButtonArea()
                }
            }
        }
    }
}
```

### 1. 顶部标题栏（TopBar）

使用 `Surface` 作为背景容器，`Box` 居中显示标题文字。

```kotlin
@Composable
fun TopBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1976D2)
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LiteRT AI Demo",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
```

> **📸 截图位置**：
> <img width="1068" height="634" alt="image" src="https://github.com/user-attachments/assets/f0fd6175-5baa-4f5b-a86e-de58237a5356" />



### 2. 相机预览区（CameraPreviewArea）

使用 `Box` 作为预览占位容器，后续可替换为 CameraX 相机预览。

```kotlin
@Composable
fun CameraPreviewArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📷", fontSize = 48.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Camera Preview", color = Color.Gray, fontSize = 14.sp)
        }
    }
}
```

> **📸 截图位置**：
> <img width="1433" height="860" alt="image" src="https://github.com/user-attachments/assets/7c605bfd-e92e-48b2-ac63-2b086e3dfdbe" />



### 3. 识别结果区（ResultArea）

使用 `Card` 卡片组件展示识别结果，内部用 `Column` 垂直排列信息。

```kotlin
@Composable
fun ResultArea() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Model: MobileNet", fontSize = 16.sp)
            Text(text = "Result: Cat", fontSize = 16.sp)
            Text(text = "Confidence: 96.2%", fontSize = 16.sp)
            Text(text = "Time: 28 ms", fontSize = 16.sp)
        }
    }
}
```

> **📸 截图位置**：
> <img width="1311" height="745" alt="image" src="https://github.com/user-attachments/assets/f3eda952-0a63-4980-b85e-c0786590a42d" />




### 4. 功能按钮区（ButtonArea）

使用两行 `Row` 布局，每行两个按钮，按钮之间使用 `weight` 平分宽度。

| 按钮 | 颜色 | 功能 |
|------|------|------|
| 拍照识别 | 蓝色 (#1976D2) | 调用相机拍照识别 |
| 相册导入 | 绿色 (#4CAF50) | 从相册选择图片识别 |
| 切换模型 | 紫色 (#9C27B0) | 切换不同的识别模型 |
| 清空结果 | 红色 (#F44336) | 清空当前识别结果 |

```kotlin
@Composable
fun ButtonArea() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 第一行：拍照识别 + 相册导入
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("拍照识别")
            }
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("相册导入")
            }
        }
        // 第二行：切换模型 + 清空结果
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text("切换模型")
            }
            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("清空结果")
            }
        }
    }
}
```

> **📸 截图位置**：
><img width="1419" height="843" alt="image" src="https://github.com/user-attachments/assets/59e0d9e5-8dea-4d9e-96e0-bebf95435aad" />


> 
---

## 🎨 界面预览

使用 `@Preview` 注解可以在 Android Studio 中直接预览 UI 效果：

```kotlin
@Preview(showBackground = true, widthDp = 320)
@Composable
fun LiteRTAIDemoPreview() {
    AIComposeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            CameraPreviewArea()
            ResultArea()
            ButtonArea()
        }
    }
}
```

> **📸 截图位置**：
> <img width="377" height="777" alt="image" src="https://github.com/user-attachments/assets/6b143f61-2630-4fdf-a43c-8e9814b58399" />


---

## 📊 界面布局说明

| 区域 | 使用的组件 | 说明 |
|------|------------|------|
| 顶部栏 | `Surface` + `Box` + `Text` | 固定高度，蓝色背景，标题居中 |
| 预览区 | `Box` + `Column` + `Text` | 灰色背景，高度 200dp，包含占位图标和文字 |
| 结果区 | `Card` + `Column` + `Text` | 卡片样式，圆角阴影，内边距 16dp |
| 按钮区 | `Column` + `Row` + `Button` | 两行两列按钮，使用 weight 自适应宽度 |

---

## 🔧 后续扩展建议

当前版本为静态 UI 原型，后续可以集成以下功能：

| 功能 | 集成方案 |
|------|----------|
| 相机预览 | 集成 CameraX 库，替换 `CameraPreviewArea` |
| 拍照识别 | 添加 `onClick` 事件，调用相机拍照并传入识别模型 |
| 相册导入 | 使用 `ActivityResultLauncher` 选择图片 |
| 模型切换 | 支持多模型切换（如 MobileNet、ResNet 等） |
| 结果展示 | 动态更新 `ResultArea` 中的文本内容 |

---

## 📱 运行效果

| 界面区域 | 效果描述 |
|----------|----------|
| 顶部标题栏 | 显示 "LiteRT AI Demo"，蓝色背景，白色文字 |
| 相机预览区 | 灰色背景，相机图标和 "Camera Preview" 占位文字 |
| 结果展示区 | 白色卡片，显示模型、识别结果、置信度、时间 |
| 按钮区 | 4 个彩色按钮，分两行排列 |
| 主题切换 | 支持亮色/暗色模式自动切换 |

---

## ❓ 常见问题与解决

| 问题 | 解决方法 |
|------|----------|
| 预览不显示 | 确保使用 `@Preview` 注解，且预览函数无参数 |
| 颜色不生效 | 检查是否在 `AIComposeTheme` 内部调用组件 |
| 按钮点击无反应 | 需要添加 `onClick` 事件处理逻辑 |
| 布局错位 | 检查 `modifier` 是否正确传递 |
| 暗色模式不生效 | 检查系统设置，或手动设置 `darkTheme` 参数 |

---

## 📚 参考资料

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Compose 布局基础](https://developer.android.com/jetpack/compose/layout)
- [Material Design 3 in Compose](https://developer.android.com/jetpack/compose/designsystems/material3)
- [CameraX 官方文档](https://developer.android.com/training/camerax)

