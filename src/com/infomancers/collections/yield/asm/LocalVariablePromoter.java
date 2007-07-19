package com.infomancers.collections.yield.asm;

import org.objectweb.asm.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Promotes all local variables inside the <code>yieldNextCore</code> implementation
 * to class member fields.
 */
final class LocalVariablePromoter extends ClassAdapter {
    private final static String MEMBER_NAME_PREFIX = "member";

    public static class NewMember {
        public String name;
        public String desc;
        public int index;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            NewMember newMember = (NewMember) o;

            return index == newMember.index && desc.equals(newMember.desc) && name.equals(newMember.name);

        }

        @Override
        public int hashCode() {
            int result;
            result = name.hashCode();
            result = 31 * result + desc.hashCode();
            result = 31 * result + index;
            return result;
        }
    }

    private final Set<NewMember> newMembers = new HashSet<NewMember>();

    private String owner;


    public Iterable<NewMember> getNewMembers() {
        return newMembers;
    }

    /**
     * Constructs a new {@link org.objectweb.asm.ClassAdapter} object.
     *
     * @param cv the class visitor to which this adapter must delegate calls.
     */
    public LocalVariablePromoter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visitEnd() {
        for (NewMember newMember : newMembers) {
            visitField(Opcodes.ACC_PRIVATE, newMember.name, newMember.desc, newMember.desc, null);
        }

        super.visitEnd();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        newMembers.clear();
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
//        private final String[] descs = {"I", "L", "F", "D", "Ljava/lang/Object;"};

        public MyMethodAdapter(MethodVisitor methodVisitor) {
            super(methodVisitor);
        }

        @Override
        public void visitVarInsn(final int opcode, final int var) {
            if (var == 0) {
                mv.visitVarInsn(opcode, var);
            } else {
                String memberName = MEMBER_NAME_PREFIX + var;
                NewMember newMember = new NewMember();
                newMember.name = memberName;
                newMember.index = var;

                if (opcode > Opcodes.ALOAD) {
                    mv.visitVarInsn(opcode, var);
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ISTORE);
                    createPutField(var, opcode - Opcodes.ISTORE + Opcodes.ILOAD, memberName, newMember.desc);
                } else {
                    newMember.desc = Util.descForOffset(opcode - Opcodes.ILOAD);
                    createGetField(memberName, newMember.desc);
                }
                newMembers.add(newMember);
            }
        }

        private void createPutField(int var, int op, String memberName, String desc) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(op, var);
            mv.visitFieldInsn(Opcodes.PUTFIELD, owner, memberName, desc);
        }

        private void createGetField(String memberName, String desc) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, owner, memberName, desc);
        }
    }
}
