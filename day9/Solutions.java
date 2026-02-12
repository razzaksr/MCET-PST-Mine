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
        return validate(node.left, min, node.val) 
        && validate(node.right, node.val, max);
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
    // Serialize and Deserialize BST
    /* 
    🔍 How It Works
- Serialization: Preorder traversal stores node values in a string.
- Deserialization: Use BST property with bounds (lower, upper) to rebuild the tree efficiently.
- Each node is placed in correct position without needing null markers.
🎯 Key Insight
- Unlike general binary trees, BSTs don’t need explicit null markers in serialization.
- Bounds (lower, upper) ensure correct placement during reconstruction.
- This makes the solution compact and efficient.

    📊 Complexity
    - Time Complexity: O(n) — each node visited once during serialization and deserialization.
    - Space Complexity: O(n) — string storage + recursion stack.
    Great choice — let’s dry‑run your **serialize/deserialize BST solution** step by step on the sample tree `[2,1,3]`.  

---

## 🌳 Input Tree
```
    2
   / \
  1   3
```

---

## 🔄 Serialization (Preorder Traversal)

### Code Path
```java
public String serialize(TreeNode root) {
    StringBuilder sb = new StringBuilder();
    preorder(root, sb);
    return sb.toString().trim();
}
```

### Step‑by‑Step
1. Start at root `2` → append `"2 "`  
2. Go left → node `1` → append `"1 "`  
3. Left of `1` is null → return  
4. Right of `1` is null → return  
5. Back to root → go right → node `3` → append `"3 "`  
6. Left of `3` is null → return  
7. Right of `3` is null → return  

**Final serialized string:**  
```
"2 1 3"
```

---

## 🔄 Deserialization (Rebuild Tree with Bounds)

### Code Path
```java
public TreeNode deserialize(String data) {
    if (data.isEmpty()) return null;
    String[] vals = data.split(" ");
    Queue<Integer> queue = new LinkedList<>();
    for (String v : vals) queue.offer(Integer.parseInt(v));
    return build(queue, Integer.MIN_VALUE, Integer.MAX_VALUE);
}
```

### Step‑by‑Step
Queue initially: `[2,1,3]`

#### Build Root
- Peek = 2, within bounds `(-∞, +∞)` → poll → create node(2)  
- Recurse left with bounds `(-∞, 2)`  
- Recurse right with bounds `(2, +∞)`

#### Build Left Subtree
- Peek = 1, within bounds `(-∞, 2)` → poll → create node(1)  
- Left bounds `(-∞, 1)` → next peek = 3, but 3 > 1 → return null  
- Right bounds `(1, 2)` → next peek = 3, but 3 ≥ 2 → return null  
- Left child = 1 with no children

#### Build Right Subtree
- Peek = 3, within bounds `(2, +∞)` → poll → create node(3)  
- Left bounds `(2, 3)` → queue empty → return null  
- Right bounds `(3, +∞)` → queue empty → return null  
- Right child = 3 with no children

---

## ✅ Final Tree Reconstructed
```
    2
   / \
  1   3
```

## 🎯 Key Insight
- **Serialization**: simple preorder traversal builds `"2 1 3"`.  
- **Deserialization**: bounds (`lower`, `upper`) ensure correct BST placement.  
  - `1` fits only in left subtree of `2`.  
  - `3` fits only in right subtree of `2`.  
- This guarantees the BST is reconstructed exactly.
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
    /*
    🔍 How It Works
- Traverse the tree until the node with value key is found.
- Cases:
- If the node has no child → return null.
- If the node has one child → return that child.
- If the node has two children → replace its value with the inorder successor (smallest node in right subtree), then delete that successor recursively.

📊 Complexity
- Time Complexity: O(h), where h = tree height (worst case O(n), best case O(log n) for balanced BST).
- Space Complexity: O(h) recursion stack.

🎯 Key Insight
- Using the inorder successor ensures BST properties remain valid after deletion.
- This recursive approach is concise and widely accepted in interviews.
Let’s dry‑run your **BST deleteNode solution** step by step on the sample tree:

Input BST (level order):  
`[5,3,6,2,4,null,7]`  
Key to delete: `3`

---

## 🌳 Initial Tree Structure
```
        5
       / \
      3   6
     / \    \
    2   4    7
```

---

## 🔄 Step‑by‑Step Execution

### Call: `deleteNode(root=5, key=3)`
- Root = 5  
- Key (3) < 5 → recurse left: `root.left = deleteNode(3, 3)`

---

### Call: `deleteNode(root=3, key=3)`
- Root = 3  
- Key == root.val → found node to delete.  
- Node has **two children** (left=2, right=4).  
- Find inorder successor in right subtree.

---

### Find Successor: `findMin(root.right=4)`
- Start at node 4.  
- Left child is null → successor = 4.

---

### Replace Node Value
- Replace node(3).val with successor.val = 4.  
- Tree now looks like:
```
        5
       / \
      4   6
     /     \
    2       7
```

- Now delete successor (value 4) from right subtree:  
  `root.right = deleteNode(root.right, 4)`

---

### Call: `deleteNode(root=4, key=4)`
- Root = 4  
- Key == root.val → found node to delete.  
- Node has **no children** → return `null`.  
- So node(4) is removed.

---

## ✅ Final Tree After Deletion
```
        5
       / \
      4   6
     /     \
    2       7
```

---

## 🎯 Key Insight
- **Step 1:** Traverse left until node with value 3 is found.  
- **Step 2:** Node 3 has two children → replace with inorder successor (smallest in right subtree).  
- **Step 3:** Successor = 4 → copy value into node.  
- **Step 4:** Delete original successor node (4).  
- **Result:** BST remains valid, with node 3 replaced by 4.
    */
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;
        if (key < root.val) root.left = deleteNode(root.left, key);
        else if (key > root.val) root.right = deleteNode(root.right, key);
        else {
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
        while (node.left != null) node = node.left;
        return node;
    }
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
