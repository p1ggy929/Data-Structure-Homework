# Gitlet 测试说明

本目录包含用于测试 Gitlet 项目的测试脚本。

## 快速开始

### Windows 用户

双击运行 `run_tests.bat` 文件，或者在命令行中执行：

```bash
cd testing
run_tests.bat
```

### 手动运行测试

1. **编译项目**（如果尚未编译）：
   ```bash
   javac -cp . gitlet/*.java
   ```

2. **运行基本测试**：
   ```bash
   cd testing
   python test_gitlet.py
   ```

3. **运行完整测试套件**：
   ```bash
   python test_all.py
   ```

## 测试脚本说明

### test_gitlet.py

基本功能测试脚本，测试以下功能：
- ? 初始化仓库 (init)
- ? 添加文件和提交 (add, commit)
- ? 查看状态 (status)
- ? 分支操作 (branch)
- ? 查看日志 (log)

### test_all.py

完整测试套件，包含所有基本测试以及：
- 检出操作 (checkout)
- 查找提交 (find)
- 全局日志 (global-log)
- 移除文件 (rm)
- 重置操作 (reset)

## 测试结果

基本测试结果显示所有核心功能都正常工作：
- 所有 4 个基本测试均通过 ?
- Gitlet 可以正确初始化仓库
- 可以添加文件并创建提交
- 状态和日志命令正常工作
- 分支操作正常

## 注意事项

1. 确保已安装 Python 3 和 Java
2. 测试会在临时目录中运行，不会影响项目文件
3. 如果遇到 ClassNotFoundException，请确保在项目根目录下编译 Java 文件
4. 某些测试可能需要手动验证输出结果

## 故障排除

**问题：找不到 gitlet.Main 类**
- 解决：确保在项目根目录执行 `javac -cp . gitlet/*.java`

**问题：Python 编码错误**
- 解决：确保使用 Python 3，并且文件编码为 UTF-8

**问题：测试失败**
- 检查 Java 和 Python 是否已正确安装
- 检查项目是否已正确编译
- 查看测试输出的详细错误信息

