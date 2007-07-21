package com.infomancers.collections.iterators;

import com.infomancers.collections.util.Predicate;
import com.infomancers.collections.util.Transformation;
import com.infomancers.collections.yield.Yielder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
 * Utility class to create commonly used iterators.
 */
public final class Iterators {

    /**
     * Used to create an iterable instance which yields only items answering
     * a boolean query.
     *
     * @param iterable The original iterable.
     * @param filter   The predicate to use to filter items of the original iterable.
     * @return A filtered iterable instance.
     */
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

    /**
     * Used to create an iterable instance which yields transformed items from an
     * original iterable instance.
     *
     * @param iterable       The original iterable instance.
     * @param transformation The transformation used on each element of the iterable instance.
     * @return An iterable containing transformed items.
     */
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

    /**
     * Used when a certain type T contains an iterable of items of type K.
     * <p/>
     * This iterable will iterate all contained K items within all T items returned
     * by the original iterable instance.
     *
     * @param iterable       The original iterable instance.
     * @param transformation A transformation which returns the collection of K items
     *                       contained within the T item.
     * @return An iterable of all K items referenced by the T items.
     */
    public static <T, K> Iterable<K> deepenIterable(final Iterable<T> iterable,
                                                    final Transformation<T, Iterable<K>> transformation) {
        return new Yielder<K>() {
            @Override
            protected void yieldNextCore() {
                for (T item : iterable) {
                    for (K res : transformation.transform(item)) {
                        yieldReturn(res);
                    }
                }
            }
        };
    }

    /**
     * Used to get a List item from an iterable.
     * <p/>
     * This List is detached from the Iterable - Changes
     * to the items returned by the Iterable do not change
     * the List items, and vice versa.
     * <p/>
     * If the iterable is a collection, an ArrayList is created
     * with the ArrayList(Collection) method. Otherwise, a
     * LinkedList is created and all elements are added one
     * by one.
     *
     * @param iterable The iterable to convert to a List.
     * @return A List containing all items in the iterable.
     */
    public static <T> List<T> asList(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            Collection<T> collection = (Collection<T>) iterable;
            return new ArrayList<T>(collection);
        } else {
            LinkedList<T> list = new LinkedList<T>();
            for (T item : iterable) {
                list.add(item);
            }

            return list;
        }
    }
}
