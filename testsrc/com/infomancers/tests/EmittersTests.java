package com.infomancers.tests;

import com.infomancers.collections.yield.asm.delayed.MethodEmitter;
import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

/**
 * Tests for the emitters, located in the yielder.asm.delayed package.
 */
public class EmittersTests {

    @Test
    public void parameterlessMethodReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("()L");

        Assert.assertEquals(1, methodEmitter.pushAmount());
    }

    @Test
    public void parameterlessMethodNoPop() {
        MethodEmitter methodEmitter = createMethodEmitter("()L");

        Assert.assertEquals(0, methodEmitter.popAmount());
    }

    @Test
    public void methodWithOneParameterPopCount() {
        MethodEmitter methodEmitter = createMethodEmitter("(I)V");

        Assert.assertEquals(1, methodEmitter.popAmount());
    }

    @Test
    public void methodWithTwoParameterPopCount() {
        MethodEmitter methodEmitter = createMethodEmitter("(II)V");

        Assert.assertEquals(2, methodEmitter.popAmount());
    }

    @Test
    public void methodWithTwoObjectParameterPopCount() {
        MethodEmitter methodEmitter = createMethodEmitter("(Ljava/lang/Object;Ljava/lang/Object;)V");

        Assert.assertEquals(2, methodEmitter.popAmount());
    }

    @Test
    public void methodWithOneParameterNoReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(I)V");

        Assert.assertEquals(0, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithTwoParameterNoReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(II)V");

        Assert.assertEquals(0, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithTwoObjectParameterNoReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(Ljava/lang/Object;Ljava/lang/Object;)V");

        Assert.assertEquals(0, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithOneParameterWithReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(I)I");

        Assert.assertEquals(1, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithTwoParameterWithReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(II)I");

        Assert.assertEquals(1, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithTwoObjectParameterWithReturn() {
        MethodEmitter methodEmitter = createMethodEmitter("(Ljava/lang/Object;Ljava/lang/Object;)I");

        Assert.assertEquals(1, methodEmitter.pushAmount());
    }

    @Test
    public void methodWithTwoLongs() {
        MethodEmitter methodEmitter = createMethodEmitter("(LL)V");

        Assert.assertEquals(2, methodEmitter.popAmount());
    }

    @Test
    public void methodWithLongAndObject() {
        MethodEmitter methodEmitter = createMethodEmitter("(LLjava/lang/Object;)V");

        Assert.assertEquals(2, methodEmitter.popAmount());
    }

    @Test
    public void methodWithObjectAndLong() {
        MethodEmitter methodEmitter = createMethodEmitter("(Ljava/lang/Object;L)V");

        Assert.assertEquals(2, methodEmitter.popAmount());
    }

    @Test
    public void methodWithTwoLongsAndObject() {
        MethodEmitter methodEmitter = createMethodEmitter("(LLLjava/lang/Object;)V");

        Assert.assertEquals(3, methodEmitter.popAmount());
    }

    @Test
    public void methodWithLongObjectLong() {
        MethodEmitter methodEmitter = createMethodEmitter("(LLjava/lang/Object;L)V");

        Assert.assertEquals(3, methodEmitter.popAmount());
    }

    private MethodEmitter createMethodEmitter(String desc) {
        return new MethodEmitter(Opcodes.INVOKEVIRTUAL, "com/infomancers/MyClass", "myMethod", desc);
    }
}
