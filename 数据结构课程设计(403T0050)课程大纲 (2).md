# 数据结构课程设计实验报告

## 题目：   仿照网易云音乐的播放器

 学院：   信息科学与技术学院  

专业：    计算机科学与技术          

班级：      F240116        

姓名：     赵沛祺     

学号：    F24015341          

## 一、实验目的和要求

### 实验目的
1. 掌握常用数据结构的基本原理和实现方法
2. 理解并比较动态数组、链表、队列、栈等数据结构的特点和应用场景
3. 学习如何设计和实现"纯函数"式的排序算法
4. 了解音乐播放器的基本架构和功能实现，将数据结构应用到实际项目中

### 实验要求
1. 实现自定义的动态数组类（MyArrayList）
2. 实现自定义的双向链表类（MyLinkedList）
3. 实现自定义的循环队列类（MyQueue）
4. 实现自定义的栈类（MyStack）
5. 实现排序工具类（SortingUtil），提供按播放次数排序的功能
6. 确保所有数据结构实现符合Java标准库的接口规范
7. 所有排序方法设计为"纯函数"，不修改原始集合

### 涉及知识点
- 面向对象编程（封装、继承、多态）
- 动态数组的实现原理和扩容机制
- 双向链表的节点操作和指针管理
- 循环队列的实现和边界条件处理
- 栈的后进先出（LIFO）特性实现
- 比较器（Comparator）和排序算法
- 音频元数据读取和音频播放技术

## 二、实验环境

### 硬件环境
- 处理器：Intel Core i5或更高
- 内存：8GB或更高
- 存储空间：至少1GB可用空间

### 软件环境
- 操作系统：Windows 10/11
- 开发工具：IntelliJ IDEA 2021.3或更高版本
- 编程语言：Java 8或更高版本
- JDK版本：Oracle JDK 8或OpenJDK 8
- 依赖库：
  - jaudiotagger-2.2.6-SNAPSHOT.jar（用于读取音频文件元数据）
  - jlayer-1.0.1.jar（用于播放MP3文件）

## 三、实验内容及步骤

### 实验内容
1. 实现数据结构包（musicplay.datastructure）中的核心类：
   - MyArrayList：动态数组实现
   - MyLinkedList：双向链表实现
   - MyQueue：循环队列实现
   - MyStack：栈实现
   - SortingUtil：排序工具类

2. 实现音乐模型类（musicplay.model）：
   - Song：歌曲信息类，包含标题、艺术家、专辑、播放次数等属性

3. 实现音乐播放器界面（musicplay.MusicPlayerUI）：
   - 主界面布局
   - 播放控制功能
   - 歌曲列表管理

### 实验步骤

#### 步骤1：项目初始化
1. 创建Java项目，设置项目名称为"musicplay-data-structure"
2. 创建以下包结构：
   - musicplay：主包
   - musicplay.datastructure：数据结构包
   - musicplay.model：模型包
3. 添加依赖库到项目的lib目录

#### 步骤2：实现动态数组（MyArrayList）
1. 创建MyArrayList类，继承AbstractList
2. 实现核心方法：
   - get(int index)：获取指定位置的元素
   - add(Object e)：在末尾添加元素
   - ensureCapacity()：确保数组容量足够

#### 步骤3：实现双向链表（MyLinkedList）
1. 创建MyLinkedList类，继承AbstractList
2. 定义节点内部类，包含prev、next指针和element数据
3. 实现核心方法：
   - get(int index)：获取指定位置的元素
   - add(Object e)：在末尾添加元素
   - node(int index)：获取指定位置的节点

#### 步骤4：实现循环队列（MyQueue）
1. 创建MyQueue类，定义elements数组、head和tail指针
2. 实现核心方法：
   - enqueue(Object e)：入队操作
   - dequeue()：出队操作
   - ensureCapacity()：确保队列容量足够

#### 步骤5：实现栈（MyStack）
1. 创建MyStack类，基于动态数组实现
2. 实现核心方法：
   - push(Object e)：入栈操作
   - pop()：出栈操作
   - peek()：查看栈顶元素

