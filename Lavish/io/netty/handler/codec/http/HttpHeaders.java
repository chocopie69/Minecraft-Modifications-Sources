// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.Collections;
import java.util.Set;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public abstract class HttpHeaders implements Iterable<Map.Entry<String, String>>
{
    private static final byte[] HEADER_SEPERATOR;
    private static final byte[] CRLF;
    private static final CharSequence CONTENT_LENGTH_ENTITY;
    private static final CharSequence CONNECTION_ENTITY;
    private static final CharSequence CLOSE_ENTITY;
    private static final CharSequence KEEP_ALIVE_ENTITY;
    private static final CharSequence HOST_ENTITY;
    private static final CharSequence DATE_ENTITY;
    private static final CharSequence EXPECT_ENTITY;
    private static final CharSequence CONTINUE_ENTITY;
    private static final CharSequence TRANSFER_ENCODING_ENTITY;
    private static final CharSequence CHUNKED_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_KEY1_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_KEY2_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_ORIGIN_ENTITY;
    private static final CharSequence SEC_WEBSOCKET_LOCATION_ENTITY;
    public static final HttpHeaders EMPTY_HEADERS;
    
    public static boolean isKeepAlive(final HttpMessage message) {
        final String connection = message.headers().get(HttpHeaders.CONNECTION_ENTITY);
        if (connection != null && equalsIgnoreCase(HttpHeaders.CLOSE_ENTITY, connection)) {
            return false;
        }
        if (message.getProtocolVersion().isKeepAliveDefault()) {
            return !equalsIgnoreCase(HttpHeaders.CLOSE_ENTITY, connection);
        }
        return equalsIgnoreCase(HttpHeaders.KEEP_ALIVE_ENTITY, connection);
    }
    
    public static void setKeepAlive(final HttpMessage message, final boolean keepAlive) {
        final HttpHeaders h = message.headers();
        if (message.getProtocolVersion().isKeepAliveDefault()) {
            if (keepAlive) {
                h.remove(HttpHeaders.CONNECTION_ENTITY);
            }
            else {
                h.set(HttpHeaders.CONNECTION_ENTITY, HttpHeaders.CLOSE_ENTITY);
            }
        }
        else if (keepAlive) {
            h.set(HttpHeaders.CONNECTION_ENTITY, HttpHeaders.KEEP_ALIVE_ENTITY);
        }
        else {
            h.remove(HttpHeaders.CONNECTION_ENTITY);
        }
    }
    
    public static String getHeader(final HttpMessage message, final String name) {
        return message.headers().get(name);
    }
    
    public static String getHeader(final HttpMessage message, final CharSequence name) {
        return message.headers().get(name);
    }
    
    public static String getHeader(final HttpMessage message, final String name, final String defaultValue) {
        return getHeader(message, (CharSequence)name, defaultValue);
    }
    
    public static String getHeader(final HttpMessage message, final CharSequence name, final String defaultValue) {
        final String value = message.headers().get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public static void setHeader(final HttpMessage message, final String name, final Object value) {
        message.headers().set(name, value);
    }
    
    public static void setHeader(final HttpMessage message, final CharSequence name, final Object value) {
        message.headers().set(name, value);
    }
    
    public static void setHeader(final HttpMessage message, final String name, final Iterable<?> values) {
        message.headers().set(name, values);
    }
    
    public static void setHeader(final HttpMessage message, final CharSequence name, final Iterable<?> values) {
        message.headers().set(name, values);
    }
    
    public static void addHeader(final HttpMessage message, final String name, final Object value) {
        message.headers().add(name, value);
    }
    
    public static void addHeader(final HttpMessage message, final CharSequence name, final Object value) {
        message.headers().add(name, value);
    }
    
    public static void removeHeader(final HttpMessage message, final String name) {
        message.headers().remove(name);
    }
    
    public static void removeHeader(final HttpMessage message, final CharSequence name) {
        message.headers().remove(name);
    }
    
    public static void clearHeaders(final HttpMessage message) {
        message.headers().clear();
    }
    
    public static int getIntHeader(final HttpMessage message, final String name) {
        return getIntHeader(message, (CharSequence)name);
    }
    
    public static int getIntHeader(final HttpMessage message, final CharSequence name) {
        final String value = getHeader(message, name);
        if (value == null) {
            throw new NumberFormatException("header not found: " + (Object)name);
        }
        return Integer.parseInt(value);
    }
    
    public static int getIntHeader(final HttpMessage message, final String name, final int defaultValue) {
        return getIntHeader(message, (CharSequence)name, defaultValue);
    }
    
    public static int getIntHeader(final HttpMessage message, final CharSequence name, final int defaultValue) {
        final String value = getHeader(message, name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }
    
    public static void setIntHeader(final HttpMessage message, final String name, final int value) {
        message.headers().set(name, value);
    }
    
    public static void setIntHeader(final HttpMessage message, final CharSequence name, final int value) {
        message.headers().set(name, value);
    }
    
    public static void setIntHeader(final HttpMessage message, final String name, final Iterable<Integer> values) {
        message.headers().set(name, values);
    }
    
    public static void setIntHeader(final HttpMessage message, final CharSequence name, final Iterable<Integer> values) {
        message.headers().set(name, values);
    }
    
    public static void addIntHeader(final HttpMessage message, final String name, final int value) {
        message.headers().add(name, value);
    }
    
    public static void addIntHeader(final HttpMessage message, final CharSequence name, final int value) {
        message.headers().add(name, value);
    }
    
    public static Date getDateHeader(final HttpMessage message, final String name) throws ParseException {
        return getDateHeader(message, (CharSequence)name);
    }
    
    public static Date getDateHeader(final HttpMessage message, final CharSequence name) throws ParseException {
        final String value = getHeader(message, name);
        if (value == null) {
            throw new ParseException("header not found: " + (Object)name, 0);
        }
        return HttpHeaderDateFormat.get().parse(value);
    }
    
    public static Date getDateHeader(final HttpMessage message, final String name, final Date defaultValue) {
        return getDateHeader(message, (CharSequence)name, defaultValue);
    }
    
    public static Date getDateHeader(final HttpMessage message, final CharSequence name, final Date defaultValue) {
        final String value = getHeader(message, name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return HttpHeaderDateFormat.get().parse(value);
        }
        catch (ParseException ignored) {
            return defaultValue;
        }
    }
    
    public static void setDateHeader(final HttpMessage message, final String name, final Date value) {
        setDateHeader(message, (CharSequence)name, value);
    }
    
    public static void setDateHeader(final HttpMessage message, final CharSequence name, final Date value) {
        if (value != null) {
            message.headers().set(name, HttpHeaderDateFormat.get().format(value));
        }
        else {
            message.headers().set(name, null);
        }
    }
    
    public static void setDateHeader(final HttpMessage message, final String name, final Iterable<Date> values) {
        message.headers().set(name, values);
    }
    
    public static void setDateHeader(final HttpMessage message, final CharSequence name, final Iterable<Date> values) {
        message.headers().set(name, values);
    }
    
    public static void addDateHeader(final HttpMessage message, final String name, final Date value) {
        message.headers().add(name, value);
    }
    
    public static void addDateHeader(final HttpMessage message, final CharSequence name, final Date value) {
        message.headers().add(name, value);
    }
    
    public static long getContentLength(final HttpMessage message) {
        final String value = getHeader(message, HttpHeaders.CONTENT_LENGTH_ENTITY);
        if (value != null) {
            return Long.parseLong(value);
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        throw new NumberFormatException("header not found: Content-Length");
    }
    
    public static long getContentLength(final HttpMessage message, final long defaultValue) {
        final String contentLength = message.headers().get(HttpHeaders.CONTENT_LENGTH_ENTITY);
        if (contentLength != null) {
            try {
                return Long.parseLong(contentLength);
            }
            catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        return defaultValue;
    }
    
    private static int getWebSocketContentLength(final HttpMessage message) {
        final HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            final HttpRequest req = (HttpRequest)message;
            if (HttpMethod.GET.equals(req.getMethod()) && h.contains(HttpHeaders.SEC_WEBSOCKET_KEY1_ENTITY) && h.contains(HttpHeaders.SEC_WEBSOCKET_KEY2_ENTITY)) {
                return 8;
            }
        }
        else if (message instanceof HttpResponse) {
            final HttpResponse res = (HttpResponse)message;
            if (res.getStatus().code() == 101 && h.contains(HttpHeaders.SEC_WEBSOCKET_ORIGIN_ENTITY) && h.contains(HttpHeaders.SEC_WEBSOCKET_LOCATION_ENTITY)) {
                return 16;
            }
        }
        return -1;
    }
    
    public static void setContentLength(final HttpMessage message, final long length) {
        message.headers().set(HttpHeaders.CONTENT_LENGTH_ENTITY, length);
    }
    
    public static String getHost(final HttpMessage message) {
        return message.headers().get(HttpHeaders.HOST_ENTITY);
    }
    
    public static String getHost(final HttpMessage message, final String defaultValue) {
        return getHeader(message, HttpHeaders.HOST_ENTITY, defaultValue);
    }
    
    public static void setHost(final HttpMessage message, final String value) {
        message.headers().set(HttpHeaders.HOST_ENTITY, value);
    }
    
    public static void setHost(final HttpMessage message, final CharSequence value) {
        message.headers().set(HttpHeaders.HOST_ENTITY, value);
    }
    
    public static Date getDate(final HttpMessage message) throws ParseException {
        return getDateHeader(message, HttpHeaders.DATE_ENTITY);
    }
    
    public static Date getDate(final HttpMessage message, final Date defaultValue) {
        return getDateHeader(message, HttpHeaders.DATE_ENTITY, defaultValue);
    }
    
    public static void setDate(final HttpMessage message, final Date value) {
        if (value != null) {
            message.headers().set(HttpHeaders.DATE_ENTITY, HttpHeaderDateFormat.get().format(value));
        }
        else {
            message.headers().set(HttpHeaders.DATE_ENTITY, null);
        }
    }
    
    public static boolean is100ContinueExpected(final HttpMessage message) {
        if (!(message instanceof HttpRequest)) {
            return false;
        }
        if (message.getProtocolVersion().compareTo(HttpVersion.HTTP_1_1) < 0) {
            return false;
        }
        final String value = message.headers().get(HttpHeaders.EXPECT_ENTITY);
        return value != null && (equalsIgnoreCase(HttpHeaders.CONTINUE_ENTITY, value) || message.headers().contains(HttpHeaders.EXPECT_ENTITY, HttpHeaders.CONTINUE_ENTITY, true));
    }
    
    public static void set100ContinueExpected(final HttpMessage message) {
        set100ContinueExpected(message, true);
    }
    
    public static void set100ContinueExpected(final HttpMessage message, final boolean set) {
        if (set) {
            message.headers().set(HttpHeaders.EXPECT_ENTITY, HttpHeaders.CONTINUE_ENTITY);
        }
        else {
            message.headers().remove(HttpHeaders.EXPECT_ENTITY);
        }
    }
    
    static void validateHeaderName(final CharSequence headerName) {
        if (headerName == null) {
            throw new NullPointerException("Header names cannot be null");
        }
        int index = 0;
        while (index < headerName.length()) {
            final char character = headerName.charAt(index);
            if (character > '\u007f') {
                throw new IllegalArgumentException("Header name cannot contain non-ASCII characters: " + (Object)headerName);
            }
            switch (character) {
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ' ':
                case ',':
                case ':':
                case ';':
                case '=': {
                    throw new IllegalArgumentException("Header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + (Object)headerName);
                }
                default: {
                    ++index;
                    continue;
                }
            }
        }
    }
    
    static void validateHeaderValue(final CharSequence headerValue) {
        if (headerValue == null) {
            throw new NullPointerException("Header values cannot be null");
        }
        int state = 0;
        int index = 0;
        while (index < headerValue.length()) {
            final char character = headerValue.charAt(index);
            switch (character) {
                case '\u000b': {
                    throw new IllegalArgumentException("Header value contains a prohibited character '\\v': " + (Object)headerValue);
                }
                case '\f': {
                    throw new IllegalArgumentException("Header value contains a prohibited character '\\f': " + (Object)headerValue);
                }
                default: {
                    Label_0297: {
                        switch (state) {
                            case 0: {
                                switch (character) {
                                    case '\r': {
                                        state = 1;
                                        break;
                                    }
                                    case '\n': {
                                        state = 2;
                                        break;
                                    }
                                }
                                break;
                            }
                            case 1: {
                                switch (character) {
                                    case '\n': {
                                        state = 2;
                                        break Label_0297;
                                    }
                                    default: {
                                        throw new IllegalArgumentException("Only '\\n' is allowed after '\\r': " + (Object)headerValue);
                                    }
                                }
                                break;
                            }
                            case 2: {
                                switch (character) {
                                    case '\t':
                                    case ' ': {
                                        state = 0;
                                        break Label_0297;
                                    }
                                    default: {
                                        throw new IllegalArgumentException("Only ' ' and '\\t' are allowed after '\\n': " + (Object)headerValue);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    ++index;
                    continue;
                }
            }
        }
        if (state != 0) {
            throw new IllegalArgumentException("Header value must not end with '\\r' or '\\n':" + (Object)headerValue);
        }
    }
    
    public static boolean isTransferEncodingChunked(final HttpMessage message) {
        return message.headers().contains(HttpHeaders.TRANSFER_ENCODING_ENTITY, HttpHeaders.CHUNKED_ENTITY, true);
    }
    
    public static void removeTransferEncodingChunked(final HttpMessage m) {
        final List<String> values = m.headers().getAll(HttpHeaders.TRANSFER_ENCODING_ENTITY);
        if (values.isEmpty()) {
            return;
        }
        final Iterator<String> valuesIt = values.iterator();
        while (valuesIt.hasNext()) {
            final String value = valuesIt.next();
            if (equalsIgnoreCase(value, HttpHeaders.CHUNKED_ENTITY)) {
                valuesIt.remove();
            }
        }
        if (values.isEmpty()) {
            m.headers().remove(HttpHeaders.TRANSFER_ENCODING_ENTITY);
        }
        else {
            m.headers().set(HttpHeaders.TRANSFER_ENCODING_ENTITY, values);
        }
    }
    
    public static void setTransferEncodingChunked(final HttpMessage m) {
        addHeader(m, HttpHeaders.TRANSFER_ENCODING_ENTITY, HttpHeaders.CHUNKED_ENTITY);
        removeHeader(m, HttpHeaders.CONTENT_LENGTH_ENTITY);
    }
    
    public static boolean isContentLengthSet(final HttpMessage m) {
        return m.headers().contains(HttpHeaders.CONTENT_LENGTH_ENTITY);
    }
    
    public static boolean equalsIgnoreCase(final CharSequence name1, final CharSequence name2) {
        if (name1 == name2) {
            return true;
        }
        if (name1 == null || name2 == null) {
            return false;
        }
        final int nameLen = name1.length();
        if (nameLen != name2.length()) {
            return false;
        }
        for (int i = nameLen - 1; i >= 0; --i) {
            char c1 = name1.charAt(i);
            char c2 = name2.charAt(i);
            if (c1 != c2) {
                if (c1 >= 'A' && c1 <= 'Z') {
                    c1 += ' ';
                }
                if (c2 >= 'A' && c2 <= 'Z') {
                    c2 += ' ';
                }
                if (c1 != c2) {
                    return false;
                }
            }
        }
        return true;
    }
    
    static int hash(final CharSequence name) {
        if (name instanceof HttpHeaderEntity) {
            return ((HttpHeaderEntity)name).hash();
        }
        int h = 0;
        for (int i = name.length() - 1; i >= 0; --i) {
            char c = name.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                c += ' ';
            }
            h = 31 * h + c;
        }
        if (h > 0) {
            return h;
        }
        if (h == Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }
        return -h;
    }
    
    static void encode(final HttpHeaders headers, final ByteBuf buf) {
        if (headers instanceof DefaultHttpHeaders) {
            ((DefaultHttpHeaders)headers).encode(buf);
        }
        else {
            for (final Map.Entry<String, String> header : headers) {
                encode(header.getKey(), header.getValue(), buf);
            }
        }
    }
    
    static void encode(final CharSequence key, final CharSequence value, final ByteBuf buf) {
        if (!encodeAscii(key, buf)) {
            buf.writeBytes(HttpHeaders.HEADER_SEPERATOR);
        }
        if (!encodeAscii(value, buf)) {
            buf.writeBytes(HttpHeaders.CRLF);
        }
    }
    
    public static boolean encodeAscii(final CharSequence seq, final ByteBuf buf) {
        if (seq instanceof HttpHeaderEntity) {
            return ((HttpHeaderEntity)seq).encode(buf);
        }
        encodeAscii0(seq, buf);
        return false;
    }
    
    static void encodeAscii0(final CharSequence seq, final ByteBuf buf) {
        for (int length = seq.length(), i = 0; i < length; ++i) {
            buf.writeByte((byte)seq.charAt(i));
        }
    }
    
    public static CharSequence newEntity(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(name);
    }
    
    public static CharSequence newNameEntity(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(name, HttpHeaders.HEADER_SEPERATOR);
    }
    
    public static CharSequence newValueEntity(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return new HttpHeaderEntity(name, HttpHeaders.CRLF);
    }
    
    protected HttpHeaders() {
    }
    
    public abstract String get(final String p0);
    
    public String get(final CharSequence name) {
        return this.get(name.toString());
    }
    
    public abstract List<String> getAll(final String p0);
    
    public List<String> getAll(final CharSequence name) {
        return this.getAll(name.toString());
    }
    
    public abstract List<Map.Entry<String, String>> entries();
    
    public abstract boolean contains(final String p0);
    
    public boolean contains(final CharSequence name) {
        return this.contains(name.toString());
    }
    
    public abstract boolean isEmpty();
    
    public abstract Set<String> names();
    
    public abstract HttpHeaders add(final String p0, final Object p1);
    
    public HttpHeaders add(final CharSequence name, final Object value) {
        return this.add(name.toString(), value);
    }
    
    public abstract HttpHeaders add(final String p0, final Iterable<?> p1);
    
    public HttpHeaders add(final CharSequence name, final Iterable<?> values) {
        return this.add(name.toString(), values);
    }
    
    public HttpHeaders add(final HttpHeaders headers) {
        if (headers == null) {
            throw new NullPointerException("headers");
        }
        for (final Map.Entry<String, String> e : headers) {
            this.add(e.getKey(), e.getValue());
        }
        return this;
    }
    
    public abstract HttpHeaders set(final String p0, final Object p1);
    
    public HttpHeaders set(final CharSequence name, final Object value) {
        return this.set(name.toString(), value);
    }
    
    public abstract HttpHeaders set(final String p0, final Iterable<?> p1);
    
    public HttpHeaders set(final CharSequence name, final Iterable<?> values) {
        return this.set(name.toString(), values);
    }
    
    public HttpHeaders set(final HttpHeaders headers) {
        if (headers == null) {
            throw new NullPointerException("headers");
        }
        this.clear();
        for (final Map.Entry<String, String> e : headers) {
            this.add(e.getKey(), e.getValue());
        }
        return this;
    }
    
    public abstract HttpHeaders remove(final String p0);
    
    public HttpHeaders remove(final CharSequence name) {
        return this.remove(name.toString());
    }
    
    public abstract HttpHeaders clear();
    
    public boolean contains(final String name, final String value, final boolean ignoreCaseValue) {
        final List<String> values = this.getAll(name);
        if (values.isEmpty()) {
            return false;
        }
        for (final String v : values) {
            if (ignoreCaseValue) {
                if (equalsIgnoreCase(v, value)) {
                    return true;
                }
                continue;
            }
            else {
                if (v.equals(value)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean contains(final CharSequence name, final CharSequence value, final boolean ignoreCaseValue) {
        return this.contains(name.toString(), value.toString(), ignoreCaseValue);
    }
    
    static {
        HEADER_SEPERATOR = new byte[] { 58, 32 };
        CRLF = new byte[] { 13, 10 };
        CONTENT_LENGTH_ENTITY = newEntity("Content-Length");
        CONNECTION_ENTITY = newEntity("Connection");
        CLOSE_ENTITY = newEntity("close");
        KEEP_ALIVE_ENTITY = newEntity("keep-alive");
        HOST_ENTITY = newEntity("Host");
        DATE_ENTITY = newEntity("Date");
        EXPECT_ENTITY = newEntity("Expect");
        CONTINUE_ENTITY = newEntity("100-continue");
        TRANSFER_ENCODING_ENTITY = newEntity("Transfer-Encoding");
        CHUNKED_ENTITY = newEntity("chunked");
        SEC_WEBSOCKET_KEY1_ENTITY = newEntity("Sec-WebSocket-Key1");
        SEC_WEBSOCKET_KEY2_ENTITY = newEntity("Sec-WebSocket-Key2");
        SEC_WEBSOCKET_ORIGIN_ENTITY = newEntity("Sec-WebSocket-Origin");
        SEC_WEBSOCKET_LOCATION_ENTITY = newEntity("Sec-WebSocket-Location");
        EMPTY_HEADERS = new HttpHeaders() {
            @Override
            public String get(final String name) {
                return null;
            }
            
            @Override
            public List<String> getAll(final String name) {
                return Collections.emptyList();
            }
            
            @Override
            public List<Map.Entry<String, String>> entries() {
                return Collections.emptyList();
            }
            
            @Override
            public boolean contains(final String name) {
                return false;
            }
            
            @Override
            public boolean isEmpty() {
                return true;
            }
            
            @Override
            public Set<String> names() {
                return Collections.emptySet();
            }
            
            @Override
            public HttpHeaders add(final String name, final Object value) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public HttpHeaders add(final String name, final Iterable<?> values) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public HttpHeaders set(final String name, final Object value) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public HttpHeaders set(final String name, final Iterable<?> values) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public HttpHeaders remove(final String name) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public HttpHeaders clear() {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return this.entries().iterator();
            }
        };
    }
    
    public static final class Names
    {
        public static final String ACCEPT;
        public static final String ACCEPT_CHARSET;
        public static final String ACCEPT_ENCODING;
        public static final String ACCEPT_LANGUAGE;
        public static final String ACCEPT_RANGES;
        public static final String ACCEPT_PATCH;
        public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS;
        public static final String ACCESS_CONTROL_ALLOW_HEADERS;
        public static final String ACCESS_CONTROL_ALLOW_METHODS;
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN;
        public static final String ACCESS_CONTROL_EXPOSE_HEADERS;
        public static final String ACCESS_CONTROL_MAX_AGE;
        public static final String ACCESS_CONTROL_REQUEST_HEADERS;
        public static final String ACCESS_CONTROL_REQUEST_METHOD;
        public static final String AGE;
        public static final String ALLOW;
        public static final String AUTHORIZATION;
        public static final String CACHE_CONTROL;
        public static final String CONNECTION;
        public static final String CONTENT_BASE;
        public static final String CONTENT_ENCODING;
        public static final String CONTENT_LANGUAGE;
        public static final String CONTENT_LENGTH;
        public static final String CONTENT_LOCATION;
        public static final String CONTENT_TRANSFER_ENCODING;
        public static final String CONTENT_MD5;
        public static final String CONTENT_RANGE;
        public static final String CONTENT_TYPE;
        public static final String COOKIE;
        public static final String DATE;
        public static final String ETAG;
        public static final String EXPECT;
        public static final String EXPIRES;
        public static final String FROM;
        public static final String HOST;
        public static final String IF_MATCH;
        public static final String IF_MODIFIED_SINCE;
        public static final String IF_NONE_MATCH;
        public static final String IF_RANGE;
        public static final String IF_UNMODIFIED_SINCE;
        public static final String LAST_MODIFIED;
        public static final String LOCATION;
        public static final String MAX_FORWARDS;
        public static final String ORIGIN;
        public static final String PRAGMA;
        public static final String PROXY_AUTHENTICATE;
        public static final String PROXY_AUTHORIZATION;
        public static final String RANGE;
        public static final String REFERER;
        public static final String RETRY_AFTER;
        public static final String SEC_WEBSOCKET_KEY1;
        public static final String SEC_WEBSOCKET_KEY2;
        public static final String SEC_WEBSOCKET_LOCATION;
        public static final String SEC_WEBSOCKET_ORIGIN;
        public static final String SEC_WEBSOCKET_PROTOCOL;
        public static final String SEC_WEBSOCKET_VERSION;
        public static final String SEC_WEBSOCKET_KEY;
        public static final String SEC_WEBSOCKET_ACCEPT;
        public static final String SERVER;
        public static final String SET_COOKIE;
        public static final String SET_COOKIE2;
        public static final String TE;
        public static final String TRAILER;
        public static final String TRANSFER_ENCODING;
        public static final String UPGRADE;
        public static final String USER_AGENT;
        public static final String VARY;
        public static final String VIA;
        public static final String WARNING;
        public static final String WEBSOCKET_LOCATION;
        public static final String WEBSOCKET_ORIGIN;
        public static final String WEBSOCKET_PROTOCOL;
        public static final String WWW_AUTHENTICATE;
        
        private Names() {
        }
        
        static {
            IF_MODIFIED_SINCE = "If-Modified-Since";
            ACCEPT_PATCH = "Accept-Patch";
            IF_MATCH = "If-Match";
            SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
            SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
            WEBSOCKET_LOCATION = "WebSocket-Location";
            LAST_MODIFIED = "Last-Modified";
            ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
            ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
            AUTHORIZATION = "Authorization";
            WARNING = "Warning";
            PROXY_AUTHORIZATION = "Proxy-Authorization";
            PROXY_AUTHENTICATE = "Proxy-Authenticate";
            ETAG = "ETag";
            RETRY_AFTER = "Retry-After";
            TRAILER = "Trailer";
            USER_AGENT = "User-Agent";
            HOST = "Host";
            CONTENT_LOCATION = "Content-Location";
            CACHE_CONTROL = "Cache-Control";
            ORIGIN = "Origin";
            TRANSFER_ENCODING = "Transfer-Encoding";
            ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
            ACCEPT_RANGES = "Accept-Ranges";
            VIA = "Via";
            SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
            ACCEPT_ENCODING = "Accept-Encoding";
            SET_COOKIE2 = "Set-Cookie2";
            REFERER = "Referer";
            WWW_AUTHENTICATE = "WWW-Authenticate";
            TE = "TE";
            SET_COOKIE = "Set-Cookie";
            CONTENT_MD5 = "Content-MD5";
            CONTENT_TYPE = "Content-Type";
            WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
            IF_NONE_MATCH = "If-None-Match";
            LOCATION = "Location";
            ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
            IF_RANGE = "If-Range";
            CONTENT_LANGUAGE = "Content-Language";
            SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
            CONTENT_LENGTH = "Content-Length";
            DATE = "Date";
            RANGE = "Range";
            SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
            SERVER = "Server";
            PRAGMA = "Pragma";
            ALLOW = "Allow";
            CONTENT_RANGE = "Content-Range";
            VARY = "Vary";
            EXPIRES = "Expires";
            CONTENT_ENCODING = "Content-Encoding";
            IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
            UPGRADE = "Upgrade";
            ACCEPT_CHARSET = "Accept-Charset";
            AGE = "Age";
            ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
            ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
            ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
            SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
            FROM = "From";
            CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
            COOKIE = "Cookie";
            ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
            ACCEPT = "Accept";
            EXPECT = "Expect";
            CONNECTION = "Connection";
            WEBSOCKET_ORIGIN = "WebSocket-Origin";
            CONTENT_BASE = "Content-Base";
            SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
            ACCEPT_LANGUAGE = "Accept-Language";
            MAX_FORWARDS = "Max-Forwards";
            SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
        }
    }
    
    public static final class Values
    {
        public static final String APPLICATION_X_WWW_FORM_URLENCODED;
        public static final String BASE64;
        public static final String BINARY;
        public static final String BOUNDARY;
        public static final String BYTES;
        public static final String CHARSET;
        public static final String CHUNKED;
        public static final String CLOSE;
        public static final String COMPRESS;
        public static final String CONTINUE;
        public static final String DEFLATE;
        public static final String GZIP;
        public static final String IDENTITY;
        public static final String KEEP_ALIVE;
        public static final String MAX_AGE;
        public static final String MAX_STALE;
        public static final String MIN_FRESH;
        public static final String MULTIPART_FORM_DATA;
        public static final String MUST_REVALIDATE;
        public static final String NO_CACHE;
        public static final String NO_STORE;
        public static final String NO_TRANSFORM;
        public static final String NONE;
        public static final String ONLY_IF_CACHED;
        public static final String PRIVATE;
        public static final String PROXY_REVALIDATE;
        public static final String PUBLIC;
        public static final String QUOTED_PRINTABLE;
        public static final String S_MAXAGE;
        public static final String TRAILERS;
        public static final String UPGRADE;
        public static final String WEBSOCKET;
        
        private Values() {
        }
        
        static {
            MUST_REVALIDATE = "must-revalidate";
            BASE64 = "base64";
            IDENTITY = "identity";
            NO_STORE = "no-store";
            TRAILERS = "trailers";
            COMPRESS = "compress";
            DEFLATE = "deflate";
            NO_TRANSFORM = "no-transform";
            MIN_FRESH = "min-fresh";
            APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
            MULTIPART_FORM_DATA = "multipart/form-data";
            KEEP_ALIVE = "keep-alive";
            BOUNDARY = "boundary";
            CLOSE = "close";
            MAX_STALE = "max-stale";
            BYTES = "bytes";
            ONLY_IF_CACHED = "only-if-cached";
            CONTINUE = "100-continue";
            PROXY_REVALIDATE = "proxy-revalidate";
            QUOTED_PRINTABLE = "quoted-printable";
            PUBLIC = "public";
            BINARY = "binary";
            S_MAXAGE = "s-maxage";
            UPGRADE = "Upgrade";
            GZIP = "gzip";
            NONE = "none";
            PRIVATE = "private";
            NO_CACHE = "no-cache";
            WEBSOCKET = "WebSocket";
            CHUNKED = "chunked";
            MAX_AGE = "max-age";
            CHARSET = "charset";
        }
    }
}
