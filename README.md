# MyAndroid

## 项目简介

MyAndroid 是一个基于 Android 的示例应用，适合作为入门与日常练习的模板工程。

## 功能特性

- 多页面导航：包含多个 Activity（`MainActivity`、`MainActivity1` ～ `MainActivity5`）之间的跳转示例  
- 自定义转场动画：使用 `res/anim` 目录下的补间动画实现页面左右滑动切换效果  
- Edge-to-Edge 布局：通过 `EdgeToEdge.enable(this)` 与 WindowInsets 适配系统状态栏与导航栏  
- Material Design：集成 `material`、`appcompat` 等依赖，基础样式遵循 Material Design 规范  
- ViewBinding 支持：在 `build.gradle.kts` 中开启 `viewBinding`，方便后续扩展时进行安全、便捷的视图绑定  

## 技术栈

- 语言：Java（应用层代码）  
- 最低 SDK：24  
- 目标 SDK：34  
- 构建工具：Gradle（Kotlin DSL 配置）  
- 主要依赖：
  - `androidx.appcompat:appcompat`
  - `com.google.android.material:material`
  - `androidx.activity:activity`
  - `androidx.constraintlayout:constraintlayout`
  - `androidx.navigation:navigation-fragment`
  - `androidx.navigation:navigation-ui`

## 目录结构

项目核心目录说明如下（省略无关文件，仅展示关键部分）：

```text
MyAndroid/
├── app/
│   ├── build.gradle.kts           # 应用模块构建配置
│   └── src/main/
│       ├── AndroidManifest.xml    # 清单文件
│       ├── java/com/example/myapplication/
│       │   ├── MainActivity.java  # 应用入口 Activity
│       │   ├── MainActivity1.java
│       │   ├── MainActivity2.java
│       │   ├── MainActivity3.java
│       │   ├── MainActivity4.java
│       │   └── MainActivity5.java
│       └── res/
│           ├── layout/            # 界面布局 XML
│           ├── anim/              # 转场动画 XML
│           ├── navigation/        # Navigation 图配置
│           └── values/            # 颜色、字符串、主题等
├── build.gradle.kts               # 顶层构建配置
├── settings.gradle.kts            # 模块配置
└── gradle/                        # Gradle Wrapper 配置
```

## 本地运行

1. 安装最新版本的 Android Studio（推荐使用包含 Android Gradle Plugin 8.x 的版本）  
2. 克隆或下载本项目代码到本地  
3. 使用 Android Studio 打开项目根目录 `MyAndroid`  
4. 等待 Gradle 同步完成（首次可能需要较长时间下载依赖）  
5. 连接真机或启动模拟器  
6. 在 Android Studio 中选择运行配置 `app`，点击 Run 按钮即可安装并运行

## 代码入口说明

- 入口 Activity：`MainActivity`  
  - 布局文件：`res/layout/activity_main.xml`  
  - 通过按钮点击跳转到其他示例页面（如 `MainActivity1`、`MainActivity2`），并在部分跳转中应用左右滑动的转场动画
- 动画资源：
  - `res/anim/slide_in_left.xml`
  - `res/anim/slide_in_right.xml`
  - `res/anim/slide_out_left.xml`
  - `res/anim/slide_out_right.xml`

## 后续规划（可选）

根据需要，可以在此项目基础上继续扩展，例如：

- 增加 Fragment + Navigation Component 的复杂导航示例  
- 引入 MVVM 架构与 ViewModel、LiveData 等组件  
- 接入网络请求与本地数据存储示例  
- 添加单元测试与 UI 自动化测试

---

如需根据具体业务自定义此模板，可在保留当前结构的基础上逐步替换 Activity、布局与资源文件，以快速启动新的 Android 项目。
