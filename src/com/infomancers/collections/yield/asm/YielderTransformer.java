package com.infomancers.collections.yield.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * A class transformation file which creates three chains of ASM visitors, all three
 * eventually create the enhanced class which keeps state and supports the yield idea.
 * <p/>
 * The chains are:
 * <p/>
 * 1. reader (of origin) -> returnCounter -> null
 * 2. reader (of origin) -> stateKeeper (using returnCounter) -> promoter -> writer (to output1)
 * 3. reader (of output1) -> assigner (using promoter) -> writer (to output2)
 * <p/>
 * And then returns output2.
 */
public class YielderTransformer implements ClassFileTransformer {
    private final boolean debug;


    public YielderTransformer(boolean debug) {
        this.debug = debug;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] result = classfileBuffer;

        if (classBeingRedefined == null) {
            // first pass - gather statistics
            ClassReader reader = new ClassReader(classfileBuffer);
            YieldReturnCounter counter = new YieldReturnCounter(new EmptyVisitor());
            YielderChecker checker = new YielderChecker(counter);

            reader.accept(checker, 0);

            if (checker.isYielder()) {
                // second pass - write new code
                ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                ClassVisitor nextVisitor = writer;
                if (debug) {
                    nextVisitor = new TraceClassVisitor(writer, new PrintWriter(System.out));
                }
                LocalVariablePromoter promoter = new LocalVariablePromoter(nextVisitor);
                StateKeeper stateKeeper = new StateKeeper(promoter, counter);

                reader.accept(stateKeeper, 0);

                // third pass - write finalizers
                ClassReader thirdReader = new ClassReader(writer.toByteArray());
                ClassWriter thirdWriter = new ClassWriter(thirdReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                nextVisitor = thirdWriter;
                if (debug) {
                    nextVisitor = new TraceClassVisitor(thirdWriter, new PrintWriter(System.out));
                }
                LocalVariableAssigner assigner = new LocalVariableAssigner(nextVisitor, promoter);

                thirdReader.accept(assigner, 0);

                result = thirdWriter.toByteArray();
            }
        }
        return result;
    }
}
