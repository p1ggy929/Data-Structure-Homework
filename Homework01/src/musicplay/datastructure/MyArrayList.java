package musicplay.datastructure;

import java.util.AbstractList;
import java.util.Arrays;

/**
 * 自定义动态数组实现。
 * 内部使用 Object 数组存储数据。
 */
public class MyArrayList extends AbstractList {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 100;
    
    
    /**
     * 获取指定索引位置的元素。
     * @param index 要获取的元素的索引
     * @return 列表中指定索引的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围 (index < 0 || index >= size())
     */
    @Override
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }
    
    /**
     * 将指定的元素追加到此列表的末尾。
     * @param e 要添加到列表的元素
     * @return true (遵从 Collection.add 的约定)
     */
    @Override
    public boolean add(Object e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }
    

    /**
     * 构造函数，初始化一个具有默认容量的空列表。
     */
    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
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
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Object oldValue = elements[index];
        elements[index] = element;
        return oldValue;
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
     * 如有必要，增加此ArrayList实例的容量，以确保它至少可以容纳由size字段指示的元素数量。
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
} 