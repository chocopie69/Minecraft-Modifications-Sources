// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils.render;

import java.util.Iterator;
import vip.radium.utils.Wrapper;
import java.util.Arrays;
import java.util.List;

public final class ChangeLogUtils
{
    private static final List<ChangeLogEntry> ENTRIES;
    
    static {
        ENTRIES = Arrays.asList(new ChangeLogEntry("Added Change log", ChangeType.ADD), new ChangeLogEntry("Added Alt manager", ChangeType.ADD), new ChangeLogEntry("Fixed Jump boost with fly, speed and long jump", ChangeType.FIX), new ChangeLogEntry("Removed Minor version number", ChangeType.REMOVE), new ChangeLogEntry("Fixed Third person rotations with auto pot", ChangeType.FIX), new ChangeLogEntry("Inventory manager bug fix", ChangeType.FIX), new ChangeLogEntry("Renamed Auto Heal to Auto Potion", ChangeType.FIX), new ChangeLogEntry("Added delay option to Criticals", ChangeType.ADD), new ChangeLogEntry("Added Ray Tracing option to Kill Aura", ChangeType.ADD), new ChangeLogEntry("Removed Walls, Target HUD and Prediction options from Kill Aura", ChangeType.REMOVE), new ChangeLogEntry("Fixed Fly silent flag", ChangeType.FIX), new ChangeLogEntry("Fixed No Fall (use Packet mode)", ChangeType.FIX), new ChangeLogEntry("Added Gradient Health Bar Start and End options to ESP", ChangeType.ADD), new ChangeLogEntry("Added Tags and Tags Health options to ESP", ChangeType.ADD), new ChangeLogEntry("Added Armor Bar option to ESP", ChangeType.ADD), new ChangeLogEntry("Fixed Sprint Bugging Out", ChangeType.FIX), new ChangeLogEntry("Added void and block above checks to Auto Potion", ChangeType.FIX), new ChangeLogEntry("Added Color Pickers", ChangeType.ADD), new ChangeLogEntry("Added Value representations (ms, %, m) to Click Gui sliders", ChangeType.ADD), new ChangeLogEntry("Added Scaffold", ChangeType.ADD));
    }
    
    public static void drawChangeLog() {
        Wrapper.getMinecraftFontRenderer().drawStringWithShadow("Change Log:", 2.0f, 2.0f, -1);
        int y = 13;
        for (final ChangeLogEntry entry : ChangeLogUtils.ENTRIES) {
            Wrapper.getMinecraftFontRenderer().drawStringWithShadow(String.valueOf(entry.type.prefix) + entry.text, 2.0f, (float)y, entry.type.color);
            y += 11;
        }
    }
    
    private enum ChangeType
    {
        ADD("ADD", 0, "[+] ", -16711936), 
        FIX("FIX", 1, "[*] ", -256), 
        REMOVE("REMOVE", 2, "[-] ", -65536);
        
        private final String prefix;
        private final int color;
        
        private ChangeType(final String name, final int ordinal, final String prefix, final int color) {
            this.prefix = prefix;
            this.color = color;
        }
    }
    
    private static class ChangeLogEntry
    {
        private final String text;
        private final ChangeType type;
        
        public ChangeLogEntry(final String text, final ChangeType type) {
            this.text = text;
            this.type = type;
        }
    }
}
