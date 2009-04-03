package com.infomancers.collections.yield.asm;

import com.infomancers.collections.yield.asmbase.AbstractYielderTransformer;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

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
 * A class transformation file which creates three chains of ASM visitors, all three
 * eventually create the enhanced class which keeps state and supports the yield idea.
 * <p/>
 * The chains are:
 * <p/>
 * 1. reader (of origin) -> returnCounter -> assignMapper -> null
 * 2. reader (of origin) -> promoter -> stateKeeper (using returnCounter) -> writer (to output1)
 * 3. reader (of output1) -> assigner (using promoter) -> writer (to output2)
 * <p/>
 * And then returns output2.
 * <p/>
 * Also, notice that the order of the visitors is important:
 * The promoter counts on the labels to be in the exact same order as the assignMapper sees them.
 */
public final class YielderTransformer extends AbstractYielderTransformer {

    public YielderTransformer(boolean debug) {
        super(debug);
    }

    protected byte[] enhanceClass(ClassReader reader, YielderInformationContainer info) {
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        CastChecker caster = new CastChecker(writer);
//        StateKeeper stateKeeper = new StateKeeper(writer, counter);
        StateKeeper stateKeeper = new StateKeeper(caster, info);
        LocalVariablePromoter promoter = new LocalVariablePromoter(stateKeeper, info);
        reader.accept(promoter, 0);

        return writer.toByteArray();
    }
}
