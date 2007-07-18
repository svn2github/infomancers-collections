package com.infomancers.tests;

import com.infomancers.collections.yield.Yielder;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 18, 2007
 * Time: 10:44:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class YielderTests {
    public static void main(String[] args) {
        final int[] arr = new int[] { 1, 3, 5, 7 };

        Iterable<Integer> it = new Yielder<Integer>() {

            @Override
            protected void yieldNextCore() {
                for (int i : arr) {
                    yieldReturn(i);
                }
            }
        };

        for (Integer integer : it) {
            System.out.println("int = " + integer);
        }
    }

}
