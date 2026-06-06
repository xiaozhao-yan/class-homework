# 第五次作业：花卉图片分类器

基于 TensorFlow Lite 的 Android 花卉识别应用。

## 📌 项目简介

本项目实现了一个花卉图片分类器，使用 TensorFlow 训练 MobileNetV2 模型，并转换为 TFLite 格式部署到 Android 应用中。应用可以实时识别 5 种花卉：

| 中文名称 | 英文名称 | 标签 |
|---------|---------|------|
| 雏菊 | Daisy | daisy |
| 蒲公英 | Dandelion | dandelion |
| 玫瑰 | Roses | roses |
| 向日葵 | Sunflowers | sunflowers |
| 郁金香 | Tulips | tulips |

## 🖥️ 环境要求

### Python 环境
| 软件 | 版本 |
|------|------|
| Python | 3.9+ |
| TensorFlow | 2.17.0 |
| Jupyter Notebook | 最新版 |
| NumPy | 1.23+ |
| Matplotlib | 3.7+ |

### Android 环境
| 软件 | 版本 |
|------|------|
| Android Studio | 最新版 |
| TensorFlow Lite | 2.17.0 |
| 最低 SDK 版本 | 21 |
| 目标 SDK 版本 | 33 |

## 📁 项目结构
FifthAssignment/
├── training/
│ └── 第五次作业_花卉分类器_training.ipynb # Jupyter 训练代码
├── exported_flower_model/
│ ├── model.tflite # TFLite 模型文件 (2.5 MB)
│ ├── labels.txt # 类别标签文件
│ └── flower_classifier.keras # Keras 原始模型 (9.5 MB)
├── TFLClassify-main/ # Android 项目源码
└── README.md # 项目说明文档

text

## 🚀 模型训练步骤

### 1. 安装依赖

```bash
pip install tensorflow==2.17.0 matplotlib numpy jupyter
2. 导入库并设置参数
python
import tarfile
from pathlib import Path
import numpy as np
import tensorflow as tf

# TensorFlow 官方花卉数据集
FLOWER_URL = "https://storage.googleapis.com/download.tensorflow.org/example_images/flower_photos.tgz"

# 训练参数配置
DATA_DIR = None                    # None 表示使用官方数据集
EXPORT_DIR = "exported_flower_model"  # 模型导出目录
EPOCHS = 5                         # 训练轮数
BATCH_SIZE = 32                    # 批次大小
IMAGE_SIZE = 224                   # 输入图片尺寸
LEARNING_RATE = 1e-3               # 学习率
QUANTIZATION = "dynamic"           # 动态量化
SEED = 123                         # 随机种子

print("TensorFlow 版本:", tf.__version__)
3. 加载数据集
python
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
输出示例：

text
Found 3670 files belonging to 5 classes.
Using 2936 files for training.
Found 3670 files belonging to 5 classes.
Using 734 files for validation.
类别数量: 5
类别名称: ['daisy', 'dandelion', 'roses', 'sunflowers', 'tulips']
4. 构建模型（迁移学习）
使用 MobileNetV2 作为基础模型，冻结其权重，只训练最后的分类层：

python
def build_model(num_classes, image_size, learning_rate):
    inputs = tf.keras.Input(shape=(image_size, image_size, 3), name="image")
    
    # MobileNetV2 预处理
    x = tf.keras.applications.mobilenet_v2.preprocess_input(inputs)
    
    # 加载预训练模型（不包括顶层）
    base_model = tf.keras.applications.MobileNetV2(
        input_shape=(image_size, image_size, 3),
        include_top=False,
        weights="imagenet",
        pooling="avg",
    )
    
    # 冻结预训练层
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
模型结构摘要：

text
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
5. 训练模型
python
# 开始训练
history = model.fit(train_ds, validation_data=val_ds, epochs=EPOCHS)

# 评估模型
loss, accuracy = model.evaluate(test_ds)
print(f"test_loss={loss:.4f}, test_accuracy={accuracy:.4f}")
训练输出示例：

text
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
6. 转换为 TensorFlow Lite 模型
python
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

# 导出模型
export_dir = Path(EXPORT_DIR)
export_dir.mkdir(parents=True, exist_ok=True)

# 保存标签文件
labels_path = export_dir / "labels.txt"
labels_path.write_text("\n".join(class_names) + "\n", encoding="utf-8")

# 保存 Keras 模型
keras_path = export_dir / "flower_classifier.keras"
model.save(keras_path)

# 转换并保存 TFLite 模型
tflite_model = convert_to_tflite(model, QUANTIZATION, train_ds)
tflite_path = export_dir / "model.tflite"
tflite_path.write_bytes(tflite_model)

print(f"已保存 Keras 模型: {keras_path}")
print(f"已保存 TFLite 模型: {tflite_path}")
print(f"已保存标签文件: {labels_path}")
7. 测试 TFLite 模型
python
def smoke_test_tflite(tflite_path, test_ds, class_names):
    interpreter = tf.lite.Interpreter(model_path=str(tflite_path))
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()[0]
    output_details = interpreter.get_output_details()[0]
    
    # 取测试集中的图片进行推理
    images, labels = next(iter(test_ds.unbatch().batch(8)))
    input_data = tf.cast(images, input_details["dtype"]).numpy()
    
    # 处理 uint8 输入
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
    
    # 输出结果
    predicted_ids = np.argmax(np.asarray(predictions), axis=1)
    for expected, predicted in zip(labels.numpy()[:5], predicted_ids[:5]):
        print(f"真实类别={class_names[expected]}, 预测类别={class_names[predicted]}")

smoke_test_tflite(tflite_path, test_ds, class_names)
测试输出示例：

text
真实类别=daisy, 预测类别=daisy
真实类别=tulips, 预测类别=tulips
真实类别=sunflowers, 预测类别=sunflowers
真实类别=daisy, 预测类别=daisy
真实类别=sunflowers, 预测类别=sunflowers
📱 Android 部署步骤
1. 替换模型文件
将 exported_flower_model/ 目录下的文件复制到 Android 项目：

源文件	目标位置
model.tflite	app/src/main/assets/FlowerModel.tflite
labels.txt	app/src/main/assets/labels.txt
2. 配置 build.gradle
gradle
dependencies {
    // TensorFlow Lite 依赖
    implementation 'org.tensorflow:tensorflow-lite:2.17.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-metadata:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.17.0'
}
3. 手动加载模型（MainActivity.kt）
kotlin
val modelFile = FileUtil.loadMappedFile(ctx, "FlowerModel.tflite")
val interpreter = Interpreter(modelFile)
📊 模型性能
指标	数值
测试集准确率	88.64%
模型大小 (TFLite)	~2.5 MB
输入尺寸	224×224×3
输出类别	5 类
推理时间 (CPU)	~30-50ms/张
📚 参考链接
TensorFlow 花卉数据集

TensorFlow Lite 官方文档

MobileNetV2 论文