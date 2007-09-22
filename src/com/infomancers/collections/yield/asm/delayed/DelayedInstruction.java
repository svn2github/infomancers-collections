package com.infomancers.collections.yield.asm.delayed;

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
public enum DelayedInstruction {
    FIELD {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new FieldEmitter(insn, params);
        }
    },
    METHOD {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new MethodEmitter(insn, params);
        }
    },
    INSN {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new InsnEmitter(insn);
        }
    },
    TABLESWITCH {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new TableSwitchEmitter(params);
        }
    },
    LDC {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new LdcEmitter(params);
        }
    },
    VAR {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new VarEmitter(insn, params);
        }
    },
    LABEL {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new LabelEmitter(params);
        }
    },
    LINE {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new LineEmitter(params);
        }
    },
    TYPE {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new TypeEmitter(insn, params);
        }},
    FRAME {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new FrameEmitter(params);
        }},
    INT {

        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new IntEmitter(insn, params);
        }

    },
    JUMP {

        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new JumpEmitter(insn, params);
        }},
    LOCALVAR {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new LocalVariableEmitter(params);
        }
    },
    MAXS {
        @Override
        public DelayedInstructionEmitter createEmitter(int insn, Object... params) {
            return new MaxsEmitter(params);
        }};

    public abstract DelayedInstructionEmitter createEmitter(int insn, Object... params);
}
