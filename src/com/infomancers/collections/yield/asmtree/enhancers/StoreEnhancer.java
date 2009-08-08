package com.infomancers.collections.yield.asmtree.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.CodeStack;
import com.infomancers.collections.yield.asmtree.Util;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.text.MessageFormat;
import java.util.List;

/**
 * Copyright (c) 2007, Aviad Ben Dov
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

public final class StoreEnhancer implements PredicatedInsnEnhancer {

    private static final String[] wrappers = new String[]{
            "java/lang/Integer",
            "java/lang/Long",
            "java/lang/Float",
            "java/lang/Double"
    };

    private static final String sigTypes = "IJFD";

    private static MessageFormat valueOfSignatureFormat = new MessageFormat("({0})L{1};");

    public AbstractInsnNode enhance(ClassNode clz, InsnList instructions, List<AbstractInsnNode> limits, YielderInformationContainer info, AbstractInsnNode instruction) {
        final VarInsnNode varInstruction = (VarInsnNode) instruction;

        final NewMember member = info.getSlot(varInstruction.var);
        FieldInsnNode replacementInstruction = new FieldInsnNode(Opcodes.PUTFIELD, clz.name,
                member.getName(), member.getDesc());

        AbstractInsnNode backNode = CodeStack.backUntilStackSizedAt(instruction, 0, true, limits);

        final VarInsnNode load0 = new VarInsnNode(Opcodes.ALOAD, 0);
        Util.insertOrAdd(instructions, backNode, load0);

        instructions.insert(instruction, replacementInstruction);
        instructions.remove(instruction);

        // Dealing with case when the member is an object due to local variable merge,
        // but the store code is for a primitive - in these cases, box the primitive
        // value in an object.
        // Todo: Maybe the slots can have different types, for example slot$2_i and slot$2_a?
        if (varInstruction.getOpcode() != Opcodes.ASTORE &&
                member.getType() == TypeDescriptor.Object) {

            final int offset = varInstruction.getOpcode() - Opcodes.ISTORE;

            final String desc = valueOfSignatureFormat.format(new Object[]{sigTypes.charAt(offset), wrappers[offset]});

            instructions.insertBefore(replacementInstruction,
                    new MethodInsnNode(Opcodes.INVOKESTATIC, wrappers[offset], "valueOf", desc));
        }

        return replacementInstruction;
    }

    public boolean shouldEnhance(AbstractInsnNode node) {
        return node.getOpcode() >= Opcodes.ISTORE && node.getOpcode() <= Opcodes.ASTORE;
    }
}
