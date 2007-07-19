package com.infomancers.collections.yield.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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
 * 1. reader (of origin) -> returnCounter -> null
 * 2. reader (of origin) -> stateKeeper (using returnCounter) -> promoter -> writer (to output1)
 * 3. reader (of output1) -> assigner (using promoter) -> writer (to output2)
 * <p/>
 * And then returns output2.
 */
public final class YielderTransformer implements ClassFileTransformer {
    private final boolean debug;


    public YielderTransformer(boolean debug) {
        this.debug = debug;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] result = classfileBuffer;

        if (classBeingRedefined == null) {
            // first pass - gather statistics
            ClassReader reader = new ClassReader(classfileBuffer);
            YieldReturnCounter counter = new YieldReturnCounter(new EmptyVisitor());
            YielderChecker checker = new YielderChecker(counter);

            reader.accept(checker, 0);

            if (checker.isYielder()) {
                // second pass - write new code
                ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                ClassVisitor nextVisitor = writer;
                if (debug) {
                    nextVisitor = new TraceClassVisitor(writer, new PrintWriter(System.out));
                }
                LocalVariablePromoter promoter = new LocalVariablePromoter(nextVisitor);
                StateKeeper stateKeeper = new StateKeeper(promoter, counter);

                reader.accept(stateKeeper, 0);

                // third pass - write finalizers
                ClassReader thirdReader = new ClassReader(writer.toByteArray());
                ClassWriter thirdWriter = new ClassWriter(thirdReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                nextVisitor = thirdWriter;
                if (debug) {
                    nextVisitor = new TraceClassVisitor(thirdWriter, new PrintWriter(System.out));
                }
                LocalVariableAssigner assigner = new LocalVariableAssigner(nextVisitor, promoter);

                thirdReader.accept(assigner, 0);

                result = thirdWriter.toByteArray();
            }
        }
        return result;
    }
}
