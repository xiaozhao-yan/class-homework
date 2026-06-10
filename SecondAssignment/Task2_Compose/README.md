
# 实验三：构建 Kotlin 应用并使用 Compose 布局

---

## 一、实验目的
1.  掌握使用 Kotlin 语言开发 Android 应用的基本流程。
2.  掌握 Jetpack Compose 布局的核心组件与声明式 UI 开发方式。
3.  进一步熟悉 Kotlin 语言特性（如高阶函数、空安全、Lambda 表达式）。
4.  完成面向 AI 图像识别场景的 Compose 界面布局开发，为后续模型集成做准备。

---

## 二、实验环境
| 工具/依赖 | 版本/说明 |
| :--- | :--- |
| Android Studio | Hedgehog / Iguana / Jellyfish（支持Compose） |
| 开发语言 | Kotlin |
| 最低 SDK | API 21（Compose 最低支持版本） |
| 构建工具 | Gradle 7.0+ |
| 界面框架 | Jetpack Compose |

---

## 三、实验内容与步骤

### 任务一：创建首个 Kotlin 应用
#### 步骤说明
1.  打开 Android Studio，选择 `New Project`，选择 `Empty Activity` 模板。
2.  配置项目：
    - Language：`Kotlin`
    - Minimum SDK：`API 21: Android 5.0 (Lollipop)`
    - 勾选 `Use Compose`（新版Android Studio默认已勾选）
3.  完成项目创建，等待Gradle同步完成。
4.  运行默认项目，验证应用正常启动并显示`Hello Android!`。

#### 任务完成截图
- 应用首次运行成功界面：
<img width="2559" height="1528" alt="运行结果" src="https://github.com/user-attachments/assets/c0c73231-cfac-4476-acc1-556e9b53823e" />

---

### 任务二：实践 Compose 基础布局
#### 步骤说明
1.  学习 Compose 核心布局组件：`Column`、`Row`、`Box`、`Card`、`Text`、`Button` 等。
2.  实现基础布局练习：
    - 使用 `Column` 实现垂直排列
    - 使用 `Row` 实现水平排列
    - 使用 `Box` 实现层叠布局
    - 为组件添加 `padding`、`margin`、`modifier` 样式
3.  按照教程完成基础练习，验证声明式UI的编写方式。

```

#### 任务完成截图
- Compose 基础布局代码界面\ 基础布局运行效果：
<img width="2559" height="1525" alt="d11c7678eacaf3f85c6fc1486072cf86" src="https://github.com/user-attachments/assets/19376509-66b9-410e-9d17-aa75cc3f9655" />
<img width="2546" height="1501" alt="8daf75590048f598e408bca8a969b9bb" src="https://github.com/user-attachments/assets/f1da7b67-0bb4-4bb5-9ff8-c5160f0a9c56" />
<img width="2559" height="1526" alt="ee281a0325cb002dc990f53c076dd6e9" src="https://github.com/user-attachments/assets/9d99cca7-71e3-4923-aa31-b1fa4bff742c" />
<img width="2559" height="1527" alt="68b139dc608d47512e54c0a3eb554984" src="https://github.com/user-attachments/assets/05d653ad-5ae6-4eff-a8d4-3b4c82175e41" />
<img width="2559" height="1529" alt="7c98b8152f3efd5a21309d4ebbcf5201" src="https://github.com/user-attachments/assets/78538952-611e-49fd-82ed-fae59f83da77" />

---

### 任务三：完成面向AI应用的 Compose 布局
#### 界面需求说明
为后续AI图像识别应用设计完整界面，包含以下模块：
1.  **顶部栏**：显示应用标题 + 操作入口（如设置）
2.  **预览区**：预留相机/图片预览位置（用`Box`占位）
3.  **结果展示区**：使用`Card + Column`展示识别结果（模型名称、识别结果、置信度、推理时间）
4.  **按钮区**：提供拍照识别、相册导入、切换模型、清空结果功能按钮
```

#### 任务完成截图
- AI应用布局代码界面、应用运行整体效果：
<img width="2559" height="1523" alt="运行结果" src="https://github.com/user-attachments/assets/fedaf310-be27-465c-9073-685edf52b2a4" />

---

## 四、实验结果
1.  成功创建了基于Kotlin的Android项目，最低SDK版本设置为API 21，满足Compose运行要求。
2.  完成了Compose基础布局练习，掌握了`Column`/`Row`/`Box`/`Card`等核心组件的使用。
3.  实现了面向AI图像识别场景的完整Compose界面，包含顶部栏、预览区、结果展示区与按钮区。
4.  项目可正常编译、运行，界面布局符合设计预期，为后续相机与模型集成做好了准备。

---

## 五、实验总结
本次实验系统学习了Kotlin语言与Jetpack Compose开发Android应用的完整流程。通过三个任务，从基础项目创建、Compose组件练习，到最终完成面向AI应用的完整界面布局，我不仅掌握了声明式UI的开发方式，也进一步熟悉了Kotlin的语法特性。实验中遇到了布局对齐、modifier使用等问题，通过查阅官方文档和调试，加深了对Compose布局原理的理解。本次实验为后续AI模型在Android端的集成开发打下了坚实的基础。
---

## 六、参考资料
- [Kotlin 官方文档](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose 官方教程](https://developer.android.com/jetpack/compose/tutorial)
- [Compose 基础布局指南](https://developer.android.com/jetpack/compose/layouts/basics)
- Android Studio 官方开发文档
```
