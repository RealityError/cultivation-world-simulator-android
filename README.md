# Cultivation World Simulator Android

基于上游项目 [4thfever/cultivation-world-simulator](https://github.com/4thfever/cultivation-world-simulator) 改造的安卓版本仓库。

这不是独立原创项目，而是把现有修仙世界模拟器整理成一套可以在安卓设备上运行、构建和持续维护的工程。安卓专属能力会先在本仓库迭代，能通用回流的修复会再逐步向上游提 PR。

## 这是什么

- 一个可运行的安卓预览版工程
- 一个面向安卓发布的宿主封装方案
- 一个基于上游项目持续演化的移动端分支

当前公开的是首轮安卓预览版本，重点目标是先做到“能装、能进、能玩、能继续修”。

## 双入口

### 玩家入口

- 这是安卓预览版，不是正式稳定版
- 推荐从 GitHub Releases 页面下载 APK 安装
- 当前验证通过的是 `debug APK`
- 支持 Android 7.0+（`minSdk 24`）
- 首次启动可能较慢，因为应用需要解包运行资源并初始化本地服务

### 开发者入口

- 技术路线：`WebView + Kotlin + Chaquopy + 内嵌 Python`
- 运行时会把 `src/`、`static/`、`assets/`、`web/dist/` 打进安卓资源并在本机拉起服务
- 当前主要在 Windows 环境完成构建与验证
- 已具备继续迭代移动端适配、打包和发布流程的基础

## 当前能做什么

- 安卓应用可以启动内嵌运行时并进入游戏
- 已完成第一轮移动端界面适配
- 安卓真机存档失败的核心链路已经修复
- 工程可从源码构建出调试版 APK

## 直接下载安装

推荐方式是从本仓库未来的 GitHub Releases 页面下载 APK。

当前本地验证通过的 APK 构建产物位置：

```text
app/build/outputs/apk/debug/app-debug.apk
```

说明：

- 当前公开版本应视为 `debug APK`
- 这不是正式签名的 release 包
- 如果你只想体验，请优先下载 Release 附件，而不是自己从源码构建

## 安装说明

1. 下载 APK 到手机
2. 允许未知来源安装
3. 如果你之前装过旧测试包，建议先卸载再安装首版预览包
4. 首次启动时耐心等待初始化完成

建议反馈时一并说明这些信息：

- 手机机型
- 安卓版本
- 真机还是模拟器
- 是闪退、卡启动、界面挤压，还是存档失败

## 从源码构建

当前安卓构建至少需要：

- JDK 17
- Android SDK 35
- Python 3.11
- Node.js 18+
- npm

### 构建步骤

1. 安装 Python 依赖

```powershell
pip install -r requirements-runtime.txt
```

2. 安装前端依赖

```powershell
Set-Location web
npm install
Set-Location ..
```

3. 配置本机 Android SDK 与 JDK 环境变量

```powershell
$env:JAVA_HOME = "<你的 JDK 17 路径>"
$env:ANDROID_HOME = "<你的 Android SDK 路径>"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
```

4. 打包调试版 APK

```powershell
.\gradlew.bat :app:assembleDebug
```

## 仓库结构

```text
app/        Android 宿主工程
src/        后端与游戏运行逻辑
static/     静态配置与本地化资源
assets/     游戏资源
web/        前端界面
tests/      Python 测试
```

## 与上游项目的关系

本仓库是上游项目的安卓修改版，不是独立原创世界观或全新引擎项目。

- 上游仓库：`4thfever/cultivation-world-simulator`
- 当前主要增量：安卓封装、移动端适配、安卓构建链路、真机兼容修复
- 后续策略：安卓专属部分继续在本仓库维护，通用修复尽量回提上游

更完整的署名与改造说明见 [ATTRIBUTION.md](ATTRIBUTION.md)。

## 许可证

本仓库沿用并遵守上游项目当前使用的 `CC BY-NC-SA 4.0` 许可证。

这意味着：

- 必须保留上游署名
- 必须注明本仓库属于修改版
- 禁止商用
- 继续分发修改版时需要保持相同或兼容许可

详细条款见 [LICENSE](LICENSE)。

## 当前限制

- 当前仍是安卓预览版，不应视为长期稳定版
- 当前对外更适合发布 `debug APK`，正式 release 流程还没有补齐
- 移动端 UI 仍在继续优化
- 后台保活和长时间挂机能力还不稳定

## 后续方向

- 补 GitHub Releases 发布流程
- 拆分可回提上游的通用修复
- 继续优化手机端布局和交互
- 评估正式签名、后台运行和 release 发布方案
