##　说明

**本目录是mvvm 模块的目录结构，该模块定义了一些app 模块使用的基本组件和项目支撑。**

包名为**com.code.mvvm** 

## 目录结构简要说明

    │.
    │  AndroidManifest.xml # 模块的清单文件
    │
    ├─java
    │  └─com
    │      └─mvvm
    │          ├─base
    │          │      AbsLifecycleActivity.java  # 生命周期Activity ,继承自BaseActivty
    │          │      AbsLifecycleFragment.java # 生命周期Fragment ,继承自BaseFragment
    │          │      AbsRepository.java
    │          │      AbsViewModel.java
    │          │      BaseActivity.java  # 自定义Acitity
    │          │      BaseFragment.java # 自定义Fragment
    │          │
    │          ├─event  # 事件总线
    │          │      LiveBus.java
    │          │
    │          ├─http # 网络请求事件Log
    │          │  │  HttpHelper.java
    │          │  │  HttpLogger.java
    │          │  │
    │          │  └─rx  # Rxjava 订阅
    │          │          RxSchedulers.java
    │          │
    │          ├─stateview # 页面状态
    │          │      ErrorState.java
    │          │      LoadingState.java
    │          │      StateConstants.java
    │          │
    │          └─util # 工具类
    │                  Logger.java
    │                  TUtil.java
    │
    └─res  # 项目资源
        ├─layout
        │      common_empty_view.xml
        │      loading.xml
        │      loading_view.xml
        │
        ├─mipmap-xxhdpi
        │      empty_network.png
        │      empty_server.png
        │
        ├─styles
        └─values
                colors.xml
            strings.xml