package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Solutions {
    // Graph Intro, Representation
    public static class AdjacencyMatrix {
        private int[][] adjacencyWeighted;
        public AdjacencyMatrix(int verSize){
            adjacencyWeighted = new int[verSize][verSize];
        }
        public void buildEdge(int ver1,int ver2, int weight){
            adjacencyWeighted[ver1][ver2] = weight;
            adjacencyWeighted[ver2][ver1] = weight;
        }
        public void viewMatrix(){
            for(int[] row : adjacencyWeighted){
                for(int col:row) System.out.print(col+" ");
                System.out.println();
            }
        }
        public static void main(String[] args) {
            AdjacencyMatrix adjacent = new AdjacencyMatrix(5);
            adjacent.buildEdge(0,1,100);
            adjacent.buildEdge(0, 3, 250);
            adjacent.buildEdge(1, 3, 75);
            adjacent.buildEdge(2, 4, 60);
            adjacent.buildEdge(1, 4, 100);
            adjacent.buildEdge(4, 3, 10);
            adjacent.viewMatrix();
        }
    }
    // Direct Graph unweighted
    public static class AdjacencyList {
        private List<List<Integer>> adjUnweighted;
        public AdjacencyList(int size){
            adjUnweighted = new ArrayList<>();
            for(int index = 0; index<size; index++)
                adjUnweighted.add(new ArrayList<>());
        }
        public void buildEdges(int row, int col){
            adjUnweighted.get(row).add(col);
        }
        public void viewList(){
            for(List<Integer> row:adjUnweighted){
                for(Integer temp:row)
                    System.out.print(temp+" ");
                System.out.println();
            }
        }
        public static void main(String[] args) {
            AdjacencyList adj = new AdjacencyList(5);
            adj.buildEdges(0,1);
            adj.buildEdges(0, 3);
            adj.buildEdges(1, 3);
            adj.buildEdges(2, 4);
            adj.buildEdges(1, 4);
            adj.buildEdges(4, 3);
            adj.viewList();
        }
    }
    public static class AdjacencyMap{
        public Map<Integer, List<Integer>> adjancency;
        int size;
        public AdjacencyMap(int vertSize){
            size = vertSize;
            adjancency = new Hashtable<>();
        }
        // undirected since vertex1, vertex2 will be added as adjacencies vice versa
        public void buildEdges(int vertex1, int vertex2){
            adjancency.putIfAbsent(vertex1, new ArrayList<>());
            adjancency.putIfAbsent(vertex2, new ArrayList<>());
            adjancency.get(vertex1).add(vertex2);
            adjancency.get(vertex2).add(vertex1);
        }
        public void viewMap(){
            for(Integer row:adjancency.keySet()){
                System.out.print(row+" -> ");
                for(Integer temp:adjancency.get(row))
                    System.out.print(temp+" ");
                System.out.println();
            }
        }
        public static void main(String[] args) {
            AdjacencyMap maps = new AdjacencyMap(5);
            maps.buildEdges(0, 1);
            maps.buildEdges(1, 2);
            maps.buildEdges(2, 3);
            maps.buildEdges(3, 4);
            maps.viewMap();
        }
    }
    // BFS 
    /*
    🔍 How It Works
- Start BFS from vertex 0 (as per GFG convention).
- Use a queue to process nodes level by level.
- Maintain a visited[] array to avoid revisiting nodes.
- For each node dequeued:
- Add it to result.
- Enqueue all unvisited neighbors.

📊 Complexity
- Time Complexity: O(V + E) — each vertex and edge is processed once.
- Space Complexity: O(V) — for visited array and queue.

🎯 Example Dry‑Run
Graph:
V = 5
Edges: 0-1, 0-2, 1-3, 2-4


Adjacency list:
0 → [1,2]
1 → [0,3]
2 → [0,4]
3 → [1]
4 → [2]

Steps:
- Start at 0 → result = [0]
- Neighbors: 1,2 → enqueue → queue=[1,2]
- Process 1 → result=[0,1] → enqueue 3 → queue=[2,3]
- Process 2 → result=[0,1,2] → enqueue 4 → queue=[3,4]
- Process 3 → result=[0,1,2,3]
- Process 4 → result=[0,1,2,3,4]
Final BFS traversal: [0,1,2,3,4]
    */
    public ArrayList<Integer> bfs(ArrayList<ArrayList<Integer>> adj) {
        ArrayList<Integer> result = new ArrayList<>();
        int V = adj.size();
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0); // Start BFS from node 0
        visited[0] = true;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }
    // DFS
    /*
    🔍 How It Works
- adj is the adjacency list representation of the graph.
- Start DFS from vertex 0.
- Use a recursive helper dfsUtil:
- Mark node as visited.
- Add node to result.
- Recursively visit all unvisited neighbors.

📊 Complexity
- Time Complexity: O(V + E) — each vertex and edge is processed once.
- Space Complexity: O(V) — visited array + recursion stack.
🎯 Example Dry‑Run
Graph:
V = 5
Edges: 0-1, 0-2, 1-3, 2-4


Adjacency list:
0 → [1,2]
1 → [0,3]
2 → [0,4]
3 → [1]
4 → [2]


Steps:
- Start at 0 → result = [0]
- Visit 1 → result = [0,1]
- Visit 3 → result = [0,1,3]
- Backtrack → visit 2 → result = [0,1,3,2]
- Visit 4 → result = [0,1,3,2,4]
Final DFS traversal: [0,1,3,2,4]
    */
    public ArrayList<Integer> dfs(ArrayList<ArrayList<Integer>> adj) {
        int V = adj.size(); // number of vertices
        ArrayList<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[V];
        // Start DFS from vertex 0 (as per GFG convention)
        dfsUtil(0, adj, visited, result);
        return result;
    }
    private void dfsUtil(int node, ArrayList<ArrayList<Integer>> adj,
                         boolean[] visited, ArrayList<Integer> result) {
        visited[node] = true;
        result.add(node);
        for (int neighbor : adj.get(node))
            if (!visited[neighbor]) dfsUtil(neighbor, adj, visited, result);
    }
    // Clone graph using DFS
    private Map<Node, Node> visited = new HashMap<>();
    public Node cloneGraph(Node node) {
        if (node == null) return null;
        if (visited.containsKey(node)) return visited.get(node);
        Node copy = new Node(node.val);
        visited.put(node, copy);
        for (Node neighbor : node.neighbors)
            copy.neighbors.add(cloneGraph(neighbor));
        return copy;
    }
    // Number of islands
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int count = 0, m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) 
            for (int j = 0; j < n; j++)
                if (grid[i][j] == '1') {count++; dfs(grid, i, j);} // sink this island
        return count;
    }
    private void dfs(char[][] grid, int i, int j) {
        int m = grid.length, n = grid[0].length;
        // boundary + water check
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == '0') return;
        grid[i][j] = '0'; // mark visited
        // explore neighbors (up, down, left, right)
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
    // Max area of islands
    /*
    🔍 How It Works
- Traverse each cell in the grid.
- When you find land (1), run DFS to explore the entire island.
- DFS marks visited cells as 0 to avoid revisiting.
- Count the area of each island and track the maximum.

📊 Complexity
- Time Complexity: O(rows × cols) — each cell visited once.
- Space Complexity: O(rows × cols) recursion stack in worst case (all land).
🎯 Example Dry‑Run
Input:
grid = [
  [0,0,1,0,0],
  [0,1,1,1,0],
  [0,0,0,0,0],
  [1,1,0,0,1]
]
Steps:
- First island at (0,2) → area = 4.
- Second island at (3,0) → area = 2.
- Third island at (3,4) → area = 1.
Max area = 4
    */
    public int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        int rows = grid.length, cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    int area = dfs(grid, r, c);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        return maxArea;
    }
    private int dfs(int[][] grid, int r, int c) {
        // boundary + water check
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] == 0)
            return 0;
        grid[r][c] = 0; // mark visited
        int area = 1;   // count current cell
        // explore 4 directions
        area += dfs(grid, r + 1, c);
        area += dfs(grid, r - 1, c);
        area += dfs(grid, r, c + 1);
        area += dfs(grid, r, c - 1);
        return area;
    }
    // Number of closed island
    public int closedIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int count = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                // land
                if (grid[i][j] == 0) if (dfs(grid, i, j, m, n)) count++;
        return count;
    }
    private boolean dfs(int[][] grid, int r, int c, int m, int n) {
        // if out of bounds → touches boundary → not closed
        if (r < 0 || c < 0 || r >= m || c >= n) return false;
        if (grid[r][c] == 1) return true; // water → safe
        grid[r][c] = 1; // mark visited
        boolean up    = dfs(grid, r - 1, c, m, n);
        boolean down  = dfs(grid, r + 1, c, m, n);
        boolean left  = dfs(grid, r, c - 1, m, n);
        boolean right = dfs(grid, r, c + 1, m, n);
        return up && down && left && right;
    }
    // rotting oranges
    /*
    Perfect — let’s dry‑run the **rotting oranges BFS solution** step by step so you can see how the queue evolves and how minutes are counted.

---

## 🔍 Reminder of the Code
- Collect all rotten oranges (`2`) into a queue.  
- Count fresh oranges (`1`).  
- BFS level by level: each level = 1 minute.  
- Spread rot to adjacent fresh oranges.  
- Stop when queue empty or no fresh left.  
- Return minutes if all fresh are rotten, else `-1`.

---

## 🎯 Example Input
```
grid = [
  [2,1,1],
  [1,1,0],
  [0,1,1]
]
```

---

## 🌳 Dry‑Run Step by Step

### Initialization
- `m=3, n=3`  
- Queue = [(0,0)] (only rotten orange at top‑left)  
- Fresh = 7 (all other 1’s)  
- Minutes = 0  

---

### Minute 1
Process queue size=1 → cell (0,0).  
Neighbors:
- (1,0) → fresh → rot → enqueue (1,0), fresh=6  
- (0,1) → fresh → rot → enqueue (0,1), fresh=5  
Others invalid.  

Queue after minute 1 = [(1,0),(0,1)]  
Minutes = 1  

---

### Minute 2
Process queue size=2 → cells (1,0),(0,1).  
- (1,0): neighbors → (2,0 invalid), (0,0 already rotten), (1,1 fresh→rot→enqueue), fresh=4  
- (0,1): neighbors → (0,2 fresh→rot→enqueue), fresh=3  

Queue after minute 2 = [(1,1),(0,2)]  
Minutes = 2  

---

### Minute 3
Process queue size=2 → cells (1,1),(0,2).  
- (1,1): neighbors → (2,1 fresh→rot→enqueue), fresh=2  
- (0,2): neighbors → (1,2 invalid), (0,1 already rotten)  

Queue after minute 3 = [(2,1)]  
Minutes = 3  

---

### Minute 4
Process queue size=1 → cell (2,1).  
Neighbors → (2,2 fresh→rot→enqueue), fresh=1  

Queue after minute 4 = [(2,2)]  
Minutes = 4  

---

### Minute 5
Process queue size=1 → cell (2,2).  
Neighbors → none fresh.  

Queue empty, fresh=0 → stop.  
Minutes = 5  

---

## ✅ Final Answer
All oranges rotten in **5 minutes**.  
Return `5`.

---

## 📊 Trace Table

| Minute | Queue Before | Processed Cells | New Rotten | Fresh Left | Queue After |
|--------|--------------|-----------------|------------|------------|-------------|
| 0      | [(0,0)]      | –               | –          | 7          | [(0,0)]     |
| 1      | [(0,0)]      | (0,0)           | (1,0),(0,1)| 5          | [(1,0),(0,1)] |
| 2      | [(1,0),(0,1)]| (1,0),(0,1)     | (1,1),(0,2)| 3          | [(1,1),(0,2)] |
| 3      | [(1,1),(0,2)]| (1,1),(0,2)     | (2,1)      | 2          | [(2,1)]     |
| 4      | [(2,1)]      | (2,1)           | (2,2)      | 1          | [(2,2)]     |
| 5      | [(2,2)]      | (2,2)           | –          | 0          | []          |

    */
    public static int orangesRotting(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> q = new LinkedList<>();
        int fresh = 0;    
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                if(grid[i][j] == 2) q.offer(new int[]{i,j});
                if(grid[i][j] == 1) fresh++;
            }
        }
        int minutes = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while(!q.isEmpty() && fresh > 0) {
            int size = q.size();
            for(int k=0; k<size; k++) {
                int[] cell = q.poll();
                for(int[] d: dirs) {
                    int x = cell[0]+d[0], y = cell[1]+d[1];
                    if(x<0||y<0||x>=m||y>=n||grid[x][y]!=1) continue;
                    grid[x][y] = 2;
                    fresh--;
                    q.offer(new int[]{x,y});
                }
            }
            minutes++;
        }   
        return fresh==0 ? minutes : -1;
    }
    // Cheapest flight booking within K stops
    /*
    📊 Complexity
- Time Complexity: O(k × E), where E = number of flights.
- Space Complexity: O(V) for distance array.

🎯 Example Dry‑Run
Input:
n = 4
flights = [[0,1,100],[1,2,100],[2,3,100],[0,2,500]]
src = 0, dst = 3, k = 1


Steps:
- Iteration 0: dist = [0, ∞, ∞, ∞]
- Iteration 1: relax → dist = [0,100,500,∞]
- Iteration 2: relax → dist = [0,100,500,600]
Answer = 600.
Let’s dry‑run the **optimized Bellman‑Ford style solution** for *Cheapest Flights Within K Stops* so you can see exactly how the algorithm evolves the distance array layer by layer.

---

## ✨ Input Example
```
n = 4
flights = [[0,1,100],[1,2,100],[2,3,100],[0,2,500]]
src = 0, dst = 3, k = 1
```

---

## 🚀 Algorithm Recap
- Initialize `dist[src] = 0`, others = ∞.  
- Repeat **k+1 times** (because k stops = k+1 edges allowed).  
- Each iteration: relax all edges into a temporary array.  
- After k+1 iterations, `dist[dst]` is the cheapest cost with ≤ k stops.

---

## 🌳 Dry‑Run Step by Step

### Initialization
```
dist = [0, ∞, ∞, ∞]
```

---

### Iteration 0 (allowing 0 stops → 1 edge)
Relax all edges:
- 0 → 1 (100): dist[1] = 0 + 100 = 100  
- 1 → 2: ignored (dist[1] was ∞ before this iteration)  
- 2 → 3: ignored  
- 0 → 2 (500): dist[2] = 0 + 500 = 500  

Result after iteration:
```
dist = [0, 100, 500, ∞]
```

---

### Iteration 1 (allowing 1 stop → 2 edges)
Relax all edges again:
- 0 → 1: no improvement (already 100)  
- 1 → 2 (100): dist[2] = min(500, 100 + 100) = 200  
- 2 → 3 (100): dist[3] = 200 + 100 = 300  
- 0 → 2: no improvement (200 < 500)  

Result after iteration:
```
dist = [0, 100, 200, 300]
```

---

### Iteration 2 (allowing 2 stops → 3 edges)
Relax all edges again:
- 1 → 2: no improvement (200 already optimal)  
- 2 → 3: dist[3] = min(300, 200 + 100) = 300 (unchanged)  
- Others no improvement  

Final:
```
dist = [0, 100, 200, 300]
```

---

## ✅ Answer
`dist[dst] = dist[3] = 300`

So the cheapest price from 0 → 3 with at most 1 stop is **300**.

---

## 🔑 Key Insight
- The algorithm builds solutions layer by layer.  
- Each iteration corresponds to allowing one more stop.  
- By iteration 1, we found the valid path `0 → 1 → 2 → 3` with cost 300.  
- The direct path `0 → 2 → 3` costs 600, but is pruned because 300 is cheaper.  
    */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Distance array: dist[i] = min cost to reach city i
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        // Relax edges up to k+1 times (because k stops means k+1 edges)
        for (int i = 0; i <= k; i++) {
            int[] temp = Arrays.copyOf(dist, n);
            for (int[] flight : flights) {
                int u = flight[0], v = flight[1], w = flight[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < temp[v]) 
                    temp[v] = dist[u] + w;
            }
            dist = temp;
            System.out.println(Arrays.toString(temp));
        }
        return dist[dst] == Integer.MAX_VALUE? -1 : dist[dst];
    }
    public static void main(String[] args) {
        System.out.println(findCheapestPrice(4, new int[][]{
            {0,1,100},{1,2,100},{2,0,100},{1,3,600},{2,3,200}
        }, 0, 3, 1));
    }
}
