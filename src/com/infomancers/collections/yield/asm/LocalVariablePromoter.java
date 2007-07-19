package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

import java.util.HashSet;
import java.util.Set;

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

    private final Set<NewMember> newMembers = new HashSet<NewMember>();

    private String owner;


    public Iterable<NewMember> getNewMembers() {
        return newMembers;
    }

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public LocalVariablePromoter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visitEnd() {
        for (NewMember newMember : newMembers) {
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
//        private final String[] descs = {"I", "L", "F", "D", "Ljava/lang/Object;"};

        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            if (var == 0) {
                mv.visitVarInsn(opcode, var);
            } else {
                String memberName = MEMBER_NAME_PREFIX + var;
                NewMember newMember = new NewMember();
                newMember.name = memberName;
                newMember.index = var;

                if (opcode > Opcodes.ALOAD) {
                    mv.visitVarInsn(opcode, var);
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ISTORE);
                    createPutField(var, opcode - Opcodes.ISTORE + Opcodes.ILOAD, memberName, newMember.desc);
                } else {
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ILOAD);
                    createGetField(memberName, newMember.desc);
                }
                newMembers.add(newMember);
            }
        }

        private void createPutField(int var, int op, String memberName, String desc) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(op, var);
            mv.visitFieldInsn(Opcodes.PUTFIELD, owner, memberName, desc);
        }

        private void createGetField(String memberName, String desc) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, memberName, desc);
        }
    }
}
