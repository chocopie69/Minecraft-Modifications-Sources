// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.vfs;

import java.nio.file.Path;
import java.util.Iterator;
import java.io.IOException;
import org.reflections.ReflectionsException;
import java.nio.file.LinkOption;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.util.Collections;
import java.io.File;

public class SystemDir implements Vfs.Dir
{
    private final java.io.File file;
    
    public SystemDir(final java.io.File file) {
        if (file != null && (!file.isDirectory() || !file.canRead())) {
            throw new RuntimeException("cannot use dir " + file);
        }
        this.file = file;
    }
    
    @Override
    public String getPath() {
        if (this.file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return this.file.getPath().replace("\\", "/");
    }
    
    @Override
    public Iterable<Vfs.File> getFiles() {
        if (this.file == null || !this.file.exists()) {
            return (Iterable<Vfs.File>)Collections.emptyList();
        }
        final ReflectionsException ex;
        return () -> {
            try {
                return (Iterator<Vfs.File>)Files.walk(this.file.toPath(), new FileVisitOption[0]).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).map(path -> new SystemFile(this, path.toFile())).iterator();
            }
            catch (IOException e) {
                new ReflectionsException("could not get files for " + this.file, e);
                throw ex;
            }
        };
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        return this.getPath();
    }
}
