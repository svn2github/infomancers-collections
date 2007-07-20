package com.infomancers.collections.tree;

import com.infomancers.collections.yield.Yielder;

import javax.swing.tree.TreeModel;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 12:20:01 PM
 * To change this template use File | Settings | File Templates.
 */
public final class TreeModelAdapter implements TreeAdapter {

    private TreeModel model;

    /**
     * Retrieves the root of the tree.
     * <p/>
     * Cannot return null.
     *
     * @return The root of the tree.
     */
    public Object getRoot() {
        return model.getRoot();
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
                for (int idx = 0; idx < model.getChildCount(node); idx++) {
                    yieldReturn(model.getChild(node, idx));
                }
            }
        };
    }
}
