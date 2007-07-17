package com.infomancers.collections.yield;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 12, 2007
 * Time: 12:12:54 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Yielder<T> implements Iterable<T> {

    protected abstract void yieldNextCore();

    private void yieldNext() {
        hasNextItem = false;
        yieldNextCore();
    }

    private T nextItem = null;
    private boolean hasNextItem = false;

    protected final void yieldReturn(T item) {
        hasNextItem = true;
        nextItem = item;
    }

    protected final void yieldBreak() {
        hasNextItem = false;
    }


    public Iterator<T> iterator() {
        yieldNext();

        return new Iterator<T>() {
            public boolean hasNext() {
                return hasNextItem;
            }

            public T next() {
                T result = nextItem;
                yieldNext();
                return result;
            }

            public void remove() {
                throw new UnsupportedOperationException("Unable to delete using this iterator");
            }
        };
    }
}
