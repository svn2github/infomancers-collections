package com.infomancers.collections.iterators;

import com.infomancers.collections.util.Transformation;

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
 * Utility class to create chained transformations.
 */
public final class ChainedTransformations {

    public static <T1, T2, T3> Transformation<T1, T3> chainTransformations(final Transformation<T1, T2> first,
                                                                           final Transformation<T2, T3> second) {
        return new Transformation<T1, T3>() {
            public T3 transform(T1 item) {
                return second.transform(first.transform(item));
            }
        };
    }

    public static <T1, T2, T3, T4> Transformation<T1, T4> chainTransformations(final Transformation<T1, T2> first,
                                                                               final Transformation<T2, T3> second,
                                                                               final Transformation<T3, T4> third) {
        return chainTransformations(chainTransformations(first, second), third);
    }

    public static <T1, T2, T3, T4, T5> Transformation<T1, T5> chainTransformations(final Transformation<T1, T2> first,
                                                                                   final Transformation<T2, T3> second,
                                                                                   final Transformation<T3, T4> third,
                                                                                   final Transformation<T4, T5> forth) {
        return chainTransformations(chainTransformations(first, second, third), forth);
    }
}
