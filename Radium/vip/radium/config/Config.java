// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.config;

import java.util.Iterator;
import com.google.gson.JsonElement;
import vip.radium.module.Module;
import vip.radium.RadiumClient;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.File;

public final class Config implements Serializable
{
    private final String name;
    private final File file;
    
    public Config(final String name) {
        this.name = name;
        this.file = new File(ConfigManager.CONFIGS_DIR, String.valueOf(name) + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public JsonObject save() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        for (final Module module : RadiumClient.getInstance().getModuleManager().getModules()) {
            modulesObject.add(module.getLabel(), (JsonElement)module.save());
        }
        jsonObject.add("Modules", (JsonElement)modulesObject);
        return jsonObject;
    }
    
    @Override
    public void load(final JsonObject object) {
        if (object.has("Modules")) {
            final JsonObject modulesObject = object.getAsJsonObject("Modules");
            for (final Module module : RadiumClient.getInstance().getModuleManager().getModules()) {
                if (modulesObject.has(module.getLabel())) {
                    module.load(modulesObject.getAsJsonObject(module.getLabel()));
                }
            }
        }
    }
}
