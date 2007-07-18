package com.infomancers.collections.yield;

import com.infomancers.collections.yield.asm.YielderTransformer;

import java.lang.instrument.Instrumentation;

/**
 * The entry point for the Java instrumentation.
 *
 * This class is pointed to in the Premain-Class attribute
 * of the yielder JAR file (done by the ant task).
 */
public final class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new YielderTransformer());
    }

}