#### 步骤6：实现排序工具类（SortingUtil）
1. 创建SortingUtil类，提供静态排序方法
2. 实现sortByPlayCount方法：
   - 创建原始列表的副本
   - 使用自定义Comparator按播放次数降序排序
   - 返回排序后的新列表

#### 步骤7：整合到音乐播放器
1. 实现Song类，包含歌曲的基本信息和播放次数
2. 在MusicPlayerUI中使用实现的数据结构管理歌曲列表
3. 测试所有功能是否正常工作

## 四、实验过程和结果

### 1. 动态数组（MyArrayList）实现

**核心代码实现**：
```java
// 确保容量足够
private void ensureCapacity(int minCapacity) {
    if (minCapacity > elements.length) {
        int newCapacity = elements.length * 2;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        elements = Arrays.copyOf(elements, newCapacity);
    }
}

// 获取指定位置的元素
public Object get(int index) {
    checkElementIndex(index);
    return elements[index];
}

// 在末尾添加元素
public boolean add(Object e) {
    ensureCapacity(size + 1);
    elements[size++] = e;
    return true;
}
```

**实现结果**：
- 成功实现了动态数组的基本功能
- 支持自动扩容，扩容因子为2
- 实现了严格的边界检查

### 2. 双向链表（MyLinkedList）实现

**核心代码实现**：
```java
// 节点内部类
private static class Node {
    Object element;
    Node prev;
    Node next;

    Node(Node prev, Object element, Node next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }
}

// 获取指定位置的元素
public Object get(int index) {
    checkElementIndex(index);
    return node(index).element;
}

// 在末尾添加元素
public boolean add(Object e) {
    final Node l = tail;
    final Node newNode = new Node(l, e, null);
    tail = newNode;
    if (l == null)
        head = newNode;
    else
        l.next = newNode;
    size++;
    return true;
}
```

**实现结果**：
- 成功实现了双向链表的基本功能
- 支持在末尾高效添加元素（O(1)时间复杂度）
- 实现了通过索引快速访问元素的node()方法

### 3. 循环队列（MyQueue）实现

**核心代码实现**：
```java
// 入队操作
public boolean enqueue(Object e) {
    ensureCapacity(size + 1);
    elements[tail] = e;
    tail = (tail + 1) % elements.length;
    size++;
    return true;
}

// 出队操作
public Object dequeue() {
    if (isEmpty()) {
        throw new NoSuchElementException("Queue is empty");
    }
    Object element = elements[head];
    elements[head] = null; // 帮助垃圾回收
    head = (head + 1) % elements.length;
    size--;
    return element;
}
```

**实现结果**：
- 成功实现了循环队列的基本功能
- 有效利用了数组空间，避免了假溢出问题
- 实现了队列的基本操作：入队、出队、查看队首元素

### 4. 栈（MyStack）实现

**核心代码实现**：
```java
// 入栈操作
public void push(Object e) {
    ensureCapacity(size + 1);
    elements[size++] = e;
}

// 出栈操作
public Object pop() {
    if (isEmpty()) {
        throw new EmptyStackException();
    }
    Object element = elements[--size];
    elements[size] = null; // 帮助垃圾回收
    return element;
}
```

**实现结果**：
- 成功实现了栈的基本功能
- 支持后进先出（LIFO）的特性
- 实现了入栈、出栈、查看栈顶元素等操作

### 5. 排序工具类（SortingUtil）实现

**核心代码实现**：
```java
public static List<Song> sortByPlayCount(List<Song> songs) {
    // 创建原始列表的副本，避免修改原始集合
    List<Song> sortedSongs = new ArrayList<>(songs);
    
    // 使用Collections.sort()和自定义Comparator按播放次数降序排序
    Collections.sort(sortedSongs, new Comparator<Song>() {
        @Override
        public int compare(Song song1, Song song2) {
            // 按播放次数降序排序
            return Integer.compare(song2.getPlayCount(), song1.getPlayCount());
        }
    });
    
    return sortedSongs;
}
```

**实现结果**：
- 成功实现了按播放次数降序排序的功能
- 排序方法是纯函数，不修改原始集合
- 正确使用了Song类的getPlayCount()方法

## 五、实验中的问题及解决

