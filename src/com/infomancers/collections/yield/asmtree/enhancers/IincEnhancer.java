package com.infomancers.collections.yield.asmtree.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

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

public final class IincEnhancer implements PredicatedInsnEnhancer {
    public AbstractInsnNode enhance(ClassNode clz, InsnList instructions, List<AbstractInsnNode> limits, YielderInformationContainer info, AbstractInsnNode instruction) {
        IincInsnNode iinc = (IincInsnNode) instruction;

        NewMember member = info.getSlot(iinc.var);

        AbstractInsnNode aload0_0 = new VarInsnNode(Opcodes.ALOAD, 0);
        AbstractInsnNode aload0_1 = new VarInsnNode(Opcodes.ALOAD, 0);
        AbstractInsnNode getfield_2 = new FieldInsnNode(Opcodes.GETFIELD, clz.name, member.getName(), member.getDesc());

        AbstractInsnNode bipush_3 = new IntInsnNode(Opcodes.BIPUSH, iinc.incr);
        AbstractInsnNode iadd_4 = new InsnNode(Opcodes.IADD);
        AbstractInsnNode putfield_5 = new FieldInsnNode(Opcodes.PUTFIELD, clz.name, member.getName(), member.getDesc());

        instructions.insert(instruction, aload0_0);
        instructions.insert(aload0_0, aload0_1);
        instructions.insert(aload0_1, getfield_2);
        instructions.insert(getfield_2, bipush_3);
        instructions.insert(bipush_3, iadd_4);
        instructions.insert(iadd_4, putfield_5);

        // should we unbox and re-box the value?
        if (member.getType() == TypeDescriptor.Object) {
            AbstractInsnNode unbox_0 = new TypeInsnNode(Opcodes.CHECKCAST, "java/lang/Integer");
            AbstractInsnNode unbox_1 = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");

            AbstractInsnNode rebox_0 = new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");

            instructions.insert(getfield_2, unbox_0);
            instructions.insert(unbox_0, unbox_1);

            instructions.insert(iadd_4, rebox_0);
        }

        instructions.remove(instruction);

        return putfield_5;
    }

    public boolean shouldEnhance(AbstractInsnNode node) {
        return node.getOpcode() == Opcodes.IINC;
    }
}
