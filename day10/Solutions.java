package day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solutions {
    /*
    🔍 How It Works
- Start from index 0 and try each candidate.
- If target == 0, we found a valid combination.
- If target < 0, we discard the path.
- Reuse the same element by passing i again in recursion.
- Backtrack by removing the last added element.

📊 Complexity
- Time Complexity: O(2^n) worst case — exponential due to branching.
- Space Complexity: O(target) recursion depth + result storage.

Let’s dry‑run the **Combination Sum backtracking solution** step by step for:

```
candidates = [2, 3, 6, 7]
target = 7
```

---

## 🔄 Step-by-Step Backtracking Tree

### Start: `[]`, target = 7  
Try each candidate starting from index 0.

---

### Path: `[2]`, target = 5  
→ `[2,2]`, target = 3  
→ `[2,2,2]`, target = 1  
→ `[2,2,2,2]`, target = -1 ❌ backtrack  
→ `[2,2,3]`, target = 0 ✅ valid → add `[2,2,3]`  
→ backtrack to `[2]`, try `[2,3]`, target = 0 ✅ valid → add `[2,3]`  
→ try `[2,6]`, target = -1 ❌  
→ try `[2,7]`, target = -2 ❌

---

### Path: `[3]`, target = 4  
→ `[3,3]`, target = 1  
→ `[3,3,2]`, target = -1 ❌  
→ `[3,3,3]`, target = -2 ❌  
→ `[3,3,6]`, target = -5 ❌  
→ `[3,3,7]`, target = -6 ❌  
→ `[3,4]`, target = 0 ✅ valid → add `[3,4]`  
→ `[3,6]`, target = -2 ❌  
→ `[3,7]`, target = -3 ❌

---

### Path: `[6]`, target = 1  
→ `[6,2]`, target = -1 ❌  
→ `[6,3]`, target = -2 ❌  
→ `[6,6]`, target = -5 ❌  
→ `[6,7]`, target = -6 ❌

---

### Path: `[7]`, target = 0 ✅ valid → add `[7]`

---

## ✅ Final Result
```java
[[2,2,3], [2,3,2], [3,4], [7]]
```

(Note: `[2,3,2]` is a permutation of `[2,2,3]`, but if duplicates are filtered, final result is `[[2,2,3], [3,4], [7]]`)

---

## 🎯 Key Insight
- Backtracking explores all paths recursively.  
- Prunes early when target < 0.  
- Adds path when target == 0.  
- Reuses elements by passing same index in recursive call.
    */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    private void backtrack(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (target < 0) return;
        for (int i = start; i < candidates.length; i++) {
            path.add(candidates[i]);
            backtrack(candidates, target - candidates[i], i, path, result); // reuse same element
            path.remove(path.size() - 1); // backtrack
        }
    }
    // combinations 2
    /*
    Perfect, let’s dry‑run the **optimized Combination Sum II solution** on the sample:

```
candidates = [10,1,2,7,6,1,5], target = 8
```

---

## 🔄 Step 1: Sort Candidates
Sorted → `[1,1,2,5,6,7,10]`

---

## 🔄 Step 2: Backtracking with Pruning

### Path `[1]`, target = 7  
- `[1,1]`, target = 6  
  - `[1,1,2]`, target = 4  
    - `[1,1,2,5]`, target = -1 ❌ (pruned early because 5 > target)  
    - `[1,1,2,6]`, target = -2 ❌  
    - `[1,1,2,7]`, target = -3 ❌  
  - `[1,1,5]`, target = 1  
    - Next candidate is 6 > target → prune immediately ✅ (no wasted calls)  
  - `[1,1,6]`, target = 0 ✅ valid → add `[1,1,6]`  
- `[1,2]`, target = 5  
  - `[1,2,5]`, target = 0 ✅ valid → add `[1,2,5]`  
- `[1,5]`, target = 2  
  - Next candidate is 6 > target → prune ✅  
- `[1,6]`, target = 1  
  - Next candidate is 7 > target → prune ✅  
- `[1,7]`, target = 0 ✅ valid → add `[1,7]`

---

### Path `[2]`, target = 6  
- `[2,5]`, target = 1 → next candidate 6 > target → prune ✅  
- `[2,6]`, target = 0 ✅ valid → add `[2,6]`

---

### Path `[5]`, target = 3  
- Next candidate is 6 > target → prune ✅  

---

### Path `[6]`, target = 2  
- Next candidate is 7 > target → prune ✅  

---

### Path `[7]`, target = 1  
- Next candidate is 10 > target → prune ✅  

---

### Path `[10]`, target = -2 ❌ (immediate prune)

---

## ✅ Final Result
```java
[[1,1,6], [1,2,5], [1,7], [2,6]]
```

---

## 📊 Why This Is Faster
- **Pruning:** As soon as a candidate > remaining target, recursion stops.  
- **Duplicate skipping:** Prevents exploring identical branches.  
- **Memory efficiency:** Reuses the same `path` list, only copying when adding to result.  

This eliminates a huge number of unnecessary recursive calls compared to the unoptimized version, which explains why runtime and memory scores jump significantly.
    */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates); // sort once
        List<List<Integer>> result = new ArrayList<>();
        dfs(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void dfs(int[] candidates, int target, int start, List<Integer> path, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            // pruning: if current candidate > target, break early
            if (candidates[i] > target) break;

            // skip duplicates at same recursion level
            if (i > start && candidates[i] == candidates[i - 1]) continue;

            path.add(candidates[i]);
            dfs(candidates, target - candidates[i], i + 1, path, result);
            path.remove(path.size() - 1); // backtrack
        }
    }
    // Combination 3
    /*
    🔍 How It Works
- Start from 1 up to 9.
- At each step, choose a number and recurse with updated target.
- Stop when:
- Path length > k, or target < 0.
- Path length == k and target == 0 → add to result.
- Backtracking ensures all unique sets are explored.

📊 Complexity
- Time Complexity: O(C(9,k)) — combinations of 9 numbers taken k at a time.
- Space Complexity: O(k) recursion depth + result storage.

🎯 Example Dry‑Run
Input: k=3, n=7
- Start with [1] → target=6
- [1,2] → target=4
    - [1,2,4] → target=0 ✅ valid
- [1,3] → target=3
    - [1,3,3] invalid (duplicate not allowed)
    - [1,3,4] → target=-? ❌
- [2,2,...] invalid (reuse not allowed).
- Another valid path: [2,3,2] not allowed, but [2,5] → target=0 with [2,5] incomplete (needs 3 numbers).
- Final valid combinations: [[1,2,4], [1,3,3] ❌, [2,5]] → actual result is [[1,2,4], [1,5,1] ❌, [2,5]] → after pruning, correct output is [[1,2,4], [1,5,1] ❌, [2,5]].
For k=3, n=7, the correct output is:
[[1,2,4], [1,3,3] ❌ skipped, [2,5]]
    */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(1, k, n, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int k, int target, List<Integer> path, List<List<Integer>> result) {
        // Base case: if we picked k numbers and target is 0 → valid combination
        if (path.size() == k && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        // If path too long or target negative → stop
        if (path.size() > k || target < 0) return;

        for (int i = start; i <= 9; i++) {
            path.add(i);
            backtrack(i + 1, k, target - i, path, result); // move to next number
            path.remove(path.size() - 1); // backtrack
        }
    }
    // phone keypad letter combinations
    /*
    🔍 How It Works
- Each digit maps to a set of letters (2 → abc, 3 → def, etc.).
- Recursively build combinations by appending one letter at a time.
- When the path length equals the number of digits, add it to the result.
- Backtracking ensures all possibilities are explored.

📊 Complexity
- Time Complexity: O(3m), where n = number of digits mapping to 3 letters, m = number of digits mapping to 4 letters.
- Space Complexity: O(n) recursion depth + result storage.

🎯 Example Dry‑Run
Input: "23"
- Digit 2 → "abc"
- Digit 3 → "def"
- Combinations:
- "ad", "ae", "af"
- "bd", "be", "bf"
- "cd", "ce", "cf"
Final result:
["ad","ae","af","bd","be","bf","cd","ce","cf"]
    */
    private static final String[] KEYPAD = {
        "",    "",    "abc", "def", "ghi", "jkl",
        "mno", "pqrs", "tuv", "wxyz"
    };

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.length() == 0) return result;
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(String digits, int index, StringBuilder path, List<String> result) {
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }

        String letters = KEYPAD[digits.charAt(index) - '0'];
        for (char c : letters.toCharArray()) {
            path.append(c);
            backtrack(digits, index + 1, path, result);
            path.deleteCharAt(path.length() - 1); // backtrack
        }
    }
    // permutation1
    /*
    🔍 How It Works
- Maintain a used[] array to track which numbers are already in the current path.
- Recursively build permutations by adding unused numbers.
- When path length equals nums.length, add it to result.
- Backtrack by removing the last number and marking it unused.

📊 Complexity
- Time Complexity: O(n × n!) — there are n! permutations, each of length n.
- Space Complexity: O(n) recursion depth + result storage.
🎯 Example Dry‑Run
Input: nums = [1,2,3]
- Start: []
- Choose 1 → [1]
- Choose 2 → [1,2]
- Choose 3 → [1,2,3] ✅
- Backtrack → [1] → choose 3 → [1,3,2] ✅
- Backtrack → [] → choose 2 → [2,1,3], [2,3,1] ✅
- Backtrack → [] → choose 3 → [3,1,2], [3,2,1] ✅
Final result:
[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
    */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, new boolean[nums.length], new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // skip already used numbers

            used[i] = true;
            path.add(nums[i]);

            backtrack(nums, used, path, result);

            // backtrack
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    // permuation2
    /*
    🔍 How It Works
- Sorting first ensures duplicates are adjacent.
- Skip condition:
if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
- → prevents generating duplicate permutations by only using the first occurrence of a duplicate at each recursion level.
- Standard backtracking builds permutations by marking numbers as used and backtracking after recursion.

📊 Complexity
- Time Complexity: O(n × n!) — same as permutations, but pruning reduces duplicate branches.
- Space Complexity: O(n) recursion depth + result storage.
🎯 Example Dry‑Run
Input: nums = [1,1,2]
- Start: []
- Choose first 1 → [1]
- Choose second 1 → [1,1]
- Choose 2 → [1,1,2] ✅
- Backtrack → [1] → choose 2 → [1,2,1] ✅
- Backtrack → [] → skip second 1 (duplicate rule)
- Choose 2 → [2]
- Choose 1 → [2,1]
- Choose other 1 → [2,1,1] ✅
Final result:
[[1,1,2],[1,2,1],[2,1,1]]
    */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // sort to group duplicates
        backtrack2(nums, new boolean[nums.length], new ArrayList<>(), result);
        return result;
    }
    private void backtrack2(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;

            // Skip duplicates: if current == previous and previous not used, skip
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;

            used[i] = true;
            path.add(nums[i]);

            backtrack2(nums, used, path, result);

            // backtrack
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    // target sum
    /*
    🔍 How It Works
- Convert problem into subset sum counting.
- Use 1D DP array to count ways efficiently.
- dp[j] = number of ways to reach sum j.
- Iterate backwards to avoid reusing the same number multiple times.

📊 Complexity
- Time Complexity: O(n × target)
- Space Complexity: O(target)

    📊 Example Dry‑Run with Negative Target
Input: nums = [1,1,1,1,1], target = -3
- sum = 5
- Check bounds: |target| = 3 ≤ 5 → possible.
- (target + sum) = (-3 + 5) = 2 → even.
- Subset sum = 1.
- Count subsets with sum = 1 → 5 ways (each single 1).
- Output = 5. ✅
🎯 Key Insight
- Negative targets are valid — they just shift the balance between positive and negative groups.
- The corrected solution ensures both bounds check and parity check are applied before computing DP.

    */
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) sum += num;

        // If target is outside [-sum, sum], impossible
        if (Math.abs(target) > sum) return 0;

        // (target + sum) must be even to form valid subset sum
        if ((target + sum) % 2 != 0) return 0;

        int subsetSum = (target + sum) / 2;
        return countSubsets(nums, subsetSum);
    }

    private int countSubsets(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1; // one way to make sum 0

        for (int num : nums) {
            for (int j = target; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        return dp[target];
    }
    // subset 1
    /*
    🔍 How It Works
- Start with an empty path [].
- At each step, add the current path to the result.
- Recursively explore including/excluding each element.
- Backtracking ensures all subsets are generated.

📊 Complexity
- Time Complexity: O(n × 2n subsets, each up to length n.
- Space Complexity: O(n) recursion depth + result storage.
🎯 Example Dry‑Run
Input: nums = [1,2,3]
- Start: [] → add []
- Include 1 → [1] → add [1]
- Include 2 → [1,2] → add [1,2]
- Include 3 → [1,2,3] → add [1,2,3]
- Exclude 3 → backtrack → [1,2]
- Exclude 2 → [1,3] → add [1,3]
- Exclude 1 → [2] → add [2]
- Include 3 → [2,3] → add [2,3]
- Exclude 2 → [3] → add [3]
Final result:
[[], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]
    */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }
    private void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        // Add current subset
        result.add(new ArrayList<>(path));

        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1); // backtrack
        }
    }
    // subset 2
    /*
    🔍 How It Works
- Sorting first ensures duplicates are adjacent.
- Skip condition:
if (i > start && nums[i] == nums[i - 1]) continue;
- → prevents generating duplicate subsets by only using the first occurrence of a duplicate at each recursion level.
- Standard backtracking explores all subsets by including/excluding each element.

📊 Complexity
- Time Complexity: O(n × 2n subsets, but pruning reduces duplicate branches.
- Space Complexity: O(n) recursion depth + result storage.
🎯 Example Dry‑Run
Input: nums = [1,2,2]
- Start: [] → add []
- Include 1 → [1] → add [1]
- Include 2 → [1,2] → add [1,2]
- Include second 2 → [1,2,2] → add [1,2,2]
- Skip second 2 (duplicate rule)
- Exclude 1 → [2] → add [2]
- Include second 2 → [2,2] → add [2,2]
Final result:
[[], [1], [1,2], [1,2,2], [2], [2,2]]
    */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // sort to group duplicates
        backtrack2(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack2(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));

        for (int i = start; i < nums.length; i++) {
            // Skip duplicates at the same recursion level
            if (i > start && nums[i] == nums[i - 1]) continue;

            path.add(nums[i]);
            backtrack2(nums, i + 1, path, result);
            path.remove(path.size() - 1); // backtrack
        }
    }
}
