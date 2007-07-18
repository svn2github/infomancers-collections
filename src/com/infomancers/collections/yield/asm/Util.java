package com.infomancers.collections.yield.asm;

import org.objectweb.asm.Opcodes;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 14, 2007
 * Time: 11:26:04 AM
 * To change this template use File | Settings | File Templates.
 */
final class Util {
    public static boolean isYieldNextCoreMethod(String name, String desc) {
        return "yieldNextCore".equals(name) && "()V".equals(desc);
    }

    static boolean isInvokeYieldReturn(int opcode, String name, String desc) {
        return opcode == Opcodes.INVOKEVIRTUAL && "yieldReturn".equals(name) && "(Ljava/lang/Object;)V".equals(desc);
    }

    public static boolean isYielderClassName(String name) {
        return "com/infomancers/collections/yield/Yielder".equals(name);
    }
}
