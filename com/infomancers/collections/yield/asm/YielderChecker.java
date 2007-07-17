package com.infomancers.collections.yield.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 14, 2007
 * Time: 4:41:17 PM
 * To change this template use File | Settings | File Templates.
 */
public final class YielderChecker extends ClassAdapter {
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
