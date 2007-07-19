package com.infomancers.collections.yield.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

/**
 * Makes sure that the class being examined is in fact
 * a sub-class of the Yielder class.
 * <p/>
 * This is necessary because during instrumentation, all we have
 * is the bytecode. Only a bytecode framework can load up the class
 * from that and check for such information.
 */
final class YielderChecker extends ClassAdapter {
    private boolean isYielder = false;


    public boolean isYielder() {
        return isYielder;
    }

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public YielderChecker(ClassVisitor cv) {
        super(cv);
    }


    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        if (Util.isYielderClassName(superName)) {
            isYielder = true;
        }
    }
}
