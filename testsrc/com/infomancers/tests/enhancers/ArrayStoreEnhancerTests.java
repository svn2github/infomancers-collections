package com.infomancers.tests.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.InsnEnhancer;
import com.infomancers.collections.yield.asmtree.enhancers.ArrayStoreEnhancer;
import com.infomancers.tests.TestYIC;
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
public class ArrayStoreEnhancerTests extends EnhancerTestsBase {
    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{Opcodes.IASTORE, "[I", Opcodes.ICONST_0},
                new Object[]{Opcodes.FASTORE, "[F", Opcodes.FCONST_0},
                new Object[]{Opcodes.DASTORE, "[D", Opcodes.DCONST_0},
                new Object[]{Opcodes.LASTORE, "[J", Opcodes.LCONST_0},
                new Object[]{Opcodes.AASTORE, "[Ljava/lang/Object;", Opcodes.ACONST_NULL},
                new Object[]{Opcodes.BASTORE, "[B", Opcodes.ICONST_0},
                new Object[]{Opcodes.CASTORE, "[C", Opcodes.ICONST_0},
                new Object[]{Opcodes.SASTORE, "[S", Opcodes.ICONST_0}
        );
    }

    private final int opcode;
    private final String desc;
    private final int valueCode;

    public ArrayStoreEnhancerTests(int opcode, String desc, int valueCode) {
        this.opcode = opcode;
        this.desc = desc;
        this.valueCode = valueCode;
    }

    @Test
    public void arrayStore_fromConst_valueConst() {
        YielderInformationContainer info = new TestYIC(1);

        final InsnNode insn = new InsnNode(opcode);
        InsnList original = createList(
                new VarInsnNode(Opcodes.ALOAD, 1),
                new InsnNode(Opcodes.ICONST_0),
                new InsnNode(valueCode),
                insn);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 1),
                new TypeInsnNode(Opcodes.CHECKCAST, desc),
                new InsnNode(Opcodes.ICONST_0),
                new InsnNode(valueCode),
                new InsnNode(opcode)
        );

        InsnEnhancer enhancer = new ArrayStoreEnhancer();

        enhancer.enhance(owner, original, null, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void arrayStore_fromField_valueConst() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Integer));

        final InsnNode insn = new InsnNode(opcode);
        final NewMember slot = info.getSlot(1);
        InsnList original = createList(
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slot.getName(), slot.getDesc()),
                new InsnNode(valueCode),
                insn);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 1),
                new TypeInsnNode(Opcodes.CHECKCAST, desc),
                new VarInsnNode(Opcodes.ALOAD, 0),
                new FieldInsnNode(Opcodes.GETFIELD, owner.name, slot.getName(), slot.getDesc()),
                new InsnNode(valueCode),
                new InsnNode(opcode)
        );

        InsnEnhancer enhancer = new ArrayStoreEnhancer();

        enhancer.enhance(owner, original, null, info, insn);

        compareLists(expected, original);
    }

}
