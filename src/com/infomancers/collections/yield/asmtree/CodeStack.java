package com.infomancers.collections.yield.asmtree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public final class CodeStack {
    public static int getChange(AbstractInsnNode node) {
        switch (node.getOpcode()) {

            case Opcodes.ARRAYLENGTH:

            case Opcodes.CHECKCAST:

            case Opcodes.GETFIELD:
                return 0;

            case Opcodes.PUTFIELD:

            case Opcodes.IALOAD:
            case Opcodes.AALOAD:
            case Opcodes.DALOAD:
            case Opcodes.FALOAD:
            case Opcodes.LALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:

            case Opcodes.ISTORE:
            case Opcodes.ASTORE:
            case Opcodes.DSTORE:
            case Opcodes.FSTORE:
            case Opcodes.LSTORE:

                return -1;

            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:

            case Opcodes.ILOAD:
            case Opcodes.ALOAD:
            case Opcodes.DLOAD:
            case Opcodes.FLOAD:
            case Opcodes.LLOAD:

            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:

            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:

            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:

            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:


            case Opcodes.LDC:
                return 1;

            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKEVIRTUAL: {
                int result = -1;
                MethodInsnNode method = (MethodInsnNode) node;

                if (!method.desc.contains("V")) {
                    result++;
                }

                result -= countParams(method.desc);

                return result;
            }
            case Opcodes.INVOKESTATIC: {
                int result = 0;
                MethodInsnNode method = (MethodInsnNode) node;

                if (!method.desc.contains("V")) {
                    result++;
                }

                result -= countParams(method.desc);

                return result;
            }
            default:
                System.err.println("Returned default for node: " + node + ", opcode: " + node.getOpcode());
                return 0;
        }
    }

    private static int countParams(String desc) {
        final Pattern params = Pattern.compile("[BCDFIJSZ]|L[\\w\\d_]+(?:/[\\w\\d_]+)*;");
        int counter = 0;
        Matcher m = params.matcher(desc.split("\\)")[0]);
        while (m.find()) {
            counter++;
        }

        return counter;
    }
}
