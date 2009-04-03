package com.infomancers.collections.yield.asmtree.enhancers;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.CodeStack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

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

    public AbstractInsnNode enhance(ClassNode clz, InsnList instructions, YielderInformationContainer info, AbstractInsnNode instruction) {
        final VarInsnNode varInstruction = (VarInsnNode) instruction;

        final NewMember member = info.getSlot(varInstruction.var);
        FieldInsnNode replacementInstruction = new FieldInsnNode(Opcodes.PUTFIELD, clz.name,
                member.getName(), member.getDesc());

        int stackSize = 0;
        AbstractInsnNode backNode = instruction;
        do {
            stackSize += CodeStack.getChange(backNode);
            backNode = backNode.getPrevious();
        } while (stackSize != 0);

        final VarInsnNode load0 = new VarInsnNode(Opcodes.ALOAD, 0);
        if (backNode == null) {
            instructions.insert(load0);
        } else {
            instructions.insert(backNode, load0);
        }

        instructions.insert(instruction, replacementInstruction);
        instructions.remove(instruction);

        return replacementInstruction;
    }

    public boolean shouldEnhance(AbstractInsnNode node) {
        return node.getOpcode() >= Opcodes.ISTORE && node.getOpcode() <= Opcodes.ASTORE;
    }
}
