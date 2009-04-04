package com.infomancers.collections.yield.asmtree.enhancers;

import com.infomancers.collections.yield.asmbase.Util;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
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

public class YieldReturnEnhancer implements PredicatedInsnEnhancer {
    public boolean shouldEnhance(AbstractInsnNode node) {
        if (node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            MethodInsnNode method = (MethodInsnNode) node;

            return Util.isInvokeYieldReturn(method.getOpcode(), method.name, method.desc);
        } else {
            return false;
        }
    }

    public AbstractInsnNode enhance(ClassNode clz, InsnList instructions, YielderInformationContainer info, AbstractInsnNode instruction) {
        final int state = info.takeState();

        assert state > 0;

        final VarInsnNode load0 = new VarInsnNode(Opcodes.ALOAD, 0);
        final IntInsnNode bipush = new IntInsnNode(Opcodes.BIPUSH, state);
        final FieldInsnNode setState = new FieldInsnNode(Opcodes.PUTFIELD, clz.name, "state$", "B");
        final InsnNode ret = new InsnNode(Opcodes.RETURN);

        instructions.insert(instruction, load0);
        instructions.insert(load0, bipush);
        instructions.insert(bipush, setState);
        instructions.insert(setState, ret);

        final AbstractInsnNode label = createOrReuseLabel(state, instructions, ret, info);
//        final AbstractInsnNode frame = createOrReuseFrame(instructions, label, info);

        return label;
    }

    private AbstractInsnNode createOrReuseLabel(int state, InsnList instructions, AbstractInsnNode previous, YielderInformationContainer info) {
        // if the next node is already a label, use that instead of creating a new one.
        final AbstractInsnNode next = previous.getNext();
        if (next != null && next.getType() == AbstractInsnNode.LABEL) {
            info.setStateLabel(state, (LabelNode) next);
            return next;
        } else {
            LabelNode label = info.getStateLabel(state);
            instructions.insert(previous, label);
            return label;
        }
    }

    private AbstractInsnNode createOrReuseFrame(InsnList instructions, AbstractInsnNode previous, YielderInformationContainer info) {
        // if the next node is already a frame, use that instead of creating a new one.
        final AbstractInsnNode next = previous.getNext();
        if (next != null && next.getType() == AbstractInsnNode.FRAME) {
            return next;
        } else {
            FrameNode frame = new FrameNode(Opcodes.F_NEW, 0, new Object[0], 0, new Object[0]);
            instructions.insert(previous, frame);
            return frame;
        }
    }
}
