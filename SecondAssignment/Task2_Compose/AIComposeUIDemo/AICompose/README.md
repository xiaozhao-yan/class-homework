# AICompose - LiteRT AI Demo
## 软件开发实践 - 第二次作业 - 第三个任务

### 项目简介
本项目基于 **Kotlin + Jetpack Compose** 开发，实现一个完整的 AI 图像识别 Demo 界面。
包含顶部标题栏、相机预览区、识别结果展示区、功能按钮区，界面美观、布局规范，完全符合课程实践要求。

### 开发环境
- 开发工具：Android Studio
- 开发语言：Kotlin
- UI 框架：Jetpack Compose
- 主题：Material Design 3

### 页面结构
1. **TopBar（顶部栏）**
    蓝色标题栏，居中显示 "LiteRT AI Demo"。

2. **CameraPreviewArea（相机预览区）**
    灰色占位区域，显示相机图标与提示文字，模拟相机预览界面。

3. **ResultArea（识别结果区）**
    使用 Card 卡片展示 AI 识别结果，包括：
    - 模型名称
    - 识别结果
    - 置信度
    - 识别耗时

4. **ButtonArea（功能按钮区）**
    两行四按钮布局：
    - 拍照识别（蓝色）
    - 相册导入（绿色）
    - 切换模型（紫色）
    - 清空结果（红色）

### 核心知识点
- Column / Row / Box 基础布局使用
- Modifier 修饰符：宽度、高度、内边距、背景、填充
- Card 组件实现结果卡片
- Button 按钮样式与颜色自定义
- 界面均分、对齐、间距设置

### 运行截图
运行截图存放于上级目录 `screenshots` 文件夹中。

### 运行方式
1. 使用 Android Studio 打开项目
2. 同步 Gradle 依赖
3. 运行到模拟器或真机，查看完整 AI Demo 界面