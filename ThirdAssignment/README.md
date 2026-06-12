# Jupyter Notebook 基础教程

## 📌 项目简介

本教程详细介绍 Jupyter Notebook 的基本操作，包括创建、编辑、运行 cell，管理 kernel，以及使用快捷键。此外，还展示了如何进行 Python 编程和数据分析，涉及数据清洗、绘图。最后，讨论了 Notebook 的分享、导出及扩展工具的安装与使用。

---

## 📁 项目结构

```
JupyterNotebookTutorial/
├── README.md                    # 项目说明文档
├── Untitled.ipynb               # Jupyter Notebook 源文件
├── fortune500.csv               # Fortune 500 数据集
└── requirements.txt             # 依赖库列表
```

---

## 一、Jupyter Notebook 简介

Anaconda 是安装 Jupyter Notebook 的最佳方式。安装完成之后，启动 Anaconda Navigator，并启动 Notebook，浏览器中会显示类似 `https://localhost:8888/tree` 的网址，代表本地运行着 Notebook 的服务器。

---

## 二、创建一个新的 Notebook

新建一个 Notebook `Python 3 (ipykernel)`，生成一个 `.ipynb` 文件。`.ipynb` 文件即所谓的一个 Notebook，实际是基于 JSON 格式的文本文件。

新建的 Notebook 界面包含两个关键元素：

| 元素 | 说明 |
|------|------|
| **Cell** | 文本或者代码执行单元，由 kernel 执行 |
| **Kernel** | 计算引擎，执行 cell 的文本或者代码 |

---

## 三、Cell 详解

### 3.1 Cell 类型

| 类型 | 说明 |
|------|------|
| **代码 Cell** | 包含可被 kernel 执行的代码，执行之后在下方显示输出 |
| **Markdown Cell** | 书写 Markdown 标记语言的 cell |

### 3.2 代码执行示例

```python
print('Hello World!')
```

**输出**：
```
Hello World!
```

代码执行之后，cell 左侧的标签从 `In [ ]` 变成了 `In [1]`。`In` 代表输入，`[]` 中的数字代表 kernel 执行的顺序，而 `In [*]` 则表示代码 cell 正在执行代码。
<img width="903" height="62" alt="image" src="https://github.com/user-attachments/assets/70626739-b207-4f38-8e6d-fd84fb9e7538" />


### 3.3 Cell 模式

| 模式 | 切换方式 | 外观 |
|------|----------|------|
| **编辑模式** | `Enter` 键 | 绿色轮廓 |
| **命令模式** | `Esc` 键 | 蓝色轮廓 |

### 3.4 常用快捷键

#### 命令模式快捷键

| 快捷键 | 功能 |
|--------|------|
| `↑` / `↓` | 上下移动 cell |
| `A` / `B` | 在上方/下方插入 cell |
| `M` | 转换为 Markdown cell |
| `Y` | 转换为代码 cell |
| `D` + `D` | 删除 cell |
| `Z` | 撤销删除 |
| `H` | 显示所有快捷键帮助 |
| `Ctrl + Shift + P` | 查看所有支持的命令 |

#### 编辑模式快捷键

| 快捷键 | 功能 |
|--------|------|
| `Ctrl + Shift + -` | 以光标处分割 cell |

---

## 四、Kernel（内核）

每个 notebook 都基于一个内核运行，当执行 cell 代码时，代码将在内核当中运行，运行的结果会显示在页面上。Kernel 中运行的状态在整个文档中是延续的，可以跨越所有的 cell。

### 示例：跨 cell 使用变量

**Cell 1**：
```python
import numpy as np

def square(x):
    return x * x
```

**Cell 2**：
```python
x = np.random.randint(1, 10)
y = square(x)
print('%d squared is %d' % (x, y))
```

**输出**：
```
3 squared is 9
```
<img width="894" height="191" alt="image" src="https://github.com/user-attachments/assets/53c071b5-9cc4-447e-9627-e7847c2d767b" />


### Kernel 管理

| 操作 | 说明 |
|------|------|
| `Restart Kernel` | 清空保存在内存中的变量 |
| `File > Close and Halt` | 真正关闭 kernel |
| `Kernel > Shutdown` | 关闭 kernel |

> **注意**：在浏览器中关闭一个正在运行的 notebook 页面，并未真正关闭终止 Kernel 的运行，其还是后台执行。

---

## 五、简单的 Python 程序示例

本节主要目的掌握 Python 的基本语法，要求完成基于 Python 的选择排序算法。

### 选择排序实现

