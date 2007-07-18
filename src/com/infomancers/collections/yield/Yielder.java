package com.infomancers.collections.yield;

import java.util.Iterator;

/**
 * Abstract class from which all yielders extend.
 * <p/>
 * When a sub-class implements the yieldNextCore method,
 * during runtime it will be enhanced to the following:
 * <p/>
 * 1. All local variables within the yieldNextCore method
 * will be promoted to class member fields.
 * 2. A new member, "state" of type byte, will be added.
 * 3. Each call to yieldReturn will have the following code
 * appended to after it:
 * <code>
 * state = [number];
 * return;
 * Label[number]:
 * </code>
 * 4. Each call to yieldBreak will have a <code>return</code>
 * statement appended to it.
 * 5. At the beginning of the method, a switch will be added
 * containing a number of case branches equal to the number
 * of yieldReturn calls, such as the following code:
 * <code>
 * switch (state) {
 * case [number]: goto Label[number];
 * ..
 * }
 * </code>
 */
public abstract class Yielder<T> implements Iterable<T> {

    /**
     * Implemented by the developer to yield elements of
     * type T back to the caller.
     * <p/>
     * A yieldReturn call will set the next element returned
     * by the iterator's <code>next</code> call. A yieldBreak
     * call will mark that there are no more elements in the
     * iteration.
     * <p/>
     * If the method reaches its end without any
     * yielding, it is considered as if it had implicitly called
     * yieldBreak.
     *
     * @see #yieldBreak()
     * @see #yieldReturn(Object)
     */
    protected abstract void yieldNextCore();

    private void yieldNext() {
        hasNextItem = false;
        yieldNextCore();
    }

    private T nextItem = null;
    private boolean hasNextItem = false;

    /**
     * Sets the next element returned by the iterator's
     * <code>next</code> call, and marks the result
     * of <code>hasNext</code> to true.
     *
     * @param item The next element returned.
     */
    protected final void yieldReturn(T item) {
        hasNextItem = true;
        nextItem = item;
    }

    /**
     * Sets the element returned by the iterator's
     * <code>next</code> call to <code>null</code> and marks
     * the result of <code>hasNext</code> to false.
     */
    protected final void yieldBreak() {
        hasNextItem = false;
    }


    /**
     * Returns an iterator which goes through all
     * elements returned using the <code>yieldReturn</code>
     * method during a <code>yieldNextCore</code> implementation.
     *
     * @return An iterator for all yielded items of type T.
     */
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
