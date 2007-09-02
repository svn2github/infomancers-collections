package com.infomancers.collections.yield.asm;

import com.infomancers.collections.yield.asm.promotion.FieldMemberAccessorCreator;
import com.infomancers.collections.yield.asm.promotion.FieldMemberAccessorCreators;

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

public enum TypeDescriptor {
    Integer("I", "int", "Integer", FieldMemberAccessorCreators.BOXING),
    Long("L", "long", "Long", FieldMemberAccessorCreators.BOXING),
    Float("F", "float", "Float", FieldMemberAccessorCreators.BOXING),
    Double("D", "double", "Double", FieldMemberAccessorCreators.BOXING),
    Object("Ljava/lang/Object;", null, "Object", FieldMemberAccessorCreators.SIMPLE),
    Byte("B", "byte", "Byte", FieldMemberAccessorCreators.BOXING),
    Char("C", "char", "Character", FieldMemberAccessorCreators.BOXING),
    Short("S", "short", "Short", FieldMemberAccessorCreators.BOXING);

    private final String desc;
    private final String primitive;
    private final String wrapper;
    private final FieldMemberAccessorCreator accessorCreator;

    public String getDesc() {
        return desc;
    }

    public String getClassName() {
        return wrapper;
    }

    public String getClassNameAsOwner() {
        return "java/lang/" + wrapper;
    }

    public String getClassNameAsDesc() {
        return "L" + getClassNameAsOwner() + ";";
    }

    public String getPrimitiveName() {
        return primitive;
    }

    public String getPrimitiveNameCapitalised() {
        if (getPrimitiveName() == null) {
            return "";
        }

        char[] c = getPrimitiveName().toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    public String getBoxMethodDesc() {
        return "(" + getDesc() + ")" + getClassNameAsDesc();
    }

    public String getBoxMethodName() {
        return "valueOf";
    }

    public String getUnboxMethodDesc() {
        return "()" + getDesc();
    }

    public String getUnboxMethodName() {
        return getPrimitiveName() + "Value";
    }

    public String getArraySetterMethodDesc() {
        return "(Ljava/lang/Object;I" + getDesc() + ")V";
    }

    public String getArraySetterMethodName() {
        return "set" + getPrimitiveNameCapitalised();
    }

    public String getArrayGetterMethodDesc() {
        return "(Ljava/lang/Object;I)" + getDesc();
    }

    public String getArrayGetterMethodName() {
        return "get" + getPrimitiveNameCapitalised();
    }

    public FieldMemberAccessorCreator getAccessorCreator() {
        return accessorCreator;
    }

    TypeDescriptor(String desc, String primitive, String wrapper, FieldMemberAccessorCreator accessorCreator) {
        this.desc = desc;
        this.primitive = primitive;
        this.wrapper = wrapper;
        this.accessorCreator = accessorCreator;
    }
}
