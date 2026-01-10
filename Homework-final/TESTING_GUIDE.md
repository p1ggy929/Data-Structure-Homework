# Gitlet 功能测试指南

本指南介绍如何测试 Gitlet 的所有功能，包括自动化测试和手动测试两种方法。

## 目录
1. [自动化测试](#自动化测试)
2. [手动测试](#手动测试)
3. [功能测试清单](#功能测试清单)

---

## 自动化测试

### 方法一：使用 Makefile（推荐）

```bash
# 编译并运行所有测试
make check

# 或者分步执行
make              # 先编译
cd testing
make check        # 运行测试
```

### 方法二：直接使用测试脚本

```bash
cd testing
python3 tester.py samples/*.in
```

### 测试选项

```bash
# 显示所有测试详情
python3 tester.py --show=all samples/*.in

# 只显示前 N 个失败的测试
python3 tester.py --show=5 samples/*.in

# 保留测试目录（用于调试）
python3 tester.py --keep samples/*.in

# 调试模式（可以单步执行命令）
python3 tester.py --debug samples/*.in
```

---

## 手动测试

### 准备测试环境

创建一个测试目录：

```bash
mkdir test-gitlet
cd test-gitlet
```

### 基本命令格式

```bash
# 从项目根目录运行
java gitlet.Main <command> [args...]

# 例如
java gitlet.Main init
java gitlet.Main add wug.txt
java gitlet.Main commit "message"
```

---

## 功能测试清单

### 1. 初始化测试

```bash
java gitlet.Main init

# 验证：
# - .gitlet 目录被创建
# - .gitlet/HEAD 文件存在
# - .gitlet/refs/heads/master 文件存在
# - 初始提交被创建
```

### 2. 添加文件测试

```bash
# 创建测试文件
echo "This is a wug." > wug.txt

# 添加到暂存区
java gitlet.Main add wug.txt

# 验证：
java gitlet.Main status  # 应该看到 wug.txt 在暂存区
```

### 3. 提交测试

```bash
java gitlet.Main commit "added wug"

# 验证：
java gitlet.Main log     # 应该看到提交记录
```

### 4. 状态查看测试

```bash
java gitlet.Main status

# 应该显示：
# - 当前分支（标有 *）
# - 已暂存文件
# - 已移除文件
# - 已修改但未暂存的文件
# - 未跟踪的文件
```

### 5. 日志查看测试

```bash
# 查看当前分支的提交历史
java gitlet.Main log

# 查看所有提交
java gitlet.Main global-log

# 查找特定提交
java gitlet.Main find "added wug"
```

### 6. Checkout 测试

#### 6.1 从 HEAD 检出文件
```bash
# 修改文件
echo "This is not a wug." > wug.txt

# 从 HEAD 恢复文件
java gitlet.Main checkout -- wug.txt

# 验证：文件内容应该恢复到上次提交的版本
cat wug.txt
```

#### 6.2 从指定提交检出文件
```bash
# 获取提交 ID（从 log 中）
java gitlet.Main log

# 使用提交 ID 检出文件
java gitlet.Main checkout <commit-id> -- wug.txt
```

#### 6.3 检出分支
```bash
# 先创建新分支
java gitlet.Main branch feature

# 切换到新分支
java gitlet.Main checkout feature

# 验证：
java gitlet.Main status  # 应该显示当前分支是 feature
```

### 7. 分支管理测试

```bash
# 创建分支
java gitlet.Main branch new-branch

# 列出分支
java gitlet.Main status  # 应该看到所有分支

# 删除分支（不能删除当前分支）
java gitlet.Main checkout master
java gitlet.Main rm-branch new-branch
```

### 8. 删除文件测试

```bash
# 创建并提交文件
echo "test" > test.txt
java gitlet.Main add test.txt
java gitlet.Main commit "add test.txt"

# 从暂存区移除文件（如果已暂存）
java gitlet.Main rm test.txt

# 或者直接从工作目录删除并标记为移除
rm test.txt
java gitlet.Main rm test.txt

# 提交移除
java gitlet.Main commit "remove test.txt"
```

### 9. Reset 测试

```bash
# 查看提交历史
java gitlet.Main log

# 重置到指定提交
java gitlet.Main reset <commit-id>

# 验证：
# - HEAD 指向指定的提交
# - 工作目录的文件恢复到该提交的状态
# - 暂存区被清空
```

### 10. Merge 测试

#### 10.1 简单合并
```bash
# 在 master 分支上工作
echo "master version" > file.txt
java gitlet.Main add file.txt
java gitlet.Main commit "commit on master"

# 创建并切换到新分支
java gitlet.Main branch feature
java gitlet.Main checkout feature

# 在新分支上修改
echo "feature version" > file.txt
java gitlet.Main add file.txt
java gitlet.Main commit "commit on feature"

# 切回 master 并合并
java gitlet.Main checkout master
java gitlet.Main merge feature

# 验证：
java gitlet.Main log  # 应该看到合并提交
```

#### 10.2 冲突合并
```bash
# 在两个分支上修改同一文件的不同部分
# master 分支
echo "master line" > conflict.txt
java gitlet.Main add conflict.txt
java gitlet.Main commit "master change"

# feature 分支
java gitlet.Main checkout feature
echo "feature line" > conflict.txt
java gitlet.Main add conflict.txt
java gitlet.Main commit "feature change"

# 合并（应该产生冲突）
java gitlet.Main checkout master
java gitlet.Main merge feature

# 验证冲突标记
cat conflict.txt
# 应该包含：
# <<<<<<< HEAD
# master line
# =======
# feature line
# >>>>>>>
```

### 11. 远程仓库测试

#### 11.1 添加远程仓库
```bash
# 假设在另一个目录有远程仓库
# 在远程仓库目录
cd /path/to/remote-repo
java gitlet.Main init

# 回到本地仓库
cd /path/to/local-repo
java gitlet.Main add-remote origin /path/to/remote-repo/.gitlet
```

#### 11.2 Fetch 测试
```bash
# 从远程获取分支
java gitlet.Main fetch origin master

# 验证：
java gitlet.Main branch -a  # 应该看到 origin/master
```

#### 11.3 Push 测试
```bash
# 推送当前分支到远程
java gitlet.Main push origin master
```

#### 11.4 Pull 测试
```bash
# Pull = Fetch + Merge
java gitlet.Main pull origin master
```

#### 11.5 删除远程仓库
```bash
java gitlet.Main rm-remote origin
```

---

## 完整测试脚本示例

创建一个 `test-all.sh` 脚本进行完整测试：

```bash
#!/bin/bash

# 创建测试目录
mkdir -p test-gitlet-full
cd test-gitlet-full

# 1. 初始化
echo "=== 测试 1: 初始化 ==="
java gitlet.Main init
ls -la .gitlet

# 2. 添加和提交
echo "=== 测试 2: 添加和提交 ==="
echo "This is a wug." > wug.txt
java gitlet.Main add wug.txt
java gitlet.Main commit "added wug"
java gitlet.Main log

# 3. 状态
echo "=== 测试 3: 状态 ==="
java gitlet.Main status

# 4. 修改和 checkout
echo "=== 测试 4: 修改和 checkout ==="
echo "This is not a wug." > wug.txt
java gitlet.Main checkout -- wug.txt
cat wug.txt

# 5. 分支
echo "=== 测试 5: 分支 ==="
java gitlet.Main branch feature
java gitlet.Main checkout feature
java gitlet.Main status

# 6. 合并
echo "=== 测试 6: 合并 ==="
echo "feature change" > feature.txt
java gitlet.Main add feature.txt
java gitlet.Main commit "add feature"
java gitlet.Main checkout master
java gitlet.Main merge feature
java gitlet.Main log

echo "=== 所有测试完成 ==="
```

---

## 调试技巧

### 1. 查看 .gitlet 目录结构
```bash
tree .gitlet
# 或
find .gitlet -type f
```

### 2. 检查序列化对象
```bash
# 使用 DumpObj 查看对象内容
java gitlet.DumpObj .gitlet/HEAD
java gitlet.DumpObj .gitlet/index
```

### 3. 查看提交内容
```bash
# 从 log 获取提交 ID，然后查看对象文件
java gitlet.DumpObj .gitlet/objects/XX/XXXX...
```

### 4. 清理并重新测试
```bash
# 删除测试目录重新开始
rm -rf test-gitlet
```

---

## 常见问题排查

### 问题 1：命令未找到
- 确保在项目根目录运行
- 确保已编译：`make` 或 `cd gitlet && make`

### 问题 2：文件路径问题
- Windows 用户注意路径分隔符
- 使用相对路径而不是绝对路径

### 问题 3：提交信息为空
- 提交消息必须用引号括起来：`commit "message"`

### 问题 4：合并冲突
- 检查冲突标记是否正确
- 手动解决冲突后需要重新 add 和 commit

---

## 测试覆盖检查

确保测试覆盖以下所有功能：

- [x] init
- [x] add
- [x] commit
- [x] rm
- [x] log
- [x] global-log
- [x] find
- [x] status
- [x] checkout (文件、提交、分支)
- [x] branch
- [x] rm-branch
- [x] reset
- [x] merge (正常合并、冲突合并、fast-forward)
- [x] add-remote
- [x] rm-remote
- [x] fetch
- [x] push
- [x] pull

---

## 参考

- 查看 `testing/samples/` 目录中的示例测试文件
- 参考 `testing/tester.py` 了解测试脚本的工作原理
- 查看 `gitlet/Main.java` 中的帮助信息
