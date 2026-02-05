package day4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solutions {
    // unreachable handled
    public static int jump(int[] nums) {
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
                // If currentEnd cannot move forward, break early
                if (currentEnd <= i) return -1; // unreachable
            }
        }
        // If farthest never reaches the last index
        return farthest >= nums.length - 1 ? jumps : -1;
    }
    /*
    🧠 Stepwise Explanation
    - Sort intervals by start time → ensures we process them in order.
    - Initialize current as the first interval.
    - For each interval:
    - If it overlaps (intervals[i][0] <= current[1]), merge by extending current[1].
    - Otherwise, add current to result and move to the next interval.
    - Add the last interval to the result list.
    - Convert list back to array.

    🔍 Example Walkthrough
    Input: [[1,3],[2,6],[8,10],[15,18]]
    - Sort → [[1,3],[2,6],[8,10],[15,18]]
    - Merge [1,3] and [2,6] → [1,6]
    - Next [8,10] → no overlap → keep
    - Next [15,18] → no overlap → keep
    - Output: [[1,6],[8,10],[15,18]]
    🛠 Complexity
    - Sorting: O(n\log n)
    - Merging: O(n)
    - Total Time Complexity: O(n\log n)
    - Space Complexity: O(n) (for result list)
    */
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        // Step 1: Sort intervals by start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        // Step 2: Iterate and merge
        int[] current = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                // Overlap → extend current interval
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                // No overlap → add current and move forward
                merged.add(current);
                current = intervals[i];
            }
        }
        // Add the last interval
        merged.add(current);
        return merged.toArray(new int[merged.size()][]);
    }
    /*
    🔍 Example Dry Run
    Input: [[1,2],[2,3],[3,4],[1,3]]
    - Sorted by end → [[1,2],[2,3],[1,3],[3,4]]
    - Keep [1,2], end=2
    - [2,3] → start=2 ≥ end=2 → keep, update end=3
    - [1,3] → start=1 < end=3 → overlap → removeCount=1
    - [3,4] → start=3 ≥ end=3 → keep, update end=4
    - Result = 1
    🛠 Complexity
    - Time: O(n log n) due to sorting.
    - Space: O(1) if sorting is in-place.
    */
    public static int eraseOverlapIntervals(int[][] intervals) {
        // Step 1: Sort by end time
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        int removeCount = 0;
        int end = intervals[0][1]; // first interval's end
        // Step 2: Iterate
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < end) {
                // Overlap → remove this interval
                removeCount++;
            } else {
                // Non-overlap → update end
                end = intervals[i][1];
            }
        }
        return removeCount;
    }
    /*
    🧠 Stepwise Explanation
    - Count frequencies of each task.
    - Find the maximum frequency (maxFreq).
    - Count how many tasks have this maximum frequency (countMax).
    - Use the formula:
        \mathrm{result}=\max (\mathrm{tasks.length},(maxFreq-1)\cdot (n+1)+countMax)
    - (maxFreq - 1) → number of full blocks.
    - (n + 1) → size of each block (task + cooldown).
    - countMax → tasks filling the last block.
    - Take the maximum with tasks.length to cover cases where idle slots aren’t needed.
    🔍 Example Walkthrough
    Input: tasks = [A,A,A,B,B,B], n = 2
    - Frequencies: A=3, B=3
    - maxFreq = 3, countMax = 2
    - Formula: (3-1) * (2+1) + 2 = 8
    - Compare with tasks.length = 6 → result = 8
    Output: 8
    🛠 Complexity
    - Counting frequencies: O(|tasks|)
    - Sorting frequencies: O(26\log 26) → constant
    - Total Time Complexity: O(|tasks|)
    - Space Complexity: O(1) (fixed array of size 26)
    ✅ This greedy + math solution is optimal and elegant.
    */
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char task : tasks) freq[task - 'A']++;
        // Step 1: Find the maximum frequency
        Arrays.sort(freq);
        int maxFreq = freq[25];
        // Step 2: Count how many tasks have this maximum frequency
        int countMax = 0;
        for (int i = 25; i >= 0; i--) {
            if (freq[i] == maxFreq) countMax++;
            else break;
        }
        // Step 3: Apply formula
        int partCount = maxFreq - 1;
        int partLength = n + 1;
        int emptySlots = partCount * partLength + countMax;
        return Math.max(tasks.length, emptySlots);
    }
    // Approach B: Tabulation (Bottom-Up)
    // - Iteratively build Fibonacci numbers.
    // - Time Complexity: O(n).
    // - Space Complexity: O(1) if optimized.
    public int fibonacci(int n) {
        if (n <= 1) return n;
        int prev2 = 0, prev1 = 1;
        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
    public static int tribonacci(int n) {
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        dp[2] = 1;
        for (int i = 3; i <= n; i++) dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3];
        return dp[n];
    }
    public static int count(int[] dom, int target){
        int initial = target+1;
        int[] dp = new int[initial];
        Arrays.fill(dp, initial);
        dp[0]=0;
        for(int cash:dom){
            for(int index = cash; index<=target;index++){
                dp[index]=Math.min(dp[index], 
                    dp[index-cash]+1);
            }
        }
        if(dp[target]<initial) return dp[target];
        else return -1;
        // return (dp[target]<target+1)?dp[target]:-1;
    }
}
