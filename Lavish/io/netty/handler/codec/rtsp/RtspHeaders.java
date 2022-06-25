// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.rtsp;

public final class RtspHeaders
{
    private RtspHeaders() {
    }
    
    public static final class Names
    {
        public static final String ACCEPT;
        public static final String ACCEPT_ENCODING;
        public static final String ACCEPT_LANGUAGE;
        public static final String ALLOW;
        public static final String AUTHORIZATION;
        public static final String BANDWIDTH;
        public static final String BLOCKSIZE;
        public static final String CACHE_CONTROL;
        public static final String CONFERENCE;
        public static final String CONNECTION;
        public static final String CONTENT_BASE;
        public static final String CONTENT_ENCODING;
        public static final String CONTENT_LANGUAGE;
        public static final String CONTENT_LENGTH;
        public static final String CONTENT_LOCATION;
        public static final String CONTENT_TYPE;
        public static final String CSEQ;
        public static final String DATE;
        public static final String EXPIRES;
        public static final String FROM;
        public static final String HOST;
        public static final String IF_MATCH;
        public static final String IF_MODIFIED_SINCE;
        public static final String KEYMGMT;
        public static final String LAST_MODIFIED;
        public static final String PROXY_AUTHENTICATE;
        public static final String PROXY_REQUIRE;
        public static final String PUBLIC;
        public static final String RANGE;
        public static final String REFERER;
        public static final String REQUIRE;
        public static final String RETRT_AFTER;
        public static final String RTP_INFO;
        public static final String SCALE;
        public static final String SESSION;
        public static final String SERVER;
        public static final String SPEED;
        public static final String TIMESTAMP;
        public static final String TRANSPORT;
        public static final String UNSUPPORTED;
        public static final String USER_AGENT;
        public static final String VARY;
        public static final String VIA;
        public static final String WWW_AUTHENTICATE;
        
        private Names() {
        }
        
        static {
            VARY = "Vary";
            BANDWIDTH = "Bandwidth";
            ACCEPT = "Accept";
            TRANSPORT = "Transport";
            BLOCKSIZE = "Blocksize";
            FROM = "From";
            CACHE_CONTROL = "Cache-Control";
            USER_AGENT = "User-Agent";
            PROXY_AUTHENTICATE = "Proxy-Authenticate";
            SERVER = "Server";
            SESSION = "Session";
            REFERER = "Referer";
            ALLOW = "Allow";
            PROXY_REQUIRE = "Proxy-Require";
            IF_MODIFIED_SINCE = "If-Modified-Since";
            RTP_INFO = "RTP-Info";
            CONTENT_TYPE = "Content-Type";
            RANGE = "Range";
            CONTENT_LOCATION = "Content-Location";
            CSEQ = "CSeq";
            KEYMGMT = "KeyMgmt";
            ACCEPT_LANGUAGE = "Accept-Language";
            CONTENT_BASE = "Content-Base";
            PUBLIC = "Public";
            CONFERENCE = "Conference";
            RETRT_AFTER = "Retry-After";
            AUTHORIZATION = "Authorization";
            ACCEPT_ENCODING = "Accept-Encoding";
            CONTENT_LANGUAGE = "Content-Language";
            REQUIRE = "Require";
            EXPIRES = "Expires";
            TIMESTAMP = "Timestamp";
            IF_MATCH = "If-Match";
            CONNECTION = "Connection";
            CONTENT_ENCODING = "Content-Encoding";
            HOST = "Host";
            UNSUPPORTED = "Unsupported";
            LAST_MODIFIED = "Last-Modified";
            CONTENT_LENGTH = "Content-Length";
            SPEED = "Speed";
            VIA = "Via";
            DATE = "Date";
            SCALE = "Scale";
            WWW_AUTHENTICATE = "WWW-Authenticate";
        }
    }
    
    public static final class Values
    {
        public static final String APPEND;
        public static final String AVP;
        public static final String BYTES;
        public static final String CHARSET;
        public static final String CLIENT_PORT;
        public static final String CLOCK;
        public static final String CLOSE;
        public static final String COMPRESS;
        public static final String CONTINUE;
        public static final String DEFLATE;
        public static final String DESTINATION;
        public static final String GZIP;
        public static final String IDENTITY;
        public static final String INTERLEAVED;
        public static final String KEEP_ALIVE;
        public static final String LAYERS;
        public static final String MAX_AGE;
        public static final String MAX_STALE;
        public static final String MIN_FRESH;
        public static final String MODE;
        public static final String MULTICAST;
        public static final String MUST_REVALIDATE;
        public static final String NONE;
        public static final String NO_CACHE;
        public static final String NO_TRANSFORM;
        public static final String ONLY_IF_CACHED;
        public static final String PORT;
        public static final String PRIVATE;
        public static final String PROXY_REVALIDATE;
        public static final String PUBLIC;
        public static final String RTP;
        public static final String RTPTIME;
        public static final String SEQ;
        public static final String SERVER_PORT;
        public static final String SSRC;
        public static final String TCP;
        public static final String TIME;
        public static final String TIMEOUT;
        public static final String TTL;
        public static final String UDP;
        public static final String UNICAST;
        public static final String URL;
        
        private Values() {
        }
        
        static {
            INTERLEAVED = "interleaved";
            NO_TRANSFORM = "no-transform";
            RTP = "RTP";
            CLOCK = "clock";
            BYTES = "bytes";
            COMPRESS = "compress";
            CONTINUE = "100-continue";
            RTPTIME = "rtptime";
            IDENTITY = "identity";
            TIME = "time";
            SEQ = "seq";
            MAX_STALE = "max-stale";
            AVP = "AVP";
            PRIVATE = "private";
            LAYERS = "layers";
            CLOSE = "close";
            TIMEOUT = "timeout";
            UDP = "UDP";
            CLIENT_PORT = "client_port";
            NONE = "none";
            PUBLIC = "public";
            KEEP_ALIVE = "keep-alive";
            MAX_AGE = "max-age";
            URL = "url";
            TCP = "TCP";
            DESTINATION = "destination";
            MIN_FRESH = "min-fresh";
            UNICAST = "unicast";
            NO_CACHE = "no-cache";
            ONLY_IF_CACHED = "only-if-cached";
            MUST_REVALIDATE = "must-revalidate";
            GZIP = "gzip";
            PROXY_REVALIDATE = "proxy-revalidate";
            CHARSET = "charset";
            APPEND = "append";
            SSRC = "ssrc";
            SERVER_PORT = "server_port";
            PORT = "port";
            MODE = "mode";
            TTL = "ttl";
            DEFLATE = "deflate";
            MULTICAST = "multicast";
        }
    }
}
