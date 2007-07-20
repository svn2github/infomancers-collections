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
 * Used to chain together simple transformations so that they are
 * done sequentially.
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
