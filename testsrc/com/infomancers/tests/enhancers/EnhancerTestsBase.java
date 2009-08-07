package com.infomancers.tests.enhancers;

import org.junit.Assert;
import org.junit.Before;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.TraceMethodVisitor;

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

public class EnhancerTestsBase {
    ClassNode owner;

    @Before
    public void before() {
        owner = new ClassNode();
        owner.name = "owner";
    }

    public static void printList(String title, InsnList list) {
        TraceMethodVisitor tracer = new TraceMethodVisitor();
        System.out.println("--- " + title + " ---");
        list.accept(tracer);
        System.out.println(tracer.text);
    }

    public static void compareLists(InsnList expected, InsnList actual) {
        try {
            AbstractInsnNode expectedNode, originalNode;

            expectedNode = expected.getFirst();
            originalNode = actual.getFirst();

            while (originalNode != null) {
                Assert.assertNotNull("expected is null", expectedNode);

                compareNodes(expectedNode, originalNode);

                originalNode = originalNode.getNext();
                expectedNode = expectedNode.getNext();
            }

            Assert.assertNull("Lists with different lengths", expectedNode);
        } catch (AssertionError e) {
            printList("Expected", expected);
            printList("Actual", actual);

            throw e;
        }
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

    public static void compareNodes(AbstractInsnNode expectedNode, AbstractInsnNode actualNode) {
        org.junit.Assert.assertEquals("type", expectedNode.getType(), actualNode.getType());
        org.junit.Assert.assertEquals("opcode", expectedNode.getOpcode(), actualNode.getOpcode());

        if (expectedNode instanceof VarInsnNode) {
            VarInsnNode eVarNode = (VarInsnNode) expectedNode;
            VarInsnNode oVarNode = (VarInsnNode) actualNode;

            org.junit.Assert.assertEquals("var", eVarNode.var, oVarNode.var);
        } else if (expectedNode instanceof FieldInsnNode) {
            FieldInsnNode eFieldNode = (FieldInsnNode) expectedNode;
            FieldInsnNode oFieldNode = (FieldInsnNode) actualNode;

            org.junit.Assert.assertEquals("name", eFieldNode.name, oFieldNode.name);
            org.junit.Assert.assertEquals("desc", eFieldNode.desc, oFieldNode.desc);
        } else if (expectedNode instanceof IntInsnNode) {
            IntInsnNode eIntNode = (IntInsnNode) expectedNode;
            IntInsnNode oIntNode = (IntInsnNode) actualNode;

            org.junit.Assert.assertEquals("operand", eIntNode.operand, oIntNode.operand);
        } else if (expectedNode instanceof TypeInsnNode) {
            TypeInsnNode eTypeNode = (TypeInsnNode) expectedNode;
            TypeInsnNode oTypeNode = (TypeInsnNode) actualNode;

            org.junit.Assert.assertEquals("desc", eTypeNode.desc, oTypeNode.desc);
        }
    }
}
