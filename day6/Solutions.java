package day6;

import java.util.Arrays;

public class Solutions {
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
    public static int robII(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        // Case 1: exclude last house
        int case1 = robLinear(nums, 0, n - 2);
        // Case 2: exclude first house
        int case2 = robLinear(nums, 1, n - 1);
        return Math.max(case1, case2);
    }
    private static int robLinear(int[] nums, int start, int end) {
        int prev2 = 0, prev1 = 0;
        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
    // O(m.n) time, O(n) space optimized
    public static int longestCommonSubsequence(String sample, String dna) {
        int m = sample.length(), n = dna.length();
        int[] dp = new int[n+1];
        for (int i = 1; i <= m; i++) {
            int prevDiagonal = 0; // dp[j-1] from previous row
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (sample.charAt(i-1) == dna.charAt(j-1))
                    dp[j] = prevDiagonal + 1;
                else
                    dp[j] = Math.max(dp[j], dp[j-1]);
                prevDiagonal = temp;
            }
        }
        return dp[n];
    }
    public static int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];

        // Base case: single characters
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // Fill for substrings of length 2..n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + (len == 2 ? 0 : dp[i+1][j-1]);
                } else {
                    dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
                }
            }
        }

        return dp[0][n-1];
    }
    public static int longestPalindromeSubseqVia1DP(String s) {
        int n = s.length();
        char[] arr = s.toCharArray();   // faster than repeated charAt
        int[] dp = new int[n];          // single array for space optimization
        // Iterate backwards for i (start of substring)
        for (int i = n - 1; i >= 0; i--) {
            int prevDiagonal = 0;       // holds dp[j-1] from previous row
            dp[i] = 1;                  // base case: single char is palindrome
            for (int j = i + 1; j < n; j++) {
                int temp = dp[j];       // store current dp[j] before overwriting
                if (arr[i] == arr[j]) dp[j] = prevDiagonal + 2;
                else dp[j] = Math.max(dp[j], dp[j - 1]);
                prevDiagonal = temp;    // update for next iteration
            }
        }
        return dp[n - 1];    
    }
    // optimal only in terms of leetcode's memory and runtime beats
    public static int lengthOfLISViaGreedyWithBinSearch(int[] nums) {
        if (nums.length <= 1) return nums.length;
        int[] sub = new int[nums.length];
        int size = 0;
        for (int num : nums) {
            int i = Arrays.binarySearch(sub, 0, size, num);
            System.out.println("Found "+num+" @ "+i);
            if (i < 0) i = -(i + 1);
            sub[i] = num;
            System.out.println(Arrays.toString(sub));
            if (i == size) size++;
        }
        return size;
    }
    public static void main(String[] args) {
        System.out.println(lengthOfLISViaGreedyWithBinSearch(new int[]{10,9,2,5,3,7,101,18}));
    }
    // this is standard DP way however not optimal interms of 
    // leetcode memory and runtime beats
    public int lengthOfLISViaDP(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) 
                if (nums[i] > nums[j]) dp[i] = Math.max(dp[i], dp[j] + 1);
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }
    // Edit distnace with leetcodes best runtime and memory beats
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        char[] w1 = word1.toCharArray();
        char[] w2 = word2.toCharArray();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        // Base case: converting empty word1 to word2
        for (int j = 0; j <= n; j++) prev[j] = j;
        for (int i = 1; i <= m; i++) {
            curr[0] = i; // converting word1[0..i-1] to empty word2
            for (int j = 1; j <= n; j++) {
                if (w1[i - 1] == w2[j - 1]) curr[j] = prev[j - 1];
                else
                    curr[j] = 1 + Math.min(prev[j - 1], Math.min(prev[j], curr[j - 1]));
            }
            // swap rows
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        return prev[n];
    }
    // DP Way of LVP
    public int longestValidParenthesesViaDP(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int maxLen = 0;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(')
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                else if (i - dp[i - 1] - 1 >= 0 && s.charAt(i - dp[i - 1] - 1) == '(')
                    dp[i] = dp[i - 1] + 2 + (i - dp[i - 1] >= 2 ? dp[i - dp[i - 1] - 2] : 0);
                maxLen = Math.max(maxLen, dp[i]);
            }
        }
        return maxLen;
    }
    // Two pass scan for LVP
    public int longestValidParenthesesViaTwoPassScan(String s) {
        int maxLen = 0;
        int left = 0, right = 0;
        char[] arr = s.toCharArray();
        // Left to right scan
        for (char c : arr) {
            if (c == '(') left++;
            else right++;
            if (left == right) maxLen = Math.max(maxLen, 2 * right);
            else if (right > left) left = right = 0;
        }
        // Right to left scan
        left = right = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == '(') left++;
            else right++;
            if (left == right) maxLen = Math.max(maxLen, 2 * left);
            else if (left > right) left = right = 0;
        }
        return maxLen;
    }
}
