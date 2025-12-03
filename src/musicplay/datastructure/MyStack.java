package musicplay.datastructure;

import java.util.EmptyStackException;
import java.util.Arrays;

/**
 * 自定义栈 (Stack) 实现。
 * 内部使用一个动态数组（顺序表）来存储元素。
 */
public class MyStack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 100;

    /**
     * 构造函数，初始化一个具有默认容量的空栈。
     */
    public MyStack() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    /**
     * 将一个元素压入栈顶 (Push)。
     * @param item 要压入的元素
     */
    public void push(Object item) {
        ensureCapacity();
        elements[size++] = item;
    }

    /**
     * 移除并返回栈顶的元素，出栈 (Pop)
     * @return 栈顶的元素
     */
    public Object pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // 清除引用，帮助垃圾回收
        return result;
    }

    /**
     * 查看栈顶的元素但不移除它 (Peek)。
     * @return 栈顶的元素
     * @throws EmptyStackException 如果栈为空
     */
    public Object peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements[size - 1];
    }

    /**
     * 从栈中移除第一个匹配的指定元素。
     * @param o 要移除的元素
     * @return 如果成功移除则返回 true
     */
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                removeElementAtIndex(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 移除并返回栈底的元素。
     * 此方法是为了满足特定业务需求（如历史记录上限）而添加，不是标准栈操作。
     * @return 栈底的元素
     * @throws EmptyStackException 如果栈为空
     */
    public Object removeLast() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        Object lastItem = elements[0];
        removeElementAtIndex(0);
        return lastItem;
    }

    /**
     * 私有辅助方法，移除指定索引处的元素。
     * @param index 要移除的元素的索引
     */
    private void removeElementAtIndex(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // 清除最后一个元素以帮助GC
    }

    /**
     * 检查栈是否为空。
     * @return 如果栈为空则返回 true
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回栈中的元素数量。
     * @return 栈中的元素数量
     */
    public int size() {
        return size;
    }

    /**
     * 清空栈。
     */
    public void clear() {
        // 让GC处理旧数组，并重置状态
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * 内部私有方法，用于当数组容量不足时进行扩容。
     * 会创建一个容量为原数组两倍的新数组。
     */
    private void ensureCapacity() {
        int newCapacity = elements.length * 2;
        elements = Arrays.copyOf(elements, newCapacity);
    }
} 