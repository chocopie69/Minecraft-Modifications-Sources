// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

final class CookieHeaderNames
{
    static final String PATH;
    static final String EXPIRES;
    static final String MAX_AGE;
    static final String DOMAIN;
    static final String SECURE;
    static final String HTTPONLY;
    static final String COMMENT;
    static final String COMMENTURL;
    static final String DISCARD;
    static final String PORT;
    static final String VERSION;
    
    private CookieHeaderNames() {
    }
    
    static {
        DISCARD = "Discard";
        PATH = "Path";
        VERSION = "Version";
        EXPIRES = "Expires";
        MAX_AGE = "Max-Age";
        HTTPONLY = "HTTPOnly";
        SECURE = "Secure";
        PORT = "Port";
        COMMENTURL = "CommentURL";
        DOMAIN = "Domain";
        COMMENT = "Comment";
    }
}
