// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import java.io.FileInputStream;
import java.util.List;
import java.io.File;

public final class FileUtils
{
    public static List<String> getLines(final File file) {
        try {
            final InputStream fis = new FileInputStream(file);
            return (List<String>)IOUtils.readLines(fis, Charsets.UTF_8);
        }
        catch (IOException e) {
            return new ArrayList<String>();
        }
    }
}
