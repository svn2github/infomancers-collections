package com.infomancers.collections.yield.asm;

import com.infomancers.collections.yield.asmbase.Util;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import org.objectweb.asm.*;

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
 * Creates the state switch, which decides where code execution needs
 * to resume to the next call to <code>yieldNextCore</code>.
 * <p/>
 * This creates a new member, a swtich statement at the beginning of the
 * method, and state assignments after each call to <code>yieldReturn</code>.
 *
 * @see com.infomancers.collections.yield.Yielder
 */
final class StateKeeper extends ClassAdapter {
    private static final String STATE_FIELD_NAME = "state";
    private final YielderInformationContainer info;
    private String owner;
    private static final String STATE_FIELD_DESC = "B";

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv   the class visitor to which this adapter must delegate calls.
     * @param info The counter visitor counting the amount of yieldReturn calls in a
     */
    public StateKeeper(ClassVisitor cv, YielderInformationContainer info) {
        super(cv);
        this.info = info;
    }


    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        this.owner = name;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (com.infomancers.collections.yield.asmbase.Util.isYieldNextCoreMethod(name, desc)) {
            return new MyMethodAdapter(methodVisitor);
        } else {
            return methodVisitor;
        }
    }


    @Override
    public void visitEnd() {
        cv.visitField(Opcodes.ACC_PRIVATE, STATE_FIELD_NAME, STATE_FIELD_DESC, STATE_FIELD_DESC, null);

        super.visitEnd();
    }

    private class MyMethodAdapter extends MethodAdapter {
        private int stateIndex;
        private Label[] labels;

        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
            stateIndex = 1;
        }

        @Override
        public void visitCode() {
            super.visitCode();

            // Create the label for the first line of code after the switching - This will be the
            // default label.
            Label dflt = new Label();

            // Create the labels for the rest of the elements.
            labels = new Label[info.getCounter()];
            for (int i = 0; i < info.getCounter(); i++) {
                labels[i] = new Label();
            }

            // the first thing in the method should be switching to the previous state.
            // so, load the state member
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, owner, STATE_FIELD_NAME, STATE_FIELD_DESC);
            // then, lookup the next line of code
            super.visitTableSwitchInsn(1, info.getCounter(), dflt, labels);
            // write the default label.
            super.visitLabel(dflt);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitMethodInsn(opcode, owner, name, desc);

            if (Util.isInvokeYieldReturn(opcode, name, desc)) {
                // save the current state, first load this
                super.visitVarInsn(Opcodes.ALOAD, 0);
                // push value <stateIndex>
                super.visitIntInsn(Opcodes.BIPUSH, stateIndex);
                // set this.state
                super.visitFieldInsn(Opcodes.PUTFIELD, owner, STATE_FIELD_NAME, "B");
                // quit method
                super.visitInsn(Opcodes.RETURN);
                // now mark the label for coming back here
                super.visitLabel(labels[stateIndex - 1]);

                // advance stateIndex
                stateIndex++;
            } else if (com.infomancers.collections.yield.asmbase.Util.isInvokeYieldBreak(opcode, name, desc)) {
                super.visitInsn(Opcodes.RETURN);
            }
        }
    }
}
