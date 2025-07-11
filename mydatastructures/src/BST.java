import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * Binary Search Tree（二叉搜索树，简称 BST）。
 * <p>
 * 一种特殊的二叉树，满足：<br>
 * - 每个节点的左子树所有节点值小于该节点值<br>
 * - 每个节点的右子树所有节点值大于该节点值<br>
 * - 左右子树也分别为二叉搜索树
 * </p>
 * @author Tomatos
 * @date 2025/7/2 14:05
 */
public class BST<T extends Comparable<T>> {
    private Node<T> root;

    /**
     * 插入值到二叉搜索树中
     *
     * @param node 当前所处节点
     * @param val 插入值
     * @return BST.Node<T> 当前所处节点
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:36
     */
    private Node<T> insert(Node<T> node, T val) {
        if (node == null)
            return new Node<>(val, 1);

        if (val.compareTo(node.val) == 0)
            node.count++;
        else if (val.compareTo(node.val) < 0)
            node.left = insert(node.left, val);
        else if (val.compareTo(node.val) > 0)
            node.right = insert(node.right, val);

        node.size = 1;
        if (node.left != null)
            node.size += node.left.size;
        if (node.right != null)
            node.size += node.right.size;

        return node;
    }

    public void insert(T val) {
        if (root == null) {
            root = new Node<>(val, 1);
        } else
            insert(root, val);
    }

    /**
     * 查询二叉搜索树中升序排序后的第 k 个元素（从小到大）。
     *
     * @param k 第 k 个元素（1 表示最小元素）
     * @return 第 k 小的元素值；如果 k 超出范围，返回 null
     * @since 1.0
     * @throws IndexOutOfBoundsException 查询不在合法范围[1,size]
     * @author Tomatos
     * @date 2025/7/3 21:39
     * @timeComplexity O(h)，其中 h 为树的高度
     */
    public T queryKth(int k) {
        checkInRange(k);

        return queryKth(root, k);
    }

    private T queryKth(Node<T> node, int k) {
        if (node.left != null) {
            // 左子树大小 >= k, 说明查询的元素在左子树
            if (node.left.size >= k)
                return queryKth(node.left, k);

            // 去掉比k小的节点
            k -= node.left.size;
        }

        if (k != 1)
            return queryKth(node.right, k - 1);

        return node.val;
    }

    /**
     * 检查值是否在合法范围内
     *
     */
    private void checkInRange(int v) {
        if (v < 1 || v > root.size) {
            throw new IndexOutOfBoundsException(v + " is out of valid range [1, " + root.size + "]");
        }
    }

    /**
     * val 的数量减少1, 当val的数量为0移除该节点
     *
     * @param node 当前所处节点
     * @param val 需要减少计数或移除的值
     * @return BST.Node<T>
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:29
     */
    private Node<T> remove(Node<T> node, T val) {
        if (node == null)
            return null;

        if (val.compareTo(node.val) < 0)
            node.left = remove(node.left, val);
        else if (val.compareTo(node.val) > 0)
            node.right = remove(node.right, val);
        else if (node.count > 1)
            --node.count;
        else {
            Node<T> newSubTreeRoot = mergeSubtrees(node.left, node.right);
            if (root == node)
                root = newSubTreeRoot;

            return newSubTreeRoot;
        }

        node.size = 1;
        if (node.left != null)
            node.size += node.left.size;
        if (node.right != null)
            node.size += node.right.size;

        return node;
    }

    public void remove(T val) {
        remove(root, val);
    }

    /**
     * 获取二叉搜索树的节点数量
     *
     * @return int 节点数量
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:29
     */
    public int size() {
        return root.size;
    }

    /**
     * 合并两棵子树
     *
     * @param lSubtreeRoot  左子树的根节点
     * @param rSubtreeRoot 右子树的根节点
     * @return 合并后的子树根节点
     */
    private Node<T> mergeSubtrees(Node<T> lSubtreeRoot, Node<T> rSubtreeRoot) {
        if (rSubtreeRoot == null)
            return lSubtreeRoot;

        if (lSubtreeRoot == null)
            return rSubtreeRoot;

        if (rSubtreeRoot.left != null)
            rSubtreeRoot.right = mergeSubtrees(rSubtreeRoot.left, rSubtreeRoot.right);

        rSubtreeRoot.left = lSubtreeRoot;
        rSubtreeRoot.size += lSubtreeRoot.size;
        return rSubtreeRoot;
    }


    /**
     *
     * 查询最小节点值
     *
     * @return T 所有节点值中最小的节点值
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:30
     */
    public T findMin() {
        Node<T> node = root;
        while (node.left != null)
            node = node.left;

        return node.val;
    }

    /**
     *
     * 查询最大节点值
     *
     * @return T 所有节点值中最大的节点值
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:30
     */
    public T findMax() {
        Node<T> node = root;
        while (node.right != null)
            node = node.right;

        return node.val;
    }

    /**
     * 判断BST是否包含某个值
     *
     * @param val 需要判断的值
     * @return java.lang.Boolean true 表示包含, false 表示不包含
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:44
     */
    public Boolean contains(T val) {
        return contains(root, val);
    }

    private Boolean contains(Node<T> node, T val) {
        if (node == null)
            return false;

        if (node.val == val)
            return true;
        else if (val.compareTo(node.val) > 0)
            return contains(node.right, val);
        else
            return contains(node.left, val);
    }


    private void inOrderTraversal(Node<T> node) {
        if (node.left != null)
            inOrderTraversal(node.left);

        System.out.print(node.val + " ");

        if (node.right != null)
            inOrderTraversal(node.right);
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
        System.out.println();
    }

    public void levelOrderTraversal() {
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            StringBuilder curStr = new StringBuilder();
            for (int i = 0; i < size; ++i) {
                Node<T> node = queue.poll();

                Objects.requireNonNull(node);

                curStr.append(node.val)
                      .append(" ");

                if (node.left != null)
                    queue.add(node.left);

                if (node.right != null)
                    queue.add(node.right);
            }
            System.out.println(curStr);
        }
    }

    /**
     * BST节点的抽象
     *
     * @since : 1.0
     * @author : Tomatos
     * @date : 2025/7/3 21:47
     */
    private static class Node<T> {
        T val;
        Node<T> left, right;
        int count;
        int size = 1; // 以当前节点为根的子树的大小

        public Node(T val, int count) {
            this.val = val;
            this.count = count;
        }
    }
}
