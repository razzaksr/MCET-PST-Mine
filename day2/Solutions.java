package day2;

import java.util.Deque;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class Solutions {
    // Function to find the maximum sum of subarray of size k(fixed window)
    public static int maximumSumSubarray(int k, int[] arr) {
        int n = arr.length;
        int sum = 0;
        // Step 1: Compute sum of first k elements
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        int maxSum = sum;
        // Step 2: Slide the window
        for (int i = k; i < n; i++) {
            sum += arr[i] - arr[i - k]; // add new element, remove old
            maxSum = Math.max(maxSum, sum);
        }
        return maxSum;
    }
    /*
    🧠 Stepwise Explanation
    - Initialize sum of the first k elements.
    - Set maxSum = sum initially.
    - Slide the window one element at a time:
    - Add the next element (arr[i]).
    - Remove the first element of the previous window (arr[i-k]).
    - Update maxSum.
    - Return maxSum.

    🔍 Example Walkthrough
    Input: arr = [100, 200, 300, 400], k = 2
    - First window: 100 + 200 = 300 → maxSum = 300
    - Slide: 200 + 300 = 500 → maxSum = 500
    - Slide: 300 + 400 = 700 → maxSum = 700
    Output: 700

    ✅ This solution runs in O(N) time and uses O(1) space.
    */
    
    // variable window
    public static int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            // If character already seen, move left pointer
            if (map.containsKey(c)) left = Math.max(left, map.get(c) + 1);
            // Update character's latest index
            map.put(c, right);
            // Update max length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
    /*
    lengthOfLongestSubstring:
    🧠 Stepwise Explanation
    - Two pointers:
    - left marks the start of the current window.
    - right expands the window by iterating through the string.
    - HashMap:
    - Stores the most recent index of each character.
    - If a duplicate is found, move left to one position after the last occurrence.
    - Update max length at each step:
    - maxLen = max(maxLen, right - left + 1)

    🔍 Example Walkthrough
    Input: "abcabcbb"
    | Step | Right | Char | Left | Window | MaxLen |
    |------|-------|------|------|--------|--------|
    | 0    | 0     | a    | 0    | "a"    | 1      |
    | 1    | 1     | b    | 0    | "ab"   | 2      |
    | 2    | 2     | c    | 0    | "abc"  | 3      |
    | 3    | 3     | a    | 1    | "bca"  | 3      |
    | 4    | 4     | b    | 2    | "cab"  | 3      |
    | 5    | 5     | c    | 3    | "abc"  | 3      |
    | 6    | 6     | b    | 5    | "cb"   | 3      |
    | 7    | 7     | b    | 7    | "b"    | 3      |

    Final Answer: **3** (substring `"abc"`)
    ✅ This solution runs in O(n) time and uses O(min(n, charset)) space.
    */
    
    // variable window
    public static String minWindow(String s, String t) {
        if (s.length() == 0 || t.length() == 0) return "";
        // Frequency map for characters in t
        HashMap<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray())
            need.put(c, need.getOrDefault(c, 0) + 1);
        // Sliding window variables
        HashMap<Character, Integer> window = new HashMap<>();
        int have = 0, needCount = need.size();
        int left = 0, minLen = Integer.MAX_VALUE;
        int start = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window.put(c, window.getOrDefault(c, 0) + 1);
            if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) 
                have++;
            // Shrink window when all required chars are present
            while (have == needCount) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }
                char leftChar = s.charAt(left);
                window.put(leftChar, window.get(leftChar) - 1);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) 
                    have--;
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }
    /*
    Minimum Window Substring:
    🧠 Stepwise Explanation
    - Build frequency map of characters in t.
    - Expand window with right pointer:
    - Add characters to window map.
    - If a character’s count matches the requirement, increment have.
    - Shrink window with left pointer when all required characters are present (have == needCount).
    - Update minimum length if current window is smaller.
    - Remove left character from window and adjust have if needed.
    - Return the smallest valid substring.
    🔍 Example Walkthrough
    Input: s = "ADOBECODEBANC", t = "ABC"
    - Expand until window contains "ABC" → "ADOBEC"
    - Shrink to "BEC" → still valid, length = 3
    - Continue scanning → smallest valid window = "BANC"
    Output: "BANC"
    ✅ This solution runs in O(s + t) time and uses O(alphabet) space.
    */

    // fixed window
    public static int totalFruit(int[] fruits) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < fruits.length; right++) {
            // Add current fruit to map
            map.put(fruits[right], map.getOrDefault(fruits[right], 0) + 1);
            // If more than 2 distinct fruits, shrink window
            while (map.size() > 2) {
                map.put(fruits[left], map.get(fruits[left]) - 1);
                if (map.get(fruits[left]) == 0) map.remove(fruits[left]);
                left++;
            }
            // Update max length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
    /*
    fruits and buckets:
    🧠 Stepwise Explanation
    - Two pointers:
    - left marks the start of the window.
    - right expands the window by iterating through the array.
    - HashMap:
    - Tracks counts of each fruit type in the current window.
    - If more than 2 distinct fruits are present, shrink the window from the left until only 2 remain.
    - Update max length:
    - At each step, compute right - left + 1.
    - Keep track of the maximum.
    🔍 Example Walkthrough
    Input: fruits = [1,2,1]
    - right=0 → add 1 → window = [1] → maxLen = 1
    - right=1 → add 2 → window = [1,2] → maxLen = 2
    - right=2 → add 1 → window = [1,2,1] → maxLen = 3
    Output: 3
    ✅ This solution runs in O(n) time and uses O(1) space (since at most 2 fruit types are tracked).
    */

    // fixed window
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) return new int[0];
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new LinkedList<>(); // stores indices
        for (int i = 0; i < n; i++) {
            // Remove indices out of current window
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) deque.pollFirst();
            // Remove smaller values from the back
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) deque.pollLast();
            // Add current index
            deque.offerLast(i);
            // Record max for current window
            if (i >= k - 1) result[i - k + 1] = nums[deque.peekFirst()];
        }
        return result;
    }
    /*
    maxSlidingWindow:
    🧠 Stepwise Explanation
    - Deque stores indices of elements in decreasing order of values.
    - For each index i:
    - Remove indices that are out of the current window (i - k).
    - Remove indices from the back if their values are smaller than nums[i].
    - Add current index.
    - If window size ≥ k, record the maximum (front of deque).
    - Continue until the end of the array.

    🔍 Example Walkthrough
    Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
    Windows and maximums:
    - [1,3,-1] → max = 3
    - [3,-1,-3] → max = 3
    - [-1,-3,5] → max = 5
    - [-3,5,3] → max = 5
    - [5,3,6] → max = 6
    - [3,6,7] → max = 7
    Output: [3,3,5,5,6,7]
    ✅ This solution runs in O(n) time and uses O(k) space, which is optimal.
    */
    public static int subarraySum(int[] nums, int k) {
        // Map to store prefix sum frequencies
        HashMap<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1); // base case: sum = 0 occurs once
        int sum = 0;
        int count = 0;
        for (int num : nums) {
            sum += num;
            // If (sum - k) exists, it means there's a subarray ending here with sum = k
            if (prefixCount.containsKey(sum - k)) count += prefixCount.get(sum - k);
            // Update prefix sum frequency
            prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
    /*
    🧠 Stepwise Explanation
    - Prefix Sum Idea:
    - Keep a running sum of elements.
    - If sum - k has been seen before, then a subarray ending at current index has sum = k.
    - HashMap:
    - Stores how many times each prefix sum has occurred.
    - Initialize with {0:1} to handle cases where the subarray starts at index 0.
    - Iterate through array:
    - Update running sum.
    - Check if (sum - k) exists in map → add its frequency to count.
    - Update map with current sum.
    🔍 Example Walkthrough
        Input: nums = [1,2,3], k = 3
        - Start: prefixCount = {0:1}, sum = 0, count = 0
        - num = 1 → sum = 1 → sum-k = -2 (not in map) → prefixCount = {0:1,1:1}
        - num = 2 → sum = 3 → sum-k = 0 (exists, freq=1) → count = 1 → prefixCount = {0:1,1:1,3:1}
        - num = 3 → sum = 6 → sum-k = 3 (exists, freq=1) → count = 2 → prefixCount = {0:1,1:1,3:1,6:1}
        Output: 2 (subarrays [1,2] and [3])

        ✅ This solution runs in O(n) time and uses O(n) space. It’s optimal compared to the naive O(n²) approach.
    */
    
    // Brute Force Approach (O(n²)) Check each element’s frequency by scanning the array.
    public static int majorityElementViaBrute(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) 
                if (nums[j] == nums[i]) count++;
            if (count > n / 2) return nums[i];
        }
        return -1; // should never happen as majority element always exists
    }
    // HashMap Counting (O(n) time, O(n) space) Count frequencies using a map.
    public static int majorityElementViaHT(int[] nums) {
        Hashtable<Integer, Integer> map = new Hashtable<>();
        int n = nums.length;
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            if (map.get(num) > n / 2) return num;
        }
        return -1;
    }
    //  Boyer–Moore Voting Algorithm (O(n) time, O(1) space) Optimal solution: cancel out pairs of different elements.
    public static int majorityElementViaOptimal(int[] nums) {
        int candidate = 0, count = 0;
        for (int num : nums) {
            if (count == 0) candidate = num;
            count += (num == candidate) ? 1 : -1;
        }
        return candidate;
    }
    public static void main(String[] args) {
        // Test maximum sum subarray
        int[] arr = {100, 200, 300, 400};
        int k = 2;
        int maxSum = maximumSumSubarray(k, arr);
        System.out.println("Maximum sum of subarray of size " + k + " is: " + maxSum);
        // Test length of longest substring without repeating characters
        String s = "abcabcbb";
        int length = lengthOfLongestSubstring(s);
        System.out.println("Length of longest substring without repeating characters in \"" + s + "\" is: " + length);
        // Test minimum window substring method
        String strS = "ADOBECODEBANC";
        String minWindow = minWindow(strS, "ABC");
        System.out.println("Minimum window substring: \"" + minWindow + "\"");
        // Test total fruit method
        int[] fruits = {1, 2, 1};
        int maxFruits = totalFruit(fruits);
        System.out.println("Maximum number of fruits that can be picked: " + maxFruits);
        // Test max sliding window method
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        k = 3;
        int[] maxSliding = maxSlidingWindow(nums, k);
        System.out.print("Max sliding window of size " + k + ": ");
        for (int num : maxSliding) {
            System.out.print(num + " ");
        }
        System.out.println(); // New line after printing sliding window
        // Test subarray sum equals k
        arr = new int[]{1, 2, 3};
        int target = 3;
        int count = subarraySum(arr, target);
        System.out.println("Number of subarrays with sum " + target + " is: " + count);
        // Test majority element methods
        arr = new int[]{3, 2, 3};
        System.out.println("Majority element (Brute Force): " + majorityElementViaBrute(arr));
        System.out.println("Majority element (HashMap): " + majorityElementViaHT(arr));
        System.out.println("Majority element (Optimal): " + majorityElementViaOptimal(arr));
    }
}
/*







*/
