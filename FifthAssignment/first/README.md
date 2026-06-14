# 第五次作业：花卉图片分类器

## 📌 项目简介

本项目实现了一个花卉图片分类器，使用 TensorFlow/Keras 训练 MobileNetV2 模型，并转换为 TFLite 格式部署到 Android 应用中。应用可以实时识别 5 种花卉：雏菊、蒲公英、玫瑰、向日葵、郁金香。

**核心特点**：
- 不依赖 `tflite-model-maker`，避免版本冲突问题
- 使用迁移学习（MobileNetV2 + ImageNet 预训练权重）
- 模型转换为 TFLite 格式，支持动态量化
- 部署到 Android 应用，实现实时相机识别

---

## 📁 项目结构

```
FifthAssignment/
├── TFLClassify-main/              # Android 项目源码
│   ├── finish/                    # 已完成项目（参考实现）
│   └── start/                     # 实践项目
│       └── src/main/
│           ├── assets/            # 模型和标签文件位置
│           │   ├── FlowerModel.tflite
│           │   └── labels.txt
│           └── ml/                # 备用模型位置
│               └── FlowerModel.tflite
├── exported_flower_model/         # 训练生成的模型文件
│   ├── model.tflite               # TFLite 模型
│   ├── labels.txt                 # 类别标签
│   └── flower_classifier.keras    # Keras 原始模型
├── 花卉分类器_training.ipynb      # Jupyter 训练代码
├── 花卉分类器_training.md          # 训练代码文档
├── images/                        # 截图文件夹
│   ├── run.png                    # 运行效果图
│   └── 测试.jpg                    # 测试截图
└── README.md                      # 项目说明文档
```
---

## 🖥️ 开发环境

### Python 环境（模型训练）

| 工具 | 版本 | 说明 |
|------|------|------|
| Python | 3.10+ | 推荐 3.10 |
| TensorFlow | 2.16.1+ | 模型训练与转换 |
| Jupyter Notebook | 最新版 | 代码交互 |
| NumPy | 1.23+ | 数值计算 |
| Matplotlib | 3.7+ | 图表绘制 |

### Android 环境（应用部署）

| 工具 | 版本 |
|------|------|
| Android Studio | 4.1+ |
| Kotlin | 1.9+ |
| TensorFlow Lite | 2.16.1 |
| 最低 SDK | API 21 |
| 目标 SDK | API 33 |

---

## 🚀 模型训练步骤

### 1. 安装依赖

如果当前 Jupyter kernel 里还没有安装 TensorFlow，请先行安装。建议使用 Python 3.10 或更高版本。

```bash
# 可选：如果当前 notebook 环境还没有安装依赖，取消下一行开头的 # 后运行。
# %pip install -r requirements-modern.txt
```

`requirements-modern.txt` 的内容：
```
tensorflow>=2.15
matplotlib>=3.7
numpy>=1.23
```

### 2. 导入库并设置参数

这里会导入训练、数据读取、模型转换需要的库。`FLOWER_URL` 是 TensorFlow 官方示例花卉数据集的下载地址。

```python
import tarfile
from pathlib import Path

import numpy as np
import tensorflow as tf

# TensorFlow 官方花卉数据集。第一次运行时会自动下载，之后会复用本地缓存。
FLOWER_URL = "https://storage.googleapis.com/download.tensorflow.org/example_images/flower_photos.tgz"

print("TensorFlow 版本:", tf.__version__)
```

**训练参数配置**：

```python
# 数据目录配置：
# - DATA_DIR = None：自动下载并使用 TensorFlow 官方 flowers 数据集。
# - DATA_DIR = r"D:\path\to\my_images"：使用你自己的图片分类目录。
#
# 自定义图片目录需要按类别分文件夹，例如：
# my_images/
#   daisy/
#     1.jpg
#   roses/
#     2.jpg
DATA_DIR = None

# 导出目录。训练完成后会在这里生成 model.tflite、labels.txt 和 flower_classifier.keras。
EXPORT_DIR = "exported_flower_model"

# 训练参数。教程演示可以先用 3 到 5 个 epoch；如果使用自己的数据，可以适当增加。
EPOCHS = 5
BATCH_SIZE = 32
IMAGE_SIZE = 224
LEARNING_RATE = 1e-3

# TFLite 量化方式：
# - "dynamic"：默认推荐，模型更小，通常最容易成功。
# - "float16"：适合部分支持 float16 的设备。
# - "int8"：体积更小，但需要代表性数据集，转换要求更严格。
# - "none"：不量化，保留浮点模型。
QUANTIZATION = "dynamic"

# 固定随机种子，方便训练/验证划分尽量可复现。
SEED = 123
```

