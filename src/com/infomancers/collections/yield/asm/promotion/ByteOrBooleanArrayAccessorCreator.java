package com.infomancers.collections.yield.asm.promotion;

import com.infomancers.collections.yield.asm.TypeDescriptor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Sep 7, 2007
 * Time: 9:11:01 PM
 * To change this template use File | Settings | File Templates.
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
        AccessorCreators.ARRAY_SIMPLE.createGetValueCode(mv, TypeDescriptor.Byte);
        mv.visitJumpInsn(Opcodes.GOTO, l2);
        mv.visitLabel(l1);
        AccessorCreators.ARRAY_SIMPLE.createGetValueCode(mv, TypeDescriptor.Boolean);
        mv.visitLabel(l2);
    }
}
