package com.infomancers.collections.yield;

import java.util.Iterator;

/**
 * Copyright (c) 2007, Aviad Ben Dov
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 3. Neither the name of Infomancers, Ltd. nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */


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
