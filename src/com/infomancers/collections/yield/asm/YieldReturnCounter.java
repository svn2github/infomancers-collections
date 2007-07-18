package com.infomancers.collections.yield.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.MethodAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 14, 2007
 * Time: 3:00:51 PM
 * To change this template use File | Settings | File Templates.
 */
public final class YieldReturnCounter extends ClassAdapter {
    private int counter = 0;


    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public YieldReturnCounter(ClassVisitor cv) {
        super(cv);
    }


    public int getCounter() {
        return counter;
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
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            if (Util.isInvokeYieldReturn(opcode, name, desc)) {
                counter++;
            }

            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

}
