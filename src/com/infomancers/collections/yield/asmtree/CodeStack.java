package com.infomancers.collections.yield.asmtree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

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

public class CodeStack {
    private static interface Change {
        int change();
    }

    private static class StaticChange implements Change {
        private final int change;

        public StaticChange(int change) {
            this.change = change;
        }

        public int change() {
            return change;
        }
    }

    public static int getChange(AbstractInsnNode node) {
        switch (node.getOpcode()) {
            case Opcodes.ARRAYLENGTH:
                return 0;

            case Opcodes.PUTFIELD:
                return -1;
            case Opcodes.GETFIELD:
                return 0;

            case Opcodes.BIPUSH:
                return 1;
            case Opcodes.SIPUSH:
                return 1;

            case Opcodes.ILOAD:
                return 1;
            case Opcodes.ALOAD:
                return 1;
            case Opcodes.DLOAD:
                return 1;
            case Opcodes.FLOAD:
                return 1;
            case Opcodes.LLOAD:
                return 1;

            case Opcodes.IALOAD:
                return -1;
            case Opcodes.AALOAD:
                return -1;
            case Opcodes.DALOAD:
                return -1;
            case Opcodes.FALOAD:
                return -1;
            case Opcodes.LALOAD:
                return -1;

            case Opcodes.ISTORE:
                return -1;
            case Opcodes.ASTORE:
                return -1;
            case Opcodes.DSTORE:
                return -1;
            case Opcodes.FSTORE:
                return -1;
            case Opcodes.LSTORE:
                return -1;

            case Opcodes.ICONST_0:
                return 1;
            case Opcodes.ICONST_1:
                return 1;
            case Opcodes.ICONST_2:
                return 1;
            case Opcodes.ICONST_3:
                return 1;
            case Opcodes.ICONST_4:
                return 1;
            case Opcodes.ICONST_5:
                return 1;
            default:
                System.err.println("Returned default for node: " + node + ", opcode: " + node.getOpcode());
                return 0;
        }
    }
}
