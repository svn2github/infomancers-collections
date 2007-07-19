package com.infomancers.tests;

import com.infomancers.collections.yield.Yielder;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Some base tests.
 */
public class YielderTests {

    @Test
    public void array() {
        final int[] arr = new int[]{1, 3, 5, 7};

        Iterable<Integer> it = new Yielder<Integer>() {

            @Override
            protected void yieldNextCore() {
                for (int i : arr) {
                    yieldReturn(i);
                }
            }
        };

        int i = 0;
        for (Integer integer : it) {
            Assert.assertEquals("Bad value for index " + i, (int) integer, arr[i]);
            i++;
        }
    }
}
