package com.infomancers.tests.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.InsnEnhancer;
import com.infomancers.collections.yield.asmtree.Util;
import com.infomancers.collections.yield.asmtree.enhancers.EnhancersFactory;
import com.infomancers.tests.TestYIC;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
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

public final class EnhancerIntegrationTests extends EnhancerTestsBase {

    @Test
    public void getFromArrayMember_indexIsMember_storeToMember_intArray() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object),
                new NewMember(2, TypeDescriptor.Integer),
                new NewMember(3, TypeDescriptor.Integer)
        );

        NewMember[] slots = {
                info.getSlot(1), info.getSlot(2), info.getSlot(3)
        };

        AbstractInsnNode[] nodes = new AbstractInsnNode[]{
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new InsnNode(Opcodes.IALOAD),
                new VarInsnNode(Opcodes.ISTORE, 3)
        };

        InsnList actual = createList(nodes);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slots[0].getName(), slots[0].getDesc()),
                new TypeInsnNode(Opcodes.CHECKCAST, "[I"),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slots[1].getName(), slots[1].getDesc()),
                new InsnNode(Opcodes.IALOAD),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slots[2].getName(), slots[2].getDesc())
        );

        printList("Origin", actual);

        int i = 1;
        // enhance lines as required
        for (AbstractInsnNode instruction = actual.getLast();
             instruction != null;
             instruction = instruction.getPrevious()) {

            if (instruction.getType() == AbstractInsnNode.FRAME) {
                instruction = instruction.getPrevious();
                actual.remove(instruction.getNext());
                continue;
            }

            InsnEnhancer enhancer = EnhancersFactory.instnace().createEnhancer(instruction);
            instruction = enhancer.enhance(owner, actual, info, instruction);

            printList("" + i++, actual);
        }

        compareLists(expected, actual);
    }

    @Test
    public void getFromArrayMember_indexIsMember_storeToMember_booleanArray() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object),
                new NewMember(2, TypeDescriptor.Integer),
                new NewMember(3, TypeDescriptor.Boolean)
        );

        NewMember[] slots = {
                info.getSlot(1), info.getSlot(2), info.getSlot(3)
        };

        AbstractInsnNode[] nodes = new AbstractInsnNode[]{
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new InsnNode(Opcodes.BALOAD),
                new VarInsnNode(Opcodes.ISTORE, 3)
        };

        InsnList actual = createList(nodes);

        LabelNode l1 = new LabelNode(), l2 = new LabelNode();

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slots[0].getName(), slots[0].getDesc()),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slots[1].getName(), slots[1].getDesc()),
                new InsnNode(Opcodes.DUP_X1),   // stack: [..., index, array, index]
                new InsnNode(Opcodes.POP),      // stack: [..., index, array]
                new InsnNode(Opcodes.DUP_X1),   // stack: [..., array, index, array]
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;"),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getComponentType", "()Ljava/lang/Class;"),
                new MethodInsnNode(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;"),
                new JumpInsnNode(Opcodes.IF_ACMPNE, l1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/reflect/Array", "getByte", "(Ljava/lang/Object;I)B"),
                new JumpInsnNode(Opcodes.GOTO, l2),
                l1,
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/reflect/Array", "getBoolean", "(Ljava/lang/Object;I)Z"),
                l2,
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slots[2].getName(), slots[2].getDesc())
        );

        printList("Origin", actual);

        Util.enhanceLines(info, owner, actual, EnhancersFactory.instnace());

        compareLists(expected, actual);
    }
}
