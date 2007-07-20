package com.infomancers.collections.iterators;

import com.infomancers.collections.tree.TreeAdapter;
import com.infomancers.collections.yield.Yielder;

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
 * Utility class used to create an iterator which iterates over
 * all the nodes of a tree, either in prefix or postfix mode.
 */
public final class TreeIterators {

    public enum Type {
        Prefix,
        Postfix;
    }

    public static Iterable<Object> prefixIterator(final TreeAdapter tree) {
        return getIterator(tree, tree.getRoot(), Type.Prefix);
    }

    public static Iterable<Object> postfixIterator(final TreeAdapter tree) {
        return getIterator(tree, tree.getRoot(), Type.Postfix);
    }

    private static Iterable<Object> getIterator(final TreeAdapter tree, final Object treeNode, final Type type) {
        return new Yielder<Object>() {

            @Override
            protected void yieldNextCore() {
                if (type == Type.Prefix) {
                    yieldReturn(treeNode);
                }

                for (Object child : tree.getChildren(treeNode)) {
                    for (Object item : TreeIterators.getIterator(tree, child, type)) {
                        yieldReturn(item);
                    }
                }

                if (type == Type.Postfix) {
                    yieldReturn(treeNode);
                }
            }
        };
    }
}
