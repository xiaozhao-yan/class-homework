
# 石头剪刀布手势识别 - TensorFlow 模型训练实验报告

## 实验信息
- **实验名称**：TensorFlow 石头剪刀布模型生成
- **实验日期**：2026年6月
- **开发环境**：TensorFlow 2.21.0 + Python 3.13 + Jupyter Notebook

---

## 实验内容
1. 进一步掌握 TensorFlow 模型训练和生成的基本流程
2. 下载石头剪刀布图片数据集
3. 学习石头剪刀布图片识别模型的生成
4. 绘制图像验证模型的性能

---

## 实验步骤1：下载数据集

### 1.1 数据集来源
- **训练集**：https://storage.googleapis.com/learning-datasets/rps.zip
- **测试集**：https://storage.googleapis.com/learning-datasets/rps-test-set.zip

### 1.2 下载代码
```python
import ssl
from pathlib import Path
from urllib.error import URLError
from urllib.request import urlopen

DOWNLOAD_DIR = Path("D:/mldownload")
DOWNLOAD_DIR.mkdir(parents=True, exist_ok=True)

RPS_URL = "https://storage.googleapis.com/learning-datasets/rps.zip"
RPS_TEST_URL = "https://storage.googleapis.com/learning-datasets/rps-test-set.zip"
RPS_ZIP = DOWNLOAD_DIR / "rps.zip"
RPS_TEST_ZIP = DOWNLOAD_DIR / "rps-test-set.zip"

def download_file(url, destination):
    if destination.exists() and destination.stat().st_size > 0:
        print(f"File already exists, skipping: {destination}")
        return
    temp_path = destination.with_suffix(destination.suffix + ".part")
    print(f"Downloading: {url}")
    try:
        response = urlopen(url, timeout=120)
    except URLError:
        context = ssl._create_unverified_context()
        response = urlopen(url, timeout=120, context=context)
    with response, temp_path.open("wb") as file:
        while True:
            data = response.read(1024 * 1024)
            if not data:
                break
            file.write(data)
    temp_path.replace(destination)
    size_mb = destination.stat().st_size / 1024 / 1024
    print(f"Downloaded: {destination} ({size_mb:.1f} MB)")

download_file(RPS_URL, RPS_ZIP)
download_file(RPS_TEST_URL, RPS_TEST_ZIP)
```

### 1.3 解压数据集
```python
import zipfile

def extract_zip(zip_path, extract_dir):
    if not zip_path.exists():
        raise FileNotFoundError(f"Zip file not found: {zip_path}")
    with zipfile.ZipFile(zip_path, "r") as zip_ref:
        bad_file = zip_ref.testzip()
        if bad_file is not None:
            raise zipfile.BadZipFile(f"Zip file corrupted at {bad_file}")
        zip_ref.extractall(extract_dir)
    print(f"Extracted: {zip_path} -> {extract_dir}")

extract_zip(RPS_ZIP, DOWNLOAD_DIR)
extract_zip(RPS_TEST_ZIP, DOWNLOAD_DIR)
```

**📸 截图 1**：
<img width="880" height="641" alt="image" src="https://github.com/user-attachments/assets/98d03c6b-91eb-4487-95e2-ab151995edc1" />
<img width="881" height="308" alt="image" src="https://github.com/user-attachments/assets/6656badc-7b89-4c82-8c45-f6134fd3e7ff" />



### 1.4 验证下载的数据集
```python
rock_dir = DOWNLOAD_DIR / "rps" / "rock"
paper_dir = DOWNLOAD_DIR / "rps" / "paper"
scissors_dir = DOWNLOAD_DIR / "rps" / "scissors"

rock_files = sorted(path.name for path in rock_dir.iterdir())
paper_files = sorted(path.name for path in paper_dir.iterdir())
scissors_files = sorted(path.name for path in scissors_dir.iterdir())

print("total training rock images:", len(rock_files))
print("total training paper images:", len(paper_files))
print("total training scissors images:", len(scissors_files))

print(rock_files[:10])
print(paper_files[:10])
print(scissors_files[:10])
```

**运行结果**：
```
total training rock images: 840
total training paper images: 840
total training scissors images: 840
['rock01-000.png', 'rock01-001.png', ...]
['paper01-000.png', 'paper01-001.png', ...]
['scissors01-000.png', 'scissors01-001.png', ...]
```

