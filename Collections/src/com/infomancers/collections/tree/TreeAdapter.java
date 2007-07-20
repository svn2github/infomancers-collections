package com.infomancers.collections.tree;

/**
 * Used as an adapter between an already existing tree
 * model and this collections library.
 * <p/>
 * A few well known trees already have implementations in this
 * library, such as TreeModelAdapter for Swing's TreeModel interface
 * and DOMAdapter for XML DOM.
 *
 * @see com.infomancers.collections.tree.DOMAdapter
 * @see com.infomancers.collections.tree.TreeModelAdapter
 */
public interface TreeAdapter {
    /**
     * Retrieves the root of the tree.
     * <p/>
     * Cannot return null.
     *
     * @return The root of the tree.
     */
    Object getRoot();


    /**
     * Retrieves the children of the node specified. If the node
     * has no children (is a leaf), an empty iterable is returned.
     * <p/>
     * Cannot return null.
     *
     * @param node The node to retrieve the children for.
     * @return The children nodes of the node.
     */
    Iterable<Object> getChildren(Object node);
}
