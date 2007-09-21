package com.infomancers.collections.yield.asm;

import com.infomancers.collections.yield.asm.delayed.DelayedMethodVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
public class CastChecker extends ClassAdapter {

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public CastChecker(ClassVisitor cv) {
        super(cv);
    }


    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (Util.isYieldNextCoreMethod(name, desc)) {
            return new MyMethodAdapter(methodVisitor);
        } else {
            return methodVisitor;
        }
    }

    private class MyMethodAdapter extends DelayedMethodVisitor {
        /**
         * Constructs a new {@link org.objectweb.asm.MethodAdapter} object.
         *
         * @param mv the code visitor to which this adapter must delegate calls.
         */
        public MyMethodAdapter(MethodVisitor mv) {
            super(mv);
        }


        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            if (opcode == Opcodes.GETFIELD) {
                startMiniFrame(1); // for ALOAD 0
            }

            super.visitFieldInsn(opcode, owner, name, desc);
        }


        @Override
        protected void handleEmptyStack() {
            emitAll(mv);
            endMiniFrame();
        }

        /**
         * When a virtual method is encountered, emit all previously cached code with the exception
         * that the first emition, which will be (has to be!) the GETFIELD code, will be followed by a
         * CHECKCAST with the owner of the method invocation.
         */
        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitMethodInsn(opcode, owner, name, desc);

            if (Opcodes.INVOKEVIRTUAL == opcode && insideMiniFrame()) {
                emit(mv, 1);
                mv.visitTypeInsn(Opcodes.CHECKCAST, owner);

                emitAll(mv);
                endMiniFrame();
            }
        }
    }

}