```python
def selection_sort(arr):
    n = len(arr)
    
    for i in range(n):
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        if min_idx != i:
            arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr

def test():
    print("--" * 25)
    print("选择排序算法测试")
    print("--" * 25)
    
    test_cases = [
        [64, 25, 12, 22, 11],
        [5, 2, 8, 1, 9, 3],
        [1, 2, 3, 4, 5],
        [5, 4, 3, 2, 1]
    ]
    
    for i, arr in enumerate(test_cases, 1):
        original = arr.copy()
        sorted_arr = selection_sort(arr.copy())
        print(f"用例{i}: {original} -> {sorted_arr}")

if __name__ == "__main__":
    test()
```

**输出**：
```
--------------------------------------------------
选择排序算法测试
--------------------------------------------------
用例1: [64, 25, 12, 22, 11] -> [11, 12, 22, 25, 64]
用例2: [5, 2, 8, 1, 9, 3] -> [1, 2, 3, 5, 8, 9]
用例3: [1, 2, 3, 4, 5] -> [1, 2, 3, 4, 5]
用例4: [5, 4, 3, 2, 1] -> [1, 2, 3, 4, 5]
```
<img width="918" height="571" alt="image" src="https://github.com/user-attachments/assets/6de62382-c75a-4b1f-a379-7942764c042a" />

---

## 六、数据分析示例：Fortune 500

本例中将分析历年财富世界 500 强的数据（1955-2005）。

### 6.1 设置与导入

```python
%matplotlib inline
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
```

| 库 | 用途 |
|---|------|
| `pandas` | 数据处理 |
| `matplotlib` | 绘图 |
| `seaborn` | 美化图表 |

> `%matplotlib inline` 是 line magic，表示使用 matplotlib 画图，并将图片输出在页面中。

### 6.2 加载数据集

```python
df = pd.read_csv('fortune500.csv')
```

### 6.3 检查数据集

**查看前 5 行**：
```python
df.head()
```

| Year | Rank | Company | Revenue (in millions) | Profit (in millions) |
|------|------|---------|----------------------|---------------------|
| 1955 | 1 | General Motors | 9823.5 | 806 |
| 1955 | 2 | Exxon Mobil | 5661.4 | 584.8 |
| 1955 | 3 | U.S. Steel | 3250.4 | 195.4 |
| 1955 | 4 | General Electric | 2959.1 | 212.6 |
| 1955 | 5 | Esmark | 2510.8 | 19.1 |

**查看后 5 行**：
```python
df.tail()
```

**重命名列**：
```python
df.columns = ['year', 'rank', 'company', 'revenue', 'profit']
```

**检查数据条目**：
```python
len(df)  # 输出: 25500
```

**检查数据类型**：
```python
df.dtypes
```

| 列名 | 类型 |
|------|------|
| year | int64 |
| rank | int64 |
| company | object |
| revenue | float64 |
| profit | object |
<img width="909" height="708" alt="image" src="https://github.com/user-attachments/assets/08410be4-e715-4ec0-a764-08f1606be721" />


### 6.4 数据清洗

profit 列包含非数字的值，需要进行清洗。

**查找非数字记录**：
```python
non_numeric_profits = df.profit.str.contains('[^0-9.-]')
df.loc[non_numeric_profits].head()
```
<img width="911" height="204" alt="image" src="https://github.com/user-attachments/assets/57c2bbba-cbea-4aba-b95f-277852a96b6b" />


**统计非数字记录数量**：
```python
len(df.profit[non_numeric_profits])  # 输出: 369
```
<img width="918" height="58" alt="image" src="https://github.com/user-attachments/assets/5517ead4-de0d-49df-9a1e-1866b430cc7b" />


**绘制非数字记录分布**：
```python
bin_sizes, _, _ = plt.hist(df.year[non_numeric_profits], bins=range(1955, 2006))
```
> **📸 截图位置**：
> <img width="912" height="364" alt="image" src="https://github.com/user-attachments/assets/5c88d5ec-1ea2-4701-ac54-8a37d66ee5ad" />


**删除非数字记录**：
```python
df = df.loc[~non_numeric_profits]
df.profit = df.profit.apply(pd.to_numeric)
```

**验证清洗结果**：
```python
len(df)  # 输出: 25131
df.dtypes
```

| 列名 | 类型 |
|------|------|
| year | int64 |
| rank | int64 |
| company | object |
| revenue | float64 |
| profit | float64 |
<img width="920" height="247" alt="屏幕截图 2026-06-12 215923" src="https://github.com/user-attachments/assets/c140932e-dea9-4f76-9951-ef45224da7a9" />