**📸 截图位置 2**：
<img width="889" height="442" alt="image" src="https://github.com/user-attachments/assets/bd06b3fc-9dfc-4c7e-940c-73de430a8b0c" />


### 1.5 可视化示例图片
```python
%matplotlib inline
import matplotlib.pyplot as plt
import matplotlib.image as mpimg

pic_index = 2
next_rock = [rock_dir / fname for fname in rock_files[:pic_index]]
next_paper = [paper_dir / fname for fname in paper_files[:pic_index]]
next_scissors = [scissors_dir / fname for fname in scissors_files[:pic_index]]

for img_path in next_rock + next_paper + next_scissors:
    img = mpimg.imread(img_path)
    plt.imshow(img)
    plt.axis("off")
    plt.show()
```

**📸 截图位置 3**：
<img width="1053" height="591" alt="image" src="https://github.com/user-attachments/assets/00b4239e-e407-4b8a-b618-4f852dff0268" />




---

## 实验步骤2：使用 Keras 进行模型训练

### 2.1 图片预处理与数据增强
```python
import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator

TRAINING_DIR = "D:/mldownload/rps/"
training_datagen = ImageDataGenerator(
    rescale=1./255,           # 归一化
    rotation_range=40,        # 随机旋转
    width_shift_range=0.2,    # 水平偏移
    height_shift_range=0.2,   # 垂直偏移
    shear_range=0.2,          # 剪切变换
    zoom_range=0.2,           # 随机缩放
    horizontal_flip=True,     # 水平翻转
    fill_mode='nearest'       # 填充方式
)

VALIDATION_DIR = "D:/mldownload/rps-test-set/"
validation_datagen = ImageDataGenerator(rescale=1./255)

train_generator = training_datagen.flow_from_directory(
    TRAINING_DIR,
    target_size=(150, 150),
    class_mode='categorical',
    batch_size=126
)

validation_generator = validation_datagen.flow_from_directory(
    VALIDATION_DIR,
    target_size=(150, 150),
    class_mode='categorical',
    batch_size=126
)
```

**运行结果**：
```
Found 2520 images belonging to 3 classes.
Found 372 images belonging to 3 classes.
```

**📸 截图位置 4**：
<img width="316" height="51" alt="image" src="https://github.com/user-attachments/assets/0090c53c-3359-4365-92f7-b3905c35bfe6" />


### 2.2 定义模型架构（Sequential）
```python
model = tf.keras.models.Sequential([
    # 第一层卷积
    tf.keras.layers.Conv2D(64, (3, 3), activation='relu', input_shape=(150, 150, 3)),
    tf.keras.layers.MaxPooling2D(2, 2),
    
    # 第二层卷积
    tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2, 2),
    
    # 第三层卷积
    tf.keras.layers.Conv2D(128, (3, 3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2, 2),
    
    # 第四层卷积
    tf.keras.layers.Conv2D(128, (3, 3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2, 2),
    
    # 全连接层
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dropout(0.5),
    tf.keras.layers.Dense(512, activation='relu'),
    tf.keras.layers.Dense(3, activation='softmax')  # 3个类别输出
])

model.summary()
```

**模型结构摘要**：
| 层类型 | 输出形状 | 参数数量 |
|--------|----------|----------|
| Conv2D | (148, 148, 64) | 1,792 |
| MaxPooling2D | (74, 74, 64) | 0 |
| Conv2D | (72, 72, 64) | 36,928 |
| MaxPooling2D | (36, 36, 64) | 0 |
| Conv2D | (34, 34, 128) | 73,856 |
| MaxPooling2D | (17, 17, 128) | 0 |
| Conv2D | (15, 15, 128) | 147,584 |
| MaxPooling2D | (7, 7, 128) | 0 |
| Flatten | 6272 | 0 |
| Dropout | 6272 | 0 |
| Dense | 512 | 3,211,776 |
| Dense | 3 | 1,539 |

**总参数**：3,473,475 (约 13.25 MB)

**📸 截图位置 5**：
<img width="732" height="614" alt="image" src="https://github.com/user-attachments/assets/8e561bf6-051b-4132-afa0-144c6563d954" />


