# 第五次作业：花卉图片分类器（基于 TensorFlow Lite Android 应用）
## 📌 项目简介
本项目基于 **TensorFlow + MobileNetV2** 训练花卉分类模型，将模型转换为 **TensorFlow Lite（TFLite）** 轻量化格式，并部署至 Android 移动端，实现本地离线花卉图像识别。
应用可识别 **5 种常见花卉**，具体分类标签如下：

| 中文名称 | 英文名称 | 分类标签 |
|---------|---------|----------|
| 雏菊 | Daisy | daisy |
| 蒲公英 | Dandelion | dandelion |
| 玫瑰 | Roses | roses |
| 向日葵 | Sunflowers | sunflowers |
| 郁金香 | Tulips | tulips |

---

## 🖥️ 整体环境要求
### 一、Python 模型训练环境
用于数据集加载、模型训练、模型转换与验证，基于 Jupyter Notebook 运行。

| 软件/库 | 版本要求 |
|--------|----------|
| Python | 3.9+ |
| TensorFlow | 2.17.0 |
| Jupyter Notebook | 最新版 |
| NumPy | 1.23+ |
| Matplotlib | 3.7+ |

### 二、Android 部署环境
用于移动端项目开发、模型集成与真机测试。

| 软件/组件 | 版本要求 |
|----------|----------|
| Android Studio | 最新正式版 |
| TensorFlow Lite | 2.17.0 |
| 最低兼容 SDK | 21（Android 5.0） |
| 目标编译 SDK | 33 |

---

## 📁 项目整体目录结构
```
FifthAssignment/
├── training/
│   └── 第五次作业_花卉分类器_training.ipynb   # 模型训练、转换完整代码（Jupyter）
├── exported_flower_model/                    # 导出模型与标签文件
│   ├── model.tflite                         # 轻量化TFLite模型（2.5 MB）
│   ├── labels.txt                           # 花卉类别标签文本
│   └── flower_classifier.keras              # 原始Keras训练模型（9.5 MB）
├── TFLClassify-main/                         # Android 客户端完整源码
└── README.md                                # 项目整体说明文档
```

---

## 🚀 第一部分：模型训练与转换（Python 端）
### 1. 安装依赖库
打开终端，执行以下命令安装训练所需依赖：
```bash
pip install tensorflow==2.17.0 matplotlib numpy jupyter
```

### 2. 导入依赖库与全局参数配置
```python
import tarfile
from pathlib import Path
import numpy as np
import tensorflow as tf

# TensorFlow 官方花卉数据集地址
FLOWER_URL = "https://storage.googleapis.com/download.tensorflow.org/example_images/flower_photos.tgz"

# 训练全局参数
DATA_DIR = None                    # 使用官方在线数据集
EXPORT_DIR = "exported_flower_model"  # 模型导出保存目录
EPOCHS = 5                         # 训练轮数
BATCH_SIZE = 32                    # 批次大小
IMAGE_SIZE = 224                   # 模型输入图像尺寸 224×224
LEARNING_RATE = 1e-3               # 学习率
QUANTIZATION = "dynamic"           # 量化方式：动态量化
SEED = 123                         # 随机种子

print("TensorFlow 版本:", tf.__version__)
```

### 3. 加载花卉数据集
自动下载、解压官方花卉数据集，并划分为**训练集、验证集、测试集**，启用缓存与预取加速训练。
```python
def load_flower_datasets(data_dir, image_size, batch_size, seed):
    # 自动下载并解压 TensorFlow 官方花卉数据集
    # 将数据划分为训练集、验证集和测试集
    # 启用缓存和预取加速训练
    return train_ds, val_ds, test_ds, class_names

# 加载数据集
train_ds, val_ds, test_ds, class_names = load_flower_datasets(
    DATA_DIR, IMAGE_SIZE, BATCH_SIZE, SEED
)

print("类别数量:", len(class_names))
print("类别名称:", class_names)
```

**运行输出示例**
```
Found 3670 files belonging to 5 classes.
Using 2936 files for training.
Found 3670 files belonging to 5 classes.
Using 734 files for validation.
类别数量: 5
类别名称: ['daisy', 'dandelion', 'roses', 'sunflowers', 'tulips']
```

