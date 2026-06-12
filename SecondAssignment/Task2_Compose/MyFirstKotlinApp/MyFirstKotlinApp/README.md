# 任务一：首个 Kotlin + Compose Android 应用

本项目为基于 **Kotlin** 语言和 **Jetpack Compose** 框架开发的第一个 Android 应用程序。通过本任务，熟悉 Android Studio 的项目结构、Compose UI 构建方式以及 Kotlin 基础语法。

---

## 📌 项目简介

- **项目名称**：MyFirstKotlinApp
- **开发语言**：Kotlin
- **UI 框架**：Jetpack Compose
- **功能描述**：展示一个简单的欢迎界面，包含动态 UI 组件和主题切换支持

---

## 🖥️ 开发环境

| 工具 | 版本 |
|------|------|
| Android Studio | 4.1+ |
| Kotlin 插件 | 1.9+ |
| Compose 版本 | BOM 2024.02.00+ |
| 最低 SDK | API 21 (Android 5.0) |
| 目标 SDK | API 33 (Android 13) |

---

## 🚀 项目创建步骤

### 1. 新建项目

1. 打开 Android Studio，选择 **New Project**
2. 在 `Phone and Tablet` 选项卡中，选择 **Empty Activity**
3. 配置项目信息：
   - **Name**：`MyFirstKotlinApp`
   - **Package name**：`com.example.myfirstkotlinapp`
   - **Language**：`Kotlin`
   - **Minimum SDK**：`API 21`

> 点击 **Finish**，等待项目构建完成

### 2. 项目结构预览

项目创建完成后，默认生成以下关键文件：

```
MyFirstKotlinApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/myfirstkotlinapp/
│   │   │   │   └── MainActivity.kt          # 主 Activity
│   │   │   └── res/                         # 资源文件
│   │   └── test/                            # 测试目录
│   ├── build.gradle                         # 模块级构建配置
│   └── ...
├── build.gradle                             # 项目级构建配置
└── ...
```

---

## 📝 核心代码解析

### MainActivity.kt

```kotlin
package com.example.myfirstkotlinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myfirstkotlinapp.ui.theme.MyFirstKotlinAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // 启用沉浸式布局
        setContent {
            MyFirstKotlinAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyFirstKotlinAppTheme {
        Greeting("Android")
    }
}
```

### 代码说明

| 代码块 | 说明 |
|--------|------|
| `onCreate(savedInstanceState: Bundle?)` | Activity 生命周期方法，在 Activity 创建时调用 |
| `enableEdgeToEdge()` | 启用沉浸式状态栏与导航栏模式 |
| `setContent { ... }` | Jetpack Compose 的 UI 入口，替代传统的 `setContentView()` |
| `MyFirstKotlinAppTheme { ... }` | 应用自定义主题（颜色、字体、形状等） |
| `Scaffold { ... }` | Compose 中的结构性布局容器，提供默认的 Material Design 结构 |
| `Modifier.fillMaxSize()` | 让组件填满整个屏幕 |
| `@Composable` 注解 | 标记该函数为可组合的 UI 单元 |
| `@Preview` 注解 | 在 Android Studio 中预览 UI，无需运行到设备上 |

---

## 🔧 构建与运行

### 运行方式

1. **连接设备**：使用 USB 数据线连接 Android 手机（开启开发者模式），或创建虚拟设备（AVD）
2. **运行项目**：点击 Android Studio 工具栏中的绿色三角形 ▶ `Run` 按钮
3. **选择设备**：在弹出的设备列表中选择目标设备

### 预期效果

- 应用启动后，屏幕中央显示 `Hello Android!` 文字
- 界面遵循 Material Design 3 设计规范
- 支持深色/浅色主题自动切换

---

## 📊 运行结果截图

> 请在此处插入你的运行截图

### 项目结构截图

<img width="699" height="1046" alt="image" src="https://github.com/user-attachments/assets/75f29681-d866-4885-bf71-9a6c398dee6d" />


### 代码编辑界面

<img width="2559" height="1488" alt="image" src="https://github.com/user-attachments/assets/be87c051-11e6-4f1f-800a-cc857c041734" />


### 运行效果截图
<img width="337" height="656" alt="image" src="https://github.com/user-attachments/assets/a07d38cc-3ff6-4470-8d8a-b05949b92efd" />


---

## ❓ 常见问题与解决

| 问题 | 解决方法 |
|------|----------|
| Gradle 同步失败 | 配置国内镜像源（如阿里云、腾讯云）加速依赖下载 |
| Compose 版本冲突 | 确保使用 Compose BOM 统一管理版本 |
| 预览界面不显示 | 检查 `@Preview` 注解是否用于无参 `@Composable` 函数 |
| 模拟器无法启动 | 检查 HAXM/WHX 是否安装，或尝试使用真机调试 |

---

## 📚 参考资料

- [Android 开发者官方文档 - Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin 语言官方文档](https://kotlinlang.org/docs/home.html)
- [Material Design 3 设计规范](https://m3.material.io/)

---
