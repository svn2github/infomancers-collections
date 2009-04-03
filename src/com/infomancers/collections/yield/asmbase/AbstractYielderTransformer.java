package com.infomancers.collections.yield.asmbase;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 8:32:12 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractYielderTransformer implements ClassFileTransformer {
    private final boolean debug;

    public AbstractYielderTransformer(boolean debug) {
        this.debug = debug;
    }

    public final byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] result = classfileBuffer;

        if (classBeingRedefined == null) {
            try {
                // first pass - gather statistics
                ClassReader reader = new ClassReader(classfileBuffer);
                final YieldReturnCounter counter = new YieldReturnCounter(new EmptyVisitor());
                final LocalVariableMapper mapper = new LocalVariableMapper(counter);
                YielderChecker checker = new YielderChecker(mapper);

                reader.accept(checker, 0);

                if (checker.isYielder()) {
                    // second pass - write new code
                    trace("Before", result);

                    YielderInformationContainer info = new DelegatingInformationContainer(counter, mapper);

                    result = enhanceClass(reader, info);

                    trace("After", result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    protected abstract byte[] enhanceClass(ClassReader reader, YielderInformationContainer info);

    private void trace(String title, byte[] classfileBytes) {
        if (debug) {
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(new PrintWriter(System.out));

            System.out.println();
            System.out.println("<------------- " + title + " ---------------> ");
            new ClassReader(classfileBytes).accept(traceClassVisitor, 0);
        }
    }

}
