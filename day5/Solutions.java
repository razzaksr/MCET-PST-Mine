package day5;

public class Solutions {
    // Classic 0/1 Knapsack DP solution
    public static int maxProfit(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) 
                    dp[i][w] = Math.max(
                values[i - 1] + dp[i - 1][w - weights[i - 1]], dp[i - 1][w]);
                else dp[i][w] = dp[i - 1][w];
            }
        }
        return dp[n][capacity];
    }
    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        int[] dp = new int[n + 1];
        dp[0] = 1; // base case: empty string
        // First character must not be '0'
        dp[1] = s.charAt(0) != '0' ? 1 : 0;
        for (int i = 2; i <= n; i++) {
            // Single digit decode
            int one = s.charAt(i - 1) - '0';
            if (one >= 1 && one <= 9) dp[i] += dp[i - 1];
            // Two digit decode
            int two = Integer.parseInt(s.substring(i - 2, i));
            if (two >= 10 && two <= 26) dp[i] += dp[i - 2];
        }
        return dp[n];
    }
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int[] dp = new int[n + 1];
        dp[1] = 1; dp[2] = 2;
        for (int i = 3; i <= n; i++) dp[i] = dp[i - 1] + dp[i - 2];
        return dp[n];
    }
    /*
    
    */
    public static long countFriendsPairings(int n) {
        int dp[] = new int[n + 1];
        // Filling dp[] in bottom-up manner using
        // recursive formula explained above.
        for (int i = 0; i <= n; i++) {
            if (i <= 2) dp[i] = i;
            else dp[i] = dp[i - 1] + (i - 1) * dp[i - 2];
        }
        return dp[n];
    }
    /*
    ⚙️ Dynamic Programming Approach
    Step 1: Define State
    - dp[i] = maximum money you can rob from the first i houses.
    Step 2: Transition
    For each house i:
    - If you rob it → add its value to dp[i-2].
    - If you skip it → take dp[i-1].
    dp[i]=max (dp[i-1],dp[i-2]+nums[i])
    Step 3: Initialization
    - dp[0] = nums[0]
    - dp[1] = max(nums[0], nums[1])
    Step 4: Answer
    - Return dp[n-1].

    🛠 Complexity
    - Time Complexity: O(n) (single pass through houses).
    - Space Complexity: O(1) (only two variables needed).

    🔍 Example Walkthrough
    Input: nums = [2,7,9,3,1]
    - House 0 → rob = 2
    - House 1 → max(2,7) = 7
    - House 2 → max(7, 2+9=11) = 11
    - House 3 → max(11, 7+3=10) = 11
    - House 4 → max(11, 11+1=12) = 12
    Answer = 12

    */
    public static int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0]; // dp[i-2]
        int prev1 = Math.max(nums[0], nums[1]); // dp[i-1]
        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
    /*
    ⚙️ Approach
    - Observation:
    - In the circular case, if you rob the first house, you cannot rob the last.
    - If you rob the last house, you cannot rob the first.
    - So split into two linear subproblems:
    - Case 1: Rob houses [0..n-2] (exclude last).
    - Case 2: Rob houses [1..n-1] (exclude first).
    - Answer = max(case1, case2).
    - Helper Function (Linear Robber):
    - Same as House Robber I.
    - Use rolling variables (prev2, prev1) to keep track of max loot.

    🛠 Complexity
    - Time Complexity: O(n) (two linear passes).
    - Space Complexity: O(1) (constant variables).

    🔍 Example Walkthrough
    Input: nums = [1,2,3,1]
    - Case 1 (exclude last): [1,2,3] → max = 3
    - Case 2 (exclude first): [2,3,1] → max = 4
    - Answer = max(3,4) = 4
    */
    public int robII(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        // Case 1: exclude last house
        int case1 = robLinear(nums, 0, n - 2);
        // Case 2: exclude first house
        int case2 = robLinear(nums, 1, n - 1);
        return Math.max(case1, case2);
    }
    private int robLinear(int[] nums, int start, int end) {
        int prev2 = 0, prev1 = 0;
        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
    /*
    - Time Complexity: O(\min (m,n))
    - Space Complexity: O(1)
    - ✅ Fastest approach for large grids.
    */
    public int uniquePaths(int m, int n) {
        long res = 1;
        int total = m + n - 2;
        int r = Math.min(m-1, n-1); // choose smaller for efficiency
        for (int i = 1; i <= r; i++) 
            res = res * (total - r + i) / i;
        return (int) res;
    }
}
