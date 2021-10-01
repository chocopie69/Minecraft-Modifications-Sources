// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumChatFormatting;
import vip.radium.utils.Wrapper;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Better Chat", category = ModuleCategory.OTHER)
public final class BetterChat extends Module
{
    private String lastMessage;
    private int amount;
    private int line;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    
    public BetterChat() {
        this.lastMessage = "";
        final Packet<?> packet;
        S02PacketChat s02PacketChat;
        IChatComponent message;
        String rawMessage;
        GuiNewChat chatGui;
        this.onPacketReceiveEvent = (e -> {
            packet = e.getPacket();
            if (packet instanceof S2EPacketCloseWindow) {
                if (this.isTypingInChat()) {
                    e.setCancelled();
                }
            }
            else if (packet instanceof S02PacketChat) {
                s02PacketChat = (S02PacketChat)packet;
                if (s02PacketChat.getType() == 0) {
                    message = s02PacketChat.getChatComponent();
                    rawMessage = message.getFormattedText();
                    chatGui = Wrapper.getMinecraft().ingameGUI.getChatGUI();
                    if (this.lastMessage.equals(rawMessage)) {
                        chatGui.deleteChatLine(this.line);
                        ++this.amount;
                        s02PacketChat.getChatComponent().appendText(EnumChatFormatting.GRAY + " [x" + this.amount + "]");
                    }
                    else {
                        this.amount = 1;
                    }
                    ++this.line;
                    this.lastMessage = rawMessage;
                    chatGui.printChatMessageWithOptionalDeletion(message, this.line);
                    if (this.line > 256) {
                        this.line = 0;
                    }
                    e.setCancelled();
                }
            }
        });
    }
    
    private boolean isTypingInChat() {
        return Wrapper.getCurrentScreen() instanceof GuiChat;
    }
}
