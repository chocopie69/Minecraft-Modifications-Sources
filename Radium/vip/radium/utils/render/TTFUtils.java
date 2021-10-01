// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils.render;

import java.io.IOException;
import java.awt.FontFormatException;
import net.minecraft.util.ResourceLocation;
import vip.radium.utils.Wrapper;
import java.awt.Font;

public final class TTFUtils
{
    private TTFUtils() {
    }
    
    public static Font getFontFromLocation(final String fileName, final int size) {
        try {
            return Font.createFont(0, Wrapper.getMinecraft().getResourceManager().getResource(new ResourceLocation("radium/" + fileName)).getInputStream()).deriveFont(0, (float)size);
        }
        catch (FontFormatException | IOException ex2) {
            final Exception ex;
            final Exception ignored = ex;
            return null;
        }
    }
}
