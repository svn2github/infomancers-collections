package com.infomancers.collections.yield.asm.promotion;

import com.infomancers.collections.yield.asm.NewMember;
import com.infomancers.collections.yield.asm.TypeDescriptor;
import org.objectweb.asm.MethodVisitor;

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

public interface FieldMemberAccessorCreator {
    /**
     * Creates the code to fetch a field from a certain member.
     * <p/>
     * The type of the field is not necessarily the type of the requested item, as
     * the field might be boxing it - that's why another type is passed.
     * <p/>
     * The bytecode is expected to leave the stack the same as it was when the code started,
     * except for the value of the fetched member which is supposed to be of the type passed
     * and on the top of the stack.
     *
     * @param mv     The method visitor through which to write bytecode.
     * @param owner  The owner of the field.
     * @param type   The type of the fetched value.
     * @param member The member containing the value.
     */
    public void createGetFieldCode(MethodVisitor mv, String owner, TypeDescriptor type, NewMember member);

    /**
     * Creates the code to set a field to a certain value.
     * <p/>
     * The type of the field is not necessarily the type of the set item, as
     * the field might be boxing it - that's why another type is passed.
     * <p/>
     * The bytecode is expected to remove the first element from the stack - which is the 'this'
     * value of the owner - and the second element - which is the value to set - and to leave the
     * rest of the stack intact at the end of execution.
     *
     * @param mv     The method visitor through which to write bytecode.
     * @param owner  The owner of the field.
     * @param type   The type of the set value.
     * @param member The member the value is set to.
     */
    public void createPutFieldCode(MethodVisitor mv, String owner, TypeDescriptor type, NewMember member);
}
