package com.infomancers.collections.iterators;

import com.infomancers.collections.graph.GraphAdapter;
import com.infomancers.collections.util.Predicate;
import com.infomancers.collections.util.PredicateNegation;
import com.infomancers.collections.util.predicates.InCollection;
import com.infomancers.collections.yield.Yielder;

import java.util.Collection;
import java.util.HashSet;

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
 * all the nodes of a graph, using different common algorithms.
 */
public final class GraphIterators {

    public Iterable<Object> depthFirstSearch(final GraphAdapter adapter) {
        return dfsCore(adapter, adapter.getNode(0), new HashSet<Object>());
    }

    private Iterable<Object> dfsCore(final GraphAdapter adapter, final Object graphNode,
                                     final Collection<Object> visited) {
        return new Yielder<Object>() {

            @Override
            protected void yieldNextCore() {
                yieldReturn(graphNode);

                visited.add(graphNode);

                // First, let's create a filter for all vertices not visited yet.
                InCollection<Object> inVisited = new InCollection<Object>(visited);
                Predicate<Object> notInVisited = new PredicateNegation<Object>(inVisited);

                // Then, let's create a filtered iteration over the neighbours of the current node.
                Iterable<Object> neighbours = adapter.getNodeNeighbours(graphNode);
                Iterable<Object> neighboursNotVisited = Iterators.filteredIterable(neighbours, notInVisited);

                // Now, let's iterate the neighbours and recursively call DFS.
                for (Object neighbour : neighboursNotVisited) {
                    for (Object neighboursNeighbour : dfsCore(adapter, neighbour, visited)) {
                        yieldReturn(neighboursNeighbour);
                    }
                }
            }
        };
    }
}
