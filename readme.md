# Android端代码

## 提交说明

##### 本目录是Android端的目录文件，其中包括必要的环境配置文件和源代码。

##### **本项目中有两个模块，app和mvvm base。**

**app 是程序的主模块，mvvm base 模块是mvvm 架构的基本构建。**

为了方便整理了解前端代码结构，在此目录中列出主模块app文件夹的目录结构。

在相应目录中有对应的readme.txt 文件。

其余文件为配置文件。

## 开发环境

### IDE

    Android Studio 4.1.2
    Build #AI-201.8743.12.41.7042882, built on December 20, 2020
    Runtime version: 1.8.0_242-release-1644-b01 amd64
    VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
    Windows 10 10.0

### Android API

```
targetSdkVersion = 30
minSdkVersion = 29 
```

## 目录结构简要说明

    .
    │  .gitignore
    │  build.gradle            #项目配置文件
    │  gradle.properties
    │  gradlew
    │  gradlew.bat
    │  settings.gradle
    │  travelLog.jks        # 项目签名
    │
    ├─app
    │  │
    │  ├─libs         # 项目使用的依赖包
    │  │      aiboost_aar-release.aar  # AiBoost aar包
    │  │      album-release.aar   # 照片展示包
    │  │      com.coloros.ocs.base-release-1.0.4-SNAPSHOT.aar   # AIUnit 所需包
    │  │      CVUnit-release.aar  # CVUnit aar包
    │  │
    │  ├─release
    │  │      app-release.apk   # 项目输出的apk文件
    │  │      output-metadata.json
    │  │
    │  └─src  #src文件夹在app目录中单独说明。
    ├─gradle   
    │  └─wrapper
    │          gradle-wrapper.jar
    │          gradle-wrapper.properties
    │
    └─mvvm base # readme.md 在mvvm base 文件夹中单独列出。