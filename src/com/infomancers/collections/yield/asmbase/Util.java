package com.infomancers.collections.yield.asmbase;

import com.infomancers.collections.yield.Yielder;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import org.objectweb.asm.Opcodes;

/**
 * Copyright (c) 2007, Aviad Ben Dov
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 3. Neither the name of Infomancers, Ltd. nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
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
 *
 */

/**
 * Utility class for doing redundant checks on fields, variables and methods while
 * visiting different classes.
 */
public final class Util {

    public static boolean isYieldNextCoreMethod(String name, String desc) {
        return "yieldNextCore".equals(name) && "()V".equals(desc);
    }

    public static boolean isInvokeYieldReturn(int opcode, String name, String desc) {
        return opcode == Opcodes.INVOKEVIRTUAL && "yieldReturn".equals(name) && "(Ljava/lang/Object;)V".equals(desc);
    }

    public static boolean isInvokeYieldBreak(int opcode, String name, String desc) {
        return opcode == Opcodes.INVOKEVIRTUAL && "yieldBreak".equals(name) && "()V".equals(desc);
    }

    public static boolean isYielderClassName(String name) {
        return "com/infomancers/collections/yield/Yielder".equals(name);
    }

    public static int offsetForDesc(String desc) {
        for (int i = 0; i < TypeDescriptor.values().length; i++) {
            String cur = TypeDescriptor.values()[i].getDesc();
            if (cur.equals(desc)) {
                return i;
            }
        }

        return -1;
    }

    public static TypeDescriptor typeForOffset(int offset) {
        return TypeDescriptor.values()[offset];
    }

    public static boolean isYielderInHierarchyTree(String className) {
        String name = className.replace('/', '.');
        try {
            return Yielder.class.isAssignableFrom(Class.forName(name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
