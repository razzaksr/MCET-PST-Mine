package day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Solutions {
    /*
    Coloring Border
    🎯 Dry‑Run on Your Input
grid = [
 [1,1,1],
 [1,1,1],
 [1,1,1]
]
row=1, col=1, color=2


- DFS visits all cells with originalColor=1.
- Center cell (1,1) has 4 neighbors of originalColor → not added to borders.
- All edge cells have <4 neighbors of originalColor → added to borders.
- After DFS, recolor only borders.
Final output:
[[2,2,2],
 [2,1,2],
 [2,2,2]]


✅ Matches expected.
    */
    public static class Solution {
        int m, n;
        int[][] grid;
        boolean[][] visited;
        int originalColor;
        int newColor;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        List<int[]> borders = new ArrayList<>();
        public int[][] colorBorder(int[][] grid, int row, int col, int color) {
            this.m = grid.length;
            this.n = grid[0].length;
            this.grid = grid;
            this.visited = new boolean[m][n];
            this.originalColor = grid[row][col];
            this.newColor = color;
            dfs(row, col);
            // Recolor only border cells
            for (int[] cell : borders) grid[cell[0]][cell[1]] = newColor;
            return grid;
        }
        private void dfs(int r, int c) {
            visited[r][c] = true;
            int countSame = 0;
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < m && nc < n) {
                    if (grid[nr][nc] == originalColor) {
                        countSame++;
                        if (!visited[nr][nc]) dfs(nr, nc);
                    }
                }
            }
            // If not all 4 neighbors are same-color, mark as border
            if (countSame < 4) borders.add(new int[]{r, c});
        }
    }
    // Surrounded regions
    public void solve(char[][] board) {
        if (board == null || board.length == 0) return;
        int m = board.length, n = board[0].length;
        // Step 1: Mark all 'O's connected to border
        for (int i = 0; i < m; i++) {
            dfs(board, i, 0);         // left border
            dfs(board, i, n - 1);     // right border
        }
        for (int j = 0; j < n; j++) {
            dfs(board, 0, j);         // top border
            dfs(board, m - 1, j);     // bottom border
        }
        // Step 2: Flip all remaining 'O' to 'X', and restore '#' to 'O'
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X'; // captured
                } else if (board[i][j] == '#') {
                    board[i][j] = 'O'; // restore
                }
            }
        }
    }
    private void dfs(char[][] board, int i, int j) {
        int m = board.length, n = board[0].length;
        if (i < 0 || j < 0 || i >= m || j >= n || board[i][j] != 'O') return;
        board[i][j] = '#'; // mark as safe
        dfs(board, i + 1, j);
        dfs(board, i - 1, j);
        dfs(board, i, j + 1);
        dfs(board, i, j - 1);
    }
    // Battle ship
    /*
    🔍 How It Works
- Battleships are placed horizontally or vertically, never diagonally.
- They are separated by at least one empty cell (.).
- So each battleship has a unique “head” cell:
- No X directly above it.
- No X directly to the left.
- By counting only these head cells, you count each ship exactly once.

📊 Complexity
- Time Complexity: O(m × n) — scan each cell once.
- Space Complexity: O(1) — no extra storage needed.

🎯 Example Dry‑Run
Input:
board = [
  ['X','.','.','X'],
  ['.','.','.','X'],
  ['.','.','.','X']
]


Step through:
- (0,0) = 'X' → no top/left neighbor → count=1
- (0,3) = 'X' → no top/left neighbor → count=2
- (1,3) = 'X' → has top neighbor 'X' → skip
- (2,3) = 'X' → has top neighbor 'X' → skip
Final answer = 2 battleships.
    */
    public int countBattleships(char[][] board) {
        int m = board.length, n = board[0].length;
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Skip empty cells
                if (board[i][j] != 'X') continue;
                // If there's an 'X' above or to the left, it's part of an existing ship
                if (i > 0 && board[i - 1][j] == 'X') continue;
                if (j > 0 && board[i][j - 1] == 'X') continue;
                // Otherwise, this is the start of a new battleship
                count++;
            }
        }
        return count;
    }
    // Word Ladder 1
    /*
    Stepwise Explanation (Your Style)
Example 1
beginWord = "hit"
endWord   = "cog"
wordList  = ["hot","dot","dog","lot","log","cog"]


- Graph Construction (Implicit)
- We don’t build all edges upfront (too costly).
- Instead, for each word, generate all possible one-letter variations and check if they’re in the dictionary.
- Example: "hit" → *it, h*t, hi*
- Matches "hot".

Another tracking:
queue [hit]
Polled hit
Before neighbors
[h, i, t]
taken original h
Restoring [h, i, t]
taken original i
new match hot available in dict
[hit, hot]
[hot]
Restoring [h, i, t]
taken original t
Restoring [h, i, t]
Level incremented 2
queue [hot]
Polled hot
Before neighbors
[h, o, t]
taken original h
new match dot available in dict
[hit, dot, hot]
[dot]
new match lot available in dict
[lot, hit, dot, hot]
[dot, lot]
Restoring [h, o, t]
taken original o
Restoring [h, o, t]
taken original t
Restoring [h, o, t]
Level incremented 3
queue [dot, lot]
Polled dot
Before neighbors
[d, o, t]
taken original d
Restoring [d, o, t]
taken original o
Restoring [d, o, t]
taken original t
new match dog available in dict
[lot, hit, dot, hot, dog]
[lot, dog]
Restoring [d, o, t]
Polled lot
Before neighbors
[l, o, t]
taken original l
Restoring [l, o, t]
taken original o
Restoring [l, o, t]
taken original t
new match log available in dict
[lot, hit, log, dot, hot, dog]
[dog, log]
Restoring [l, o, t]
Level incremented 4
queue [dog, log]
Polled dog
Before neighbors
[d, o, g]
taken original d
new match cog available in dict
[lot, hit, log, dot, cog, hot, dog]
[log, cog]
Restoring [d, o, g]
taken original o
Restoring [d, o, g]
taken original g
Restoring [d, o, g]
Polled log
Before neighbors
[l, o, g]
taken original l
Restoring [l, o, g]
taken original o
Restoring [l, o, g]
taken original g
Restoring [l, o, g]
Level incremented 5
queue [cog]
Polled cog
5

BFS Traversal
Queue starts with ("hit", level=1).
- Visit "hit" → neighbors: "hot".
- Queue: ("hot", level=2).
- Visit "hot" → neighbors: "dot", "lot".
- Queue: ("dot",2+1=3), ("lot",3).
- Visit "dot" → neighbor "dog".
- Queue: ("dog",4).
- Visit "dog" → neighbor "cog".
- Found endWord at level 5.
- Answer
Shortest transformation length = 5

Example 2beginWord = "hit"
endWord   = "cog"
wordList  = ["hot","dot","dog","lot","log"]
- "cog" not in dictionary → immediately return 0.
    */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return 0;

        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Set<String> visited = new HashSet<>();
        visited.add(beginWord);

        int level = 1; // beginWord counts as level 1

        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.println("queue "+queue);
            /* 
            BFS level-order traversal:
            Level 1: ["hit"]
            Level 2: ["hot"] (1 word)
            Level 3: ["dot", "lot"] (2 words at same distance)
            Level 4: ["dog", "log"] (2 words)
            Level 5: ["cog"]
            */
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                System.out.println("Polled "+word);
                if (word.equals(endWord)) return level;
                // Generate neighbors by changing one letter
                char[] chars = word.toCharArray();
                System.out.println("Before neighbors\n"+Arrays.toString(chars));
                for (int pos = 0; pos < chars.length; pos++) {
                    char original = chars[pos];
                    System.out.println("taken original "+original);
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == original) continue;
                        chars[pos] = c;
                        String newWord = new String(chars);
                        if (dict.contains(newWord) && !visited.contains(newWord)) {
                            System.out.println("new match "+newWord+" available in dict");
                            visited.add(newWord);
                            queue.add(newWord);
                            System.out.println(visited+"\n"+queue);
                        }
                    }
                    chars[pos] = original; // restore
                    System.out.println("Restoring "+Arrays.toString(chars));
                }
            }
            level++;
            System.out.println("Level incremented "+level);
        }
        return 0;
    }
    // Word Ladder II
    /*
    🎯 Example Dry‑Run
Input:
beginWord = "hit"
endWord   = "cog"
wordList  = ["hot","dot","dog","lot","log","cog"]


- BFS builds parent links:
- hot ← hit
- dot ← hot
- lot ← hot
- dog ← dot
- log ← lot
- cog ← dog, log
- Backtrack from cog → dog → dot → hot → hit
- Backtrack from cog → log → lot → hot → hit
Output:
[["hit","hot","dot","dog","cog"],
 ["hit","hot","lot","log","cog"]]
    */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        List<List<String>> result = new ArrayList<>();
        if (!dict.contains(endWord)) return result;

        // BFS: build parent map
        Map<String, List<String>> parents = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        boolean found = false;

        while (!queue.isEmpty() && !found) {
            Set<String> levelVisited = new HashSet<>();
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                char[] chars = word.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char old = chars[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == old) continue;
                        chars[j] = c;
                        String newWord = new String(chars);
                        if (dict.contains(newWord)) {
                            if (!visited.contains(newWord)) {
                                if (!levelVisited.contains(newWord)) {
                                    queue.offer(newWord);
                                    levelVisited.add(newWord);
                                }
                                parents.computeIfAbsent(newWord, k -> new ArrayList<>()).add(word);
                            }
                            if (newWord.equals(endWord)) found = true;
                        }
                    }
                    chars[j] = old;
                }
            }
            visited.addAll(levelVisited);
        }

        if (found) {
            List<String> path = new ArrayList<>();
            path.add(endWord);
            backtrack(endWord, beginWord, parents, path, result);
        }
        return result;
    }

    private void backtrack(String word, String beginWord, Map<String, List<String>> parents,
                           List<String> path, List<List<String>> result) {
        if (word.equals(beginWord)) {
            List<String> copy = new ArrayList<>(path);
            Collections.reverse(copy);
            result.add(copy);
            return;
        }
        if (!parents.containsKey(word)) return;
        for (String p : parents.get(word)) {
            path.add(p);
            backtrack(p, beginWord, parents, path, result);
            path.remove(path.size() - 1);
        }
    }
    // Max Probability
    /*
    🔍 How It Works
- Build adjacency list with edge indices to access probabilities.
- Use a max‑heap (priority queue) to always expand the path with the highest probability.
- Relaxation: if p * succProb[idx] > prob[v], update and push into heap.
- Stop early when reaching end with maximum probability.

📊 Complexity
- Time Complexity: O(E log V) — each edge relaxation involves a heap operation.
- Space Complexity: O(V + E) — adjacency list + probability array.
🎯 Example Dry‑Run
Input:
n = 3
edges = [[0,1],[1,2],[0,2]]
succProb = [0.5,0.5,0.2]
start = 0, end = 2


Steps:
- Start at 0 → prob[0] = 1.0
- Relax (0→1): prob[1] = 0.5
- Relax (0→2): prob[2] = 0.2
- Next, expand node 1 (0.5): relax (1→2): prob[2] = max(0.2, 0.25) = 0.25
- Destination reached → Answer = 0.25
    */
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        // Build adjacency list
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0], v = edges[i][1];
            graph.get(u).add(new int[]{v, i});
            graph.get(v).add(new int[]{u, i});
        }
        // Max-heap: (probability, node)
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(b[0], a[0]));
        pq.offer(new double[]{1.0, start});
        double[] prob = new double[n];
        prob[start] = 1.0;
        while (!pq.isEmpty()) {
            double[] curr = pq.poll();
            double p = curr[0];
            int u = (int) curr[1];
            if (u == end) return p; // reached destination with max probability
            if (p < prob[u]) continue; // stale entry
            for (int[] edge : graph.get(u)) {
                int v = edge[0], idx = edge[1];
                double newProb = p * succProb[idx];
                if (newProb > prob[v]) {
                    prob[v] = newProb;
                    pq.offer(new double[]{newProb, v});
                }
            }
        }
        return 0.0;
    }
    // judge in town
    public int findJudge(int n, int[][] trust) {
        int[] score = new int[n + 1]; // people labeled 1..n
        for (int[] relation : trust) {
            int a = relation[0];
            int b = relation[1];
            score[a]--;   // a trusts someone → not judge
            score[b]++;   // b is trusted → potential judge
        }
        for (int i = 1; i <= n; i++) 
            if (score[i] == n - 1) return i; // judge found
        return -1; // no judge
    }
}
