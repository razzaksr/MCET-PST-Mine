package day3;

import java.util.Arrays;

public class Solutions {
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // avoid overflow
            if (nums[mid] == target) return mid; // found target
            else if (nums[mid] < target) left = mid + 1; // search right half
            else right = mid - 1; // search left half
        }
        return -1; // not found
    }
    public static int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            // If mid element is greater than right element,
            // minimum must be in the right half
            if (nums[mid] > nums[right]) left = mid + 1;
            // Otherwise, minimum is in the left half (including mid)
            else right = mid;
        }
        // At the end, left == right, pointing to the minimum element
        return nums[left];
    }
    public static int searchInRotatedSorted(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid; // found
            // Check if left half is sorted
            if (nums[left] <= nums[mid]) {
                if (target >= nums[left] && target < nums[mid]) 
                    right = mid - 1; // target in left half
                else left = mid + 1;  // target in right half
            } else {
                // Right half is sorted
                if (target > nums[mid] && target <= nums[right]) 
                    left = mid + 1;  // target in right half
                else right = mid - 1; // target in left half
            }
        }
        return -1; // not found
    }
    /*
    🧠 Stepwise Explanation
    - Binary search loop:
    - Compute mid.
    - If nums[mid] == target, return mid.
    - Determine which half is sorted:
    - If nums[left] <= nums[mid], left half is sorted.
    - Else, right half is sorted.
    - Check if target lies in the sorted half:
    - If yes, move pointers accordingly.
    - If not, search the other half.
    - Continue until found or window closes.
    🔍 Example Walkthrough
    Input: nums = [4,5,6,7,0,1,2], target = 0
    - left=0, right=6 → mid=3 → nums[3]=7 → left half sorted, target not in [4..7] → left=4
    - left=4, right=6 → mid=5 → nums[5]=1 → right half sorted, target in [0..2] → right=4
    - left=4, right=4 → mid=4 → nums[4]=0 → found → return 4
    Output: 4
    ✅ This iterative solution runs in O(log n) time and uses O(1) space.
    */
    // koku banana
    public static int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = getMax(piles); // maximum pile size
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canEatAll(piles, h, mid)) right = mid; // try smaller speed
            else left = mid + 1; // need larger speed
        }
        return left;
    }
    private static int getMax(int[] piles) {
        int max = 0;
        max = Arrays.stream(piles).max().orElse(0);
        return max;
    }
    private static boolean canEatAll(int[] piles, int h, int speed) {
        long hours = 0;
        for (int pile : piles) 
            // ceil(pile / speed) → (pile + speed - 1) / speed
            hours += (pile + speed - 1) / speed;
        return hours <= h;
    }  
    // koku banana
    
    public boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int left = 0, right = rows * cols - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            // Map mid back to 2D indices
            int row = mid / cols;
            int col = mid % cols;
            if (matrix[row][col] == target) return true;
            else if (matrix[row][col] < target) left = mid + 1;
            else right = mid - 1;
        }
        return false;
    }
    // rotate array
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n; // handle cases where k > n
        // Step 1: Reverse entire array
        reverse(nums, 0, n - 1);
        // Step 2: Reverse first k elements
        reverse(nums, 0, k - 1);
        // Step 3: Reverse remaining n-k elements
        reverse(nums, k, n - 1);
    }
    private void reverse(int[] nums, int left, int right) {
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }
    // rotate array
    /*
    🧠 Stepwise Explanation
    - Normalize k with k % n.
    - Reverse the entire array.
    - Reverse the first k elements.
    - Reverse the remaining n-k elements.
    This achieves rotation in-place with O(n) time and O(1) space.

    🔍 Example Walkthrough
    Input: nums = [1,2,3,4,5,6,7], k = 3
    - Reverse all → [7,6,5,4,3,2,1]
    - Reverse first 3 → [5,6,7,4,3,2,1]
    - Reverse last 4 → [5,6,7,1,2,3,4]
    Output: [5,6,7,1,2,3,4]
    Reversal Method time - O(n), space - O(1)
    */

    public long minimumTime(int[] time, int totalTrips) {
        long left = 1;
        long right = Long.MAX_VALUE; // safe upper bound
        long ans = right;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            if (canComplete(time, totalTrips, mid)) {
                ans = mid;
                right = mid - 1; // try smaller time
            } else {
                left = mid + 1; // need more time
            }
        }
        return ans;
    }
    private static boolean canComplete(int[] time, int totalTrips, long givenTime) {
        long trips = 0;
        for (int t : time) {
            trips += givenTime / t; // trips each bus can finish
            if (trips >= totalTrips) return true; // early exit
        }
        return trips >= totalTrips;
    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    public ListNode insertionSortList(ListNode head) {
        if (head == null) return null;
        // Dummy node to simplify insertions
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode curr = head, prev = dummy;
        while (curr != null) {
            if (curr.next != null && curr.next.val < curr.val) {
                // Find position to insert curr.next
                ListNode toInsert = curr.next;
                ListNode pos = dummy;
                // Find where to insert toInsert
                while (pos.next.val < toInsert.val) pos = pos.next;
                // Insert toInsert after pos
                curr.next = toInsert.next;
                toInsert.next = pos.next;
                pos.next = toInsert;
            } else curr = curr.next;
        }
        return dummy.next;
    }
    /*
    🧠 Stepwise Explanation
    - Use a dummy node to simplify insertion at the head.
    - Traverse the list with curr.
    - If curr.next is smaller than curr, 
            - it’s out of order → find the correct position starting from dummy.
            - Insert curr.next into its correct position.
    - Otherwise, move curr forward.
    - Continue until the list is sorted.

    🔍 Example Walkthrough
    Input: 4 -> 2 -> 1 -> 3
    - Start with dummy → 0 -> 4 -> 2 -> 1 -> 3
    - 2 is smaller than 4 → insert before 4 → 0 -> 2 -> 4 -> 1 -> 3
    - 1 is smaller than 4 → insert before 2 → 0 -> 1 -> 2 -> 4 -> 3
    - 3 is smaller than 4 → insert before 4 → 0 -> 1 -> 2 -> 3 -> 4

    Output: 1 -> 2 -> 3 -> 4
    🛠 Complexity
    - Time Complexity: O(n²) in worst case (like array insertion sort).
    - Space Complexity: O(1) (in-place sorting).
    */
    public static void main(String[] args) {
        
    }
}
