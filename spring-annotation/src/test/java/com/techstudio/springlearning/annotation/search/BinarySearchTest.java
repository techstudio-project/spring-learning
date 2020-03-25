package com.techstudio.springlearning.annotation.search;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author lj
 * @since 2020/3/23
 */
public class BinarySearchTest {

    private int[] arr = {1, 4, 6, 3, 4, 87, 9, 3, 1, 1, 222, 344, 9};

    public int searchIndex(int[] intArr, int val) {

        int low = 0;
        int high = arr.length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (val == arr[mid]) {
                return mid;
            }
            if (val < arr[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }

        }

        if (arr[low] == val) {
            return low;
        }

        return -1;
    }

    @Test
    public void test() {
        Arrays.sort(arr);
        int index = searchIndex(arr, 87);
        System.out.println("index = " + index);
    }

}
