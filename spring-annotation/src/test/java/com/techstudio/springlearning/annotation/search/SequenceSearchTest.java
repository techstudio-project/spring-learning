package com.techstudio.springlearning.annotation.search;

import org.junit.Test;

/**
 * @author lj
 * @since 2020/3/23
 */
public class SequenceSearchTest {

    private int[] arr = {1, 4, 6, 3, 4, 87, 9, 3, 1, 1, 222, 344, 9};

    public int searchIndex(int val) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            if (arr[i] == val) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void test() {
        int index = searchIndex(87);
        System.out.println("index = " + index);
    }

}
