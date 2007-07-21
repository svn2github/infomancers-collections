package com.infomancers.collections.util;

import java.util.Collection;
import java.util.LinkedList;

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
 * Used to evaluate an item against a collection of predicates.
 */
public final class PredicateCollection<T> implements Predicate<T> {
    public enum Type {
        OR(false),
        AND(true);
        private final boolean needsAll;

        Type(boolean needsAll) {
            this.needsAll = needsAll;
        }

        public boolean isNeedsAll() {
            return needsAll;
        }
    }

    private Collection<Predicate<T>> predicates;
    private Type type;


    public PredicateCollection(Type type) {
        this.type = type;
    }

    public void add(Predicate<T> pred) {
        if (predicates == null) {
            predicates = new LinkedList<Predicate<T>>();
        }

        predicates.add(pred);
    }

    public void remove(Predicate<T> pred) {
        if (predicates != null) {
            predicates.remove(pred);
        }
    }

    /**
     * Evaluates the item against all predicates in the collection.
     * <p/>
     * If needsAll is set to true, the collection will behave like an
     * "AND" chain. Otherwise, the collection will behave like an "OR" chain.
     *
     * @param item The item to evaluate.
     * @return Whether the item fits all the collection's predicates.
     */
    public boolean evaluate(T item) {
        for (Predicate<T> predicate : predicates) {
            // if:
            // 1. item did not evaluate AND needsAll (0 ^ 1 == 1) - return false, because one failed,
            // 2. item did evaluate AND NOT needsAll (1 ^ 0 == 1) - return true, because one passed.
            if (predicate.evaluate(item) ^ type.isNeedsAll()) {
                return !type.isNeedsAll();
            }
        }

        // if gone out of loop and:
        // 1. needAll - return true, because all passed without fail,
        // 2. not needAll - return false, because none passed.
        return type.isNeedsAll();
    }
}
