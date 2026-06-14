# 实践课程作业合集

> **课程名称**：软件开发实践
---

## 📌 作业总览

| 序号 | 作业名称 | 包含内容 | 文件夹 | 跳转 |
|:----:|---------|---------|--------|------|
| 1 | 开发环境搭建 | Android Studio / Anaconda / VS Code 安装配置 | `FirstAssignment/` | [→ 进入](./FirstAssignment) |
| 2 | Kotlin + Compose 开发 | 任务一：首个Kotlin应用<br>任务二：Compose布局实践<br>任务三：CameraX相机应用 | `SecondAssignment/` | [→ 进入](./SecondAssignment) |
| 3 | Python 数据分析 | Fortune 500 数据分析与可视化 | `ThirdAssignment/` | [→ 进入](./ThirdAssignment) |
| 4 | TFLite 花卉识别 | 模型训练 / Android部署 / GPU加速 | `FourthAssignment/TFLClassify-main/` | [→ 进入](./FourthAssignment/TFLClassify-main) |
| 5 | 完整花卉分类器 | 模型训练 / TFLite转换 / Android部署 | `FifthAssignment/` | [→ 进入](./FifthAssignment) |

---

## 📁 项目结构

```
课程作业/
│
├── FirstAssignment/                      # 📁 实验一：开发环境搭建
│   ├── screenshot/                       # 安装过程截图
│   └── README.md                         # 详细实验报告
│
├── SecondAssignment/                     # 📁 实验二：Kotlin + Compose 开发
│   ├── Task1_kotlin/                     # 任务一：首个Kotlin应用
│   │   ├── screenshot/                   # 运行截图
│   │   └── README.md                     # 任务说明
│   ├── Task2_Compose/                    # 任务二：Compose布局实践
│   │   ├── AIComposeUIDemo/              # AI应用界面项目
│   │   ├── ComposeLayoutPractice/        # 布局练习项目
│   │   ├── MyFirstKotlinApp/             # 基础应用项目
│   │   └── README.md                     # 任务说明
│   ├── Task3_Android CameraX/            # 任务三：CameraX相机应用
│   │   └── CameraXApp/                   # 相机应用源码
│   └── README.md                         # 作业总体说明
│
├── ThirdAssignment/                      # 📁 实验三：Python 数据分析
│   ├── Untitled1.ipynb                   # Jupyter Notebook源码
│   ├── fortune500.csv                    # Fortune 500数据集
│   └── README.md                         # 详细实验报告
│
├── FourthAssignment/                     # 📁 实验四：TFLite 花卉识别
│   └── TFLClassify-main/                 # Android项目源码
│       ├── finish/                       # 已完成项目（参考）
│       ├── start/                        # 实践项目
│       ├── images/                       # 运行截图
│       ├── gradle/                       # Gradle配置
│       └── README.md                     # 详细实验报告
│
├── FifthAssignment/                      # 📁 实验五：完整花卉分类器
│   ├── first/                            # 第一个版本
│   │   ├── TFLClassify-main/             # Android项目
│   │   ├── exported_flower_model/        # 训练模型
│   │   ├── 花卉分类器_training.ipynb     # 训练代码
│   │   └── README.md                     # 说明文档
│   ├── second/                           # 第二个版本
│   │   ├── Rock paper scissors.ipynb     # 石头剪刀布训练
│   │   └── README.md                     # 说明文档
│   └── README.md                         # 作业总体说明
│
└── README.md                             # 📄 本文件（总体说明）
```

---

## 📊 作业详情

### 实验一：开发环境搭建

| 项目 | 内容 |
|------|------|
| 文件夹 | `FirstAssignment/` |
| 主要内容 | Android Studio、Anaconda、Jupyter Notebook、VS Code 安装配置 |
| 验证方式 | 各工具正常运行并截图 |
| 详细报告 | [→ 查看](./FirstAssignment/README.md) |

---

### 实验二：Kotlin + Compose 开发

| 任务 | 内容 | 文件夹 | 跳转 |
|------|------|--------|------|
| 任务一 | 首个Kotlin应用 | `Task1_kotlin/` | [→ 进入](./SecondAssignment/Task1_kotlin) |
| 任务二 | Compose布局实践 | `Task2_Compose/` | [→ 进入](./SecondAssignment/Task2_Compose) |
| 任务三 | CameraX相机应用 | `Task3_Android CameraX/` | [→ 进入](./SecondAssignment/Task3_Android%20CameraX) |

**详细报告**：[→ 查看](./SecondAssignment/README.md)

---

### 实验三：Python 数据分析

| 项目 | 内容 |
|------|------|
| 文件夹 | `ThirdAssignment/` |
| 数据文件 | `fortune500.csv`（1955-2005 Fortune 500企业数据） |
| 代码文件 | `Untitled1.ipynb`（Jupyter Notebook） |
| 分析内容 | 数据清洗、分组统计、利润/收入趋势可视化 |
| 详细报告 | [→ 查看](./ThirdAssignment/README.md) |

---

### 实验四：TFLite 花卉识别

| 项目 | 内容 |
|------|------|
| 文件夹 | `FourthAssignment/TFLClassify-main/` |
| 项目结构 | `finish/`（已完成）、`start/`（实践项目） |
| 主要功能 | 模型训练、TFLite转换、Android部署、CameraX识别 |
| 识别花卉 | daisy、dandelion、roses、sunflowers、tulips |
| 详细报告 | [→ 查看](./FourthAssignment/TFLClassify-main/README.md) |

---

### 实验五：完整花卉分类器

| 项目 | 内容 |
|------|------|
| 文件夹 | `FifthAssignment/` |
| 子目录 | `first/`（第一版）、`second/`（第二版） |

#### 第一版（`first/`）

| 文件/文件夹 | 说明 |
|-------------|------|
| `TFLClassify-main/` | Android项目源码 |
| `exported_flower_model/` | 训练生成的模型文件 |
| `花卉分类器_training.ipynb` | Jupyter训练代码 |
| `README.md` | 说明文档 |

#### 第二版（`second/`）

| 文件/文件夹 | 说明 |
|-------------|------|
| `Rock paper scissors.ipynb` | 石头剪刀布模型训练 |
| `README.md` | 说明文档 |

**详细报告**：[→ 查看](./FifthAssignment/README.md)

---

## 🔧 环境配置速查

### Python环境（模型训练）

```bash
# 创建虚拟环境
conda create -n tf161 python=3.10
conda activate tf161

# 安装依赖
pip install tensorflow>=2.16.1 numpy>=1.23 matplotlib>=3.7 jupyter>=1.0
```

### Android环境

| 配置 | 版本 |
|------|------|
| Android Studio | 4.1+ |
| minSdk | API 21 |
| targetSdk | API 33 |
| Kotlin | 1.9+ |
| TensorFlow Lite | 2.16.1 |

---

## 📚 参考资料

- [TensorFlow Lite 官方文档](https://www.tensorflow.org/lite)
- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [CameraX 官方文档](https://developer.android.com/training/camerax)
- [Kotlin 官方文档](https://kotlinlang.org/)

---

## 👤 作者信息

| 项目 | 信息 |
|------|------|
| 姓名 | [你的姓名] |
| 学号 | [你的学号] |
| 专业 | [你的专业] |
| 提交日期 | 2026年6月 |

---

*最后更新：2026年6月*
```
