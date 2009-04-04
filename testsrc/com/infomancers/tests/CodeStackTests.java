package com.infomancers.tests;

import com.infomancers.collections.yield.asmtree.CodeStack;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;

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

        Assert.assertEquals("node: " + node.getClass() + ", opcode: " + node.getOpcode(), expected, CodeStack.getChange(node));
    }

}
