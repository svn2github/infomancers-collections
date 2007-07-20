package com.infomancers.collections.iterators;

import com.infomancers.collections.tree.TreeAdapter;
import com.infomancers.collections.yield.Yielder;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 12:39:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeIterators {


    public static Iterable<Object> prefixIterator(final TreeAdapter tree) {
        return getIterator(tree, tree.getRoot(), TreeIterationType.Prefix);
    }

    public static Iterable<Object> postfixIterator(final TreeAdapter tree) {
        return getIterator(tree, tree.getRoot(), TreeIterationType.Postfix);
    }

    private static Iterable<Object> getIterator(final TreeAdapter tree, final Object treeNode, final TreeIterationType type) {
        return new Yielder<Object>() {

            @Override
            protected void yieldNextCore() {
                if (type == TreeIterationType.Prefix) {
                    yieldReturn(treeNode);
                }

                for (Object child : tree.getChildren(treeNode)) {
                    for (Object grandchild : TreeIterators.getIterator(tree, child, type)) {
                        yieldReturn(grandchild);
                    }
                }

                if (type == TreeIterationType.Postfix) {
                    yieldReturn(treeNode);
                }
            }
        };
    }
}
