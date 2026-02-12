package day8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import day7.TreeNode;

public class Solutions {
    // count good pair of leaf node
    /*
    Perfect, S — let’s dry‑run your **`countPairs` solution** step by step on the sample tree:

```
Tree: 1,2,3,4,5,6,7
Structure:
            1
          /   \
         2     3
        / \   / \
       4   5 6   7
```
Suppose `distance = 3`.
---

## 🔄 Dry‑Run Flow

### Step 1: Leaves
- Node 4 → returns `[1,0,0,0]` (leaf at distance 0).  
- Node 5 → returns `[1,0,0,0]`.  
- Node 6 → returns `[1,0,0,0]`.  
- Node 7 → returns `[1,0,0,0]`.

---

### Step 2: Node 2
- Left = [1,0,0,0] (from 4), Right = [1,0,0,0] (from 5).  
- Count pairs: i=0, j=0 → i+j+2 = 2 ≤ 3 → ans += 1×1 = 1.  
- Build curr: curr[1] = left[0]+right[0] = 2 → `[0,2,0,0]`.  
- Return `[0,2,0,0]`.

---

### Step 3: Node 3
- Left = [1,0,0,0] (from 6), Right = [1,0,0,0] (from 7).  
- Count pairs: i=0, j=0 → i+j+2 = 2 ≤ 3 → ans += 1×1 = 1.  
- Build curr: curr[1] = 2 → `[0,2,0,0]`.  
- Return `[0,2,0,0]`.

---

### Step 4: Node 1 (root)
- Left = [0,2,0,0] (from 2), Right = [0,2,0,0] (from 3).  
- Count pairs:  
  - i=1, j=1 → i+j+2 = 4 > 3 → not valid.  
  - No valid pairs.  
- Build curr: curr[2] = left[1]+right[1] = 4 → `[0,0,4,0]`.  
- Return `[0,0,4,0]`.

---

## ✅ Final Answer
`ans[0] = 2`

Good leaf pairs within distance 3 are:
- (4,5) under node 2  
- (6,7) under node 3  

---

## 🎯 Key Insight
- Each leaf returns `[1,0,0,…]`.  
- Internal nodes combine child arrays, shifting distances by +1.  
- Pairs are counted when `i+j+2 ≤ distance`.  
- The recursion bubbles up counts and builds distance arrays layer by layer.
    */
    public int countPairs(TreeNode root, int distance) {
        int[] ans = new int[1];
        dfs(root, distance, ans);
        return ans[0];
    }
    private int[] dfs(TreeNode node, int distance, int[] ans) {
        if (node == null) return new int[distance + 1];
        // Leaf node
        if (node.left == null && node.right == null) {
            int[] d = new int[distance + 1];
            d[0] = 1; // distance 0 leaf
            return d;
        }
        int[] left = dfs(node.left, distance, ans);
        int[] right = dfs(node.right, distance, ans);
        // Count good pairs between left and right leaves
        for (int i = 0; i < distance; i++) {
            if (left[i] == 0) continue;
            for (int j = 0; j < distance; j++) {
                if (right[j] == 0) continue;
                if (i + j + 2 <= distance) ans[0] += left[i] * right[j];
            }
        }
        // Build current distance array
        int[] curr = new int[distance + 1];
        for (int i = 0; i < distance; i++) 
            curr[i + 1] = left[i] + right[i];
        return curr;
    }
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }
    /*
📊 Complexity
- Time Complexity: O(n) — each node visited once.
- Space Complexity: Recursive DFS: O(h) (stack depth, h = tree height).


```
Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
```

Tree structure:

```
          5
        /   \
       4     8
      /     / \
    11    13   4
   /  \         \
  7    2         1
```

---

## 🔄 Dry‑Run Flow (Recursive DFS)

We’ll track `(node, targetSum)` at each call.

### Step 1
- Call `hasPathSum(5, 22)`
- Not leaf → recurse left and right with `targetSum - 5 = 17`.

---

### Step 2 (Left Subtree)
- Call `hasPathSum(4, 17)`
- Not leaf → recurse left with `targetSum - 4 = 13`.

---

### Step 3
- Call `hasPathSum(11, 13)`
- Not leaf → recurse left with `targetSum - 11 = 2`.

---

### Step 4
- Call `hasPathSum(7, 2)`
- Leaf node → check `2 == 7` → false.

---

### Step 5
- Call `hasPathSum(2, 2)`
- Leaf node → check `2 == 2` → true ✅

So the left path `5 → 4 → 11 → 2` sums to 22.

---

### Step 6 (Right Subtree of Root)
Even though we already found a valid path, recursion continues conceptually:
- `hasPathSum(8, 17)` → explores `13` and `4 → 1`, but none equal 17.  
- Returns false.

---

## ✅ Final Result
The recursion returns **true** because the path `5 → 4 → 11 → 2` has sum = 22.
    */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;

        // Leaf node check
        if (root.left == null && root.right == null) {
            return targetSum == root.val;
        }

        // Recurse down with reduced target
        return hasPathSum(root.left, targetSum - root.val) ||
               hasPathSum(root.right, targetSum - root.val);
    }
    /*
    Input: [3,9,20,null,null,15,7]

Tree:
        3
      /   \
     9     20
          /  \
         15   7
🔄 Dry‑Run Flow
Level 1
- Queue: [3]
- Direction: left→right
- Process: addLast(3) → level = [3]
- Enqueue children: [9,20]
- Result = [[3]]

Level 2
- Queue: [9,20]
- Direction: right→left
- Process:
- Node 9 → addFirst(9) → level = [9]
- Node 20 → addFirst(20) → level = [20,9]
- Enqueue children: [15,7]
- Result = [[3],[20,9]]

Level 3
- Queue: [15,7]
- Direction: left→right
- Process:
- Node 15 → addLast(15) → level = [15]
- Node 7 → addLast(7) → level = [15,7]
- Enqueue children: none
- Result = [[3],[20,9],[15,7]]

✅ Final Zigzag Traversal
[[3], [20, 9], [15, 7]]
    */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean leftToRight = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            Deque<Integer> level = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (leftToRight) level.addLast(node.val);
                else level.addFirst(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(new ArrayList<>(level));
            leftToRight = !leftToRight; // flip direction
        }
        return result;
    }
    /*
    📊 Complexity
    - Time Complexity: O(n) — each node visited once.
    - Space Complexity: DFS: O(h) recursion stack (h = tree height).
Input: [1,2,3,null,5,null,4]

Tree:
        1
      /   \
     2     3
      \      \
       5      4
🔄 Dry‑Run Flow
We’ll track (node, depth, result) at each call.
Step 1
- Call dfs(1, depth=0, result=[])
- depth == result.size() → add 1
- result = [1]
- Recurse right → node 3

Step 2
- Call dfs(3, depth=1, result=[1])
- depth == result.size() → add 3
- result = [1,3]
- Recurse right → node 4
Step 3
- Call dfs(4, depth=2, result=[1,3])
- depth == result.size() → add 4
- result = [1,3,4]
- Recurse right → null → return
- Recurse left → null → return

Step 4 (backtrack to node 3)
- Left child = null → return

Step 5 (backtrack to node 1)
- Recurse left → node 2
Step 6
- Call dfs(2, depth=1, result=[1,3,4])
- depth != result.size() → skip (already filled with 3)
- Recurse right → node 5

Step 7
- Call dfs(5, depth=2, result=[1,3,4])
- depth != result.size() → skip (already filled with 4)
- Recurse right → null → return
- Recurse left → null → return
Final: 
[1, 3, 4]
    */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }

    private void dfs(TreeNode node, int depth, List<Integer> result) {
        if (node == null) return;
        // First time visiting this depth → rightmost node
        if (depth == result.size()) result.add(node.val);
        // Visit right first, then left
        dfs(node.right, depth + 1, result);
        dfs(node.left, depth + 1, result);
    }
    // left view
    public ArrayList<Integer> leftView(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }
    private void dfs(TreeNode node, int depth, ArrayList<Integer> result) {
        if (node == null) return;

        // First time visiting this depth → leftmost node
        if (depth == result.size()) {
            result.add(node.val);
        }

        // Visit left first, then right
        dfs(node.left, depth + 1, result);
        dfs(node.right, depth + 1, result);
    }
    // Boundary traversal
    /*
    📊 Complexity
- Time Complexity: O(n) — each node visited once.
- Space Complexity: O(h) recursion stack (h = tree height).
Input: [1,2,3,4,5,null,6,null,null,7,8]

Tree:
          1
        /   \
       2     3
      / \      \
     4   5      6
        / \
       7   8

🔄 Stepwise Boundary Traversal (Anti‑Clockwise)
Step 1: Root
- Root = 1 (not a leaf) → add to result
- Result = [1]

Step 2: Left Boundary (excluding leaves)
- Start at root.left = 2
- Node 2 → not leaf → add → Result = [1,2]
- Move left → 4 (leaf, so stop adding here).

Step 3: Leaves (left to right)
Traverse entire tree to collect leaves:
- Node 4 → leaf → add → Result = [1,2,4]
- Node 7 → leaf → add → Result = [1,2,4,7]
- Node 8 → leaf → add → Result = [1,2,4,7,8]
- Node 6 → leaf → add → Result = [1,2,4,7,8,6]

Step 4: Right Boundary (excluding leaves, bottom‑up)
- Start at root.right = 3
- Node 3 → not leaf → add to temp = [3]
- Move right → 6 (leaf, stop).
- Reverse temp → [3] → add to result.
Result = [1,2,4,7,8,6,3]

✅ Final Boundary Traversal
[1, 2, 4, 7, 8, 6, 3]
🎯 Key Insight
- Root is added if not a leaf.
- Left boundary: top‑down, excluding leaves.
- Leaves: all leaf nodes left‑to‑right.
- Right boundary: bottom‑up, excluding leaves.
- Ensures each boundary node appears exactly once.
    */
    public ArrayList<Integer> boundaryTraversal(TreeNode root) {
        ArrayList<Integer> result = new ArrayList<>();
        if (root == null) return result;
        // Root is part of boundary if it's not a leaf
        if (!isLeaf(root)) result.add(root.val);
        // Add left boundary (excluding leaves)
        addLeftBoundary(root, result);
        // Add all leaf nodes
        addLeaves(root, result);
        // Add right boundary (excluding leaves, bottom-up)
        addRightBoundary(root, result);
        return result;
    }

    private boolean isLeaf(TreeNode node) {
        return (node.left == null && node.right == null);
    }
    private void addLeftBoundary(TreeNode root, ArrayList<Integer> result) {
        TreeNode curr = root.left;
        while (curr != null) {
            if (!isLeaf(curr)) result.add(curr.val);
            if (curr.left != null) curr = curr.left;
            else curr = curr.right;
        }
    }
    private void addRightBoundary(TreeNode root, ArrayList<Integer> result) {
        TreeNode curr = root.right;
        ArrayList<Integer> temp = new ArrayList<>();
        while (curr != null) {
            if (!isLeaf(curr)) temp.add(curr.val);
            if (curr.right != null) curr = curr.right;
            else curr = curr.left;
        }
        // Add in reverse order
        for (int i = temp.size() - 1; i >= 0; i--) {
            result.add(temp.get(i));
        }
    }
    private void addLeaves(TreeNode root, ArrayList<Integer> result) {
        if (isLeaf(root)) {
            result.add(root.val);
            return;
        }
        if (root.left != null) addLeaves(root.left, result);
        if (root.right != null) addLeaves(root.right, result);
    }
    // min height
    /*
    Alright, S — let’s dry‑run the **optimized Minimum Height Trees solution** step by step on the example:

```
n = 6
edges = [[0,1],[0,2],[0,3],[3,4],[4,5]]
```

---

## 🌳 Step 1: Build Graph + Degree Array
Adjacency list:
```
0 → [1,2,3]
1 → [0]
2 → [0]
3 → [0,4]
4 → [3,5]
5 → [4]
```

Degrees:
```
deg[0]=3, deg[1]=1, deg[2]=1, deg[3]=2, deg[4]=2, deg[5]=1
```

---

## 🔄 Step 2: Initialize Leaves
Nodes with degree = 1 → `[1,2,5]`

---

## 🔄 Step 3: Trim Round 1
Remaining = 6 − 3 = 3  
Process leaves `[1,2,5]`:

- Leaf 1 → neighbor 0 → deg[0]=2  
- Leaf 2 → neighbor 0 → deg[0]=1 → new leaf = 0  
- Leaf 5 → neighbor 4 → deg[4]=1 → new leaf = 4  

New leaves = `[0,4]`

---

## 🔄 Step 4: Trim Round 2
Remaining = 3 − 2 = 1  
Process leaves `[0,4]`:

- Leaf 0 → neighbor 3 → deg[3]=1 → new leaf = 3  
- Leaf 4 → neighbor 3 → deg[3]=0 (already leaf)  

New leaves = `[3]`

---

## 🔄 Step 5: Stop
Remaining = 1 ≤ 2 → stop.  
Final centroids = `[3]`

---

## ✅ Final Answer
```
[3]
```

---

## 🎯 Key Insight
- The algorithm trims outer leaves layer by layer.  
- The last remaining node(s) are the **centroids** — the roots that minimize tree height.  
- Here, node `3` is the centroid, so the minimum height tree is rooted at `3`.
    */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) return Collections.singletonList(0);

        List<List<Integer>> graph = new ArrayList<>();
        int[] degree = new int[n];
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) leaves.offer(i);
        }

        int remaining = n;
        while (remaining > 2) {
            int size = leaves.size();
            remaining -= size;
            for (int i = 0; i < size; i++) {
                int leaf = leaves.poll();
                for (int neighbor : graph.get(leaf)) {
                    degree[neighbor]--;
                    if (degree[neighbor] == 1) leaves.offer(neighbor);
                }
            }
        }

        return new ArrayList<>(leaves);
    }
}
