package com.infomancers.tests;

import com.infomancers.collections.yield.asmtree.CodeStack;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 10:36:58 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Parameterized.class)
public class CodeStackTests {
    private final AbstractInsnNode node;
    private final int expected;
    private final boolean changeStack;

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{new LabelNode(), 0, false},

                new Object[]{new FieldInsnNode(Opcodes.PUTFIELD, "Owner", "myint", "I"), -2, true},
                new Object[]{new FieldInsnNode(Opcodes.GETFIELD, "Owner", "myint", "I"), 0, true},
                new Object[]{new InsnNode(Opcodes.ARRAYLENGTH), 0, true},
                new Object[]{new InsnNode(Opcodes.CHECKCAST), 0, false},

                new Object[]{new InsnNode(Opcodes.BIPUSH), 1, true},
                new Object[]{new InsnNode(Opcodes.SIPUSH), 1, true},

                new Object[]{new InsnNode(Opcodes.ILOAD), 1, true},
                new Object[]{new InsnNode(Opcodes.FLOAD), 1, true},
                new Object[]{new InsnNode(Opcodes.DLOAD), 1, true},
                new Object[]{new InsnNode(Opcodes.LLOAD), 1, true},
                new Object[]{new InsnNode(Opcodes.ALOAD), 1, true},

                new Object[]{new InsnNode(Opcodes.IALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.FALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.DALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.LALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.AALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.BALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.CALOAD), -1, true},
                new Object[]{new InsnNode(Opcodes.SALOAD), -1, true},

                new Object[]{new InsnNode(Opcodes.IASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.FASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.DASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.LASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.AASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.BASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.CASTORE), -3, true},
                new Object[]{new InsnNode(Opcodes.SASTORE), -3, true},

                new Object[]{new InsnNode(Opcodes.ISTORE), -1, true},
                new Object[]{new InsnNode(Opcodes.FSTORE), -1, true},
                new Object[]{new InsnNode(Opcodes.DSTORE), -1, true},
                new Object[]{new InsnNode(Opcodes.LSTORE), -1, true},
                new Object[]{new InsnNode(Opcodes.ASTORE), -1, true},

                new Object[]{new InsnNode(Opcodes.ICONST_0), 1, true},
                new Object[]{new InsnNode(Opcodes.ICONST_1), 1, true},
                new Object[]{new InsnNode(Opcodes.ICONST_2), 1, true},
                new Object[]{new InsnNode(Opcodes.ICONST_3), 1, true},
                new Object[]{new InsnNode(Opcodes.ICONST_4), 1, true},
                new Object[]{new InsnNode(Opcodes.ICONST_5), 1, true},

                new Object[]{new InsnNode(Opcodes.LCONST_0), 1, true},
                new Object[]{new InsnNode(Opcodes.LCONST_1), 1, true},

                new Object[]{new InsnNode(Opcodes.DCONST_0), 1, true},
                new Object[]{new InsnNode(Opcodes.DCONST_1), 1, true},

                new Object[]{new InsnNode(Opcodes.FCONST_0), 1, true},
                new Object[]{new InsnNode(Opcodes.FCONST_1), 1, true},
                new Object[]{new InsnNode(Opcodes.FCONST_2), 1, true},

                new Object[]{new InsnNode(Opcodes.ACONST_NULL), 1, true},

                new Object[]{new InsnNode(Opcodes.IADD), -1, true},
                new Object[]{new InsnNode(Opcodes.LADD), -1, true},
                new Object[]{new InsnNode(Opcodes.FADD), -1, true},
                new Object[]{new InsnNode(Opcodes.DADD), -1, true},

                new Object[]{new InsnNode(Opcodes.ISUB), -1, true},
                new Object[]{new InsnNode(Opcodes.LSUB), -1, true},
                new Object[]{new InsnNode(Opcodes.FSUB), -1, true},
                new Object[]{new InsnNode(Opcodes.DSUB), -1, true},

                new Object[]{new InsnNode(Opcodes.IMUL), -1, true},
                new Object[]{new InsnNode(Opcodes.LMUL), -1, true},
                new Object[]{new InsnNode(Opcodes.FMUL), -1, true},
                new Object[]{new InsnNode(Opcodes.DMUL), -1, true},

                new Object[]{new InsnNode(Opcodes.IDIV), -1, true},
                new Object[]{new InsnNode(Opcodes.LDIV), -1, true},
                new Object[]{new InsnNode(Opcodes.FDIV), -1, true},
                new Object[]{new InsnNode(Opcodes.DDIV), -1, true},

                new Object[]{new InsnNode(Opcodes.IREM), -1, true},
                new Object[]{new InsnNode(Opcodes.LREM), -1, true},
                new Object[]{new InsnNode(Opcodes.FREM), -1, true},
                new Object[]{new InsnNode(Opcodes.DREM), -1, true},

                new Object[]{new MultiANewArrayInsnNode("java/lang/Object", 1), -1, true},
                new Object[]{new MultiANewArrayInsnNode("java/lang/Object", 2), -2, true},
                new Object[]{new MultiANewArrayInsnNode("java/lang/Object", 3), -3, true},

                new Object[]{new LdcInsnNode(null), 1, true},

                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()B"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()C"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()D"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()F"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()I"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()J"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()S"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()Z"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()Ljava/lang/Object;"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)B"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)C"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)D"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)F"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)I"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)J"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)S"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)Z"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)Ljava/lang/Object;"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(B)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(C)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(D)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(F)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(J)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(S)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Z)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/Object;)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)[I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()[I"), 0, true},

                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()B"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()C"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()D"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()F"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()I"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()J"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()S"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()Z"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()Ljava/lang/Object;"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)B"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)C"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)D"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)F"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)I"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)J"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)S"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)Z"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)Ljava/lang/Object;"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(B)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(C)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(D)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(F)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(J)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(S)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Z)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/Object;)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)[I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()[I"), 0, true},

                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()B"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()C"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()D"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()F"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()I"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()J"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()S"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()Z"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()Ljava/lang/Object;"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)B"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)C"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)D"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)F"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)I"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)J"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)S"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)Z"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)Ljava/lang/Object;"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(B)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(C)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(D)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(F)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(J)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(S)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Z)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/Object;)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)[I"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)V"), -4, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()[I"), 0, true},

                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()V"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()B"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()C"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()D"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()F"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()I"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()J"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()S"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()Z"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()Ljava/lang/Object;"), 1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)B"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)C"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)D"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)F"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)I"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)J"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)S"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)Z"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)Ljava/lang/Object;"), 0, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(B)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(C)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(D)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(F)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(J)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(S)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Z)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/Object;)V"), -1, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(III)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(III)I"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)I"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)[I"), -2, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)V"), -3, true},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()[I"), 1, true},


                new Object[]{null, 0, false}
        );
    }

    public CodeStackTests(AbstractInsnNode node, int expected, boolean changeStack) {
        this.node = node;
        this.expected = expected;
        this.changeStack = changeStack;
    }

    @Test
    public void test() {
        if (node == null) return;

        Assert.assertEquals("getChange - " + toString(node), expected, CodeStack.getChange(node));
        Assert.assertEquals("changeStack - " + toString(node), changeStack, CodeStack.changeStack(node));
    }

    private String toString(AbstractInsnNode node) {
        String s = "opcode: " + node.getOpcode() + ", type: " + node.getClass().getSimpleName();

        if (node instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNode = (MethodInsnNode) node;
            s += ", desc: " + methodInsnNode.desc;
        }

        return s;
    }

}
