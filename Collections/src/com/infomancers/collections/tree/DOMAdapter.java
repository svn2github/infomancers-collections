package com.infomancers.collections.tree;

import com.infomancers.collections.yield.Yielder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 12:20:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class DOMAdapter implements TreeAdapter {

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
