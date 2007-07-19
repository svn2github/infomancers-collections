package com.infomancers.tests;

import com.infomancers.collections.yield.Yielder;

/**
 * Some base tests.
 */
public class YielderTests {
    public static void main(String[] args) {
        final int[] arr = new int[]{1, 3, 5, 7};

        Iterable<Integer> it = new Yielder<Integer>() {

            @Override
            protected void yieldNextCore() {
                for (int i : arr) {
                    yieldReturn(i);
                }

                yieldBreak();
            }
        };

        for (Integer integer : it) {
            System.out.println("int = " + integer);
        }
    }

}
