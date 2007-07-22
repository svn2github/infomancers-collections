package com.infomancers.collections.graph;

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
 * Used as an adapter between an already existing graph
 * model and this collections library.
 * <p/>
 * The graph is supposed to support two things:
 * 1. Ability to determine a specific node in the graph
 * based on an index,
 * 2. Ability to retrieve list of neighbours of a
 * node.
 * <p/>
 * The graph doesn't have to be bi-directional, and the
 * graph adapter doesn't have to be implemented as such.
 */
public interface GraphAdapter {

    /**
     * Retrieves a node at a certain index.
     * <p/>
     * As long as nodes are not being added or deleted,
     * it is assured that a certain index will retrieve
     * the same node.
     *
     * @param index The index of the node.
     * @return A node.
     */
    Object getNode(int index);

    /**
     * Retreves the list of a node's neighbours.
     *
     * @param node The node to retrieve its neighbours.
     * @return The neighbour nodes.
     */
    Iterable<Object> getNodeNeighbours(Object node);
}
