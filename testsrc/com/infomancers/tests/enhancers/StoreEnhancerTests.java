package com.infomancers.tests.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.InsnEnhancer;
import com.infomancers.collections.yield.asmtree.enhancers.StoreEnhancer;
import com.infomancers.tests.TestYIC;
import static com.infomancers.tests.enhancers.TestUtil.compareLists;
import static com.infomancers.tests.enhancers.TestUtil.createList;
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

public class StoreEnhancerTests extends EnhancerTestsBase {
    @Test
    public void getfield_istore2() {
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

        InsnEnhancer enhancer = new StoreEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void istore_memberIsObject() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object));

        final VarInsnNode insn = new VarInsnNode(Opcodes.ISTORE, 1);
        InsnList original = createList(
                new InsnNode(Opcodes.ICONST_0),
                insn
        );

        NewMember slot = info.getSlot(1);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new InsnNode(Opcodes.ICONST_0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new StoreEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void lstore_memberIsObject() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object));

        final VarInsnNode insn = new VarInsnNode(Opcodes.LSTORE, 1);
        InsnList original = createList(
                new InsnNode(Opcodes.LCONST_0),
                insn
        );

        NewMember slot = info.getSlot(1);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new InsnNode(Opcodes.LCONST_0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new StoreEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void dstore_memberIsObject() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object));

        final VarInsnNode insn = new VarInsnNode(Opcodes.DSTORE, 1);
        InsnList original = createList(
                new InsnNode(Opcodes.DCONST_0),
                insn
        );

        NewMember slot = info.getSlot(1);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new InsnNode(Opcodes.DCONST_0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new StoreEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }

    @Test
    public void fstore_memberIsObject() {
        YielderInformationContainer info = new TestYIC(1,
                new NewMember(1, TypeDescriptor.Object));

        final VarInsnNode insn = new VarInsnNode(Opcodes.FSTORE, 1);
        InsnList original = createList(
                new InsnNode(Opcodes.FCONST_0),
                insn
        );

        NewMember slot = info.getSlot(1);

        InsnList expected = createList(
                new VarInsnNode(Opcodes.ALOAD, 0),
                new InsnNode(Opcodes.FCONST_0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;"),
                new FieldInsnNode(Opcodes.PUTFIELD, owner.name, slot.getName(), slot.getDesc())
        );

        InsnEnhancer enhancer = new StoreEnhancer();

        enhancer.enhance(owner, original, info, insn);

        compareLists(expected, original);
    }


}
