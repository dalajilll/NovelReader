#!/bin/bash
# build.sh - 小说阅读器构建脚本

#!/bin/bash

echo "🔧 开始构建小说阅读器..."

# 检查环境
if ! command -v java &> /dev/null; then
    echo "❌ 未找到 Java，请先安装 JDK 11+"
    exit 1
fi

if [ ! -f "./gradlew" ]; then
    echo "❌ 未在项目根目录，请切换到正确的目录"
    exit 1
fi

# 清理代码
echo "⏳ 正在清理..."./gradlew clean

if [ $? -eq 0 ]; then
    echo "✅ 清理完成"
else
    echo "❌ 清理失败"
    exit 1
fi

# 构建调试版
echo "⏳ 正在构建调试版 APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ 调试版 APK 构建成功!"
    echo "APK 位置: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ 调试版 APK 构建失败"
    exit 1
fi

# 构建发行版
read -p "是否要构建发行版 APK? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "⏳ 正在构建发行版 APK..."
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        echo "✅ 发行版 APK 构建成功!"
        echo "APK 位置: app/build/outputs/apk/release/app-release.apk"
    else
        echo "❌ 发行版 APK 构建失败"
    fi
fi

echo "🎉 构建完成!"
