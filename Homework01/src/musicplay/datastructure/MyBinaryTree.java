package musicplay.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定的二叉树。
 */
public class MyBinaryTree {

    /**
     * 内部类，表示二叉树中的一个节点。
     */
    public static class Node {
        /**
         * 节点存储的值，可以是任何对象（如歌手名、专辑名、或Song对象）。
         */
        public Object value;
        /**
         * 指向左子节点和右子节点的引用。
         */
        public Node left, right;

        /**
         * 节点的构造函数。
         * @param value 该节点要存储的值
         */
        public Node(Object value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    
    
    
    /**
     * 先序遍历二叉树（根-左-右的）的实现。
     * @return 包含所有节点值的列表（按先序遍历顺序），即将遍历的节点都加入到result中
     */
    private void preorderTraversalRecursive(Node node, List<Object> result) {
    	// TODO 待实现
    }
    
    /**
     * 获取二叉树的深度的实现。
     * 树的深度定义为从根节点到最远叶子节点的最长路径上的节点数。
     * @return 树的最大深度
     */
    private int getDepthRecursive(Node node) {
    	// TODO 待实现
    	return -1;
    }
    
    /**
     * 递归辅助方法，用于计算从指定节点开始的子树中的叶子节点数量。
     * @param node 当前递归到的节点。
     * @return 从该节点开始的子树所包含的叶子节点总数。
     */
    private int getLeafCountRecursive(Node node) {
    	// TODO 待实现
    	return -1;
    }
    

    /**
     * 构造函数，初始化一个空的二叉树。
     */
    public MyBinaryTree() {
        root = null;
    }
    
    /**
     * 获取或设置二叉树的根节点。
     * @return 根节点
     */
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * 先序遍历二叉树（根-左-右）。
     * @return 包含所有节点值的列表（按先序遍历顺序）
     */
    public List<Object> preorderTraversal() {
        List<Object> result = new ArrayList<>();
        preorderTraversalRecursive(root, result);
        return result;
    }

    /**
     * 获取二叉树的深度（或高度）。
     * 树的深度定义为从根节点到最远叶子节点的最长路径上的节点数。
     * @return 树的最大深度
     */
    public int getDepth() {
        return getDepthRecursive(root);
    }

    /**
     * 获取叶子节点的数量。
     * 叶子节点是指没有子节点的节点。
     * @return 叶子节点的数量
     */
    public int getLeafCount() {
        return getLeafCountRecursive(root);
    }
} 