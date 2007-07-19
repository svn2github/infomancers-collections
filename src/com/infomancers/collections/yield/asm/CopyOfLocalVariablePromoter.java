package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

import java.util.*;

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
 * Promotes all local variables inside the <code>yieldNextCore</code> implementation
 * to class member fields.
 */
final class LocalVariablePromoter extends ClassAdapter {
    private final static String MEMBER_NAME_PREFIX = "member";
    private final LocalVariableAssignmentMapper mapper;

    public static class NewMember {
        public String name;
        public String desc;
        public int index;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NewMember newMember = (NewMember) o;

            return index == newMember.index && desc.equals(newMember.desc) && name.equals(newMember.name);

        }

        @Override
        public int hashCode() {
            int result;
            result = name.hashCode();
            result = 31 * result + desc.hashCode();
            result = 31 * result + index;
            return result;
        }
    }

    private final Map<Integer, NewMember> newMembers = new HashMap<Integer, NewMember>();

    private String owner;


    public Iterable<NewMember> getNewMembers() {
        return newMembers.values();
    }

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public LocalVariablePromoter(ClassVisitor cv, LocalVariableAssignmentMapper mapper) {
        super(cv);
        this.mapper = mapper;
    }

    @Override
    public void visitEnd() {
        for (NewMember newMember : getNewMembers()) {
            visitField(Opcodes.ACC_PRIVATE, newMember.name, newMember.desc, newMember.desc, null);
        }

        super.visitEnd();
    }


    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        newMembers.clear();
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

    private class MyMethodAdapter extends MethodAdapter {
        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
        }

        @Override
        public void visitLabel(final Label label) {
            super.visitLabel(label);

            dealWithLoads();
        }


        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
            super.visitJumpInsn(opcode, label);

            dealWithLoads();
        }


        @Override
        public void visitLineNumber(final int line, final Label start) {
            super.visitLineNumber(line, start);

            dealWithLoads();
        }

        private void dealWithLoads() {
            int loads = mapper.getLineNumbers().remove();

            while (loads-- > 0) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
            }
        }


        @Override
        public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
            super.visitFrame(Opcodes.F_SAME, 0, local, 0, stack);

            dealWithLoads();
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            if (var == 0) {
                mv.visitVarInsn(opcode, var);
            } else {
                NewMember newMember = new NewMember();
                newMember.name = MEMBER_NAME_PREFIX + var;
                newMember.index = var;

                if (opcode > Opcodes.ALOAD) {
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ISTORE);
                    createPutField(newMember);
                } else {
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ILOAD);
                    createGetField(newMember);
                }
                newMembers.put(var, newMember);
            }
        }


        /**
         * Converts an increment (++ or --) by a normal
         * add or sub.
         * <p/>
         * meaning, the following:
         * <code>
         * IINC 3, 1
         * </code>
         * <p/>
         * becomes the following:
         * <code>
         * ALOAD 0
         * ALOAD 0
         * GETFIELD member3
         * BIPUSH 1
         * IADD
         * PUTFIELD member3
         * </code>
         *
         * @param var       The local variable index. Used to determine member name.
         * @param increment The increment amount.
         */
        @Override
        public void visitIincInsn(final int var, final int increment) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            NewMember newMember = newMembers.get(var);
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, newMember.name, newMember.desc);
            mv.visitIntInsn(Opcodes.BIPUSH, Math.abs(increment));
            mv.visitInsn(increment > 0 ? Opcodes.IADD : Opcodes.ISUB);
            mv.visitFieldInsn(Opcodes.PUTFIELD, owner, newMember.name, newMember.desc);
        }


        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        }

        private void createPutField(NewMember newMember) {
            mv.visitFieldInsn(Opcodes.PUTFIELD, owner, newMember.name, newMember.desc);
        }

        private void createGetField(NewMember newMember) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, newMember.name, newMember.desc);
        }
    }
}
