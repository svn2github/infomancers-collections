package com.infomancers.collections.yield.asm.promotion;

import com.infomancers.collections.yield.asm.TypeDescriptor;
import org.objectweb.asm.Label;
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

/**
 * Creates code that distinguishes a byte array from a boolean array.
 * <p/>
 * The problem with boolean arrays is that the byte code that manipulates
 * them is the same bytecode that manipulates byte-arrays. Since in the worst
 * case scenerio the manipulator tries to use an Object for the slot replacement,
 * it will need to use the Array.get/setByte methods for byte-array related bytecode.
 * <p/>
 * This is bad though, since getByte will fail if operated on a boolean array. Therefore,
 * the code for any byte-array related bytecode needs to be replaced with a type check
 * for the array, and then the appropriate Array.get method.
 * <p/>
 * The code will look like this:
 * <p/>
 * if (arr.getClass().getComponentType() == Byte.TYPE) {
 * // do byte-array related code
 * } else {
 * // do boolean-array related code
 * }
 */
public class ByteOrBooleanArrayAccessorCreator implements ArrayAccessorCreator {
    /**
     * Sets a value into an array.
     * <p/>
     * Since this might be a byte array OR a boolean array, first check for what type
     * of array it is (by calling getClass.getCompoundClass.equals(Byte.TYPE)) and then
     * access either setBoolean or setByte using a simple array accessor.
     * <p/>
     * The stack is already filled with [..., arr, index, value ].
     * <p/>
     * The stack should be [ ... ] at the end.
     *
     * @param mv   The method visitor to write the code through.
     * @param type The type of the array.
     */
    public void createSetValueCode(MethodVisitor mv, TypeDescriptor type) {
        Label l1 = new Label();
        Label l2 = new Label();

        // Need to make the stack look like this: [... arr, index, value, arr] so we're going to duplicate value
        // and index to be before arr, and then arr itself.

        mv.visitInsn(Opcodes.DUP_X2);
        mv.visitInsn(Opcodes.POP);
        mv.visitInsn(Opcodes.DUP_X2);
        mv.visitInsn(Opcodes.POP);
        mv.visitInsn(Opcodes.DUP_X2);

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getComponentType", "()Ljava/lang/Class;");
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
        mv.visitJumpInsn(Opcodes.IF_ACMPNE, l1);
        AccessorCreators.ARRAY_SIMPLE.createSetValueCode(mv, TypeDescriptor.Byte);
        mv.visitJumpInsn(Opcodes.GOTO, l2);
        mv.visitLabel(l1);
        AccessorCreators.ARRAY_SIMPLE.createSetValueCode(mv, TypeDescriptor.Boolean);
        mv.visitLabel(l2);
    }

    /**
     * Gets a value from an array.
     * <p/>
     * Since this might be a byte array OR a boolean array, first check for what type
     * of array it is (by calling getClass.getCompoundClass.equals(Byte.TYPE)) and then
     * access either getBoolean or getByte using a simple array accessor.
     * <p/>
     * The stack is already filled with [..., arr, index ].
     * <p/>
     * The should be [ ..., value ] at the end.
     *
     * @param mv   The method visitor to write the code through.
     * @param type The type of the array.
     */
    public void createGetValueCode(MethodVisitor mv, TypeDescriptor type) {
        Label l1 = new Label();
        Label l2 = new Label();

        // Need to make the stack look like this: [... arr, index, arr] so we're going to duplicate value
        // and index to be before arr, and then arr itself.

        mv.visitInsn(Opcodes.DUP_X1);
        mv.visitInsn(Opcodes.POP);
        mv.visitInsn(Opcodes.DUP_X1);

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getComponentType", "()Ljava/lang/Class;");
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
        mv.visitJumpInsn(Opcodes.IF_ACMPNE, l1);
        AccessorCreators.ARRAY_SIMPLE.createGetValueCode(mv, TypeDescriptor.Byte);
        mv.visitJumpInsn(Opcodes.GOTO, l2);
        mv.visitLabel(l1);
        AccessorCreators.ARRAY_SIMPLE.createGetValueCode(mv, TypeDescriptor.Boolean);
        mv.visitLabel(l2);
    }
}