> **📸 截图位置**：
> <img width="1228" height="1026" alt="1ecd97a26e00ba198385b6ceed60907f" src="https://github.com/user-attachments/assets/8e1ac64a-68da-4934-bdb9-77e1feabd6c8" />


### 3. 读取并划分数据集

`load_flower_datasets` 完成三件事：
- 如果 `DATA_DIR` 是 `None`，自动下载并解压官方花卉数据集。
- 使用 `image_dataset_from_directory` 按文件夹名生成分类标签。
- 将数据划分为训练集、验证集和测试集，并开启缓存、打乱与预取，加快训练过程。

```python
def load_flower_datasets(data_dir, image_size, batch_size, seed):
    # 如果没有传入自定义数据目录，就下载 TensorFlow 官方 flower_photos 数据集。
    if data_dir is None:
        archive_path = tf.keras.utils.get_file(
            "flower_photos.tgz",
            FLOWER_URL,
            extract=False,
        )
        archive_path = Path(archive_path)

        # Keras 可能已经缓存了解压后的目录；先检查常见位置，避免重复解压。
        candidates = [
            archive_path.parent / "flower_photos",
            archive_path.parent / "flower_photos_extracted" / "flower_photos",
        ]
        data_dir = next((path for path in candidates if path.exists()), None)
        if data_dir is None:
            with tarfile.open(archive_path, "r:gz") as tar:
                tar.extractall(archive_path.parent / "flower_photos_extracted")
            data_dir = archive_path.parent / "flower_photos_extracted" / "flower_photos"
    else:
        data_dir = Path(data_dir)

    # 从目录读取图片。目录下的每个子文件夹会被当作一个类别。
    train_ds = tf.keras.utils.image_dataset_from_directory(
        data_dir,
        validation_split=0.2,
        subset="training",
        seed=seed,
        image_size=(image_size, image_size),
        batch_size=batch_size,
    )
    val_ds = tf.keras.utils.image_dataset_from_directory(
        data_dir,
        validation_split=0.2,
        subset="validation",
        seed=seed,
        image_size=(image_size, image_size),
        batch_size=batch_size,
    )
    class_names = train_ds.class_names

    # 原始 validation 部分再拆成验证集和测试集：验证集用于训练过程中观察效果，测试集用于最后评估。
    val_batches = int(tf.data.experimental.cardinality(val_ds).numpy())
    test_ds = val_ds.take(val_batches // 2)
    val_ds = val_ds.skip(val_batches // 2)

    # cache/prefetch 可以减少数据读取等待；shuffle 只用于训练集。
    autotune = tf.data.AUTOTUNE
    train_ds = train_ds.cache().shuffle(1000, seed=seed).prefetch(autotune)
    val_ds = val_ds.cache().prefetch(autotune)
    test_ds = test_ds.cache().prefetch(autotune)
    return train_ds, val_ds, test_ds, class_names

# 加载数据集并查看类别名称。
train_ds, val_ds, test_ds, class_names = load_flower_datasets(
    DATA_DIR,
    IMAGE_SIZE,
    BATCH_SIZE,
    SEED,
)

print("类别数量:", len(class_names))
print("类别名称:", class_names)
```

**输出**：
```
Found 3670 files belonging to 5 classes.
Using 2936 files for training.
Found 3670 files belonging to 5 classes.
Using 734 files for validation.
类别数量: 5
类别名称: ['daisy', 'dandelion', 'roses', 'sunflowers', 'tulips']
```

> **📸 截图位置**：
> <img width="953" height="1088" alt="97829e6fe83dbc6b89f0acd713ff704c" src="https://github.com/user-attachments/assets/126f784e-35d0-418b-8a6b-14e616ba196b" />


### 4. 构建并训练 Keras 模型

这里使用迁移学习：底座模型是 ImageNet 预训练的 MobileNetV2，它已经学过很多通用图像特征。我们冻结底座模型，只训练最后新增的分类层。这样做的好处是训练速度快、需要的数据量少，也更适合后续转换为移动端可用的 TFLite 模型。

