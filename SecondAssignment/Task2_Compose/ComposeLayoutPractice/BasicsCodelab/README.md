# 任务二：Jetpack Compose 基础布局实践

本项目为基于 **Jetpack Compose** 的 Android 应用开发实践项目，通过逐步构建一个交互式 UI，掌握 Compose 的核心概念，包括：可组合函数、修饰符、布局组件、状态管理等。

---

## 📌 项目简介

- **项目名称**：BasicsCodelab
- **开发语言**：Kotlin
- **UI 框架**：Jetpack Compose
- **功能描述**：实现一个可展开/收起的问候卡片列表，每个卡片包含问候语和互动按钮，展示 Compose 状态管理能力

---

## 🖥️ 开发环境

| 工具 | 版本 |
|------|------|
| Android Studio | 4.1+ |
| Kotlin 插件 | 1.9+ |
| Compose BOM | 2024.02.00+ |
| 最低 SDK | API 21 (Android 5.0) |
| 目标 SDK | API 33 (Android 13) |

---

## 🚀 项目创建

### 1. 新建项目

1. 打开 Android Studio，选择 **New Project**
2. 选择 `Phone and Tablet` → `Empty Activity`
3. 配置项目信息：
   - **Name**：`BasicsCodelab`
   - **Package name**：`com.example.basicscodelab`
   - **Language**：`Kotlin`
   - **Minimum SDK**：`API 21`

> **📸 截图位置**：
> <img width="1361" height="977" alt="image" src="https://github.com/user-attachments/assets/8b3e933c-c64e-41c6-b93b-ac8719ccf8a3" />
<img width="1354" height="973" alt="image" src="https://github.com/user-attachments/assets/d83b8bed-f9da-41c4-b2a5-cc2a425f7479" />


---

## 📝 开发步骤与代码演进

### 第一阶段：Compose 入门

Android 使用 `@Composable` 注解来表示可组合函数，从而定义 UI。`@Preview` 注解用于在 Android Studio 中预览 UI，无需运行到设备。

**初始代码：**

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview
@Composable
fun PreviewGreeting() {
    Greeting("Android")
}
```

### 第二阶段：微调界面（使用 Surface 和 MaterialTheme）

为 `Greeting` 设置不同的背景色，使用 `Surface` 包围 `Text` 可组合项，并使用 `MaterialTheme.colorScheme.primary` 作为背景色。

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}
```

### 第三阶段：使用修饰符（Modifier）

添加 `padding` 修饰符，为文字添加 24.dp 的内边距。

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(24.dp)
        )
    }
}
```


### 第四阶段：重复使用可组合项

创建 `MyApp` 可组合项，将 `Greeting` 封装其中，便于复用。

```kotlin
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Greeting("Android")
    }
}
```

**完整代码结构：**

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicsCodelabTheme {
        MyApp()
    }
}
```


### 第五阶段：创建 Column（垂直布局）

Compose 中的三个基本标准布局元素是 `Column`、`Row` 和 `Box`。使用 `Column` 垂直排列文字。

```kotlin
import androidx.compose.foundation.layout.Column

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Column(modifier = modifier.padding(24.dp)) {
            Text(text = "Hello ")
            Text(text = name)
        }
    }
}
```

> **📸 截图位置**：
> <img width="2546" height="1501" alt="8daf75590048f598e408bca8a969b9bb" src="https://github.com/user-attachments/assets/9d6bb5d7-e606-4af2-9c15-df89a9b04f51" />


### 第六阶段：Compose 和 Kotlin 结合

可组合函数可以像 Kotlin 中的其他函数一样使用，可以使用循环动态生成 UI。

```kotlin
@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose")
) {
    Column(modifier) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}
```

**调整预览宽度（模拟小屏幕）：**

```kotlin
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    BasicsCodelabTheme {
        MyApp()
    }
}
```

