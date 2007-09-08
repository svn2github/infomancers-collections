package com.infomancers.collections.yield.asm;

import com.infomancers.collections.yield.asm.promotion.AccessorCreators;
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
 * Promotes all local variables inside the <code>yieldNextCore</code> implementation
 * to class member fields.
 */
final class LocalVariablePromoter extends ClassAdapter {
    private final LocalVariableMapper mapper;

    private String owner;

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv     the class visitor to which this adapter must delegate calls.
     * @param mapper The visitor used to map where assignments to local variables
     *               take place, so that this visitor could prepare an ALOAD_0 for the PUTFIELD
     *               opcode.
     */
    public LocalVariablePromoter(ClassVisitor cv, LocalVariableMapper mapper) {
        super(cv);
        this.mapper = mapper;
    }

    @Override
    public void visitEnd() {
        for (NewMember newMember : mapper.getSlots()) {
            visitField(Opcodes.ACC_PRIVATE, newMember.name, newMember.type.getDesc(), newMember.type.getDesc(), null);
        }

        super.visitEnd();
    }


    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
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

        @Override
        public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
            super.visitFrame(Opcodes.F_SAME, 0, local, 0, stack);

            dealWithLoads();
        }


        private void dealWithLoads() {
            int loads = mapper.getLoads().remove();

            while (loads-- > 0) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
            }
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            if (var == 0) {
                mv.visitVarInsn(opcode, var);
            } else {
                NewMember newMember = searchMember(var);

                if (opcode > Opcodes.ALOAD) {
                    createPutField(opcode, newMember);
                } else {
                    createGetField(opcode, newMember);
                }
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
            NewMember newMember = searchMember(var);

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            createGetField(Opcodes.ILOAD, newMember);
            mv.visitIntInsn(Opcodes.BIPUSH, Math.abs(increment));
            mv.visitInsn(increment > 0 ? Opcodes.IADD : Opcodes.ISUB);
            createPutField(Opcodes.ISTORE, newMember);
        }


        @Override
        public void visitInsn(final int opcode) {
            if (opcode == Opcodes.ARRAYLENGTH) {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/reflect/Array", "getLength", "(Ljava/lang/Object;)I");
            } else if (opcode >= Opcodes.IALOAD && opcode <= Opcodes.SALOAD) {
                int offset = opcode - Opcodes.IALOAD;
                TypeDescriptor type = Util.typeForOffset(offset);

                type.getArrayAccessorCreator().createGetValueCode(mv, type);
            } else if (opcode >= Opcodes.IASTORE && opcode <= Opcodes.SASTORE) {
                int offset = opcode - Opcodes.IASTORE;
                TypeDescriptor type = Util.typeForOffset(offset);

                type.getArrayAccessorCreator().createSetValueCode(mv, type);
            } else {
                super.visitInsn(opcode);
            }
        }

        private NewMember searchMember(final int var) {
            return mapper.getSlot(var);
        }


        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
            if ("this".equals(name)) {
                super.visitLocalVariable(name, desc, signature, start, end, index);
            }
        }


        private void createPutField(int opcode, NewMember newMember) {
            if (newMember.type == TypeDescriptor.Object) {
                int offset = opcode - Opcodes.ISTORE;
                TypeDescriptor type = Util.typeForOffset(offset);

                type.getMemberAccessorCreator().createPutFieldCode(mv, owner, type, newMember);
            } else {
                AccessorCreators.FIELD_SIMPLE.createPutFieldCode(mv, owner, newMember.type, newMember);
            }

        }

        private void createGetField(int opcode, NewMember newMember) {
            if (newMember.type == TypeDescriptor.Object) {
                int offset = opcode - Opcodes.ILOAD;
                TypeDescriptor type = Util.typeForOffset(offset);

                type.getMemberAccessorCreator().createGetFieldCode(mv, owner, type, newMember);
            } else {
                AccessorCreators.FIELD_SIMPLE.createGetFieldCode(mv, owner, newMember.type, newMember);
            }
        }
    }
}
