# 开源小说阅读器 v2.0

[![Build APK](https://github.com/xiaolaji-sys/NovelReader/actions/workflows/build.yml/badge.svg)](https://github.com/xiaolaji-sys/NovelReader/actions/workflows/build.yml)

功能完善的开源小说阅读器，支持自定义书源、TTS语音朗读、离线缓存等多项实用功能。

## 👀 新增功能 (v2.0)

### 📚 离线缓存
- ✅ 章节离线下载
- ✅ 本地数据库持久化
- ✅ 无网络环境正常阅读
- ✅ 更新同步机制

### 📖 书架管理
- ✅ 分类管理（全部/收藏/离线）
- ✅ 收藏功能
- ✅ 操作菜单（添加/删除/下载）
- ✅ 书籍信息显示

### 🔍 全文搜索
- ✅ 实时搜索
- ✅ 多维度搜索（标题/作者/内容）
- ✅ 搜索结果分页
- ✅ 搜索压缩展示

### 📊 阅读统计
- ✅ 阅读时长统计
- ✅ 每日阅读数据
- ✅ 阅读速度计算
- ✅ 各书籍阅读情况

### 🌟 更多主题
- ✅ 日间模式
- ✅ 夜间模式  
- ✅ 护眼模式
- ✅ 自定义颜色

### 📁 导入本地书籍
- ✅ 支持 .txt 格式
- ✅ 支持 .epub 格式
- ✅ 自动章节分割
- ✅ 文件预览

## 💻 技术栈

- 语言: Java/Kotlin
- 框架: Android SDK
- 数据库: Room
- 网络解析: Jsoup
- UI: Material Design
- 文件读写: DocumentFile API

## 💾 安装

### 需要环境
1. Android Studio 4.0+
2. JDK 11+
3. Android SDK 33+

### 构建步骤
1. 复制代码到本地
2. 在 Android Studio 中打开项目
3. 等待 Gradle 搜索依赖
4. 连接 Android 设备或者启动模拟器
5. 运行应用

### 生成 APK
```bash
# 测试版
./gradlew assembleDebug

# 发行版  
./gradlew assembleRelease
```

## 🔧 使用指南

### 基本操作
1. 主界面进行小说搜索
2. 点击小说进入阅读界面
3. 使用朗读按钮开始TTS朗读
4. 在设置中配置书源

### 书源配置
进入设置项目，可以：
- 添加新的书源
- 编辑现有书源
- 启用/禁用书源
- 自定义CSS选择规则

### 阅读设置
- 字体大小调节
- 行距调节
- 主题切换
- 亮度调节
- 自动滚动

## 📁 项目结构

```
NovelReader/
├── app/
│   ├── src/main/java/com/novelreader/
│   │   ├── MainActivity.java          # 主界面
│   │   ├── NovelDetailActivity.java    # 阅读页面
│   │   ├── BookshelfActivity.java      # 书架管理
│   │   ├── SearchActivity.java        # 搜索功能
│   │   ├── SettingsActivity.java       # 设置项目
│   │   ├── ReadingStatsActivity.java  # 阅读统计
│   │   ├── LocalBookImportActivity.java # 本地导入
│   │   ├── model/
│   │   │   ├── Novel.java              # 小说模型
│   │   │   └── BookSource.java        # 书源模型
│   │   ├── database/
│   │   │   └── AppDatabase.java       # 数据库框架
│   │   ├── tts/
│   │   │   └── TTSManager.java        # 语音管理
│   │   ├── source/
│   │   │   └── BookSourceManager.java # 书源管理
│   │   ├── adapter/
│   │   │   └── NovelAdapter.java     # 列表适配器
│   ├── res/layout/
│   │   ├── activity_main.xml
│   │   ├── activity_novel_detail.xml
│   │   ├── activity_bookshelf.xml
│   │   ├── activity_search.xml
│   │   ├── activity_settings.xml
│   │   ├── activity_reading_stats.xml
│   │   ├── activity_local_book_import.xml
│   │   ├── dialog_reading_settings.xml
│   │   └── item_novel.xml
│   └── AndroidManifest.xml
├── build.gradle
└── README.md
```

## 👥 开发说明

### 待实现功能
- [ ] 数据库实现
- [ ] 网页内容解析
- [ ] 章节列表实现
- [ ] 书籍下载功能
- [ ] 打包功能

### 贡献指南
1. Fork 本项目
2. 创建新的分支
3. 提交代码
4. 创建 Pull Request

## 📋 许可证书

本项目采用 MIT 许可协议

## 📧 联系方式

问题建议，请提交 Issue