```python
def build_model(num_classes, image_size, learning_rate):
    # 输入图片尺寸固定为 IMAGE_SIZE x IMAGE_SIZE x 3。
    inputs = tf.keras.Input(shape=(image_size, image_size, 3), name="image")

    # MobileNetV2 有自己的预处理方式，这里把像素值转换到模型期望的范围。
    x = tf.keras.applications.mobilenet_v2.preprocess_input(inputs)

    # include_top=False 表示不要 ImageNet 原始的 1000 类分类头，只保留特征提取部分。
    base_model = tf.keras.applications.MobileNetV2(
        input_shape=(image_size, image_size, 3),
        include_top=False,
        weights="imagenet",
        pooling="avg",
    )

    # 冻结预训练模型参数，只训练后面的 Dense 分类层。
    base_model.trainable = False
    x = base_model(x, training=False)
    x = tf.keras.layers.Dropout(0.2)(x)

    # 输出维度等于类别数量，softmax 输出每个类别的概率。
    outputs = tf.keras.layers.Dense(num_classes, activation="softmax", name="predictions")(x)
    model = tf.keras.Model(inputs, outputs)

    model.compile(
        optimizer=tf.keras.optimizers.Adam(learning_rate=learning_rate),
        loss=tf.keras.losses.SparseCategoricalCrossentropy(),
        metrics=["accuracy"],
    )
    return model

# 创建模型并打印结构。第一次运行会下载 MobileNetV2 的 ImageNet 预训练权重。
model = build_model(len(class_names), IMAGE_SIZE, LEARNING_RATE)
model.summary()
```

**模型结构摘要**：

