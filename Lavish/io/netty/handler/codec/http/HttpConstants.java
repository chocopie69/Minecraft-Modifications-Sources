// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

public final class HttpConstants
{
    public static final byte SP;
    public static final byte HT;
    public static final byte CR;
    public static final byte EQUALS;
    public static final byte LF;
    public static final byte COLON;
    public static final byte SEMICOLON;
    public static final byte COMMA;
    public static final byte DOUBLE_QUOTE;
    public static final Charset DEFAULT_CHARSET;
    
    private HttpConstants() {
    }
    
    static {
        COLON = 58;
        SP = 32;
        HT = 9;
        EQUALS = 61;
        DOUBLE_QUOTE = 34;
        CR = 13;
        COMMA = 44;
        SEMICOLON = 59;
        LF = 10;
        DEFAULT_CHARSET = CharsetUtil.UTF_8;
    }
}
