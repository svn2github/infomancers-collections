package com.infomancers.collections.yield.asmbase;

import com.infomancers.collections.yield.asm.NewMember;
import org.objectweb.asm.Label;

import java.util.Queue;

/**
 * Copyright (c) 2007, Aviad Ben Dov
 * <p/>
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 3. Neither the name of Infomancers, Ltd. nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 * <p/>
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
 */
final class DelegatingInformationContainer implements YielderInformationContainer {
    private final YieldReturnCounter counter;
    private final LocalVariableMapper mapper;
    private int currentState;
    private final Label[] labels;

    public DelegatingInformationContainer(YieldReturnCounter counter, LocalVariableMapper mapper) {
        this.counter = counter;
        this.mapper = mapper;
        this.currentState = counter.getCounter();

        this.labels = new Label[counter.getCounter()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label();
        }
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

    public Label getStateLabel(int state) {
        return labels[state - 1];
    }

    public int takeState() {
        return currentState--;
    }

    public void setStateLabel(int state, Label label) {
        labels[state - 1] = label;
    }

    public String toString() {
        return "mapper: [" + mapper + "], counter: [" + counter + ']';
    }
}
