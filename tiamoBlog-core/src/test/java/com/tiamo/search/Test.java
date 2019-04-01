package com.tiamo.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wangjian
 * @Date: 2019-03-20 19:48:50
 */
public class Test {

    public static void main(String[] args) {
       /* int[] arr = new int[]{2,8,6,9,3,4,10};
        int target = 3;
        int low = 0; // 最小位置
        int high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low)/2;
            int value =  arr[mid];
            if (value > target) {
                high = mid - 1;
            } else if (value < target) {
                low = mid + 1;
            } else {
                System.out.println("所在位置" + mid);
            }
        }*/
        int[] indexNum = new int[] {2,5,6,8,8,8,10,22,35};
        /*int mid = bsearchInternally(indexNum, 30, 0, indexNum.length - 1);
        System.out.println(mid);*/
//        int mid2 = bsearchRepetitionInternally(indexNum, 33, 0, indexNum.length - 1);
//        System.out.println(mid2);
//        int mid2 = bsearchRepetitionGreaterInternally(indexNum, 3, 0, indexNum.length - 1);
//        System.out.println(mid2);

//        int mid3 = bsearchRepetitionLessInternally(indexNum, 2, 0, indexNum.length - 1);
//        System.out.println(mid3);

        int[] work = new int[] {4,5,6,1,2,3}; // 使用二分法求该数组中6所在的下标位置
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < work.length; i++) {
            map.put(work[i], i);
        }
        int[] ints = Arrays.stream(work).sorted().toArray();
        int high = ints.length - 1;
        int low = 0;
        int target = 6;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (ints[mid] > target) {
                high = mid - 1;
            } else if (ints[mid] < target) {
                low = mid + 1;
            } else {
                System.out.println(map.get(ints[mid]));
                return;
            }
        }
    }

    /**
     *
     * @param arr 有序无重复数组可用
     * @param taget 目标值
     * @param low  最小下标位
     * @param high 最高下标位
     * @return
     */
    public static int bsearchInternally(int[] arr, int taget, int low, int high) {
        int mid = low + (high - low) / 2;
        if (low > high) {
            return -1;
        } else if (arr[mid] > taget){
            return bsearchInternally(arr, taget, low, mid - 1);
        } else if (arr[mid] < taget){
            return  bsearchInternally(arr, taget, mid + 1, high);
        } else  {
            return mid;
        }
    }

    /**
     * 有重复值的从小到大的顺序数组，找出第一个与目标值相等的数值下标
     * @param arr
     * @param target
     * @param low
     * @param high
     * @return
     */
    public static int bsearchRepetitionInternally(int[] arr, int target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + (high - low) / 2 ;
        if (arr[mid] > target) {
            high = mid - 1;
            return  bsearchRepetitionInternally(arr, target, low, high);
        } else if (arr[mid] < target) {
            low = mid + 1;
            return  bsearchRepetitionInternally(arr, target, low, high);
        } else {
            if (mid == 0 || arr[mid - 1] != target) return mid;
            else {
                high = mid - 1;
                return  bsearchRepetitionInternally(arr, target, low, high);
            }
        }
    }

    /**
     * 有重复值的从小到大的顺序数组，找出第一个大于目标值的数值下标
     * @param arr
     * @param target
     * @param low
     * @param high
     * @return
     */
    public static int bsearchRepetitionGreaterInternally(int[] arr, int target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + (high - low) / 2 ;
        if (arr[mid] <= target) {
            return bsearchRepetitionGreaterInternally(arr, target, mid + 1, high);
        } else  {
            if (mid == 0 || arr[mid -1 ] <= target) {
//            if (mid == high || arr[mid + 1 ] <= target) { // 取大于目标值的最后一个数值的下标
                return mid;
            }  else {
//                return bsearchRepetitionGreaterInternally(arr, target, mid+1, high); // // 取大于目标值的最后一个数值的下标
                return bsearchRepetitionGreaterInternally(arr, target, low, mid - 1);
            }
        }
    }

    /**
     * 有重复值的从小到大的顺序数组，找出第一个小于等于目标值的数值下标
     * @param arr
     * @param target
     * @param low
     * @param high
     * @return
     */
    public static int bsearchRepetitionLessInternally(int[] arr, int target, int low, int high) {
        int mid = low + (high - low) / 2;
        if (low > high) {
            return -1;
        } else if (arr[mid] > target) {
            return bsearchRepetitionLessInternally(arr, target, low, mid - 1);
        } else {
            if (mid == 0 || arr[mid - 1] > target) {
                return mid;
            } else {
                return bsearchRepetitionLessInternally(arr, target, low, mid - 1);
            }
        }
    }
}
