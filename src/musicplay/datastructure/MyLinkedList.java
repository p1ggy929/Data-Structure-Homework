package musicplay.datastructure;

import java.util.AbstractList;
import java.util.List;

/**
 * 自定义双向链表 (LinkedList) 实现。
 */
public class MyLinkedList extends AbstractList {
	// 头节点指针
    private Node head;
    
    // 尾节点指针
    private Node tail;
    
    private int size;

    /**
     * 内部类，表示链表中的一个节点。
     */
    private static class Node {
        /**
         * 节点存储的值。
         */
        Object item;
        /**
         * 指向后一个节点的引用。
         */
        Node next;
        /**
         * 指向前一个节点的引用。
         */
        Node prev;

        /**
         * 节点的构造函数。
         * @param prev 指向前一个节点的引用
         * @param element 该节点要存储的值
         * @param next 指向后一个节点的引用
         */
        Node(Node prev, Object element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
    
    /**
     * 将指定的元素追加到此列表的末尾。
     * @param e 要添加到列表的元素
     * @return true 
     */
    @Override
    public boolean add(Object e) {
        final Node l = tail;
        final Node newNode = new Node(l, e, null);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }
        size++;
        return true;
    }
    
    /**
     * 获取指定索引位置的元素。
     * @param index 要获取的元素的索引，index从0开始
     * @return 列表中指定索引的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围 (index < 0 || index >= size())
     */
    @Override
    public Object get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }
    

    /**
     * 构造函数，初始化一个空的链表。
     */
    public MyLinkedList() {
        size = 0;
    }

    /**
     * 替换列表中指定位置的元素。
     * @param index 要替换的元素的索引
     * @param element 要存储在指定位置的元素
     * @return 先前在指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围 (index < 0 || index >= size())
     */
    @Override
    public Object set(int index, Object element) {
        checkElementIndex(index);
        Node x = node(index);
        Object oldVal = x.item;
        x.item = element;
        return oldVal;
    }
    
    /**
     * 返回此列表中的元素数。
     * @return 列表中的元素数量
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 从此列表中移除所有元素。
     */
    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    /**
     * 检查给定索引是否在列表的有效范围内。
     * @param index 要检查的索引
     * @throws IndexOutOfBoundsException 如果索引无效
     */
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /**
     * 判断给定索引是否是列表中存在的一个元素的索引。
     * @param index 要判断的索引
     * @return 如果索引有效则返回 true
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * 返回指定元素索引处的（非空）节点。
     * 头节点开始遍历。
     * @param index 要获取的节点的索引
     * @return 指定索引处的节点
     */
    private Node node(int index) {
        // 1. 从头节点开始
        Node currentNode = head;

        // 2. 使用一个循环，从头向后遍历 index 次
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        // 3. 返回找到的节点
        return currentNode;
    }
} 