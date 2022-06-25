// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

final class HttpPostBodyUtil
{
    public static final int chunkSize;
    public static final String CONTENT_DISPOSITION;
    public static final String NAME;
    public static final String FILENAME;
    public static final String FORM_DATA;
    public static final String ATTACHMENT;
    public static final String FILE;
    public static final String MULTIPART_MIXED;
    public static final Charset ISO_8859_1;
    public static final Charset US_ASCII;
    public static final String DEFAULT_BINARY_CONTENT_TYPE;
    public static final String DEFAULT_TEXT_CONTENT_TYPE;
    
    private HttpPostBodyUtil() {
    }
    
    static int findNonWhitespace(final String sb, final int offset) {
        int result;
        for (result = offset; result < sb.length() && Character.isWhitespace(sb.charAt(result)); ++result) {}
        return result;
    }
    
    static int findWhitespace(final String sb, final int offset) {
        int result;
        for (result = offset; result < sb.length() && !Character.isWhitespace(sb.charAt(result)); ++result) {}
        return result;
    }
    
    static int findEndOfString(final String sb) {
        int result;
        for (result = sb.length(); result > 0 && Character.isWhitespace(sb.charAt(result - 1)); --result) {}
        return result;
    }
    
    static {
        NAME = "name";
        FILE = "file";
        chunkSize = 8096;
        FORM_DATA = "form-data";
        MULTIPART_MIXED = "multipart/mixed";
        DEFAULT_TEXT_CONTENT_TYPE = "text/plain";
        CONTENT_DISPOSITION = "Content-Disposition";
        ATTACHMENT = "attachment";
        DEFAULT_BINARY_CONTENT_TYPE = "application/octet-stream";
        FILENAME = "filename";
        ISO_8859_1 = CharsetUtil.ISO_8859_1;
        US_ASCII = CharsetUtil.US_ASCII;
    }
    
    public enum TransferEncodingMechanism
    {
        BIT7("7bit"), 
        BIT8("8bit"), 
        BINARY("binary");
        
        private final String value;
        
        private TransferEncodingMechanism(final String value) {
            this.value = value;
        }
        
        private TransferEncodingMechanism() {
            this.value = this.name();
        }
        
        public String value() {
            return this.value;
        }
        
        @Override
        public String toString() {
            return this.value;
        }
    }
    
    static class SeekAheadNoBackArrayException extends Exception
    {
        private static final long serialVersionUID = -630418804938699495L;
    }
    
    static class SeekAheadOptimize
    {
        byte[] bytes;
        int readerIndex;
        int pos;
        int origPos;
        int limit;
        ByteBuf buffer;
        
        SeekAheadOptimize(final ByteBuf buffer) throws SeekAheadNoBackArrayException {
            if (!buffer.hasArray()) {
                throw new SeekAheadNoBackArrayException();
            }
            this.buffer = buffer;
            this.bytes = buffer.array();
            this.readerIndex = buffer.readerIndex();
            final int n = buffer.arrayOffset() + this.readerIndex;
            this.pos = n;
            this.origPos = n;
            this.limit = buffer.arrayOffset() + buffer.writerIndex();
        }
        
        void setReadPosition(final int minus) {
            this.pos -= minus;
            this.readerIndex = this.getReadPosition(this.pos);
            this.buffer.readerIndex(this.readerIndex);
        }
        
        int getReadPosition(final int index) {
            return index - this.origPos + this.readerIndex;
        }
        
        void clear() {
            this.buffer = null;
            this.bytes = null;
            this.limit = 0;
            this.pos = 0;
            this.readerIndex = 0;
        }
    }
}
