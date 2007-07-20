package com.infomancers.tests;

import com.infomancers.collections.iterators.Iterators;
import com.infomancers.collections.util.SimpleTransformation;
import com.infomancers.collections.util.SimpleTransformationCollection;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 6:07:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransformationTests {

    @Test
    public void transformPlus1() {
        SimpleTransformation<Integer> plus1 = new SimpleTransformation<Integer>() {
            public Integer transform(Integer item) {
                return item + 1;
            }
        };

        SimpleTransformationCollection<Integer> coll = new SimpleTransformationCollection<Integer>();
        coll.add(plus1);
        coll.add(plus1);

        Assert.assertEquals(2, (int) coll.transform(0));
        Assert.assertEquals(102, (int) coll.transform(100));
        Assert.assertEquals(1, (int) coll.transform(-1));
    }

    @Test
    public void transformPlus1Iteration() {
        SimpleTransformation<Integer> plus1 = new SimpleTransformation<Integer>() {
            public Integer transform(Integer item) {
                return item + 1;
            }
        };

        SimpleTransformationCollection<Integer> coll = new SimpleTransformationCollection<Integer>();
        coll.add(plus1);
        coll.add(plus1);
        coll.add(plus1);

        List<Integer> list = Arrays.asList(20, 0, -1, 100, 13);
        Iterator<Integer> it = Iterators.transformIterable(list, coll).iterator();

        for (int a : list) {
            Assert.assertEquals(a + 3, (int) it.next());
        }
    }
}
