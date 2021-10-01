// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

public class StringUtils
{
    private StringUtils() {
    }
    
    public static String upperSnakeCaseToPascal(final String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 1) {
            return Character.toString(s.charAt(0));
        }
        return String.valueOf(s.charAt(0)) + s.substring(1).toLowerCase();
    }
    
    public static String replaceUserSymbols(final String str) {
        return str.replace('&', '§').replace("<3", "\u00e2\ufffd¤");
    }
    
    public static String getTrimmedClipboardContents() {
        String data = ClipboardUtils.getClipboardContents();
        if (data != null) {
            data = data.trim();
            if (data.indexOf(10) != -1) {
                data = data.replace("\n", "");
            }
        }
        return data;
    }
    
    public static String fromCharCodes(final int[] codes) {
        final StringBuilder builder = new StringBuilder();
        for (final int cc : codes) {
            builder.append((char)cc);
        }
        return builder.toString();
    }
}
