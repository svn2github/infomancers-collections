package com.infomancers.collections.yield.asmbase;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import org.objectweb.asm.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

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
final class LocalVariableMapper extends ClassAdapter {
    private LinkedList<Integer> loads = new LinkedList<Integer>();
    private Map<Integer, NewMember> slots = new TreeMap<Integer, NewMember>();

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public LocalVariableMapper(ClassVisitor cv) {
        super(cv);
    }


    public Queue<Integer> getLoads() {
        return loads;
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

    public NewMember getSlot(int index) {
        return slots.get(index);
    }

    public Iterable<? extends NewMember> getSlots() {
        return slots.values();
    }

    private class MyMethodAdapter extends MethodAdapter {
        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
        }


        @Override
        public void visitLabel(final Label label) {
            super.visitLabel(label);

            loads.add(0);
        }


        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
            super.visitJumpInsn(opcode, label);

            loads.add(0);
        }


        @Override
        public void visitLineNumber(final int line, final Label start) {
            super.visitLineNumber(line, start);

            loads.add(0);
        }


        @Override
        public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
            super.visitFrame(type, nLocal, local, nStack, stack);

            loads.add(0);
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            super.visitVarInsn(opcode, var);

            NewMember nm = slots.get(var);
            if (nm == null) {
                nm = new NewMember(var);

                slots.put(var, nm);
            }

            TypeDescriptor curType = null;
            if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
                loads.addLast(loads.removeLast() + 1);
                curType = Util.typeForOffset(opcode - Opcodes.ISTORE);
            } else if (opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD) {
                curType = Util.typeForOffset(opcode - Opcodes.ILOAD);
            }

            if (curType != null) {
                nm.mergeType(curType);
            }
        }
    }

    public String toString() {
        return "LocalVariableMapper: [loads: " + loads + ", slots: " + slots + "]";
    }
}
