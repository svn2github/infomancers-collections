package com.infomancers.collections.yield.asm.delayed;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

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
public class DelayedMethodVisitor extends MethodAdapter {
    private static class MiniFrame {
        public Deque<DelayedInstructionEmitter> workQueue = new LinkedList<DelayedInstructionEmitter>();
        int stackSize = 0;
    }

    private Stack<MiniFrame> miniFrames = new Stack<MiniFrame>();


    /**
     * Constructs a new {@link org.objectweb.asm.MethodAdapter} object.
     *
     * @param mv the code visitor to which this adapter must delegate calls.
     */
    public DelayedMethodVisitor(MethodVisitor mv) {
        super(mv);
    }

    /////////////////////////////////////////////////////////////////
    // All the visitXXX methods are here.


    @Override
    public void visitCode() {
        super.visitCode();

        startMiniFrame();
    }

    @Override
    public void visitEnd() {
        super.visitEnd();

        if (!insideMiniFrame()) {
            throw new IllegalStateException("Should have the bounding frame by the end of code!");
        }

        emitAll(mv);
        endMiniFrame();

        if (insideMiniFrame()) {
            throw new IllegalStateException("Too many mini-frames by the end of code!");
        }
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.METHOD.createEmitter(opcode, owner, name, desc));
        } else {
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.FIELD.createEmitter(opcode, owner, name, desc));
        } else {
            super.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    @Override
    public void visitInsn(final int opcode) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.INSN.createEmitter(opcode));
        } else {
            super.visitInsn(opcode);
        }
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.INT.createEmitter(opcode, operand));
        } else {
            super.visitIntInsn(opcode, operand);
        }
    }

    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.FRAME.createEmitter(-1, type, nLocal, local, nStack, stack));
        } else {
            super.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.VAR.createEmitter(opcode, var));
        } else {
            super.visitVarInsn(opcode, var);
        }
    }

    @Override
    public void visitTypeInsn(final int opcode, final String desc) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitTypeInsn(opcode, desc);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitJumpInsn(final int opcode, final Label label) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.JUMP.createEmitter(opcode, label));
        } else {
            super.visitJumpInsn(opcode, label);
        }
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.LDC.createEmitter(-1, cst));
        } else {
            super.visitLdcInsn(cst);
        }
    }

    @Override
    public void visitLabel(final Label label) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.LABEL.createEmitter(-1, label));
        } else {
            super.visitLabel(label);
        }
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitIincInsn(var, increment);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitTableSwitchInsn(final int min, final int max, final Label dflt, final Label labels[]) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.TABLESWITCH.createEmitter(-1, min, max, dflt, labels));
        } else {
            super.visitTableSwitchInsn(min, max, dflt, labels);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int keys[], final Label labels[]) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitLookupSwitchInsn(dflt, keys, labels);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitMultiANewArrayInsn(desc, dims);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitTryCatchBlock(start, end, handler, type);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        if (insideMiniFrame()) throw new IllegalStateException();
        super.visitLocalVariable(name, desc, signature, start, end, index);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        if (insideMiniFrame()) {
            delayInsn(DelayedInstruction.LINE.createEmitter(-1, line, start));
        } else {
            super.visitLineNumber(line, start);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Mini-frame management and manipulations here.

    protected final void startMiniFrame() {
        startMiniFrame(insideMiniFrame() ? getCurrentMiniFrame().stackSize : 0);
    }

    private final void startMiniFrame(int initialStack) {
        miniFrames.push(new MiniFrame());
        getCurrentMiniFrame().stackSize = initialStack;
    }

    private void delayInsn(DelayedInstructionEmitter emitter) {
        getCurrentMiniFrame().workQueue.offer(emitter);

        handleStack(emitter);
    }

    protected final void delayPriorityInsn(DelayedInstructionEmitter emitter) {
        getCurrentMiniFrame().workQueue.offerFirst(emitter);

        handleStack(emitter);
    }

    private void handleStack(DelayedInstructionEmitter emitter) {
        final int stackChange = emitter.pushAmount() - emitter.popAmount();
        getCurrentMiniFrame().stackSize += stackChange;
        boolean changedStackSize = stackChange != 0;

        if (getCurrentMiniFrame().stackSize < 0) {
            throw new IllegalStateException("Mini frame's stack <= 0 - must be a missing push");
        } else if (changedStackSize && getCurrentMiniFrame().stackSize == 0) {
            handleEmptyStack();
        }
    }

    protected final void emit(MethodVisitor mv, int count) {
        // pops the current mini frame, and emit the instructions
        // into the previous mini-frame, unless this is the last
        // mini frame available in which case, a real emition is made.
        if (count <= 0) {
            throw new IllegalArgumentException("count <= 0");
        }

        MiniFrame miniFrame = miniFrames.pop();

        while (count-- > 0) {
            if (insideMiniFrame()) {
                getCurrentMiniFrame().workQueue.offer(miniFrame.workQueue.poll());
            } else {
                miniFrame.workQueue.poll().emit(mv);
            }
        }

        miniFrames.push(miniFrame);
    }

    protected final void emitAll(MethodVisitor mv) {
        emit(mv, getCurrentMiniFrame().workQueue.size());
    }

    protected void handleEmptyStack() {
    }

    protected final void endMiniFrame() {
        MiniFrame previous = miniFrames.pop();

        if (insideMiniFrame()) {
            // the stack size of the mini frame previous to the one being popped now was added
            // when it was created.
            getCurrentMiniFrame().stackSize += previous.stackSize - getCurrentMiniFrame().stackSize;

            while (previous.workQueue.size() > 0) {
                getCurrentMiniFrame().workQueue.offer(previous.workQueue.poll());
            }
        }
    }

    protected final boolean insideMiniFrame() {
        return !miniFrames.isEmpty();
    }

    private MiniFrame getCurrentMiniFrame() {
        return insideMiniFrame() ? miniFrames.peek() : null;
    }
}
