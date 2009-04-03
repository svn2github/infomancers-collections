package com.infomancers.tests;

import com.infomancers.collections.yield.asmtree.enhancers.EnhancersFactory;
import com.infomancers.collections.yield.asmtree.enhancers.VarEnhancer;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 11:50:10 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(Parameterized.class)
public class EnhancersFactoryTests {

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{new VarInsnNode(Opcodes.ISTORE, 1), VarEnhancer.class},
                new Object[]{new VarInsnNode(Opcodes.LSTORE, 1), VarEnhancer.class},
                new Object[]{new VarInsnNode(Opcodes.DSTORE, 1), VarEnhancer.class},
                new Object[]{new VarInsnNode(Opcodes.FSTORE, 1), VarEnhancer.class},
                new Object[]{new VarInsnNode(Opcodes.ASTORE, 1), VarEnhancer.class},
                new Object[]{null, null}
        );
    }

    private final Class expected;
    private final AbstractInsnNode node;

    public EnhancersFactoryTests(AbstractInsnNode node, Class expected) {
        this.node = node;
        this.expected = expected;
    }

    @Test
    public void test() {
        if (expected == null) return;

        assertEquals(expected, EnhancersFactory.createEnhancer(node).getClass());
    }
}
