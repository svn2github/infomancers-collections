package com.infomancers.collections.yield.asmbase;

import com.infomancers.collections.yield.asm.NewMember;

import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 8:49:42 PM
 * To change this template use File | Settings | File Templates.
 */
final class DelegatingInformationContainer implements YielderInformationContainer {
    private final YieldReturnCounter counter;
    private final LocalVariableMapper mapper;

    public DelegatingInformationContainer(YieldReturnCounter counter, LocalVariableMapper mapper) {
        this.counter = counter;
        this.mapper = mapper;
    }

    public int getCounter() {
        return counter.getCounter();
    }

    public Iterable<? extends NewMember> getSlots() {
        return mapper.getSlots();
    }

    public Queue<Integer> getLoads() {
        return mapper.getLoads();
    }

    public NewMember getSlot(int var) {
        return mapper.getSlot(var);
    }
}
