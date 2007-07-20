package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 19, 2007
 * Time: 9:04:01 PM
 * To change this template use File | Settings | File Templates.
 */
final class LocalVariableMapper extends ClassAdapter {
    public static class LineLoads {
        public int line;
        public int loads;
    }

    private LinkedList<Integer> loads = new LinkedList<Integer>();
    private Map<Label, Integer> labelLocations = new HashMap<Label, Integer>();
    private int labelIndex = 0;
    private Collection<NewMember> newMembers = new HashSet<NewMember>();

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


    public Collection<NewMember> getNewMembers() {
        return newMembers;
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

            loads.add(0);
            labelLocations.put(label, labelIndex++);
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

            if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
                loads.addLast(loads.removeLast() + 1);
            }
        }


        @Override
        public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
            super.visitLocalVariable(name, desc, signature, start, end, index);

            NewMember newMember = new NewMember();
            newMember.index = index;
            newMember.start = labelLocations.get(start);
            newMember.end = labelLocations.get(end);
            newMember.name = name + "$promoted" + "$" + newMember.start + "$" + newMember.end;
            newMember.desc = desc;

            newMembers.add(newMember);
        }
    }
}
