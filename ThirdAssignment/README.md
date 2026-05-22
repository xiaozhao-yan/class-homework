# Python 综合练习项目
## 1. 基础语法练习
### Hello World
print('Hello World!')
输出：
Hello World!

延时操作
import time
time.sleep(3)
程序暂停3秒。

NumPy 与函数定义
import numpy as np
def square(x):
    return x * x

x = np.random.randint(1, 10)
y = square(x)
print('%d squared is %d' % (x, y))
输出示例：
5 squared is 25

## 2. 选择排序算法
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
    print("--" * 50)
    print("选择排序算法测试")
    print("--" * 50)
    test_cases = [
        [64, 25, 12, 22, 11],
        [5, 2, 8, 1, 9, 3],
        [1, 2, 3, 4, 5],
        [5, 4, 3, 2, 1],
        [3],
        [],
        [7, 7, 7, 7]
    ]
    for i, arr in enumerate(test_cases, 1):
        original = arr.copy()
        sorted_arr = selection_sort(arr.copy())
        print(f"用例{i}: {arr}")
        print(f"原始数据：{original}")
        print(f"排序结果：{sorted_arr}")
        print()
    
    print("=" * 50)
    print("手动输入测试")
    print("--" * 50)
    
    try:
        input_str = input("请输入要排序的数字（用空格分隔）：")
        if input_str.strip():
            user_arr = [int(x) for x in input_str.split()]
            print(f"原始数组：{user_arr}")
            sorted_arr = selection_sort(user_arr)
            print(f"排序结果：{sorted_arr}")
        else:
            print("未输入数据，跳过手动测试")
    except ValueError:
        print("输入格式错误，请输入数字并用空格分隔")
    except KeyboardInterrupt:
        print("\n手动输入已取消")
    
    print("\n" + "=" * 50)
    print("测试完成！")
    print("=" * 50)
if __name__ == "__main__":
    test()

排序算法运行结果
--------------------------------------------
选择排序算法测试
--------------------------------------------
用例1: [64, 25, 12, 22, 11]
原始数据：[64, 25, 12, 22, 11]
排序结果：[11, 12, 22, 25, 64]

用例2: [5, 2, 8, 1, 9, 3]
原始数据：[5, 2, 8, 1, 9, 3]
排序结果：[1, 2, 3, 5, 8, 9]

用例3: [1, 2, 3, 4, 5]
原始数据：[1, 2, 3, 4, 5]
排序结果：[1, 2, 3, 4, 5]

3. Fortune 500 数据分析
导入库
%matplotlib inline
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
读取数据
df = pd.read_csv(r'D:\下载\fortune500.csv')

# 查看数据
df.head()
df.tail()

# 重命名列
df.columns = ['year', 'rank', 'company', 'revenue', 'profit']
数据清洗

# 查看数据类型
df.dtypes

# 处理非数值型利润数据
non_numeric_profits = df.profit.str.contains('[-0.9.-]')
df.loc[non_numeric_profits].head()

# 查看非数值数据数量
len(df.profit[non_numeric_profits])

# 绘制非数值数据分布
bin_sizes, _, _ = plt.hist(df.year[non_numeric_profits], bins=range(1955, 2006))

# 过滤并转换数据类型
df = df.loc[~non_numeric_profits]
df.profit = df.profit.apply(pd.to_numeric)

# 查看清洗后数据
len(df)
df.dtypes
数据分析与可视化
# 按年份分组统计
group_by_year = df.loc[:, ['year', 'revenue', 'profit']].groupby('year')
avgs = group_by_year.mean()
x = avgs.index
y1 = avgs.profit

# 定义绘图函数
def plot(x, y, ax, title, y_label):
    ax.set_title(title)
    ax.set_ylabel(y_label)
    ax.plot(x, y)

# 绘制利润趋势图
fig, ax = plt.subplots()
plot(x, y1, ax, 'Increase in mean Fortune 500 company profits from 1955 to 2005', 'Profit (millions)')

# 绘制收入趋势图
y2 = avgs.revenue
fig, ax = plt.subplots()
plot(x, y2, ax, 'Increase in mean Fortune 500 company revenues from 1955 to 2005', 'Revenue (millions)')

# 带标准差的绘图函数
def plot_with_std(x, y, stds, ax, title, y_label):
    ax.fill_between(x, y - stds, y + stds, alpha=0.2)
    plot(x, y, ax, title, y_label)

# 创建子图展示带标准差的数据
fig, (ax1, ax2) = plt.subplots(ncols=2)
title = 'Increase in mean and std Fortune 500 company %s from 1955 to 2005'
stds1 = group_by_year.std().profit.values
stds2 = group_by_year.std().revenue.values
plot_with_std(x, y1.values, stds1, ax1, title % 'profits', 'Profit (millions)')
plot_with_std(x, y2.values, stds2, ax2, title % 'revenues', 'Revenue (millions)')
fig.set_size_inches(14, 4)
fig.tight_layout()

完整可视化图表
python
group_by_year = df.loc[:, ['year', 'revenue', 'profit']].groupby('year')
avg = group_by_year.mean()
stds = group_by_year.std()

x = avg.index
profits_mean = avg.profit
revenues_mean = avg.revenue
profits_std = stds.profit
revenues_std = stds.revenue

# 创建图表
fig, ax = plt.subplots(figsize=(12, 6))

ax.plot(x, profits_mean, label='Profit (mean)', color='blue', linewidth=2)
ax.fill_between(x, profits_mean - profits_std, profits_mean + profits_std,
    alpha=0.2, color='blue', label='Profit ±1 std')

ax.plot(x, revenues_mean, label='Revenue (mean)', color='red', linestyle='--', linewidth=2)
ax.fill_between(x, revenues_mean - revenues_std, revenues_mean + revenues_std,
    alpha=0.2, color='red', label='Revenue ±1 std')

ax.set_title('Fortune 500 Companies: Mean Profit & Revenue (with Std Dev)', fontsize=14)
ax.set_xlabel('Year', fontsize=12)
ax.set_ylabel('Amount (millions)', fontsize=12)
ax.legend()
ax.grid(True, linestyle=':', alpha=0.6)
ax.margins(x=0.02, y=0.05)

plt.tight_layout()
plt.show()

运行方式
方式一：Jupyter Notebook
打开 Jupyter Notebook
打开 Untitled1.ipynb
依次运行每个代码单元格

📁 项目结构
text
project/
├── README.md              # 项目说明文档
├── Untitled1.ipynb        # Jupyter Notebook 源文件
└── fortune500.csv         # 数据集文件（需要自行准备）

⚠️ 注意事项
数据文件路径：fortune500.csv 默认路径为 D:\下载\fortune500.csv，如果文件在其他位置，需要修改代码中的路径

首次运行：需要先安装依赖库：
bash
pip install numpy pandas matplotlib seaborn
Jupyter 内联绘图：代码中的 %matplotlib inline 仅在 Jupyter 中有效，转换为 .py 文件时需要删除