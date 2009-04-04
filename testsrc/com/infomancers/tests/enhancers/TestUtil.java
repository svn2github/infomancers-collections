package com.infomancers.tests.enhancers;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.objectweb.asm.tree.*;

/**
 * Copyright (c) 2009, Aviad Ben Dov
 * <p/>
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 3. Neither the name of Infomancers, Ltd. nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

class TestUtil {
    public static void compareLists(InsnList expected, InsnList original) {
        AbstractInsnNode expectedNode, originalNode;

        expectedNode = expected.getFirst();
        originalNode = original.getFirst();

        while (originalNode != null) {
            Assert.assertNotNull("expected is null", expectedNode);

            compareNodes(expectedNode, originalNode);

            originalNode = originalNode.getNext();
            expectedNode = expectedNode.getNext();
        }

        Assert.assertNull("Lists with different lengths", expectedNode);
    }

    public static InsnList createList(AbstractInsnNode... nodes) {
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

    public static void compareNodes(AbstractInsnNode expectedNode, AbstractInsnNode originalNode) {
        assertEquals("opcode", expectedNode.getOpcode(), originalNode.getOpcode());

        if (expectedNode instanceof VarInsnNode) {
            VarInsnNode eVarNode = (VarInsnNode) expectedNode;
            VarInsnNode oVarNode = (VarInsnNode) originalNode;

            assertEquals("var", eVarNode.var, oVarNode.var);
        } else if (expectedNode instanceof FieldInsnNode) {
            FieldInsnNode eFieldNode = (FieldInsnNode) expectedNode;
            FieldInsnNode oFieldNode = (FieldInsnNode) originalNode;

            assertEquals("name", eFieldNode.name, oFieldNode.name);
            assertEquals("desc", eFieldNode.desc, oFieldNode.desc);
        } else if (expectedNode instanceof IntInsnNode) {
            IntInsnNode eIntNode = (IntInsnNode) expectedNode;
            IntInsnNode oIntNode = (IntInsnNode) originalNode;

            assertEquals("operand", eIntNode.operand, oIntNode.operand);
        } else if (expectedNode instanceof TypeInsnNode) {
            TypeInsnNode eTypeNode = (TypeInsnNode) expectedNode;
            TypeInsnNode oTypeNode = (TypeInsnNode) originalNode;

            assertEquals("desc", eTypeNode.desc, oTypeNode.desc);
        }
    }
}
