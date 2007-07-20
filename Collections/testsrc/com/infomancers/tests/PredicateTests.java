package com.infomancers.tests;

import com.infomancers.collections.iterators.Iterators;
import com.infomancers.collections.util.Predicate;
import com.infomancers.collections.util.PredicateCollection;
import com.infomancers.collections.util.PredicateNegation;
import junit.framework.Assert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 5:37:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class PredicateTests {

    public void collectionAnd() {
        Predicate<Integer> pred1 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item > 0;
            }
        };

        Predicate<Integer> pred2 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item < 100;
            }
        };

        PredicateCollection<Integer> predColl = new PredicateCollection<Integer>(PredicateCollection.Type.AND);
        predColl.add(pred1);
        predColl.add(pred2);

        Assert.assertTrue("80", predColl.evaluate(80));
        Assert.assertTrue("23", predColl.evaluate(23));
        Assert.assertFalse("-1", predColl.evaluate(-1));
        Assert.assertFalse("101", predColl.evaluate(101));
        Assert.assertFalse("100", predColl.evaluate(100));
        Assert.assertTrue("99", predColl.evaluate(99));
        Assert.assertTrue("1", predColl.evaluate(1));
        Assert.assertFalse("0", predColl.evaluate(0));
    }

    public void collectionOr() {
        Predicate<Integer> pred1 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item > 0;
            }
        };

        Predicate<Integer> pred2 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item < 100;
            }
        };

        PredicateCollection<Integer> predColl = new PredicateCollection<Integer>(PredicateCollection.Type.OR);
        predColl.add(pred1);
        predColl.add(pred2);

        Assert.assertTrue("80", predColl.evaluate(80));
        Assert.assertTrue("23", predColl.evaluate(23));
        Assert.assertTrue("-1", predColl.evaluate(-1));
        Assert.assertTrue("101", predColl.evaluate(101));
        Assert.assertTrue("100", predColl.evaluate(100));
        Assert.assertTrue("99", predColl.evaluate(99));
        Assert.assertTrue("1", predColl.evaluate(1));
        Assert.assertTrue("0", predColl.evaluate(0));
    }

    public void collectionNotRange() {
        Predicate<Integer> pred1 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item > 0;
            }
        };

        Predicate<Integer> pred2 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item < 100;
            }
        };

        PredicateCollection<Integer> predColl = new PredicateCollection<Integer>(PredicateCollection.Type.OR);
        predColl.add(new PredicateNegation(pred1));
        predColl.add(new PredicateNegation(pred2));

        Assert.assertFalse("80", predColl.evaluate(80));
        Assert.assertFalse("23", predColl.evaluate(23));
        Assert.assertTrue("-1", predColl.evaluate(-1));
        Assert.assertFalse("101", predColl.evaluate(101));
        Assert.assertTrue("100", predColl.evaluate(100));
        Assert.assertFalse("99", predColl.evaluate(99));
        Assert.assertFalse("1", predColl.evaluate(1));
        Assert.assertTrue("0", predColl.evaluate(0));
    }

    public void collectionAndIteration() {
        Predicate<Integer> pred1 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item > 0;
            }
        };

        Predicate<Integer> pred2 = new Predicate<Integer>() {
            public boolean evaluate(Integer item) {
                return item < 100;
            }
        };

        PredicateCollection<Integer> predColl = new PredicateCollection<Integer>(PredicateCollection.Type.AND);
        predColl.add(pred1);
        predColl.add(pred2);

        List<Integer> list = Arrays.asList(80, 23, -1, 101, 100, 99, 1, 0);
        Iterator<Integer> it = Iterators.filteredIterable(list, predColl).iterator();

        Assert.assertEquals(80, (int) it.next());
        Assert.assertEquals(23, (int) it.next());
        Assert.assertEquals(99, (int) it.next());
        Assert.assertEquals(1, (int) it.next());

        Assert.assertFalse("Too many elements", it.hasNext());
    }

}
