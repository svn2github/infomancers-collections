package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 14, 2007
 * Time: 11:24:17 AM
 * To change this template use File | Settings | File Templates.
 */
public final class StateKeeper extends ClassAdapter {


    private static final String STATE_FIELD_NAME = "state";
    private final YieldReturnCounter counter;
    private String owner;
    private static final String STATE_FIELD_DESC = "B";

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     * @param counter The counter visitor counting the amount of yieldReturn calls in a
     * yieldNextCore method.
     */
    public StateKeeper(ClassVisitor cv, YieldReturnCounter counter) {
        super(cv);
        this.counter = counter;
    }


    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        this.owner = name;
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


    @Override
    public void visitEnd() {
        cv.visitField(Opcodes.ACC_PRIVATE, STATE_FIELD_NAME, STATE_FIELD_DESC, STATE_FIELD_DESC, null);

        super.visitEnd();
    }

    private class MyMethodAdapter extends MethodAdapter {
        private int stateIndex;
        private Label[] labels;

        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
            stateIndex = 1;
        }

        @Override
        public void visitCode() {
            super.visitCode();

            // Create the label for the first line of code after the switching - This will be the
            // default label.
            Label dflt = new Label();

            // Create the labels for the rest of the elements.
            labels = new Label[counter.getCounter()];
            for (int i = 0; i < counter.getCounter(); i++) {
                labels[i] = new Label();
            }

            // the first thing in the method should be switching to the previous state.
            // so, load the state member
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, STATE_FIELD_NAME, STATE_FIELD_DESC);
            // then, lookup the next line of code
            mv.visitTableSwitchInsn(1, counter.getCounter(), dflt, labels);
            // write the default label.
            mv.visitLabel(dflt);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitMethodInsn(opcode, owner, name, desc);

            if (Util.isInvokeYieldReturn(opcode, name, desc)) {
                // save the current state, first load this
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                // push value <stateIndex>
                mv.visitIntInsn(Opcodes.BIPUSH, stateIndex);
                // set this.state
                mv.visitFieldInsn(Opcodes.PUTFIELD, owner, STATE_FIELD_NAME, "B");
                // quit method
                mv.visitInsn(Opcodes.RETURN);
                // now mark the label for coming back here
                mv.visitLabel(labels[stateIndex - 1]);

                // advance stateIndex
                stateIndex++;
            }
        }
    }
}