> **📸 截图位置**：
> <img width="2559" height="1525" alt="d11c7678eacaf3f85c6fc1486072cf86" src="https://github.com/user-attachments/assets/0e60f718-de8e-4215-9680-9b0a0d502491" />


**添加更多修饰符（fillMaxWidth 和 padding）：**

```kotlin
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose")
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
            Text(text = "Hello ")
            Text(text = name)
        }
    }
}
```

> **📸 截图位置**：
> 





### 第七阶段：添加按钮（Row + weight）

向 `Greeting` 添加可点击按钮，使用 `Row` 水平排列内容和按钮，`weight(1f)` 让文字区域填满剩余空间。

```kotlin
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ElevatedButton

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hello ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { /* TODO */ }
            ) {
                Text("Show more")
            }
        }
    }
}
```

> **📸 截图位置**：
> 




### 第八阶段：状态管理（State）

使用 `mutableStateOf` 和 `remember` 管理组件的展开/收起状态，实现独立卡片的交互功能。

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 48.dp else 0.dp
    
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}
```

> **📸 截图位置**：
> <img width="2559" height="1527" alt="68b139dc608d47512e54c0a3eb554984" src="https://github.com/user-attachments/assets/5d4865c1-80b2-44af-9e4b-6336288a3dde" />



**运行效果说明：**

- 初始状态：每个卡片显示 "Show more" 按钮
- 点击 "Show more"：该卡片展开（增加底部内边距），按钮文字变为 "Show less"
- 点击 "Show less"：该卡片收起，按钮文字恢复 "Show more"
- 每个卡片的状态独立管理，互不影响

> **📸 截图位置**：此处应插入应用在模拟器或真机上运行的最终效果截图

---

## 📄 最终完整代码

```kotlin
package com.example.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose")
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 48.dp else 0.dp
    
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello ")
                Text(text = name)
            }
            ElevatedButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    BasicsCodelabTheme {
        MyApp()
    }
}
```

> **📸 截图位置**：此处应插入最终完整的 MainActivity.kt 代码截图

---

## 🔧 运行与测试

### 预览方式

在 Android Studio 中，点击 `GreetingPreview` 函数旁边的预览图标，即可查看 UI 效果，无需运行到设备。

### 真机/模拟器运行

1. 连接 Android 设备（开启开发者模式）或启动模拟器
2. 点击 Android Studio 工具栏的 ▶ `Run` 按钮
3. 选择目标设备

> **📸 截图位置**：此处应插入应用在模拟器上运行的完整界面截图

---

## ❓ 常见问题与解决

| 问题 | 解决方法 |
|------|----------|
| `@Preview` 不显示 | 确保预览函数无参数，且使用 `@Composable` 注解 |
| 状态不更新 | 使用 `remember { mutableStateOf() }` 包裹状态 |
| 按钮点击无反应 | 检查 `onClick` lambda 中是否正确修改了状态值 |
| 布局错位 | 检查 `modifier` 是否正确传递，使用 `weight` 时确保父容器是 `Row` 或 `Column` |
| 预览中按钮不可点击 | 预览模式仅用于静态展示，交互需运行到设备 |

---

## 📊 实验结果总结

| 学习目标 | 完成情况 |
|----------|----------|
| 掌握 `@Composable` 和 `@Preview` 注解使用 | ✅ |
| 理解 `Surface` 和 `MaterialTheme` 的作用 | ✅ |
| 学会使用 `modifier` 调整布局 | ✅ |
| 掌握 `Column`、`Row`、`weight` 布局 | ✅ |
| 学会使用 Kotlin 循环动态生成 UI | ✅ |
| 掌握 `mutableStateOf` 和 `remember` 状态管理 | ✅ |
| 实现独立的卡片展开/收起功能 | ✅ |

---

## 📚 参考资料

- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Compose 布局基础](https://developer.android.com/jetpack/compose/layout)
- [Compose 状态管理](https://developer.android.com/jetpack/compose/state)
- [Material Design 3 in Compose](https://developer.android.com/jetpack/compose/designsystems/material3)

---
