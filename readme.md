# Readme


### This repo is the Android code participating in a competition, which is for reference only, including the necessary environment configuration files and source code.
##### demo plz ref [hear](https://github.com/nancheng58/Travellog-for-android/blob/master/usage.pdf)

##### **本项目中有两个模块，app和mvvm base。**

**app 是程序的主模块，mvvm base 模块是mvvm 架构的基本构建。**

为了方便整理了解前端代码结构，在此目录中列出主模块app文件夹的目录结构。

在相应目录中有对应的readme文件。

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
## APP 目录结构说明

    │  App.java    # 系统组件 application 文件
    │
    ├─ai   # AI 包
    │  │  AiBoostYoloV5Classifier.java  # AiBoost 图像分类管理器
    │  │  Classifier.java         
    │  │  YoloV5Classifier.java   # yolo v5图像分类器
    │  │
    │  └─realtimedetection  # 端侧实时图像分类demo，调用Camera 2.0 API
    │      │  CameraActivity.java
    │      │  CameraConnectionFragment.java
    │      │  DetectorActivity.java
    │      │
    │      ├─aiboost
    │      │      Classifier.java
    │      │      DetectorFactory.java
    │      │      YoloV5Classifier.java
    │      │
    │      ├─customview
    │      │      AutoFitTextureView.java
    │      │      OverlayView.java
    │      │      RecognitionScoreView.java
    │      │      ResultsView.java
    │      │
    │      ├─env
    │      │      BorderedText.java
    │      │      ImageUtils.java
    │      │      Logger.java
    │      │      Size.java
    │      │      Utils.java
    │      │
    │      └─tracking
    │              MultiBoxTracker.java
    │
    ├─cluster  # 地图点聚合相关代码
    │      Cluster.java
    │      ClusterClickListener.java
    │      ClusterItem.java
    │      ClusterOverlay.java
    │      ClusterRender.java
    │      RegionItem.java
    │
    ├─config  # 静态配置信息
    │      Constants.java
    │      URL.java
    │
    ├─core # 核心代码块
    │  ├─data # 数据模型代码
    │  │  ├─pojo  # 静态Pojo类
    │  │  │  │  BasePojo.java
    │  │  │  │
    │  │  │  ├─album # 影集数据
    │  │  │  │      AlbumListPojo.java
    │  │  │  │      AlbumPojo.java
    │  │  │  │      AlbumPostPojo.java
    │  │  │  │      AlbumResultPojo.java
    │  │  │  │      AlbumWorkPojo.java
    │  │  │  │
    │  │  │  ├─banner  # 轮播图数据
    │  │  │  │      BannerListVo.java
    │  │  │  │      BannerVo.java
    │  │  │  │
    │  │  │  ├─common # 标签数据
    │  │  │  │      TypeVo.java
    │  │  │  │
    │  │  │  ├─extraction # 颜色提取数据
    │  │  │  │      ColorPojo.java
    │  │  │  │
    │  │  │  ├─geo  # 地理信息数据
    │  │  │  │      CityListPojo.java
    │  │  │  │      CityListResultPojo.java
    │  │  │  │      CityPojo.java
    │  │  │  │      CityResultPojo.java
    │  │  │  │      GeoItemPojo.java
    │  │  │  │      GeoPojo.java
    │  │  │  │
    │  │  │  ├─home  # 主页数据
    │  │  │  │      ButtonPojo.java
    │  │  │  │      CatagoryInfoVo.java
    │  │  │  │      CategoryVo.java
    │  │  │  │      HomeMergePojo.java
    │  │  │  │
    │  │  │  ├─image  # 验证码图片数据
    │  │  │  │      ImagePojo.java
    │  │  │  │
    │  │  │  ├─picture 
    │  │  │  │      Collection.java
    │  │  │  │      Photo.java
    │  │  │  │      PictureExifPojo.java
    │  │  │  │
    │  │  │  ├─plog # plog 数据
    │  │  │  │      PlogListPojo.java
    │  │  │  │      PlogMergePojo.java
    │  │  │  │      PlogPojo.java
    │  │  │  │      PlogPostPojo.java
    │  │  │  │      PlogResultPojo.java
    │  │  │  │      PlogStatusPojo.java
    │  │  │  │      PlogWorkPojo.java
    │  │  │  │
    │  │  │  ├─poetry # 诗词生成数据
    │  │  │  │      PoetryPojo.java
    │  │  │  │
    │  │  │  ├─supervision #超分辨率数据
    │  │  │  │      SuperVisionPojo.java
    │  │  │  │
    │  │  │  ├─user  # 用户数据
    │  │  │  │      UserInfoVo.java
    │  │  │  │      UserPojo.java
    │  │  │  │
    │  │  │  ├─video  # 影集预览视频数据
    │  │  │  │      VideoListPojo.java
    │  │  │  │      VideoMergePojo.java
    │  │  │  │      VideoPojo.java
    │  │  │  │
    │  │  │  └─weather # 天气识别数据
    │  │  │          WeatherPojo.java
    │  │  │
    │  │  └─repository # 数据仓库
    │  │          AlbumRepository.java  # 影集数据仓库
    │  │          ApiRepository.java  # Api 数据仓库
    │  │          BaseRepository.java  # 数据仓库基类
    │  │          HomeRepository.java  # 主页数据仓库
    │  │          PictureRepository.java # 本地照片和地理信息数据仓库
    │  │          PlogRepository.java # Plog 数据仓库
    │  │          UserRepository.java # 用户信息数据仓库
    │  │          VideoRepository.java # 影集视频数据仓库
    │  │
    │  ├─view # 视图代码
    │  │  │  AdapterPool.java # 页面适配池
    │  │  │  DemoActivity.java #地图动画加载页面
    │  │  │  EmptyFragment.java
    │  │  │  LaunchActivity.java  # 起始页
    │  │  │  MainActivity.java # 主页面
    │  │  │
    │  │  ├─album # 影集制作相关页面
    │  │  │      AlbumMakeFragment.java
    │  │  │      AlbumResultFragment.java
    │  │  │      MakeAlbumActivity.java
    │  │  │      MyAlbumActivity.java
    │  │  │
    │  │  ├─base # 视图base
    │  │  │  │  BaseListFragment.java
    │  │  │  │  BaseViewHolder.java
    │  │  │  │  BaseViewPagerFragment.java
    │  │  │  │
    │  │  │  ├─adapter # 自定义视图适配器
    │  │  │  │      BaseRecyclerAdapter.java
    │  │  │  │      GridImageAdapter.java
    │  │  │  │      ViewPagerAdapter.java
    │  │  │  │
    │  │  │  ├─listener # 自定义监听器
    │  │  │  │      DragListener.java  # 拖拽监听
    │  │  │  │      OnItemLongClickListener.java # 长按监听
    │  │  │  │
    │  │  │  └─widget # 自定义组件
    │  │  │      │  CropImageView.java
    │  │  │      │  CustomHeightImageView.java
    │  │  │      │  CustomHeightRelativeLayout.java
    │  │  │      │  NestedViewPager.java
    │  │  │      │
    │  │  │      └─banner # 轮播图视图
    │  │  │              BannerAdapter.java
    │  │  │              BannerItemView.java
    │  │  │              BannerView.java
    │  │  │
    │  │  ├─color # 颜色提取视图
    │  │  │      ColorFragment.java
    │  │  │
    │  │  ├─common # 主页标签视图
    │  │  │      CommonActivity.java
    │  │  │      TypeItemView.java
    │  │  │
    │  │  ├─home # 主页面视图
    │  │  │  │  HomeFragment.java
    │  │  │  │
    │  │  │  └─holder # 主页面相关视图组件
    │  │  │          AlbumItemHolder.java
    │  │  │          CategoryItemView.java
    │  │  │          HomeButtonItemView.java
    │  │  │          HomeCategoryAdapter.java
    │  │  │
    │  │  ├─map # 地图视图
    │  │  │      MapActivity.java
    │  │  │      MapAnimationActivity.java
    │  │  │      MapItemHolder.java
    │  │  │
    │  │  ├─mine # "我的"页面视图
    │  │  │      AboutActivity.java
    │  │  │      MineFragment.java
    │  │  │
    │  │  ├─object # 物体识别视图
    │  │  │      ObjectFragment.java
    │  │  │
    │  │  ├─picture # 地图照片预览视图
    │  │  │      FullyGridLayoutManager.java
    │  │  │      MyAlbumFragment.java
    │  │  │      MyPreviewActivity.java
    │  │  │      MyPreviewFragment.java
    │  │  │      PictureShowActivity.java
    │  │  │
    │  │  ├─plog # Plog 制作和展示视图
    │  │  │  │  MakePlogActivity.java
    │  │  │  │  PlogDetailsActivity.java
    │  │  │  │  PlogFragment.java
    │  │  │  │  PlogMakeFragment.java
    │  │  │  │  PlogResultFragment.java
    │  │  │  │
    │  │  │  └─holder # Plog 制作和展示相关视图组件
    │  │  │          PlogItemHolder.java
    │  │  │          PlogPicHolder.java
    │  │  │          PlogRemItemHolder.java
    │  │  │
    │  │  ├─poetry # 诗词生成视图
    │  │  │      PoetryFragment.java
    │  │  │
    │  │  ├─styletransfer # 风格迁移视图
    │  │  │      StyletransferFragment.java
    │  │  │
    │  │  ├─superresolution # 超分辨率视图
    │  │  │      SuperResolutionFragment.java
    │  │  │
    │  │  ├─user # 用户相关视图（登录、注册、忘记密码、重置密码、用户信息等）
    │  │  │      ForgetPwdActivity.java
    │  │  │      LoginActivity.java
    │  │  │      RegisterActivity.java
    │  │  │      ResetPwdActivity.java
    │  │  │      UserInfoActivity.java
    │  │  │
    │  │  ├─video # 影集视频预览视图
    │  │  │  │  ForumListFragment.java
    │  │  │  │  VideoDetailsActivity.java
    │  │  │  │  VideoFragment.java
    │  │  │  │  VideoRecommendFragment.java
    │  │  │  │
    │  │  │  └─holder # 影集视频播放器组件
    │  │  │          VideoItemHolder.java
    │  │  │
    │  │  └─weather # 天气识别视图
    │  │          WeatherFragment.java
    │  │
    │  └─viewmodel # 视图模型代码
    │          AlbumViewModel.java  #  影集视图模型
    │          ApiViewModel.java   # API 视图模型
    │          HomeViewModel.java  # 主页视图模型
    │          MineViewModel.java   # “我的”页面视图模型
    │          PictureViewModel.java  # 图片相关视图模型
    │          PlogViewModel.java  # Plog 视图模型
    │          UserViewModel.java  # 用户视图模型
    │          VideoViewModel.java  # 视频视图模型
    │
    ├─glide  # Glide(一个主流开源图像加载库)配置信息和缓存管理
    │      GlideCacheEngine.java
    │      GlideCircleTransform.java
    │      GlideConfig.java
    │      GlideEngine.java
    │      GlideRoundTransform.java
    │
    ├─network # 网络相关代码
    │  │  ApiService.java # Restful 接口定义
    │  │  ServerException.java # 服务器异常抓取
    │  │
    │  ├─interceptor # 网络缓存
    │  │      CacheInterceptor.java # 网络持久化缓存配置和管理
    │  │
    │  └─rx # Rxjava 订阅器
    │          RxSubscriber.java # 异步事件订阅
    │
    └─util # Util 工具类
            AiUtil.java
            Base64Utils.java
            BitmapUtil.java
            DisplayUtil.java
            FileUitl.java
            GeoUtil.java
            ImageSaveUtil.java
            JsonUtils.java
            NetworkUtils.java
            RefreshHelper.java
            ScreenUtil.java
            StringUtil.java
            ToastUtils.java
            ViewUtils.java
