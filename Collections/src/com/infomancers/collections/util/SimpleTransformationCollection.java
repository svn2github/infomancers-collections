package com.infomancers.collections.util;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 6:08:47 PM
 * To change this template use File | Settings | File Templates.
 */
public final class SimpleTransformationCollection<T> implements SimpleTransformation<T> {

    private Collection<SimpleTransformation<T>> transformations = null;

    public void add(SimpleTransformation<T> transformation) {
        if (transformations == null) {
            transformations = new LinkedList<SimpleTransformation<T>>();
        }

        transformations.add(transformation);
    }

    public void remove(SimpleTransformation<T> transformation) {
        if (transformations != null) {
            transformations.remove(transformation);
        }
    }

    /**
     * Transforms item of type T to another type T element
     * using a chain of simple transformations.
     *
     * @param item The item to transform.
     * @return The transformed item.
     */
    public T transform(T item) {
        T result = item;
        for (SimpleTransformation<T> transformation : transformations) {
            result = transformation.transform(result);
        }

        return result;
    }
}