### 6.5 使用 matplotlib 绘图

**按年份分组计算平均值**：
```python
group_by_year = df.loc[:, ['year', 'revenue', 'profit']].groupby('year')
avgs = group_by_year.mean()
x = avgs.index
y1 = avgs.profit
y2 = avgs.revenue
```

**定义绘图函数**：
```python
def plot(x, y, ax, title, y_label):
    ax.set_title(title)
    ax.set_ylabel(y_label)
    ax.plot(x, y)
    ax.margins(x=0, y=0)
```

**绘制利润趋势图**：
```python
fig, ax = plt.subplots()
plot(x, y1, ax, 'Increase in mean Fortune 500 company profits from 1955 to 2005', 'Profit (millions)')
```

> **📸 截图位置**：
> <img width="500" height="347" alt="屏幕截图 2026-06-12 215939" src="https://github.com/user-attachments/assets/29690177-4ebe-40f8-936d-093250fb011e" />



**绘制收入趋势图**：
```python
fig, ax = plt.subplots()
plot(x, y2, ax, 'Increase in mean Fortune 500 company revenues from 1955 to 2005', 'Revenue (millions)')
```

> **📸 截图位置**：
> <img width="552" height="342" alt="屏幕截图 2026-06-12 215951" src="https://github.com/user-attachments/assets/fb0e429c-e8e6-4c5e-afce-4a1817729dd8" />



**绘制含标准差的图表**：
```python
def plot_with_std(x, y, stds, ax, title, y_label):
    ax.fill_between(x, y - stds, y + stds, alpha=0.2)
    plot(x, y, ax, title, y_label)

fig, (ax1, ax2) = plt.subplots(ncols=2)
title = 'Increase in mean and std Fortune 500 company %s from 1955 to 2005'
stds1 = group_by_year.std().profit.values
stds2 = group_by_year.std().revenue.values

plot_with_std(x, y1.values, stds1, ax1, title % 'profits', 'Profit (millions)')
plot_with_std(x, y2.values, stds2, ax2, title % 'revenues', 'Revenue (millions)')

fig.set_size_inches(14, 4)
fig.tight_layout()
```

> **📸 截图位置**：
> <img width="918" height="410" alt="image" src="https://github.com/user-attachments/assets/b442f30d-88cf-49bb-8de0-39a153b11953" />




### 6.6 图表解读

| 图表 | 解读 |
|------|------|
| 利润趋势 | 整体呈指数增长，但 1990 年代初期出现急剧下滑（对应经济衰退和网络泡沫） |
| 收入趋势 | 持续增长，未出现明显下降 |
| 标准差 | 不同公司之间的收入和利润差距随年份扩大 |

---

## 七、分享 Notebooks

### 7.1 分享之前的工作

分享前应确保 Notebook 包含代码执行的输出，且结果符合预期：

| 步骤 | 操作 |
|------|------|
| 1 | 点击 `Cell > All Output > Clear` |
| 2 | 点击 `Kernel > Restart & Run All` |
| 3 | 等待所有代码执行完毕 |

### 7.2 导出 Notebooks

使用 `File > Download as` 可以导出多种格式：

| 格式 | 用途 |
|------|------|
| HTML | 网页查看 |
| PDF | 文档打印 |
| Markdown | 文档编辑 |
| Python (.py) | 脚本执行 |

如需协同共享 `.ipynb` 文件，可借助 GitHub 或 Google Colab。

---

## 🔧 环境配置

### 依赖库安装

```bash
pip install numpy pandas matplotlib seaborn jupyter_contrib_nbextensions
```

### requirements.txt

```
numpy>=1.23.0
pandas>=1.5.0
matplotlib>=3.7.0
seaborn>=0.12.0
jupyter_contrib_nbextensions>=0.5.0
```

---

## 📊 实验总结

| 模块 | 完成情况 | 说明 |
|------|----------|------|
| Jupyter Notebook 基本操作 | ✅ | cell、kernel、快捷键 |
| 选择排序算法 | ✅ | 实现 + 测试 |
| Fortune 500 数据分析 | ✅ | 数据清洗 + 可视化 |

---

## 📚 参考资料

- [Jupyter Notebook 官方文档](https://jupyter-notebook.readthedocs.io/)
- [How to Use Jupyter Notebook in 2020](https://www.dataquest.io/blog/jupyter-notebook-tutorial/)
- [Pandas 官方文档](https://pandas.pydata.org/)
- [Matplotlib 官方文档](https://matplotlib.org/)
