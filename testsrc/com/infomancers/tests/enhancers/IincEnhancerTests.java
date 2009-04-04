package com.infomancers.tests.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.InsnEnhancer;
import com.infomancers.collections.yield.asmtree.enhancers.IincEnhancer;
import com.infomancers.tests.TestYIC;
import static com.infomancers.tests.enhancers.TestUtil.compareLists;
import static com.infomancers.tests.enhancers.TestUtil.createList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.Collection;

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

@RunWith(Parameterized.class)
public class IincEnhancerTests extends EnhancerTestsBase {


    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{1},
                new Object[]{-1}
        );
    }

    private final int inc;

    public IincEnhancerTests(int inc) {
        this.inc = inc;
    }

    @Test
    public void inc() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Integer));

        final AbstractInsnNode insn = new IincInsnNode(1, inc);

        InsnList original = createList(insn);

        final NewMember slot = info.getSlot(1);
        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slot.getName(), slot.getDesc()),
                new IntInsnNode(Opcodes.BIPUSH, inc),
                new InsnNode(Opcodes.IADD),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new IincEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void inc_memberIsObject() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object));

        final AbstractInsnNode insn = new IincInsnNode(1, inc);

        InsnList original = createList(insn);

        final NewMember slot = info.getSlot(1);
        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slot.getName(), slot.getDesc()),
                new TypeInsnNode(Opcodes.CHECKCAST, "java/lang/Integer"),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I"),
                new IntInsnNode(Opcodes.BIPUSH, inc),
                new InsnNode(Opcodes.IADD),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new IincEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }
}
