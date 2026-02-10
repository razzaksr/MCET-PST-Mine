package day9;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import day7.TreeNode;

public class Solutions {
    // Sorted Array to BST
    public static TreeNode sortedArrayToBST(int[] arr) {
        return buildBST(arr, 0, arr.length - 1);
    }
    private static TreeNode buildBST(int[] arr, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode root = new TreeNode(arr[mid]);
        root.left = buildBST(arr, start, mid - 1);
        root.right = buildBST(arr, mid + 1, end);
        return root;
    }
    
    
    // Validate BST
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        // Node must be strictly within (min, max)
        if (node.val <= min || node.val >= max) return false;
        // Left subtree must be < node.val
        // Right subtree must be > node.val
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }
    // LCA of BST
    // LCA
    /*
    📊 Complexity
    - Time Complexity: O(n) — each node visited once.
    - Space Complexity: O(h) — recursion stack (h = tree height).
Great, S — let’s dry‑run the **recursive DFS solution for Lowest Common Ancestor (LCA)** on the sample tree:

```
Input: [3,5,1,6,2,0,8,null,null,7,4]
p = 5, q = 1

Tree:
            3
          /   \
         5     1
        / \   / \
       6   2 0   8
          / \
         7   4
```
## 🔄 Dry‑Run Flow

### Step 1
- Call `LCA(3, p=5, q=1)`
- Root = 3 → not null, not p, not q
- Recurse left → node 5
- Recurse right → node 1

---

### Step 2 (Left Subtree)
- Call `LCA(5, p=5, q=1)`
- Root = 5 → equals p → return 5

---

### Step 3 (Right Subtree)
- Call `LCA(1, p=5, q=1)`
- Root = 1 → equals q → return 1

---

### Step 4 (Back at Root 3)
- Left result = 5  
- Right result = 1  
- Both non‑null → return root (3)

---

## ✅ Final Answer
```
Lowest Common Ancestor = 3
```

---

## 🎯 Key Insight
- The recursion stops immediately when it finds `p` or `q`.  
- At node `3`, both left and right subtrees return non‑null, so `3` is the LCA.  
- This works for any binary tree, not just BSTs.
    */
    public TreeNode lowestCommonAncestorOfBST(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (p.val < root.val && q.val < root.val) root = root.left;
            else if (p.val > root.val && q.val > root.val) root = root.right;
            else return root; // split point
        }
        return null;
    }
    // Kth Smallest element in BST
    private int count = 0;
    private int result = -1;
    public int kthSmallest(TreeNode root, int k) {
        inorder(root, k);
        return result;
    }
    private void inorder(TreeNode node, int k) {
        if (node == null) return;
        inorder(node.left, k);
        count++;
        if (count == k) {
            result = node.val;
            return; // stop once found
        }
        inorder(node.right, k);
    }
    // Serialize and Deserialize BST
    /* 📊 Complexity
    - Time Complexity: O(n) — each node visited once during serialization and deserialization.
    - Space Complexity: O(n) — string storage + recursion stack.
    */
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString().trim();
    }
    private void preorder(TreeNode node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.val).append(" ");
        preorder(node.left, sb);
        preorder(node.right, sb);
    }
    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if (data.isEmpty()) return null;
        String[] vals = data.split(" ");
        Queue<Integer> queue = new LinkedList<>();
        for (String v : vals) queue.offer(Integer.parseInt(v));
        return build(queue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    private TreeNode build(Queue<Integer> queue, int lower, int upper) {
        if (queue.isEmpty()) return null;
        int val = queue.peek();
        if (val < lower || val > upper) return null;

        queue.poll();
        TreeNode node = new TreeNode(val);
        node.left = build(queue, lower, val);
        node.right = build(queue, val, upper);
        return node;
    }
    // delete node in BST
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            // Found the node to delete
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            // Node has two children → replace with inorder successor
            TreeNode successor = findMin(root.right);
            root.val = successor.val;
            root.right = deleteNode(root.right, successor.val);
        }
        return root;
    }
    private TreeNode findMin(TreeNode node) {
        while (node.left != null)
            node = node.left;
        return node;
    }
    // AVL / Balance BST from Unbalanced
    /*
    📊 Complexity
- Time Complexity: O(n) — inorder traversal + building balanced tree.
- Space Complexity: O(n) — list storage + recursion stack.

🎯 Key Insight
- Inorder traversal of BST gives sorted order.
- Rebuilding with middle elements ensures balance.
- This guarantees height ≈ log(n), making operations efficient.
Here’s the **step‑by‑step dry‑run with a diagram** for balancing the skewed BST `[1,null,2,null,3,null,4,null,5]`:

---

## 🌳 Step 1: Original Skewed Tree
```
1
 \
  2
   \
    3
     \
      4
       \
        5
```
- This is a right‑skewed BST (like a linked list).  
- Height = 5 → very inefficient.

---

## 🔄 Step 2: Inorder Traversal
- Inorder of BST = sorted order.  
- Traversal result = `[1,2,3,4,5]`.

---

## 🔄 Step 3: Build Balanced BST
- Pick middle element as root → `3`.  
- Left half `[1,2]` → subtree with root `2`.  
- Right half `[4,5]` → subtree with root `4`.  

Balanced BST:
```
        3
      /   \
     2     4
    /       \
   1         5
```
- Height = 3 → balanced and efficient.

---

## ✅ Diagram  
Here’s the visual transformation you asked for — showing the skewed tree, inorder array, and the balanced BST:

`https://copilot.microsoft.com/th/id/BCO.b979a41d-e0fc-4c1b-b1ea-75fb8302af0e.png`

---

## 🎯 Key Insight
- Inorder traversal extracts sorted values.  
- Rebuilding with middle elements ensures balance.  
- This reduces height from 5 → 3, making operations faster (O(log n) instead of O(n)).
    */
    public TreeNode balanceBST(TreeNode root) {
        List<Integer> inorderList = new ArrayList<>();
        inorder(root, inorderList);
        return buildBalanced(inorderList, 0, inorderList.size() - 1);
    }
    // Step 1: Inorder traversal to get sorted values
    private void inorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }
    // Step 2: Build balanced BST from sorted list
    private TreeNode buildBalanced(List<Integer> list, int left, int right) {
        if (left > right) return null;
        int mid = left + (right - left) / 2;
        TreeNode root = new TreeNode(list.get(mid));
        root.left = buildBalanced(list, left, mid - 1);
        root.right = buildBalanced(list, mid + 1, right);
        return root;
    }

}
