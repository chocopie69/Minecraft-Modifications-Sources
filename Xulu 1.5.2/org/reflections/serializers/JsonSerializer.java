// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.serializers;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.Charset;
import org.reflections.util.Utils;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import org.reflections.Reflections;
import java.io.InputStream;
import com.google.gson.Gson;

public class JsonSerializer implements Serializer
{
    private Gson gson;
    
    @Override
    public Reflections read(final InputStream inputStream) {
        return (Reflections)this.getGson().fromJson((Reader)new InputStreamReader(inputStream), (Class)Reflections.class);
    }
    
    @Override
    public File save(final Reflections reflections, final String filename) {
        try {
            final File file = Utils.prepareFile(filename);
            Files.write(file.toPath(), this.toString(reflections).getBytes(Charset.defaultCharset()), new OpenOption[0]);
            return file;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        return this.getGson().toJson((Object)reflections);
    }
    
    private Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return this.gson;
    }
}
