// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders;

import java.util.ArrayDeque;
import java.util.Deque;

public class ProgramStack
{
    private Deque<Program> stack;
    
    public ProgramStack() {
        this.stack = new ArrayDeque<Program>();
    }
    
    public void push(final Program p) {
        this.stack.addLast(p);
    }
    
    public Program pop() {
        if (this.stack.isEmpty()) {
            return Shaders.ProgramNone;
        }
        final Program program = this.stack.pollLast();
        return program;
    }
}
