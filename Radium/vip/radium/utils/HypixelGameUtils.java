// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import net.minecraft.client.gui.GuiIngame;
import vip.radium.RadiumClient;
import net.minecraft.network.play.server.S02PacketChat;
import vip.radium.event.impl.world.ScoreboardHeaderChangeEvent;
import vip.radium.event.impl.world.ScoreboardModeChangeEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import io.github.nevalackin.homoBus.Listener;

public final class HypixelGameUtils
{
    private static final HypixelGameUtils INSTANCE;
    private static GameMode gameMode;
    private static SkyWarsModePrefix skyWarsPrefix;
    private static SkyWarsModeSuffix skyWarsSuffix;
    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive;
    @EventLink
    private final Listener<ScoreboardModeChangeEvent> onModeChange;
    @EventLink
    private final Listener<ScoreboardHeaderChangeEvent> onHeaderChange;
    
    static {
        INSTANCE = new HypixelGameUtils();
        HypixelGameUtils.skyWarsPrefix = SkyWarsModePrefix.SOLO;
        HypixelGameUtils.skyWarsSuffix = SkyWarsModeSuffix.INSANE;
    }
    
    private HypixelGameUtils() {
        S02PacketChat packetChat;
        String chatMessage;
        this.onPacketReceive = (event -> {
            if (isSkyWars() && event.getPacket() instanceof S02PacketChat) {
                packetChat = (S02PacketChat)event.getPacket();
                chatMessage = packetChat.getChatComponent().getUnformattedText();
                if (chatMessage.equals("Teaming is not allowed on Solo mode!")) {
                    HypixelGameUtils.skyWarsPrefix = SkyWarsModePrefix.SOLO;
                }
                else if (chatMessage.equals("Cross Teaming / Teaming with other teams is not allowed!")) {
                    HypixelGameUtils.skyWarsPrefix = SkyWarsModePrefix.TEAMS;
                }
            }
            return;
        });
        final Object o;
        final int length;
        int i = 0;
        final SkyWarsModeSuffix[] array;
        SkyWarsModeSuffix suffix;
        this.onModeChange = (event -> {
            SkyWarsModeSuffix.values();
            length = o.length;
            while (i < length) {
                suffix = array[i];
                if (event.getMode().contains(suffix.name())) {
                    HypixelGameUtils.skyWarsSuffix = suffix;
                }
                else {
                    ++i;
                }
            }
            return;
        });
        final Object o2;
        final int length2;
        int j = 0;
        final GameMode[] array2;
        GameMode mode;
        this.onHeaderChange = (event -> {
            GameMode.values();
            length2 = o2.length;
            while (j < length2) {
                mode = array2[j];
                if (mode.getText().equals(event.getHeader())) {
                    HypixelGameUtils.gameMode = mode;
                }
                else {
                    ++j;
                }
            }
            return;
        });
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }
    
    public static GameMode getGameMode() {
        return ServerUtils.isOnHypixel() ? HypixelGameUtils.gameMode : GameMode.INVALID;
    }
    
    public static SkyWarsMode getSkyWarsMode() {
        if (!ServerUtils.isOnHypixel() || !isSkyWars()) {
            return SkyWarsMode.INVALID;
        }
        switch (HypixelGameUtils.skyWarsPrefix) {
            case MEGA: {
                switch (HypixelGameUtils.skyWarsSuffix) {
                    default: {
                        return SkyWarsMode.INVALID;
                    }
                    case NORMAL: {
                        return SkyWarsMode.MEGA_NORMAL;
                    }
                    case DOUBLES: {
                        return SkyWarsMode.MEGA_DOUBLES;
                    }
                }
                break;
            }
            case SOLO: {
                switch (HypixelGameUtils.skyWarsSuffix) {
                    case INSANE: {
                        return SkyWarsMode.SOLO_INSANE;
                    }
                    case NORMAL: {
                        return SkyWarsMode.SOLO_NORMAL;
                    }
                    default: {
                        return SkyWarsMode.INVALID;
                    }
                }
                break;
            }
            default: {
                switch (HypixelGameUtils.skyWarsSuffix) {
                    case INSANE: {
                        return SkyWarsMode.TEAMS_INSANE;
                    }
                    case NORMAL: {
                        return SkyWarsMode.TEAMS_NORMAL;
                    }
                    default: {
                        return SkyWarsMode.INVALID;
                    }
                }
                break;
            }
        }
    }
    
    public static boolean hasGameStarted() {
        return isSkyWars() && getModeText().length() > 0;
    }
    
    private static String getModeText() {
        return GuiIngame.modeText;
    }
    
    public static boolean isSkyWars() {
        return HypixelGameUtils.gameMode == GameMode.SKYWARS;
    }
    
    public enum GameMode
    {
        INVALID("INVALID", 0, ""), 
        LOBBY("LOBBY", 1, "HYPIXEL"), 
        BEDWARS("BEDWARS", 2, "BEDWARS"), 
        SKYWARS("SKYWARS", 3, "SKYWARS"), 
        BLITZ_SG("BLITZ_SG", 4, "BLITZ SG"), 
        PIT("PIT", 5, "THE HYPIXEL PIT"), 
        UHC("UHC", 6, "HYPIXEL UHC"), 
        DUELS("DUELS", 7, "DUELS");
        
        private final String text;
        
        private GameMode(final String name, final int ordinal, final String text) {
            this.text = text;
        }
        
        public String getText() {
            return this.text;
        }
    }
    
    public enum SkyWarsMode
    {
        INVALID("INVALID", 0), 
        SOLO_NORMAL("SOLO_NORMAL", 1), 
        SOLO_INSANE("SOLO_INSANE", 2), 
        TEAMS_NORMAL("TEAMS_NORMAL", 3), 
        TEAMS_INSANE("TEAMS_INSANE", 4), 
        RANKED_NORMAL("RANKED_NORMAL", 5), 
        MEGA_NORMAL("MEGA_NORMAL", 6), 
        MEGA_DOUBLES("MEGA_DOUBLES", 7);
        
        private SkyWarsMode(final String name, final int ordinal) {
        }
    }
    
    public enum SkyWarsModePrefix
    {
        TEAMS("TEAMS", 0), 
        SOLO("SOLO", 1), 
        MEGA("MEGA", 2);
        
        private SkyWarsModePrefix(final String name, final int ordinal) {
        }
    }
    
    public enum SkyWarsModeSuffix
    {
        INSANE("INSANE", 0), 
        NORMAL("NORMAL", 1), 
        DOUBLES("DOUBLES", 2);
        
        private SkyWarsModeSuffix(final String name, final int ordinal) {
        }
    }
}