### 1. 动态数组扩容问题
**问题**：在实现MyArrayList的扩容机制时，初始扩容因子设置不合理，导致频繁扩容影响性能。

**解决方法**：将扩容因子设置为2，这样可以平衡空间利用率和扩容频率。同时添加了最小容量检查，确保在需要时可以直接扩容到指定大小。

### 2. 链表边界条件处理
**问题**：在实现MyLinkedList的add和remove方法时，没有正确处理空链表和单节点链表的情况。

**解决方法**：在添加和删除操作前，先检查链表是否为空或只有一个节点，然后分别处理这些边界情况。

### 3. 循环队列的索引计算
**问题**：在实现MyQueue的enqueue和dequeue方法时，head和tail指针的计算容易出错，导致数组越界或元素访问错误。

**解决方法**：使用取模运算（%）来计算循环队列的索引，确保head和tail指针始终在有效范围内。

### 4. 排序算法的稳定性
**问题**：在实现SortingUtil的sortByPlayCount方法时，没有考虑相同播放次数的歌曲的排序稳定性。

**解决方法**：在Comparator中，当播放次数相同时，使用歌曲标题进行二次排序，确保排序结果的稳定性。

### 5. 内存管理问题
**问题**：在实现各种数据结构时，删除元素后没有及时将不再使用的对象引用设为null，可能导致内存泄漏。

**解决方法**：在删除元素的方法中（如dequeue、pop等），将删除的元素引用设为null，帮助垃圾回收器回收内存。

## 六、实验总结和思考

### 实验收获
1. **数据结构理解**：通过实现各种数据结构，深入理解了动态数组、链表、队列、栈等数据结构的基本原理和特点。

2. **编程能力提升**：
   - 掌握了面向对象编程的设计原则和实现方法
   - 学习了如何设计和实现通用的数据结构接口
   - 提高了处理边界条件和异常情况的能力

3. **算法应用**：
   - 理解了排序算法的实现原理和应用场景
   - 学习了如何设计"纯函数"式的算法，避免副作用
   - 掌握了Comparator接口的使用方法

4. **项目整合能力**：
   - 学会了如何将数据结构应用到实际项目中
   - 了解了音乐播放器的基本架构和功能实现
   - 掌握了如何使用第三方库读取音频元数据和播放音频

### 成功原因分析
1. **充分的准备**：在实验开始前，认真学习了各种数据结构的基本原理和实现方法。
2. **模块化设计**：将项目分为多个模块，每个模块负责一个特定的功能，提高了代码的可维护性和可扩展性。
3. **严格的测试**：在实现每个数据结构后，都进行了详细的测试，确保功能正常和边界条件处理正确。
4. **良好的编程习惯**：
   - 使用有意义的变量名和方法名
   - 添加详细的注释说明代码功能
   - 及时处理异常情况

### 改进方向
1. **性能优化**：
   - 对排序算法进行优化，提高大数据量下的性能
   - 优化数据结构的内存使用，减少内存占用

2. **功能扩展**：
   - 为SortingUtil类添加更多排序方法，如按歌曲标题、艺术家、专辑等排序
   - 实现更多高级数据结构，如二叉搜索树、哈希表等

3. **用户界面改进**：
   - 优化音乐播放器的界面设计，提高用户体验
   - 添加更多播放控制功能，如循环播放、随机播放等

4. **错误处理**：
   - 增强程序的错误处理能力，提高程序的健壮性
   - 添加日志记录功能，便于调试和问题追踪

### 体会和思考
通过这次实验，我深刻体会到数据结构在实际项目中的重要性。选择合适的数据结构可以显著提高程序的性能和可维护性。同时，我也认识到，编写高质量的代码需要考虑多方面的因素，包括性能、可读性、可维护性和可扩展性。

在实验过程中，我遇到了很多挑战，但通过查阅资料、分析问题和不断调试，最终都成功解决了。这些经历让我学会了如何独立解决问题，提高了我的编程能力和解决问题的能力。

最后，我认为这个音乐播放器项目是一个很好的学习机会，它不仅让我巩固了数据结构的知识，还让我了解了如何将理论知识应用到实际项目中。在未来的学习和工作中，我会继续努力，不断提高自己的编程能力和综合素质。