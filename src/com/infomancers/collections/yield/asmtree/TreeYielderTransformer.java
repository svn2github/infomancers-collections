package com.infomancers.collections.yield.asmtree;

import com.infomancers.collections.yield.asmbase.AbstractYielderTransformer;
import com.infomancers.collections.yield.asmbase.YielderInformationContainer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbendov
 * Date: Apr 3, 2009
 * Time: 8:28:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeYielderTransformer extends AbstractYielderTransformer {
    public TreeYielderTransformer(boolean debug) {
        super(debug);
    }

    protected byte[] enhanceClass(ClassReader reader, YielderInformationContainer info) {
        ClassNode node = new ClassNode();
        reader.accept(node, 0);


        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        node.accept(writer);

        return writer.toByteArray();
    }
}
