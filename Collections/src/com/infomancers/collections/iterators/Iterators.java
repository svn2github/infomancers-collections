package com.infomancers.collections.iterators;

import com.infomancers.collections.util.Predicate;
import com.infomancers.collections.util.Transformation;
import com.infomancers.collections.yield.Yielder;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 5:48:24 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Iterators {

    public static <T> Iterable<T> filteredIterable(final Iterable<T> iterable, final Predicate<T> filter) {
        return new Yielder<T>() {
            @Override
            protected void yieldNextCore() {
                for (T item : iterable) {
                    if (filter.evaluate(item)) {
                        yieldReturn(item);
                    }
                }
            }
        };
    }

    public static <T, K> Iterable<K> transformIterable(final Iterable<T> iterable, final Transformation<T, K> transformation) {
        return new Yielder<K>() {

            @Override
            protected void yieldNextCore() {
                for (T item : iterable) {
                    yieldReturn(transformation.transform(item));
                }
            }
        };
    }
}
