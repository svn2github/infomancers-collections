package com.infomancers.tests;

import com.infomancers.collections.yield.Yielder;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

        Assert.assertEquals("Iterated not over the entire array", i, arr.length);
    }

    @Test
    public void list() {
        final List<String> list = Arrays.asList("Yielding", "Is", "The", "Best", "Way", "To", "Write", "Iterators");

        Iterable<String> it = new Yielder<String>() {

            @Override
            protected void yieldNextCore() {
                for (String s : list) {
                    yieldReturn(s);
                }
            }
        };

        Iterator it1 = it.iterator();
        Iterator it2 = list.iterator();
        int i = 0;

        while (it1.hasNext() && it2.hasNext()) {
            Assert.assertEquals("Element " + i + " not equal!", it1.next(), it2.next());
            i++;
        }

        Assert.assertFalse("Yielder has too many elements", it1.hasNext());
        Assert.assertFalse("Yielder had too few elements", it2.hasNext());
    }

    @Test
    public void someStrings() {
        Iterator<String> it = new Yielder<String>() {
            @Override
            protected void yieldNextCore() {
                yieldReturn("This");
                yieldReturn("Is");
                yieldReturn("Great");
            }
        }.iterator();


        Assert.assertEquals("This", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("Is", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("Great", it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void someStringsWithBreak() {
        Iterator<String> it = new Yielder<String>() {
            @Override
            protected void yieldNextCore() {
                yieldReturn("Reach");
                yieldReturn("Here");
                yieldBreak();
                yieldReturn("Not");
                yieldReturn("Here");
            }
        }.iterator();

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("Reach", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("Here", it.next());
        Assert.assertFalse("Did not break", it.hasNext());
    }

    @Test
    public void stopConditionTest() {
        final int stop = 10;

        Iterator<Integer> it = new Yielder<Integer>() {

            @Override
            protected void yieldNextCore() {
                int i = 1;
                while (true) {
                    if (i == stop) {
                        yieldBreak();
                    }

                    if (checkPrime(i)) {
                        yieldReturn(i);
                    }

                    i++;
                }
            }

            private boolean checkPrime(int i) {
                switch (i) {
                    case 1:
                        return false;
                    case 2:
                        return true;
                }

                if (i % 2 == 0) {
                    return false;
                }

                for (int c = 3; c <= Math.sqrt(i); c += 2) {
                    if (i % c == 0) {
                        return false;
                    }
                }

                return true;
            }
        }.iterator();

        Assert.assertEquals(2, (int) it.next());
        Assert.assertEquals(3, (int) it.next());
        Assert.assertEquals(5, (int) it.next());
        Assert.assertEquals(7, (int) it.next());
        Assert.assertFalse("Too many elements in iterator", it.hasNext());
    }
}
