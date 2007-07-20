package com.infomancers.collections.tree;

import com.infomancers.collections.yield.Yielder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
 * Adapter wrapping an XML DOM.
 */
public final class DOMAdapter implements TreeAdapter {

    private Document document;

    public DOMAdapter(Document document) {
        this.document = document;
    }

    /**
     * Retrieves the root of the tree.
     * <p/>
     * Cannot return null.
     *
     * @return The root of the tree.
     */
    public Object getRoot() {
        return document;
    }

    /**
     * Retrieves the children of the node specified. If the node
     * has no children (is a leaf), an empty iterable is returned.
     * <p/>
     * Cannot return null.
     *
     * @param node The node to retrieve the children for.
     * @return The children nodes of the node.
     */
    public Iterable<Object> getChildren(final Object node) {
        return new Yielder<Object>() {
            @Override
            protected void yieldNextCore() {
                NodeList childNodes = ((Node) node).getChildNodes();
                int len = childNodes.getLength();
                for (int idx = 0; idx < len; idx++) {
                    yieldReturn(childNodes.item(idx));
                }
            }
        };

    }
}
