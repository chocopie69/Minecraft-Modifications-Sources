// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import java.util.Collection;
import java.util.Arrays;
import vip.radium.RadiumClient;
import vip.radium.module.impl.player.NoFall;
import vip.radium.module.impl.player.InventoryManager;
import vip.radium.module.impl.world.AutoTool;
import vip.radium.module.impl.player.NoRotate;
import vip.radium.module.impl.other.ChatBypass;
import vip.radium.module.impl.other.Spammer;
import vip.radium.module.impl.other.hackerdetect.HackerDetector;
import vip.radium.module.impl.other.AutoHypixel;
import vip.radium.module.impl.player.ChestStealer;
import vip.radium.module.impl.other.entityDesync.EntityDesync;
import vip.radium.module.impl.other.TimeChanger;
import vip.radium.module.impl.other.MemoryFix;
import vip.radium.module.impl.other.PingSpoof;
import vip.radium.module.impl.player.InventoryMove;
import vip.radium.module.impl.render.hud.NoScoreboard;
import vip.radium.module.impl.render.EnchantGlint;
import vip.radium.module.impl.render.MoreParticles;
import vip.radium.module.impl.render.TargetHUD;
import vip.radium.module.impl.render.Hitmarkers;
import vip.radium.module.impl.render.Crosshair;
import vip.radium.module.impl.render.BlockOutline;
import vip.radium.module.impl.player.Camera;
import vip.radium.module.impl.world.WorldColor;
import vip.radium.module.impl.other.BetterChat;
import vip.radium.module.impl.render.Animations;
import vip.radium.module.impl.render.ESP;
import vip.radium.module.impl.render.Chams;
import vip.radium.module.impl.render.ChestESP;
import vip.radium.module.impl.world.FullBright;
import vip.radium.module.impl.render.hud.Hud;
import vip.radium.module.impl.other.AutoBow;
import vip.radium.module.impl.combat.AntiBot;
import vip.radium.module.impl.combat.TargetStrafe;
import vip.radium.module.impl.player.Regen;
import vip.radium.module.impl.combat.AutoPotion;
import vip.radium.module.impl.ghost.AutoClicker;
import vip.radium.module.impl.ghost.Reach;
import vip.radium.module.impl.combat.Criticals;
import vip.radium.module.impl.player.Velocity;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.module.impl.world.Scaffold;
import vip.radium.module.impl.movement.Jesus;
import vip.radium.module.impl.movement.AntiFall;
import vip.radium.module.impl.movement.LongJump;
import vip.radium.module.impl.movement.Step;
import vip.radium.module.impl.movement.Flight;
import vip.radium.module.impl.movement.NoSlowdown;
import vip.radium.module.impl.movement.Speed;
import vip.radium.module.impl.movement.Sprint;
import com.google.common.collect.ImmutableClassToInstanceMap;

public final class ModuleManager
{
    private final ImmutableClassToInstanceMap<Module> instanceMap;
    
    public ModuleManager() {
        this.instanceMap = this.putInInstanceMap(new Sprint(), new Speed(), new NoSlowdown(), new Flight(), new Step(), new LongJump(), new AntiFall(), new Jesus(), new Scaffold(), new KillAura(), new Velocity(), new Criticals(), new Reach(), new AutoClicker(), new AutoPotion(), new Regen(), new TargetStrafe(), new AntiBot(), new AutoBow(), new Hud(), new FullBright(), new ChestESP(), new Chams(), new ESP(), new Animations(), new BetterChat(), new WorldColor(), new Camera(), new BlockOutline(), new Crosshair(), new Hitmarkers(), new TargetHUD(), new MoreParticles(), new EnchantGlint(), new NoScoreboard(), new InventoryMove(), new PingSpoof(), new MemoryFix(), new TimeChanger(), new EntityDesync(), new ChestStealer(), new AutoHypixel(), new HackerDetector(), new Spammer(), new ChatBypass(), new NoRotate(), new AutoTool(), new InventoryManager(), new NoFall());
        this.getModules().forEach(Module::reflectProperties);
        this.getModules().forEach(Module::resetPropertyValues);
        RadiumClient.getInstance().getEventBus().subscribe(this);
    }
    
    public void postInit() {
        this.getModules().forEach(Module::resetPropertyValues);
    }
    
    private ImmutableClassToInstanceMap<Module> putInInstanceMap(final Module... modules) {
        final ImmutableClassToInstanceMap.Builder<Module> modulesBuilder = (ImmutableClassToInstanceMap.Builder<Module>)ImmutableClassToInstanceMap.builder();
        Arrays.stream(modules).forEach(module -> modulesBuilder.put((Class)module.getClass(), (Object)module));
        return (ImmutableClassToInstanceMap<Module>)modulesBuilder.build();
    }
    
    public Collection<Module> getModules() {
        return (Collection<Module>)this.instanceMap.values();
    }
    
    public <T extends Module> T getModule(final Class<T> moduleClass) {
        return (T)this.instanceMap.getInstance((Class)moduleClass);
    }
    
    public Optional<Module> getModule(final String label) {
        return this.getModules().stream().filter(module -> module.getLabel().replace(" ", "").equalsIgnoreCase(label)).findFirst();
    }
    
    public static <T extends Module> T getInstance(final Class<T> clazz) {
        return (T)RadiumClient.getInstance().getModuleManager().getModule((Class<Module>)clazz);
    }
    
    public List<Module> getModulesForCategory(final ModuleCategory category) {
        return this.getModules().stream().filter(module -> module.getCategory() == category).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
    }
}
