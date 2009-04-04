package com.infomancers.collections.yield.asmtree.enhancers;

import com.infomancers.collections.yield.asmtree.InsnEnhancer;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.Arrays;
import java.util.Collection;

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
public final class EnhancersFactory {

    private final Collection<PredicatedInsnEnhancer> enhancers;
    private static final NullEnhancer nullEnhancer = new NullEnhancer();

    public static EnhancersFactory instnace() {
        return new EnhancersFactory(
                new YieldReturnEnhancer(),
                new YieldBreakEnhancer(),
                new StoreEnhancer(),
                new LoadEnhancer(),
                new ArrayLoadEnhancer(),
                new ArrayStoreEnhancer(),
                new IincEnhancer(),
                new ArraylengthEnhancer(),
                new MethodInvocationEnhancer());
    }

    private EnhancersFactory(Collection<PredicatedInsnEnhancer> enhancers) {
        this.enhancers = enhancers;
    }

    private EnhancersFactory(PredicatedInsnEnhancer... enhancers) {
        this(Arrays.asList(enhancers));
    }

    public InsnEnhancer createEnhancer(AbstractInsnNode node) {
        for (PredicatedInsnEnhancer enhancer : enhancers) {
            if (enhancer.shouldEnhance(node)) {
                return enhancer;
            }
        }

        return nullEnhancer;
    }
}
