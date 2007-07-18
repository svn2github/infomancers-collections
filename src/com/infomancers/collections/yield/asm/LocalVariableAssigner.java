package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;


/**
 * Since the current way to deal with storing a local
 * variable is to save the local variable back to the
 * member after storing the value in the local variable,
 * e.g. the following code:
 * <code>
 * localVariable1 = 5;
 * member1 = localVariable1;
 * </code>
 * There are some cases where a value of a variable is unknown
 * when returning to a previous state. For example, the following code:
 * <code>
 * for (int i = 0; i < arr.length; i++) {
 *   yieldReturn(arr[i]);
 * }
 * </code>
 * is enhanced to:
 * <code>
 * int i = 0;
 * member1 = i;
 * for (; member1 < member2.length; i++) {
 *   yieldReturn(member2[member1]);
 *   return;
 * LABEL:
 * }
 * </code>
 * What's going to happen is that local variable 'i' is going
 * to be incremented - however, it wasn't set (as the state switch
 * makes the code jump directly before the incrementation, immediately
 * at the beginning of the method).
 *
 * This class takes care of that by making sure the local variables
 * are set to their corresponding class field members.
 *
 * The bytecode for each new member:
 * ALOAD 0
 * GETFIELD <i>field</i>
 * STORE <i>index</i>
 *
 * @see com.infomancers.collections.yield.asm.StateKeeper
 * @see com.infomancers.collections.yield.asm.LocalVariablePromoter
 */
public class LocalVariableAssigner extends ClassAdapter {
    private final LocalVariablePromoter promoter;
    private String owner;

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public LocalVariableAssigner(ClassVisitor cv, LocalVariablePromoter promoter) {
        super(cv);
        this.promoter = promoter;
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
            return new MethodAdapter(methodVisitor) {

                @Override
                public void visitCode() {
                    super.visitCode();

                    for (LocalVariablePromoter.NewMember newMember : promoter.getNewMembers()) {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, owner, newMember.name, newMember.desc);
                        mv.visitVarInsn(Opcodes.ISTORE + Util.offsetForDesc(newMember.desc), newMember.index);
                    }
                }
            };
        } else {
            return methodVisitor;
        }
    }
}
