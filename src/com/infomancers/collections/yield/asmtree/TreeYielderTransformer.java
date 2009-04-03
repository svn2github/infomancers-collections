package com.infomancers.collections.yield.asmtree;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asmbase.AbstractYielderTransformer;
import com.infomancers.collections.yield.asmbase.Util;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import com.infomancers.collections.yield.asmtree.enhancers.EnhancersFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

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

public class TreeYielderTransformer extends AbstractYielderTransformer {
    private final EnhancersFactory factory = EnhancersFactory.instnace();

    public TreeYielderTransformer(boolean debug) {
        super(debug);
    }

    protected byte[] enhanceClass(ClassReader reader, YielderInformationContainer info) {
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (NewMember newMember : info.getSlots()) {
            FieldNode newField = new FieldNode(Opcodes.ACC_PRIVATE, newMember.getName(), newMember.getDesc(), null, null);

            node.fields.add(newField);
        }

        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, "state$", "B", null, (byte) 0));

        MethodNode method = findMethod(node);

        for (AbstractInsnNode instruction = method.instructions.getFirst();
             instruction != null;
             instruction = instruction.getNext()) {

            InsnEnhancer enhancer = factory.createEnhancer(instruction);
            instruction = enhancer.enhance(node, method.instructions, info, instruction);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        node.accept(writer);

        return writer.toByteArray();
    }

    private MethodNode findMethod(ClassNode clz) {
        for (Object m : clz.methods) {
            MethodNode method = (MethodNode) m;
            if (Util.isYieldNextCoreMethod(method.name, method.desc)) {
                return method;
            }
        }

        return null;
    }
}
