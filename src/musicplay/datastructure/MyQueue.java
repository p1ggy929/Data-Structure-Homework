package musicplay.datastructure;

import java.util.NoSuchElementException;

/**
 * 自定义循环队列 (Circular Queue) 实现。
 * 内部使用一个动态数组来存储元素，并用头尾指针来管理队列。
 */
public class MyQueue {
    private Object[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 100;

    /**
     * 构造函数，初始化一个具有默认容量的空队列。
     */
    public MyQueue() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * 将一个元素添加到队尾 (入队)。
     * @param e 要添加的元素
     * @return true
     */
    public boolean enqueue(Object e) {
        // 如果队列已满，扩容
        if (size == elements.length) {
            resize();
        }
        elements[tail] = e;
        // 更新tail指针，考虑循环
        tail = (tail + 1) % elements.length;
        size++;
        return true;
    }

    /**
     * 移除并返回队头的元素 (出队)。
     * @return 队头的元素
     * @throws NoSuchElementException 如果队列为空
     */
    public Object dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Object result = elements[head];
        // 清除引用，帮助垃圾回收
        elements[head] = null;
        // 更新head指针，考虑循环
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    /**
     * 查看队头的元素但不移除它。
     * @return 队头的元素
     * @throws NoSuchElementException 如果队列为空
     */
    public Object peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Object result = elements[head];
        return result;
    }

    /**
     * 获取队列中指定逻辑索引的元素（从队头开始计算）。
     * @param index 要获取的元素的索引 (0-based, 逻辑索引)
     * @return 指定索引的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int physicalIndex = (head + index) % elements.length;
        return elements[physicalIndex];
    }

    /**
     * 返回队列中的元素数量。
     * @return 队列大小
     */
    public int size() {
        return size;
    }

    /**
     * 检查队列是否为空。
     * @return 如果队列为空则返回 true
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 清空队列。
     */
    public void clear() {
        // 重置所有状态，让垃圾回收器处理旧数组
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * 内部私有方法，用于当数组容量不足时进行扩容。
     * 会创建一个容量为原数组两倍的新数组。
     */
    private void resize() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }
} 