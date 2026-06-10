
# 实验一：开发环境搭建与工具安装

本实验旨在完成课程所需开发工具的安装与配置，包括Android Studio、Anaconda（含Jupyter Notebook）及Visual Studio Code，并验证各工具的基础使用，为后续Android端应用开发与机器学习模型构建搭建开发环境。

---

## 一、实验目的
1.  安装并配置 **Android Studio 4.1+**，支持LiteRT相关开发，验证Android项目编译运行流程。
2.  搭建Python与机器学习开发环境，安装 **Anaconda + Jupyter Notebook**，为后续模型训练与数据处理做准备。
3.  安装并配置 **Visual Studio Code**，扩展Python与Jupyter开发能力。
4.  掌握各工具的基本使用方法，并将安装与配置过程整理为Markdown文档，上传至代码仓库。

---

## 二、实验环境
- 操作系统：Windows 10/11（64位）
- 硬件配置：Intel/AMD x86_64架构CPU，至少8GB内存，50GB以上空闲磁盘空间
- 网络环境：可访问Maven中央仓库、Anaconda镜像源（建议配置国内镜像加速）

---

## 三、实验内容与步骤

### 1. Android Studio 安装与配置
#### 1.1 下载与安装
1.  访问 [Android Studio 官方网站](https://developer.android.com/studio)，下载最新稳定版（本实验使用Android Studio Panda 3，满足4.1+版本要求）。
2.  运行安装程序，按向导完成安装，注意事项：
    - 安装路径避免包含中文、空格或特殊字符
    - 选择安装Android SDK、Android Virtual Device（AVD）组件
3.  首次启动时，选择标准配置，等待SDK组件下载完成。

#### 1.2 配置国内镜像（加速依赖下载）
为解决Gradle依赖下载缓慢问题，配置阿里云云效Maven镜像：
1.  打开项目根目录下的 `settings.gradle` 文件
2.  在 `dependencyResolutionManagement` 中添加阿里云镜像：
    ```gradle
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            maven { url 'https://maven.aliyun.com/repository/google' }
            maven { url 'https://maven.aliyun.com/repository/jcenter' }
            maven { url 'https://maven.aliyun.com/repository/public' }
            google()
            mavenCentral()
        }
    }
    ```

#### 1.3 新建并运行Android项目
1.  打开Android Studio，选择「New Project」，选择「Empty Activity」模板，语言选择Java/Kotlin，最低SDK版本建议设置为API 21及以上。
2.  等待Gradle同步完成，连接真机或启动模拟器。
3.  点击「Run」按钮，编译并运行项目，成功看到应用界面即为安装配置完成。

> 截图：<img width="2559" height="1544" alt="Android studio安装" src="https://github.com/user-attachments/assets/67caa136-6199-4d07-a5b7-29097902e915" />


### 2. Anaconda + Jupyter Notebook 安装与配置
#### 2.1 安装Anaconda（推荐方式）
1.  访问 [Anaconda 官网](https://www.anaconda.com/products/distribution)，下载Windows 64位安装包。
2.  运行安装程序，关键配置：
    - 安装路径选择无中文、无空格的目录（如 `D:\Anaconda3`）
    - 选择「Just me」安装模式，无需管理员权限
    - 勾选「Register Anaconda as my default Python 3.x」
3.  完成安装后，验证安装：
    - 打开「Anaconda Prompt」，输入命令：
      ```bash
      conda list
      ```
      若能正常输出已安装包列表，说明安装成功。

> 截图：<img width="2559" height="1523" alt="anaconda安装" src="https://github.com/user-attachments/assets/0e0417a0-7c26-4964-bdaf-f2b4b8b8bfc4" />


#### 2.2 启动并使用Jupyter Notebook
1.  通过「Anaconda Navigator」启动：
    - 打开Anaconda Navigator，在主页找到「Jupyter Notebook」，点击「Launch」。
2.  浏览器会自动打开Jupyter Notebook界面，默认路径为用户目录。
3.  新建Python 3 Notebook：
    - 点击右上角「New」→「Python 3」，创建一个新的 `.ipynb` 文件。
    - 尝试编写简单代码并运行：
      ```python
      print("Hello, Jupyter Notebook!")
      import numpy as np
      print(np.array([1, 2, 3]))
      ```
4.  （可选）修改默认工作目录：
    1.  打开Anaconda Prompt，输入：
        ```bash
        jupyter notebook --generate-config
        ```
    2.  找到生成的 `jupyter_notebook_config.py` 文件，修改 `c.NotebookApp.notebook_dir` 为自定义路径。

> 截图：<img width="2559" height="1270" alt="Jupyter Notebook界面" src="https://github.com/user-attachments/assets/b46d0b75-c4d2-4a5d-9b67-9ff225418488" />


---

### 3. Visual Studio Code 安装与配置
#### 3.1 下载与安装
1.  访问 [VS Code 官网](https://code.visualstudio.com/)，下载Windows版本安装包。
2.  按向导完成安装，可勾选「添加到PATH」选项，方便命令行启动。

#### 3.2 安装扩展插件
打开VS Code，进入扩展市场，安装以下插件：
- `Python`（Microsoft官方插件，支持Python语法与调试）
- `Jupyter`（支持在VS Code中直接编辑和运行Notebook）
- （可选）`Jupyter Keymap`，兼容Jupyter快捷键习惯

#### 3.3 在VS Code中使用Jupyter Notebook
1.  新建或打开 `.ipynb` 文件，VS Code会自动识别并进入Notebook编辑模式。
2.  尝试新建单元格，输入代码并运行，验证环境配置。

> 截图：<img width="2559" height="1520" alt="image" src="https://github.com/user-attachments/assets/ff550bd4-ee79-4988-ab68-c1be50ed45f5" />

---

## 四、实验结果
1.  Android Studio：成功安装并新建项目，可正常编译运行Android应用，支持LiteRT相关开发环境配置。
2.  Anaconda：安装成功，`conda list` 命令可正常执行，Jupyter Notebook可正常启动并运行Python代码。
3.  Visual Studio Code：安装完成，Python与Jupyter插件配置成功，可在VS Code中编辑和运行Notebook文件。
4.  所有安装与运行截图已整理并上传至代码仓库。
