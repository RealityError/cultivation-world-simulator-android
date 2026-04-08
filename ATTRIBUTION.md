# 上游署名与改造说明

本项目基于上游项目 [4thfever/cultivation-world-simulator](https://github.com/4thfever/cultivation-world-simulator) 修改而来。

## 上游信息

- 上游仓库：`https://github.com/4thfever/cultivation-world-simulator`
- 原作者 / 维护者：`4thfever`
- 上游许可证：`CC BY-NC-SA 4.0`

## 本仓库的主要改动

本仓库当前聚焦于安卓版本与移动端可用性，包含但不限于：

- 基于 `WebView + Chaquopy + 内嵌 Python` 的安卓宿主封装
- 运行时资源同步与本地 FastAPI 服务启动链路
- 移动端界面适配与安卓返回键桥接
- 安卓真机存档链路修复
- 安卓构建、打包与发布所需的工程配置

## 许可证说明

根据上游项目当前采用的 `CC BY-NC-SA 4.0` 许可证：

- 你必须保留对上游项目与原作者的署名
- 你必须说明本仓库属于修改版项目
- 你不得将本项目或其修改版用于商业用途
- 你在继续分发修改版时，必须继续采用相同或兼容的署名-非商业性使用-相同方式共享许可

本仓库在公开发布时，沿用并遵守上游项目的 `CC BY-NC-SA 4.0` 许可证要求。

## 后续计划

- 安卓专属壳层能力先在本仓库独立迭代
- 可通用的前后端修复会尽量拆分后逐步回提上游
