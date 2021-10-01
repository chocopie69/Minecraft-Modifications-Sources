// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium;

import com.thealtening.auth.service.AlteningServiceType;
import vip.radium.keybind.BindSystem;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.utils.Wrapper;
import vip.radium.alt.AltManager;
import vip.radium.command.CommandManager;
import vip.radium.notification.NotificationManager;
import vip.radium.friend.FriendManager;
import vip.radium.config.ConfigManager;
import vip.radium.module.ModuleManager;
import vip.radium.event.Event;
import io.github.nevalackin.homoBus.EventBus;

public final class RadiumClient
{
    private static final RadiumClient INSTANCE;
    private EventBus<Event> eventBus;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private FriendManager friendManager;
    private NotificationManager notificationManager;
    private CommandManager commandManager;
    private AltManager altManager;
    public static final String NAME = "Radium";
    public static final String VERSION = "v1.0";
    
    static {
        INSTANCE = new RadiumClient();
    }
    
    public FriendManager getFriendManager() {
        return this.friendManager;
    }
    
    public AltManager getAltManager() {
        return this.altManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }
    
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    
    public CommandManager getCommandHandler() {
        return this.commandManager;
    }
    
    public void onPostInit() {
        Wrapper.getFontRenderer().generateTextures();
        Wrapper.getMediumFontRenderer().generateTextures();
        Wrapper.getSmallFontRenderer().generateTextures();
        Wrapper.getCSGOFontRenderer().generateTextures();
        this.configManager = new ConfigManager();
        this.friendManager = new FriendManager();
        this.altManager = new AltManager();
        this.eventBus = new EventBus<Event>();
        this.moduleManager = new ModuleManager();
        SkeetUI.init();
        this.notificationManager = new NotificationManager();
        this.commandManager = new CommandManager();
        this.eventBus.subscribe(new BindSystem(this.moduleManager.getModules()));
        this.moduleManager.postInit();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.altManager.getAlteningAuth().updateService(AlteningServiceType.MOJANG)));
    }
    
    public EventBus<Event> getEventBus() {
        return this.eventBus;
    }
    
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }
    
    public static RadiumClient getInstance() {
        return RadiumClient.INSTANCE;
    }
}
