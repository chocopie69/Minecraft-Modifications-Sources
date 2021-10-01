// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.other;

import vip.radium.module.impl.player.InventoryManager;
import vip.radium.module.impl.combat.KillAura;
import java.util.Arrays;
import vip.radium.module.impl.movement.LongJump;
import vip.radium.module.impl.movement.Flight;
import vip.radium.module.impl.movement.Speed;
import vip.radium.module.ModuleManager;
import java.util.Iterator;
import net.minecraft.network.Packet;
import vip.radium.utils.Wrapper;
import vip.radium.utils.HypixelGameUtils;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S07PacketRespawn;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.RadiumClient;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import java.util.ArrayList;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.render.DisplayTitleEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;
import java.util.UUID;
import java.util.List;
import vip.radium.utils.TimerUtil;
import vip.radium.property.Property;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Auto Hypixel", category = ModuleCategory.OTHER)
public final class AutoHypixel extends Module
{
    private final Property<Boolean> autoDisableProperty;
    private final Property<Boolean> respawnProperty;
    private final Property<Boolean> autoJoinProperty;
    private final Property<Boolean> antiAtlasProperty;
    private final TimerUtil gameTimer;
    private final TimerUtil respawnTimer;
    private List<Module> movementModules;
    private List<Module> disableOnRespawn;
    private PingSpoof pingSpoof;
    private boolean needSend;
    private final List<UUID> reportedPlayers;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent;
    @EventLink
    public final Listener<DisplayTitleEvent> onDisplayTitleEvent;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    
    public AutoHypixel() {
        this.autoDisableProperty = new Property<Boolean>("On Flag", true);
        this.respawnProperty = new Property<Boolean>("On Respawn", true);
        this.autoJoinProperty = new Property<Boolean>("Auto Join", true);
        this.antiAtlasProperty = new Property<Boolean>("Anti Atlas", true);
        this.gameTimer = new TimerUtil();
        this.respawnTimer = new TimerUtil();
        this.reportedPlayers = new ArrayList<UUID>();
        boolean msg;
        final Iterator<Module> iterator;
        Module module;
        boolean msg2;
        final Iterator<Module> iterator2;
        Module module2;
        S02PacketChat packetChat;
        this.onPacketReceiveEvent = (event -> {
            if (event.getPacket() instanceof S08PacketPlayerPosLook && this.autoDisableProperty.getValue()) {
                msg = false;
                this.movementModules.iterator();
                while (iterator.hasNext()) {
                    module = iterator.next();
                    if (module.isEnabled()) {
                        module.toggle();
                        if (!msg) {
                            msg = true;
                        }
                        else {
                            continue;
                        }
                    }
                }
                if (msg) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Flag", "Disabling modules to prevent flags", NotificationType.WARNING));
                }
            }
            else if (event.getPacket() instanceof S07PacketRespawn && this.respawnProperty.getValue()) {
                if (this.respawnTimer.hasElapsed(50L)) {
                    msg2 = false;
                    this.disableOnRespawn.iterator();
                    while (iterator2.hasNext()) {
                        module2 = iterator2.next();
                        if (module2.isEnabled()) {
                            module2.toggle();
                            if (!msg2) {
                                msg2 = true;
                            }
                            else {
                                continue;
                            }
                        }
                    }
                    if (msg2) {
                        RadiumClient.getInstance().getNotificationManager().add(new Notification("Respawned", "Disabled some modules on respawn", NotificationType.INFO));
                    }
                    this.respawnTimer.reset();
                }
            }
            else if (event.getPacket() instanceof S02PacketChat) {
                packetChat = (S02PacketChat)event.getPacket();
                if (packetChat.getChatComponent().getUnformattedText().contains("Protect your bed and destroy the enemy beds")) {
                    RadiumClient.getInstance().getNotificationManager().add(new Notification("Bedwars", "Do not fly until this notification closes", 20000L, NotificationType.WARNING));
                }
            }
            return;
        });
        this.onDisplayTitleEvent = (event -> {
            if (this.autoJoinProperty.getValue() && event.getTitle().contains("VICTORY")) {
                RadiumClient.getInstance().getNotificationManager().add(new Notification("Auto Join", "Sending you to a new game in 2 seconds", 2000L, NotificationType.INFO));
                this.needSend = true;
                this.gameTimer.reset();
            }
            return;
        });
        final C01PacketChatMessage packet;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre() && this.needSend && this.gameTimer.hasElapsed(2000L)) {
                new C01PacketChatMessage("/play " + HypixelGameUtils.getSkyWarsMode().name().toLowerCase());
                Wrapper.sendPacketDirect(packet);
                this.needSend = false;
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.pingSpoof = ModuleManager.getInstance(PingSpoof.class);
        this.movementModules = Arrays.asList(ModuleManager.getInstance(Speed.class), ModuleManager.getInstance(Flight.class), ModuleManager.getInstance(LongJump.class));
        this.disableOnRespawn = Arrays.asList(ModuleManager.getInstance(KillAura.class), ModuleManager.getInstance(InventoryManager.class));
    }
}