| 层 (Layer) | 输出形状 (Output Shape) | 参数数量 (Param #) |
|------------|------------------------|-------------------|
| image (InputLayer) | (None, 224, 224, 3) | 0 |
| mobilenetv2_1.00_224 (Functional) | (None, 1280) | 2,257,984 |
| dropout (Dropout) | (None, 1280) | 0 |
| predictions (Dense) | (None, 5) | 6,405 |
| **总计** | | **2,264,389** |
| 可训练参数 | | 6,405 |
| 不可训练参数 | | 2,257,984 |

> **📸 截图位置**：
> <img width="1066" height="1181" alt="74b4f6c82532531a3d1e90ca49a835c5" src="https://github.com/user-attachments/assets/bb960736-925e-4286-8b2a-8016c6934ca1" />


```python
# 开始训练。history 中会保存每个 epoch 的 loss、accuracy、val_loss、val_accuracy。
history = model.fit(train_ds, validation_data=val_ds, epochs=EPOCHS)

# 使用测试集评估模型。测试集没有参与训练，用于更客观地观察最终效果。
loss, accuracy = model.evaluate(test_ds)
print(f"test_loss={loss:.4f}, test_accuracy={accuracy:.4f}")
```

**训练过程输出**：

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

**训练结果汇总**：

| Epoch | 训练准确率 | 验证准确率 | 训练损失 | 验证损失 |
|-------|------------|------------|----------|----------|
| 1 | 70.30% | 87.96% | 0.7797 | 0.4228 |
| 2 | 86.31% | 85.86% | 0.4031 | 0.3817 |
| 3 | 88.83% | 88.74% | 0.3294 | 0.3130 |
| 4 | 89.13% | 89.79% | 0.3001 | 0.2899 |
| 5 | 91.86% | 90.05% | 0.2455 | 0.2698 |

**测试集最终结果**：
- 测试损失 (test_loss): **0.3275**
- 测试准确率 (test_accuracy): **0.8864 (88.64%)**

 **📸 截图位置**：
> <img width="1075" height="326" alt="97acf019dde69a9ff2b798ffdca22dad" src="https://github.com/user-attachments/assets/5e2ab7a6-be0c-4c7d-bf57-3e0ebaaabb3c" />

### 5. 转换为 TensorFlow Lite 模型

训练完成后，先把 Keras 模型保存为 .keras 文件，再使用 `tf.lite.TFLiteConverter.from_keras_model(model)` 转换为 .tflite。本教程默认使用动态范围量化 `dynamic`，通常可以减小模型体积，并且不需要额外准备复杂的校准数据。

```python
def convert_to_tflite(model, quantization, representative_ds):
    # 从 Keras 模型创建 TFLite 转换器。
    converter = tf.lite.TFLiteConverter.from_keras_model(model)

    if quantization == "dynamic":
        # 动态范围量化：最常用、最容易成功的压缩方式。
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
    elif quantization == "float16":
        # float16 量化：权重使用半精度浮点数，适合部分移动端/GPU 场景。
        converter.optimizations = [tf.lite.Optimize.DEFAULT]
        converter.target_spec.supported_types = [tf.float16]
    elif quantization == "int8":
        # int8 全整数量化：体积更小，但需要代表性数据集校准输入分布。
        converter.optimizations = [tf.lite.Optimize.DEFAULT]

        def representative_data_gen():
            for images, _ in representative_ds.take(100):
                for image in images:
                    yield [tf.expand_dims(tf.cast(image, tf.float32), 0)]

        converter.representative_dataset = representative_data_gen
        converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
        converter.inference_input_type = tf.uint8
        converter.inference_output_type = tf.uint8
    elif quantization != "none":
        raise ValueError(f"Unsupported quantization mode: {quantization}")

    return converter.convert()

# 创建导出目录。
export_dir = Path(EXPORT_DIR)
export_dir.mkdir(parents=True, exist_ok=True)

# 保存标签文件。部署时需要 labels.txt 把模型输出编号映射回类别名称。
labels_path = export_dir / "labels.txt"
labels_path.write_text("\n".join(class_names) + "\n", encoding="utf-8")

# 保存 Keras 原始模型，便于以后继续训练或重新转换。
keras_path = export_dir / "flower_classifier.keras"
model.save(keras_path)

# 转换并保存 TFLite 模型。
tflite_model = convert_to_tflite(model, QUANTIZATION, train_ds)
tflite_path = export_dir / "model.tflite"
tflite_path.write_bytes(tflite_model)

print(f"已保存 Keras 模型: {keras_path}")
print(f"已保存 TFLite 模型: {tflite_path}")
print(f"已保存标签文件: {labels_path}")
```

> **📸 截图位置**：
> <img width="1179" height="1240" alt="8c0dc8f0cd0b7227516b40a12f120c0f" src="https://github.com/user-attachments/assets/a3ca3f71-7bc7-47d6-90a3-403f5f4211f5" />


### 6. 简单测试导出的 TFLite 模型

最后用 `tf.lite.Interpreter` 加载刚导出的 .tflite 文件，取几张测试图片做推理，确认模型文件可以正常运行。这里的 smoke test 不是完整评估，只是快速检查：模型能否加载、输入输出张量是否正常、预测流程是否能跑通。

```python
def smoke_test_tflite(tflite_path, test_ds, class_names):
    # 加载 TFLite 模型并分配张量内存。
    interpreter = tf.lite.Interpreter(model_path=str(tflite_path))
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()[0]
    output_details = interpreter.get_output_details()[0]

    # 从测试集中取 8 张图片做快速推理。
    images, labels = next(iter(test_ds.unbatch().batch(8)))
    input_data = tf.cast(images, input_details["dtype"]).numpy()

    # 如果模型是 uint8 输入，需要按照量化参数把图片转换到对应范围。
    if input_details["dtype"] == np.uint8:
        scale, zero_point = input_details["quantization"]
        if scale:
            input_data = images.numpy() / scale + zero_point
            input_data = np.clip(input_data, 0, 255).astype(np.uint8)

    predictions = []
    for image in input_data:
        interpreter.set_tensor(input_details["index"], np.expand_dims(image, 0))
        interpreter.invoke()
        predictions.append(interpreter.get_tensor(output_details["index"])[0])

    predicted_ids = np.argmax(np.asarray(predictions), axis=1)
    for expected, predicted in zip(labels.numpy()[:5], predicted_ids[:5]):
        print(f"真实类别={class_names[expected]}, 预测类别={class_names[predicted]}")

# 运行 TFLite 快速测试。
smoke_test_tflite(tflite_path, test_ds, class_names)
```

**测试输出**：
```
真实类别=daisy, 预测类别=daisy
真实类别=tulips, 预测类别=tulips
真实类别=sunflowers, 预测类别=sunflowers
真实类别=daisy, 预测类别=daisy
真实类别=sunflowers, 预测类别=sunflowers
```

> **📸 截图位置**：
> <img width="1179" height="1240" alt="8c0dc8f0cd0b7227516b40a12f120c0f" src="https://github.com/user-attachments/assets/1a7b986e-4fc4-440f-994c-464a20532dc7" />


## 📱 Android 应用部署

### 1. 替换模型文件

将训练生成的模型文件复制到 Android 项目中：

| 源文件 | 目标位置 |
|--------|----------|
| `exported_flower_model/model.tflite` | `start/src/main/ml/FlowerModel.tflite` |
| `exported_flower_model/labels.txt` | `start/src/main/ml/labels.txt` |

> **📸 截图位置**：
> <img width="323" height="399" alt="image" src="https://github.com/user-attachments/assets/c9b55964-cdd0-4c25-a1ed-2e582849c207" />


### 2. 配置 build.gradle

```gradle
dependencies {
    // TensorFlow Lite 依赖
    implementation 'org.tensorflow:tensorflow-lite:2.16.1'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-metadata:0.4.4'
}
```

### 3. 手动加载模型（MainActivity.kt）

```kotlin
private class ImageAnalyzer(ctx: Context, listener: RecognitionListener) {
    private val interpreter: Interpreter
    private val labels: List<String>
    
    init {
        val modelFile = FileUtil.loadMappedFile(ctx, "FlowerModel.tflite")
        interpreter = Interpreter(modelFile)
        labels = loadLabels(ctx)
    }
    
    private fun loadLabels(context: Context): List<String> {
        val inputStream = context.assets.open("labels.txt")
        return BufferedReader(InputStreamReader(inputStream)).useLines { it.toList() }
    }
}
```

### 4. 运行应用

1. 连接 Android 手机（开启开发者模式和 USB 调试）
2. 在 Android Studio 中选择 `start` 模块
3. 点击运行按钮 ▶
4. 授权相机权限
5. 对准花卉进行识别

> **📸 截图位置**：应用在手机上运行的最终识别效果截图 (`images/run.png`)。
> <img width="2559" height="1530" alt="run" src="https://github.com/user-attachments/assets/444e2f2e-39a0-4825-8999-48ae6c2b4b06" />

---

## 📊 识别效果

| 花卉名称 | 标签 | 识别效果 |
|----------|------|----------|
| 雏菊 | daisy | ✅ 正常识别 |
| 蒲公英 | dandelion | ✅ 正常识别 |
| 玫瑰 | roses | ✅ 正常识别 |
| 向日葵 | sunflowers | ✅ 正常识别 |
| 郁金香 | tulips | ✅ 正常识别 |

> **📸 截图位置**：手机对准花卉进行识别的测试截图 (`images/测试.jpg`)。
> > <img width="400"  alt="测试" src="https://github.com/user-attachments/assets/e619e4d3-b1af-420c-95a4-ff59343ef007" />

---

## ⚠️ 常见问题与解决

| 问题 | 原因 | 解决方法 |
|------|------|----------|
| `FULLY_CONNECTED` 错误 | TF 版本与 TFLite 库版本不匹配 | 统一使用 2.16.1 版本，并在转换时添加 `converter.target_spec.supported_ops` 设置 |
| 模型文件找不到 | 文件未放在正确位置 | 将模型放入 `assets` 文件夹，并确保文件名与代码中一致 |
| 相机无法打开 | 未授权相机权限 | 在手机设置中手动授权应用使用相机 |
| 识别准确率低 | 训练轮数不足 | 增加 `EPOCHS` 到 10 或 20，或增加训练数据 |
| Gradle 同步失败 | 网络问题或版本不兼容 | 配置国内镜像源（阿里云、清华源） |

---

## 📚 参考资料

- [TensorFlow 花卉数据集](https://storage.googleapis.com/download.tensorflow.org/example_images/flower_photos.tgz)
- [TensorFlow Lite 官方文档](https://www.tensorflow.org/lite)
- [MobileNetV2 论文](https://arxiv.org/abs/1801.04381)
- [Convert TensorFlow models](https://www.tensorflow.org/lite/models/convert)

---

### 📸 截图清单与要求
请将以下截图放入您的 `images/` 文件夹，并在文档中的对应位置插入：

1.  `run.png`: **应用运行效果图**（手机 App 识别花卉的最终界面）。
2.  `测试.jpg`: **测试截图**（App 识别过程的实拍或截图）。
