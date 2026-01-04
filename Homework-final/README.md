# Gitlet - 简化版 Git 版本控制系统

Gitlet 是 CS61B 课程项目，实现了一个简化版的 Git 版本控制系统。

## 功能特性

- 基本的版本控制功能（init, add, commit, status, log）
- 分支管理（branch, checkout, merge）
- 文件检出和恢复
- 远程仓库操作（add-remote, fetch, push, pull）
- **交互式模式**：支持连续执行多个命令

## 快速开始

### 编译项目

```bash
javac -encoding UTF-8 -cp . gitlet/*.java
```

或使用 Maven：
```bash
mvn compile
```

### 运行方式

#### 方式 1：交互式模式（推荐）

直接运行程序，进入交互式命令行：

```bash
java -cp . gitlet.Main
# 或
java -cp target/classes gitlet.Main
```

启动后会看到：
```
========================================
Gitlet Interactive Mode
========================================
Type 'help' for available commands
Type 'quit' or 'exit' to exit

gitlet> 
```

然后可以连续输入命令：
```
gitlet> init
gitlet> status
gitlet> add README.md
gitlet> commit "Initial commit"
gitlet> log
gitlet> quit
```

#### 方式 2：命令行模式

每次执行一个命令：
```bash
java -cp . gitlet.Main init
java -cp . gitlet.Main status
java -cp . gitlet.Main add file.txt
java -cp . gitlet.Main commit "message"
```

#### 方式 3：使用批处理脚本（Windows）

```bash
run_gitlet.bat init
run_gitlet.bat status
run_gitlet.bat add file.txt
```

## 主要命令

### 仓库管理
- `init` - 初始化 Gitlet 仓库

### 文件操作
- `add <file>` - 将文件添加到暂存区
- `rm <file>` - 从暂存区移除文件
- `commit "<message>"` - 提交暂存的更改

### 信息查询
- `status` - 显示工作目录状态
- `log` - 显示当前分支的提交历史
- `global-log` - 显示所有提交
- `find "<message>"` - 根据提交消息查找提交

### 检出操作
- `checkout -- <file>` - 从 HEAD 检出文件
- `checkout <commit-id> -- <file>` - 从指定提交检出文件
- `checkout <branch>` - 切换到指定分支

### 分支管理
- `branch <name>` - 创建新分支
- `rm-branch <name>` - 删除分支

### 高级操作
- `reset <commit-id>` - 重置 HEAD 到指定提交
- `merge <branch>` - 合并分支到当前分支

### 远程操作
- `add-remote <name> <dir>` - 添加远程仓库
- `rm-remote <name>` - 删除远程
- `fetch <remote> <branch>` - 从远程获取
- `push <remote> <branch>` - 推送到远程
- `pull <remote> <branch>` - 从远程拉取

### 交互式模式专用
- `help` - 显示所有可用命令
- `quit` / `exit` - 退出交互模式

## 在 IntelliJ IDEA 中运行

### 交互式模式
1. 右键点击 `gitlet/Main.java`
2. 选择 "Run 'Main.main()'"
3. **不要**设置 Program arguments
4. 在运行窗口直接输入命令

### 命令行模式
1. 右键点击 `gitlet/Main.java`
2. 选择 "Modify Run Configuration..."
3. 在 "Program arguments" 中输入命令，例如：`init` 或 `status`
4. 运行程序

## 测试

运行测试脚本：

```bash
cd testing
python test_gitlet.py
```

或使用批处理文件：
```bash
cd testing
run_tests.bat
```

详细测试说明请查看 `testing/README_TESTING.md`

## 项目结构

```
proj2/
├── gitlet/              # 源代码目录
│   ├── Main.java       # 主程序入口
│   ├── GitletUtils.java # 命令处理
│   └── ...             # 其他核心类
├── testing/            # 测试目录
│   ├── test_gitlet.py  # 测试脚本
│   └── src/            # 测试资源
├── Makefile           # 构建配置
├── pom.xml            # Maven 配置
└── README.md          # 本文件
```

## 注意事项

1. **交互式模式**：运行 `java gitlet.Main` 不提供参数时，会进入交互式模式
2. **命令行模式**：提供参数时，执行单个命令后退出（这是正常行为）
3. **带空格的消息**：在交互模式或命令行中使用引号，例如：`commit "my message"`
4. **编译**：确保使用 UTF-8 编码编译：`javac -encoding UTF-8 -cp . gitlet/*.java`

## 更多信息

- 交互式模式详细说明：查看 `INTERACTIVE_MODE.md`
- 测试说明：查看 `testing/README_TESTING.md`
- 设计文档：查看 `gitlet-design.md`

## 许可证

本项目为 CS61B 课程作业项目。

