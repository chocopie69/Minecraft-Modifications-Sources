// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import org.apache.commons.lang3.RandomUtils;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.SendMessageEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Chat Bypass", category = ModuleCategory.OTHER)
public final class ChatBypass extends Module
{
    private static final char[] INVIS_CHARS;
    private final EnumProperty<BypassMode> bypassModeProperty;
    @EventLink
    public final Listener<SendMessageEvent> SendMessageEvent;
    
    static {
        INVIS_CHARS = new char[0];
    }
    
    public ChatBypass() {
        this.bypassModeProperty = new EnumProperty<BypassMode>("Mode", BypassMode.INVIS);
        StringBuilder stringBuilder;
        final Object o;
        int length;
        int i = 0;
        final char[] array;
        char character;
        this.SendMessageEvent = (event -> {
            if (!event.getMessage().startsWith("/")) {
                switch (this.bypassModeProperty.getValue()) {
                    case INVIS: {
                        stringBuilder = new StringBuilder();
                        event.getMessage().toCharArray();
                        for (length = o.length; i < length; ++i) {
                            character = array[i];
                            stringBuilder.append(character).append(ChatBypass.INVIS_CHARS[RandomUtils.nextInt(0, ChatBypass.INVIS_CHARS.length)]);
                        }
                        event.setMessage(stringBuilder.toString());
                        break;
                    }
                }
            }
        });
    }
    
    private enum BypassMode
    {
        INVIS("INVIS", 0), 
        FONT("FONT", 1);
        
        private BypassMode(final String name, final int ordinal) {
        }
    }
}
