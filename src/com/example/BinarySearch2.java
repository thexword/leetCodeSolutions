package com.example;

/**
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * 如果数组中不存在目标值 target，返回[-1, -1]。
 */
public class BinarySearch2 {
    public int[] searchRange(int[] nums, int target) {
        if (nums == null) {
            return new int[]{-1, -1};
        }

        int left = searchLeft(nums, target);

        if (left == -1) {
            return new int[]{-1, -1};
        }

        int right = searchRight(nums, target);
        return new int[] {left, right};
    }

    public int searchLeft(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        boolean found = false;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            int temp = nums[mid];

            if (target == temp) {
                found = true;
                right = mid - 1;
            } else if (target < temp) {
                right = mid - 1;
            } else if (target > temp) {
                left = mid + 1;
            }
        }

        if (found) {
            return left;
        } else {
            return -1;
        }
    }

    public int searchRight(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        boolean found = false;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            int temp = nums[mid];

            if (target == temp) {
                found = true;
                left = mid + 1;
            } else if (target < temp) {
                right = mid - 1;
            } else if (target > temp) {
                left = mid + 1;
            }
        }

        if (found) {
            return right;
        } else {
            return -1;
        }
    }
}
