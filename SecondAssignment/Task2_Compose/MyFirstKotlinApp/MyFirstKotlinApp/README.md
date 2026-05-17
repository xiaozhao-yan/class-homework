# MyFirstKotlinApp
## 软件开发实践 - 第二次作业 - 第一个任务
### 项目简介
本项目为基于 **Kotlin + Jetpack Compose** 开发的首个Android基础应用，按照CSDN教程完成项目构建，实现基础UI页面展示，熟悉Compose可组合函数与预览功能。

### 开发环境
- 开发工具：Android Studio
- 开发语言：Kotlin
- UI框架：Jetpack Compose
- 项目模板：Empty Activity

### 核心功能与代码说明
1. **@Composable 注解**
标记可组合UI函数，是Jetpack Compose构建界面的核心单元，用于声明式构建页面布局。

2. **@Preview 注解**
实现Android Studio内实时预览UI效果，无需运行模拟器即可查看页面，仅支持无参数的可组合函数。

3. **Activity 核心逻辑**
- `onCreate()`：Activity生命周期方法，页面创建时执行；`enableEdgeToEdge()` 开启沉浸式状态栏。
- `setContent{}`：Compose页面入口，替代传统XML布局，通过代码动态构建UI。
- `Scaffold`：Compose基础布局容器，自动适配状态栏、导航栏边距，实现全屏布局。
- `Greeting()`：自定义UI函数，展示文本 `Hello Android!`。