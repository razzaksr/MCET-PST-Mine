package day1;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Solutions {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int max = 0;

        while (left < right) {
            int width = right - left;
            int area = Math.min(height[left], height[right]) * width;
            max = Math.max(max, area);

            // Move the pointer at the shorter line inward
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return max;
    }
    public int trap(int[] height) {
        if (height == null || height.length == 0) return 0;
        int n = height.length;
        int left = 0, right = n - 1, leftMax = 0, rightMax = 0, water = 0;
        // Two pointer approach
        while (left < right) {
            if (height[left] < height[right]) {
                // Process left side
                if (height[left] >= leftMax) leftMax = height[left];
                else water += leftMax - height[left];
                left++;
            } else {
                // Process right side
                if (height[right] >= rightMax) rightMax = height[right];
                else water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }
    public int largestRectangleArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            // Treat end as height = 0 to flush stack
            int h = (i == n ? 0 : heights[i]);

            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            int value = map.get(s.charAt(i));

            // If next symbol is larger, subtract current value
            if (i + 1 < s.length() && value < map.get(s.charAt(i + 1))) {
                total -= value;
            } else {
                total += value;
            }
        }
        return total;
    }
    public String intToRoman(int num) {
        // Define values and their corresponding Roman symbols
        int[] values = {1000, 900, 500, 400, 100, 90, 
                        50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", 
                            "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder sb = new StringBuilder();

        // Greedy subtraction
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }
    public String convert(String s, int numRows) {
        if (numRows == 1 || s.length() <= numRows) {
            return s;
        }

        // Create an array of StringBuilder for each row
        StringBuilder[] rows = new StringBuilder[numRows];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new StringBuilder();
        }

        int currentRow = 0;
        boolean goingDown = false;

        // Traverse characters
        for (char c : s.toCharArray()) {
            rows[currentRow].append(c);

            // Change direction at top or bottom
            if (currentRow == 0 || currentRow == numRows - 1) {
                goingDown = !goingDown;
            }

            currentRow += goingDown ? 1 : -1;
        }

        // Merge all rows
        StringBuilder result = new StringBuilder();
        for (StringBuilder row : rows) {
            result.append(row);
        }
        return result.toString();
    }
    /* 
        container in most water
        trapping rain water problem
        Longest histogram rectangle
        roman to integer
        Integer to roman
        Task: ZigZagConversion
    */
    public static void main(String[] args) {
        Solutions solutions = new Solutions();
        int[] height = {1,8,6,2,5,4,8,3,7};
        System.out.println("Max Area: " + solutions.maxArea(height));

        int[] trapHeight = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("Trapped Water: " + solutions.trap(trapHeight));

        int[] histogram = {2,1,5,6,2,3};
        System.out.println("Largest Rectangle Area: " + solutions.largestRectangleArea(histogram));

        String romanNumeral = "MCMXCIV";
        System.out.println("Roman to Integer: " + solutions.romanToInt(romanNumeral));

        int integerNumber = 1994;
        System.out.println("Integer to Roman: " + solutions.intToRoman(integerNumber));
        String zigzagString = "PAYPALISHIRING";
        int numRows = 3;
        System.out.println("ZigZag Conversion: " + solutions.convert(zigzagString, numRows));
    }
}