### 4. 构建模型（基于 MobileNetV2 迁移学习）
使用预训练 `MobileNetV2` 作为特征提取主干，冻结主干网络权重，仅训练末端分类层，降低训练成本、提升泛化能力。
```python
def build_model(num_classes, image_size, learning_rate):
    inputs = tf.keras.Input(shape=(image_size, image_size, 3), name="image")
    
    # MobileNetV2 图像预处理
    x = tf.keras.applications.mobilenet_v2.preprocess_input(inputs)
    
    # 加载预训练 MobileNetV2（不包含顶层分类层）
    base_model = tf.keras.applications.MobileNetV2(
        input_shape=(image_size, image_size, 3),
        include_top=False,
        weights="imagenet",
        pooling="avg",
    )
    
    # 冻结预训练主干网络
    base_model.trainable = False
    
    x = base_model(x, training=False)
    x = tf.keras.layers.Dropout(0.2)(x)
    outputs = tf.keras.layers.Dense(num_classes, activation="softmax", name="predictions")(x)
    
    model = tf.keras.Model(inputs, outputs)
    model.compile(
        optimizer=tf.keras.optimizers.Adam(learning_rate=learning_rate),
        loss=tf.keras.losses.SparseCategoricalCrossentropy(),
        metrics=["accuracy"],
    )
    return model

model = build_model(len(class_names), IMAGE_SIZE, LEARNING_RATE)
model.summary()
```

**模型结构摘要输出**
```
Layer (type)                 Output Shape    Param #
===================================================
image (InputLayer)           (None, 224, 224, 3)    0
mobilenetv2_1.00_224         (None, 1280)         2,257,984
dropout (Dropout)            (None, 1280)                0
predictions (Dense)          (None, 5)                 6,405
===================================================
Total params: 2,264,389 (8.64 MB)
Trainable params: 6,405 (25.02 KB)
Non-trainable params: 2,257,984 (8.61 MB)
```

### 5. 模型训练与效果评估
执行训练，并在测试集上评估模型整体精度。
```python
# 开始模型训练
history = model.fit(train_ds, validation_data=val_ds, epochs=EPOCHS)

# 在测试集评估模型
loss, accuracy = model.evaluate(test_ds)
print(f"test_loss={loss:.4f}, test_accuracy={accuracy:.4f}")
```

**训练日志输出示例**
```
Epoch 1/5
92/92 - 33s - accuracy: 0.7030 - loss: 0.7797 - val_accuracy: 0.8796 - val_loss: 0.4228
Epoch 2/5
92/92 - 22s - accuracy: 0.8631 - loss: 0.4031 - val_accuracy: 0.8586 - val_loss: 0.3817
Epoch 3/5
92/92 - 22s - accuracy: 0.8883 - loss: 0.3294 - val_accuracy: 0.8874 - val_loss: 0.3130
Epoch 4/5
92/92 - 23s - accuracy: 0.8913 - loss: 0.3001 - val_accuracy: 0.8979 - val_loss: 0.2899
Epoch 5/5
92/92 - 22s - accuracy: 0.9186 - loss: 0.2455 - val_accuracy: 0.9005 - val_loss: 0.2698

test_loss=0.3275, test_accuracy=0.8864
```

### 6. 模型转换为 TensorFlow Lite 格式
对模型进行**动态量化**压缩，转换为移动端适配的 TFLite 格式，同时保存原始 Keras 模型与类别标签文件。
```python
def convert_to_tflite(model, quantization, representative_ds):
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    
    if quantization == "dynamic":
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
    elif quantization == "float16":
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
        converter.target_spec.supported_types = [tf.float16]
    elif quantization == "int8":
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
        
        def representative_data_gen():
            for images, _ in representative_ds.take(100):
                for image in images:
                    yield [tf.expand_dims(tf.cast(image, tf.float32), 0)]
        
        converter.representative_dataset = representative_data_gen
        converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
        converter.inference_input_type = tf.uint8
        converter.inference_output_type = tf.uint8
    
    return converter.convert()

# 创建模型导出目录
export_dir = Path(EXPORT_DIR)
export_dir.mkdir(parents=True, exist_ok=True)

# 保存类别标签文件
labels_path = export_dir / "labels.txt"
labels_path.write_text("\n".join(class_names) + "\n", encoding="utf-8")

# 保存原始 Keras 模型
keras_path = export_dir / "flower_classifier.keras"
model.save(keras_path)

# 转换并保存 TFLite 轻量化模型
tflite_model = convert_to_tflite(model, QUANTIZATION, train_ds)
tflite_path = export_dir / "model.tflite"
tflite_path.write_bytes(tflite_model)

print(f"已保存 Keras 模型: {keras_path}")
print(f"已保存 TFLite 模型: {tflite_path}")
print(f"已保存标签文件: {labels_path}")
```

