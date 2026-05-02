:: build.bat - 小说阅读器 Windows 构建脚本

echo 🔧 开始构建小说阅读器...

:: 检查环境
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ 未找到 Java，请先安装 JDK 11+
    exit /b 1
)

if not exist ".\gradlew.bat" (
    echo ❌ 未在项目根目录，请切换到正确的目录
    exit /b 1
)

:: 清理代码
echo ⏳ 正在清理...
call gradlew clean

if %errorlevel% equ 0 (
    echo ✅ 清理完成
) else (
    echo ❌ 清理失败
    exit /b 1
)

:: 构建调试版
echo ⏳ 正在构建调试版 APK...
call gradlew assembleDebug

if %errorlevel% equ 0 (
    echo ✅ 调试版 APK 构建成功!
    echo APK 位置: app\build\outputs\apk\debug\app-debug.apk
) else (
    echo ❌ 调试版 APK 构建失败
    exit /b 1
)

:: 构建发行版
set /p "choice=是否要构建发行版 APK? (y/n): "
if /i "%choice%"=="y" (
    echo ⏳ 正在构建发行版 APK...
    call gradlew assembleRelease
    
    if %errorlevel% equ 0 (
        echo ✅ 发行版 APK 构建成功!
        echo APK 位置: app\build\outputs\apk\release\app-release.apk
    ) else (
        echo ❌ 发行版 APK 构建失败
    )
)

echo 🎉 构建完成!
pause
