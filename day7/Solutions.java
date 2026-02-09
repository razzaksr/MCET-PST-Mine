package day7;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solutions {
    /*
    ⚙️ Approaches
    - Recursive DFS
        - Simple, but uses call stack → O(n) space in worst case.
        - Runtime optimal, but memory percentile lower due to recursion overhead.
    - Iterative with Stack
        - Explicit stack, O(n) space.
        - Runtime good, memory moderate.
    - Morris Traversal (Threaded Binary Tree)
        - O(n) runtime, O(1) extra space (no recursion, no stack).
        - This is the most memory‑efficient solution and usually ranks 95%+ in both runtime and memory.
        🧩 Key Idea of Morris Inorder
            - If curr.left == null: visit curr, move right.
            - Else: find predecessor (pre) in left subtree.
            - If pre.right == null: create thread → move left.
            - If pre.right == curr: remove thread → visit curr → move right.

    📊 Complexity
    - Time Complexity: O(n) — each node visited at most twice.
    - Space Complexity: O(1) — no stack or recursion, only pointers
    */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;
        while (curr != null) {
            if (curr.left == null) {
                result.add(curr.val);
                curr = curr.right;
            } else {
                TreeNode pre = curr.left;
                while (pre.right != null && pre.right != curr)
                    pre = pre.right;
                if (pre.right == null) {
                    pre.right = curr; // create thread
                    curr = curr.left;
                } else {
                    pre.right = null; // remove thread
                    result.add(curr.val);
                    curr = curr.right;
                }
            }
        }
        return result;
    }
    // Traversal via recursion
    public List<Integer> inorderTraversalViaRecursion(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }
    private void inorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.val);
        inorder(node.right, result);
    }
    // recursion
    public List<Integer> preorderTraversalViaRecursion(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }
    private void preorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);       // root
        preorder(node.left, result); // left
        preorder(node.right, result); // right
    }
    // via morris
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                result.add(curr.val);
                curr = curr.right;
            } else {
                TreeNode pre = curr.left;
                while (pre.right != null && pre.right != curr) {
                    pre = pre.right;
                }
                if (pre.right == null) {
                    result.add(curr.val); // visit before creating thread
                    pre.right = curr;
                    curr = curr.left;
                } else {
                    pre.right = null; // remove thread
                    curr = curr.right;
                }
            }
        }
        return result;
    }
    // post traverse via recursion
    /*- Time Complexity: O(n).
    - Space Complexity: O(h) (stack depth, h = tree height).
    - Elegant and simple, but recursion overhead keeps memory beats lower.
    */
    public List<Integer> postorderTraversalViaRecursion(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }
    private void postorder(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postorder(node.left, result);   // left
        postorder(node.right, result);  // right
        result.add(node.val);           // root
    }
    // via recursion to count nodes
    /*
    - Time Complexity: O(n) — visits every node.
    - Space Complexity: O(h) (stack depth).
    - Easy to implement, but not optimal for large trees.
    */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
    }
    // via height based count of total
    /*
    - Time Complexity: O(log² n).
    - Each level we compute heights (O(log n)), and recurse down (O(log n) levels).
    - Space Complexity: O(1) extra (besides recursion stack).
    - Much faster than O(n) for large trees.
    */
    public static int countNodesViaHeight(TreeNode root) {
        if (root == null) return 0;
        int leftHeight = getHeight(root.left);
        int rightHeight = getHeight(root.right);
        if (leftHeight == rightHeight) {
            // Left subtree is perfect
            return (1 << leftHeight) + countNodesViaHeight(root.right);
        } else {
            // Right subtree is perfect
            return (1 << rightHeight) + countNodesViaHeight(root.left);
        }
    }
    private static int getHeight(TreeNode node) {
        int h = 0;
        while (node != null) {
            h++;
            node = node.left;
        }
        return h;
    }
    // Level Order Traversal
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // sum of left leaf nodes via DFS using recursion
    public int sumOfLeftLeaves(TreeNode root) {
        if (root == null) return 0;
        return dfs(root, false);
    }
    private int dfs(TreeNode node, boolean isLeft) {
        if (node == null) return 0;
        // Leaf node
        if (node.left == null && node.right == null) 
            return isLeft ? node.val : 0;
        return dfs(node.left, true) + dfs(node.right, false);
    }
    // sum of left leaf nodes via BFS using iterative and Queue
    public static int sumOfLeftLeavesViaBFS(TreeNode root) {
        if (root == null) return 0;
        int sum = 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            System.out.println(node.val+" "+sum);
            if (node.left != null) {
                // Check if left child is a leaf
                if (node.left.left == null && node.left.right == null) 
                    sum += node.left.val;
                else q.offer(node.left);
            }
            if (node.right != null) q.offer(node.right);
        }
        return sum;
    }
    // count nonleaf nodes
    public int countNonLeafNodes(TreeNode root) {
        if (root == null) return 0;
        // Leaf node check: no children
        if (root.left == null && root.right == null) return 0;
        // Current node is non-leaf → count it + recurse
        return 1 + countNonLeafNodes(root.left) + countNonLeafNodes(root.right);
    }
    // find diameter
    static int diameter;
    public static int diameterOfBinaryTree(TreeNode root) {
        diameter = 0;
        depth(root);
        return diameter;
    }
    // Recursive helper to compute depth
    private static int depth(TreeNode node) {
        if (node == null) return 0;
        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);
        System.out.println(node.val+" "+leftDepth+" "+rightDepth);
        // Update diameter: longest path through this node
        diameter = Math.max(diameter, leftDepth + rightDepth);
        System.out.println(diameter);
        // Return height of subtree
        return 1 + Math.max(leftDepth, rightDepth);
    }
    // max path sum
    private static int maxSum;
    public static int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxSum;
    }
    // Recursive helper function
    private static int maxGain(TreeNode node) {
        if (node == null) return 0;
        // Compute max gain from left and right subtrees
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        System.out.println(node.val+" "+leftGain+" "+rightGain);
        // Price of starting a new path at this node
        int priceNewPath = node.val + leftGain + rightGain;
        // Update global maxSum if new path is better
        maxSum = Math.max(maxSum, priceNewPath);
        // Return max gain to parent (choose one side)
        return node.val + Math.max(leftGain, rightGain);
    }
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(1);
        root.right = new TreeNode(7);
        root.right.left=new TreeNode(10);
        root.right.right=new TreeNode(8);
        System.out.println(maxPathSum(root));
    }
}
