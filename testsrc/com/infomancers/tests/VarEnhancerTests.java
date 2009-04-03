package com.infomancers.tests;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.enhancers.VarEnhancer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 11:12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class VarEnhancerTests {

    private ClassNode owner;

    @Before
    public void before() {
        owner = new ClassNode();
        owner.name = "owner";
    }

    @Test
    public void loadInteger() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Integer),
                new NewMember(2, TypeDescriptor.Integer));

        final VarInsnNode insn = new VarInsnNode(Opcodes.ISTORE, 2);
        InsnList original = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, "field", "I"),
                insn
        );


        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, "field", "I"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, "slot$2", "I")
        );

        VarEnhancer enhancer = new VarEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    private static void compareLists(InsnList expected, InsnList original) {
        AbstractInsnNode expectedNode, originalNode;

        expectedNode = expected.getFirst();
        originalNode = original.getFirst();

        while (originalNode != null) {
            compareNodes(expectedNode, originalNode);

            originalNode = originalNode.getNext();
            expectedNode = expectedNode.getNext();
        }

        Assert.assertNull("Lists with different lengths", expectedNode);
    }

    private static InsnList createList(AbstractInsnNode... nodes) {
        InsnList list = new InsnList();
        AbstractInsnNode last = null;
        for (AbstractInsnNode node : nodes) {
            if (last == null) {
                list.insert(node);
            } else {
                list.insert(last, node);
            }
            last = node;
        }

        return list;
    }

    private static void compareNodes(AbstractInsnNode expectedNode, AbstractInsnNode originalNode) {
        Assert.assertEquals("opcode", expectedNode.getOpcode(), originalNode.getOpcode());

        if (expectedNode instanceof VarInsnNode) {
            VarInsnNode eVarNode = (VarInsnNode) expectedNode;
            VarInsnNode oVarNode = (VarInsnNode) originalNode;

            Assert.assertEquals("var", eVarNode.var, oVarNode.var);
        }
    }

}
