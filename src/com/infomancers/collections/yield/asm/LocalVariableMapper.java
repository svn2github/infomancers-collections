package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 19, 2007
 * Time: 9:04:01 PM
 * To change this template use File | Settings | File Templates.
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
}