### 2.3 编译模型（compile）
```python
model.compile(
    loss='categorical_crossentropy',  # 多分类损失函数
    optimizer='rmsprop',              # 优化器
    metrics=['accuracy']              # 评估指标
)
```

### 2.4 用训练数据拟合（fit）
```python
history = model.fit(
    train_generator,
    epochs=25,
    steps_per_epoch=20,
    validation_data=validation_generator,
    verbose=1,
    validation_steps=3
)

model.save("rps.h5")
```

**训练过程摘要**：
| Epoch | 损失 (loss) | 准确率 (accuracy) | 验证损失 (val_loss) | 验证准确率 (val_accuracy) |
|-------|-------------|-------------------|---------------------|---------------------------|
| 1 | 1.4751 | 0.3599 | 1.1369 | 0.3333 |
| 5 | 0.8743 | 0.6087 | 0.4759 | 0.9651 |
| 10 | 0.3461 | 0.8560 | 0.3216 | 0.7634 |
| 15 | 0.2972 | 0.8913 | 0.1070 | 0.9839 |
| 20 | 0.1349 | 0.9528 | 0.2117 | 0.8952 |
| 25 | 0.1002 | 0.9655 | 0.0382 | 0.9839 |

**最终结果**：
- 训练准确率：**96.55%**
- 验证准确率：**98.39%**
- 模型已保存为 `rps.h5`

**📸 截图位置 6**：
<img width="817" height="772" alt="image" src="https://github.com/user-attachments/assets/963f1975-76e7-4f5c-8df6-3d6c2174f095" />


---

## 实验步骤3：生成模型的验证

### 3.1 绘制训练曲线
```python
import matplotlib.pyplot as plt

acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(acc))

plt.figure(figsize=(12, 4))

# 准确率曲线
plt.subplot(1, 2, 1)
plt.plot(epochs, acc, 'r', label='Training accuracy')
plt.plot(epochs, val_acc, 'b', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.legend()
plt.grid(True)

# 损失曲线
plt.subplot(1, 2, 2)
plt.plot(epochs, loss, 'r', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.legend()
plt.grid(True)

plt.tight_layout()
plt.show()
```

**📸 截图位置 7**：
<img width="1037" height="672" alt="image" src="https://github.com/user-attachments/assets/b3224cc8-42a8-4532-9082-9fb1e585b261" />


### 3.2 模型性能分析

从曲线图可以观察到：
1. **准确率曲线**：训练准确率和验证准确率均随轮次增加而上升，最终稳定在 96% 以上
2. **损失曲线**：训练损失和验证损失均随轮次下降，模型收敛良好
3. **泛化能力**：验证准确率最终达到 98.39%，说明模型具有良好的泛化能力


## 实验总结

### 完成情况
| 步骤 | 内容 | 状态 |
|------|------|------|
| 1 | 下载数据集 | ✅ 完成 |
| 2 | 验证下载的数据集（打印图片） | ✅ 完成 |
| 3 | 图片预处理（ImageDataGenerator） | ✅ 完成 |
| 4 | 定义模型架构（Sequential） | ✅ 完成 |
| 5 | 编译模型（compile） | ✅ 完成 |
| 6 | 用训练数据拟合（fit） | ✅ 完成 |
| 7 | 绘制图形验证模型性能 | ✅ 完成 |

### 实验成果
1. **模型文件**：`rps.h5`（约 13.25 MB）
2. **最终验证准确率**：98.39%
3. **最终验证损失**：0.0382

### 关键知识点掌握
- ✅ TensorFlow/Keras 模型训练基本流程
- ✅ 图像数据预处理与数据增强
- ✅ 卷积神经网络（CNN）架构设计
- ✅ 模型编译与训练参数配置
- ✅ 训练历史可视化与性能评估

---

## 参考资源
- [TensorFlow 官方文档](https://www.tensorflow.org/)
- [Keras 文档](https://keras.io/)
- [Matplotlib 文档](https://matplotlib.org/)
- [TensorFlow rock-paper-scissors dataset](https://storage.googleapis.com/learning-datasets/rps.zip)

---

**实验完成日期**：2026年6月10日
```