### 7. TFLite 模型本地验证
加载转换后的 TFLite 模型，使用测试集图片完成推理验证，确保模型转换有效。
```python
def smoke_test_tflite(tflite_path, test_ds, class_names):
    interpreter = tf.lite.Interpreter(model_path=str(tflite_path))
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()[0]
    output_details = interpreter.get_output_details()[0]
    
    # 取测试集中的图片进行推理
    images, labels = next(iter(test_ds.unbatch().batch(8)))
    input_data = tf.cast(images, input_details["dtype"]).numpy()
    
    # 处理 uint8 类型输入
    if input_details["dtype"] == np.uint8:
        scale, zero_point = input_details["quantization"]
        if scale:
            input_data = images.numpy() / scale + zero_point
            input_data = np.clip(input_data, 0, 255).astype(np.uint8)
    
    # 执行推理
    predictions = []
    for image in input_data:
        interpreter.set_tensor(input_details["index"], np.expand_dims(image, 0))
        interpreter.invoke()
        predictions.append(interpreter.get_tensor(output_details["index"])[0])
    
    # 输出预测结果
    predicted_ids = np.argmax(np.asarray(predictions), axis=1)
    for expected, predicted in zip(labels.numpy()[:5], predicted_ids[:5]):
        print(f"真实类别={class_names[expected]}, 预测类别={class_names[predicted]}")

smoke_test_tflite(tflite_path, test_ds, class_names)
```

**验证输出示例**
```
真实类别=daisy, 预测类别=daisy
真实类别=tulips, 预测类别=tulips
真实类别=sunflowers, 预测类别=sunflowers
真实类别=daisy, 预测类别=daisy
真实类别=sunflowers, 预测类别=sunflowers
```

---

## 📱 第二部分：Android 端部署流程
### 1. 模型与标签文件导入
将训练导出的模型、标签文件复制到 Android 项目 `assets` 资源目录：

| 源文件路径 | Android 目标存放路径 | 重命名 |
|-----------|----------------------|--------|
| exported_flower_model/model.tflite | app/src/main/assets/ | FlowerModel.tflite |
| exported_flower_model/labels.txt | app/src/main/assets/ | labels.txt |

### 2. 配置 Gradle 依赖
在模块级 `build.gradle` 中添加 TensorFlow Lite 相关依赖：
```gradle
dependencies {
    // TensorFlow Lite 核心依赖
    implementation 'org.tensorflow:tensorflow-lite:2.17.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-metadata:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.17.0'
}
```

### 3. Kotlin 代码加载并初始化 TFLite 模型
在 `MainActivity.kt` 中读取 assets 目录下的模型文件，初始化推理解释器：
```kotlin
// 加载 assets 中的 TFLite 模型
val modelFile = FileUtil.loadMappedFile(ctx, "FlowerModel.tflite")
val interpreter = Interpreter(modelFile)
```

---

## 📊 模型整体性能指标
| 性能指标 | 具体数值 |
|----------|----------|
| 测试集准确率 | 88.64% |
| TFLite 模型体积 | ~2.5 MB |
| 模型输入尺寸 | 224 × 224 × 3（RGB图像） |
| 输出分类数量 | 5 类花卉 |
| 移动端 CPU 单图推理耗时 | 30 ~ 50 ms/张 |

---

## 📚 参考资源
1. TensorFlow 官方花卉数据集
2. TensorFlow Lite 官方开发文档
3. MobileNetV2 网络论文与技术文档
