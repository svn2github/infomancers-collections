package com.infomancers.tests;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;

import java.util.Arrays;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 11:23:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestYIC implements YielderInformationContainer {
    private final int counter;
    private final NewMember[] slots;

    public TestYIC(int counter, NewMember... slots) {
        this.counter = counter;
        this.slots = slots;
    }

    public int getCounter() {
        return counter;
    }

    public Iterable<? extends NewMember> getSlots() {
        return Arrays.asList(slots);
    }

    public Queue<Integer> getLoads() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NewMember getSlot(int var) {
        for (NewMember slot : slots) {
            if (slot.getIndex() == var) {
                return slot;
            }
        }

        return null;
    }
}
