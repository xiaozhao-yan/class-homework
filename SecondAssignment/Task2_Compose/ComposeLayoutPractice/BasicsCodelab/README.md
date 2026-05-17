# BasicsCodelab
## 软件开发实践 - 第二次作业 - 第二个任务：实践Compose布局
### 项目简介
本项目基于 **Kotlin + Jetpack Compose** 开发，按照CSDN教程完成Compose布局实践，实现列、行布局、按钮交互、状态管理，完成可展开/收起的UI组件。

### 开发环境
- 开发工具：Android Studio
- 开发语言：Kotlin
- UI框架：Jetpack Compose
- 项目模板：Empty Activity

### 核心功能与知识点
1. **基础可组合项**
`@Composable` 定义UI函数，`@Preview` 实现实时预览；`Surface` 设置背景色，适配Material主题。

2. **布局组件**
- `Column`：垂直排列子元素
- `Row`：水平排列子元素
- `Modifier` 修饰符：设置内边距、权重、填充宽度、间距等样式

3. **循环复用组件**
通过 Kotlin 循环遍历列表，批量生成多个Greeting卡片。

4. **按钮交互与状态管理**
- `ElevatedButton`：实现可点击按钮
- `remember { mutableStateOf() }`：保存UI状态，实现点击展开/收起功能
- 点击按钮切换文本 `Show more / Show less`，动态修改内边距实现展开效果

### 运行截图
运行截图存放于上级目录 `screenshots` 文件夹中。

### 运行方式
1. 使用Android Studio导入本项目
2. 同步Gradle依赖
3. 运行至模拟器或真机，点击按钮查看展开/收起交互效果