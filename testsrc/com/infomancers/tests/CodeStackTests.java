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

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{new FieldInsnNode(Opcodes.PUTFIELD, "Owner", "myint", "I"), -1},
                new Object[]{new FieldInsnNode(Opcodes.GETFIELD, "Owner", "myint", "I"), 0},
                new Object[]{new InsnNode(Opcodes.ARRAYLENGTH), 0},
                new Object[]{new InsnNode(Opcodes.CHECKCAST), 0},

                new Object[]{new InsnNode(Opcodes.BIPUSH), 1},
                new Object[]{new InsnNode(Opcodes.SIPUSH), 1},

                new Object[]{new InsnNode(Opcodes.ILOAD), 1},
                new Object[]{new InsnNode(Opcodes.FLOAD), 1},
                new Object[]{new InsnNode(Opcodes.DLOAD), 1},
                new Object[]{new InsnNode(Opcodes.LLOAD), 1},
                new Object[]{new InsnNode(Opcodes.ALOAD), 1},

                new Object[]{new InsnNode(Opcodes.IALOAD), -1},
                new Object[]{new InsnNode(Opcodes.FALOAD), -1},
                new Object[]{new InsnNode(Opcodes.DALOAD), -1},
                new Object[]{new InsnNode(Opcodes.LALOAD), -1},
                new Object[]{new InsnNode(Opcodes.AALOAD), -1},
                new Object[]{new InsnNode(Opcodes.BALOAD), -1},
                new Object[]{new InsnNode(Opcodes.CALOAD), -1},
                new Object[]{new InsnNode(Opcodes.SALOAD), -1},

                new Object[]{new InsnNode(Opcodes.ISTORE), -1},
                new Object[]{new InsnNode(Opcodes.FSTORE), -1},
                new Object[]{new InsnNode(Opcodes.DSTORE), -1},
                new Object[]{new InsnNode(Opcodes.LSTORE), -1},
                new Object[]{new InsnNode(Opcodes.ASTORE), -1},

                new Object[]{new InsnNode(Opcodes.ICONST_0), 1},
                new Object[]{new InsnNode(Opcodes.ICONST_1), 1},
                new Object[]{new InsnNode(Opcodes.ICONST_2), 1},
                new Object[]{new InsnNode(Opcodes.ICONST_3), 1},
                new Object[]{new InsnNode(Opcodes.ICONST_4), 1},
                new Object[]{new InsnNode(Opcodes.ICONST_5), 1},

                new Object[]{new InsnNode(Opcodes.LCONST_0), 1},
                new Object[]{new InsnNode(Opcodes.LCONST_1), 1},

                new Object[]{new InsnNode(Opcodes.DCONST_0), 1},
                new Object[]{new InsnNode(Opcodes.DCONST_1), 1},

                new Object[]{new InsnNode(Opcodes.FCONST_0), 1},
                new Object[]{new InsnNode(Opcodes.FCONST_1), 1},
                new Object[]{new InsnNode(Opcodes.FCONST_2), 1},

                new Object[]{new LdcInsnNode(null), 1},

                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()B"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()C"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()D"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()F"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()I"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()J"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()S"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()Z"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()Ljava/lang/Object;"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)B"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)C"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)D"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)F"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)I"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)J"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)S"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)Z"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(I)Ljava/lang/Object;"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(B)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(C)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(D)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(F)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(J)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(S)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Z)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/Object;)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "(III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)[I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "([III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "owner", "method", "()[I"), 0},

                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()B"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()C"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()D"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()F"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()I"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()J"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()S"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()Z"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()Ljava/lang/Object;"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)B"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)C"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)D"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)F"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)I"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)J"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)S"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)Z"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(I)Ljava/lang/Object;"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(B)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(C)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(D)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(F)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(J)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(S)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Z)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/Object;)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "(III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)[I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "([III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESPECIAL, "owner", "method", "()[I"), 0},

                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()B"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()C"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()D"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()F"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()I"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()J"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()S"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()Z"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()Ljava/lang/Object;"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)B"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)C"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)D"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)F"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)I"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)J"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)S"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)Z"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(I)Ljava/lang/Object;"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(B)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(C)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(D)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(F)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(J)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(S)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Z)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/Object;)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "(III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)[I"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "([III)V"), -4},
                new Object[]{new MethodInsnNode(Opcodes.INVOKEINTERFACE, "owner", "method", "()[I"), 0},

                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()V"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()B"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()C"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()D"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()F"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()I"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()J"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()S"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()Z"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()Ljava/lang/Object;"), 1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)B"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)C"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)D"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)F"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)I"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)J"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)S"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)Z"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(I)Ljava/lang/Object;"), 0},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(B)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(C)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(D)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(F)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(J)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(S)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Z)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/Object;)V"), -1},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/Object;Ljava/lang/Object;)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(Ljava/lang/String;Ljava/lang/Object;)V"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(III)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "(III)I"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)I"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)[I"), -2},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "([III)V"), -3},
                new Object[]{new MethodInsnNode(Opcodes.INVOKESTATIC, "owner", "method", "()[I"), 1},


                new Object[]{null, 0}
        );
    }

    public CodeStackTests(AbstractInsnNode node, int expected) {
        this.node = node;
        this.expected = expected;
    }

    @Test
    public void test() {
        if (node == null) return;

        Assert.assertEquals(toString(node), expected, CodeStack.getChange(node));
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
