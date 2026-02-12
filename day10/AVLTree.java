package day10;

import java.util.LinkedList;
import java.util.Queue;

class AVLNode {
    int key, height;
    AVLNode left, right;
    AVLNode(int key) {
        this.key = key;
        this.height = 1;
    }
}

public class AVLTree {
    // ---------------------- UTILITY METHODS --------------------------
    int height(AVLNode node) {
        if (node == null) return 0;
        return node.height;
    }
    int getBalance(AVLNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }
    // ---------------------- ROTATIONS --------------------------
    // AVL / Balance BST from Unbalanced
    /*
    Perfect — let’s walk through **AVL tree rotations with numeric test cases** so you can see exactly how each imbalance is detected and corrected.

---

## 1. **Right Rotation (LL Rotation)**  
**Trigger:** Insert into left child’s left subtree.

### Example
Insert sequence: `[30, 20, 10]`

- Insert 30 → root = 30  
- Insert 20 → goes left of 30  
- Insert 10 → goes left of 20 → imbalance at 30 (balance factor = +2)  

**Rotation:** Right rotation at 30.  

```
    30               20
   /                /  \
  20      →        10   30
 /
10
```

---

## 2. **Left Rotation (RR Rotation)**  
**Trigger:** Insert into right child’s right subtree.

### Example
Insert sequence: `[10, 20, 30]`

- Insert 10 → root = 10  
- Insert 20 → goes right of 10  
- Insert 30 → goes right of 20 → imbalance at 10 (balance factor = -2)  

**Rotation:** Left rotation at 10.  

```
  10                   20
    \                 /  \
     20     →        10   30
       \
        30
```

---

## 3. **Left‑Right Rotation (LR Rotation)**  
**Trigger:** Insert into left child’s right subtree.

### Example
Insert sequence: `[30, 10, 20]`

- Insert 30 → root = 30  
- Insert 10 → goes left of 30  
- Insert 20 → goes right of 10 → imbalance at 30 (balance factor = +2, but heavy on left‑right side)  

**Rotation:**  
1. Left rotation at 10.  
2. Right rotation at 30.  

```
    30                 30                 20
   /                  /                  /  \
  10       →         20       →         10   30
    \               /
     20            10
```

---

## 4. **Right‑Left Rotation (RL Rotation)**  
**Trigger:** Insert into right child’s left subtree.

### Example
Insert sequence: `[10, 30, 20]`

- Insert 10 → root = 10  
- Insert 30 → goes right of 10  
- Insert 20 → goes left of 30 → imbalance at 10 (balance factor = -2, but heavy on right‑left side)  

**Rotation:**  
1. Right rotation at 30.  
2. Left rotation at 10.  

```
  10                   10                   20
    \                    \                 /  \
     30       →           20     →        10   30
    /                       \
   20                        30
```

---

## 🎯 Key Takeaways
- **LL → Right rotation**  
- **RR → Left rotation**  
- **LR → Left rotation on child, then right rotation on root**  
- **RL → Right rotation on child, then left rotation on root**  
    */
    // Right rotate (LL Rotation)
    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        // Rotation
        x.right = y;
        y.left = T2;
        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x; // new root
    }
    // Left rotate (RR Rotation)
    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        // Rotation
        y.left = x;
        x.right = T2;
        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y; // new root
    }

    // ---------------------- INSERTION --------------------------
    AVLNode insert(AVLNode node, int key) {
        // 1. Normal BST insertion
        if (node == null) return new AVLNode(key);
        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node; // duplicates not allowed
        // 2. Update height
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        // 3. Get balance factor
        int balance = getBalance(node);
        // 4. Apply rotations if unbalanced
        // LL Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);
        // RR Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);
        // LR Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    // ---------------------- DELETE --------------------------
    AVLNode delete(AVLNode root, int key) {
        // 1. Perform normal BST deletion
        if (root == null) return root;
        if (key < root.key)
            root.left = delete(root.left, key);
        else if (key > root.key)
            root.right = delete(root.right, key);
        else {
            // Node with 1 or 0 children
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = (root.left != null) ? root.left : root.right;
                if (temp == null) { // no child
                    temp = root;
                    root = null;
                } else 
                    root = temp; // one child
            }
            else {
                // Node with 2 children (inorder successor)
                AVLNode temp = minValueNode(root.right);
                root.key = temp.key;
                root.right = delete(root.right, temp.key);
            }
        }
        // if tree had 1 node
        if (root == null) return root;
        // 2. Update height
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        // 3. Balance factor
        int balance = getBalance(root);
        // 4. Balance Cases
        // LL Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        // LR Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        // RR Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        // RL Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }
    AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }
    // ---------------------- TRAVERSALS --------------------------
    void levelOrder(AVLNode root) {
        if (root == null) return;
        Queue<AVLNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            AVLNode node = q.poll();
            System.out.print(node.key + " ");
            if (node.left != null) q.add(node.left);
            if (node.right != null) q.add(node.right);
        }
    }

    // ---------------------- MAIN DRIVER --------------------------
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        AVLNode root = null;
        int[] values = {30, 20, 40, 10, 25, 35, 50};
        for (int v : values) {
            root = tree.insert(root, v);
        }
        System.out.print("\nLevel Order: ");
        tree.levelOrder(root);
        // // Delete an element
        // root = tree.delete(root, 40);
        // System.out.print("\nAfter deletion (Level Order): ");
        // tree.levelOrder(root);
    }
}