package com.infomancers.collections.util;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 20, 2007
 * Time: 5:45:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PredicateNegation<T> implements Predicate<T> {
    private Predicate<T> internal;

    public PredicateNegation(Predicate<T> internal) {
        this.internal = internal;
    }

    /**
     * Evaluates an item to see if it fits the boolean query.
     *
     * @param item The item to evaluate.
     * @return Whether the item fits the query.
     */
    public boolean evaluate(T item) {
        return !internal.evaluate(item);
    }
}
