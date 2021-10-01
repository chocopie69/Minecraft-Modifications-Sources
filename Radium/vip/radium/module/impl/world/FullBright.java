// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.world;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import vip.radium.utils.Wrapper;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.EnumProperty;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "Full Bright", category = ModuleCategory.WORLD)
public final class FullBright extends Module
{
    private final EnumProperty<Mode> modeProperty;
    private float lastGamma;
    private boolean addedNv;
    private boolean hadNv;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate;
    
    public FullBright() {
        this.modeProperty = new EnumProperty<Mode>("Mode", Mode.POTION);
        this.onUpdate = (event -> {
            if (event.isPre()) {
                switch (this.modeProperty.getValue()) {
                    case GAMMA: {
                        if (!this.hadNv && Wrapper.getPlayer().isPotionActive(Potion.nightVision)) {
                            Wrapper.getPlayer().removePotionEffect(Potion.nightVision.id);
                        }
                        Wrapper.getGameSettings().gammaSetting = 1000.0f;
                        break;
                    }
                    case POTION: {
                        if (!this.hadNv) {
                            Wrapper.getPlayer().addPotionEffect(new PotionEffect(Potion.nightVision.id, 5200, 68));
                            this.addedNv = true;
                            break;
                        }
                        else {
                            break;
                        }
                        break;
                    }
                }
            }
        });
    }
    
    @Override
    public void onEnable() {
        this.lastGamma = Wrapper.getGameSettings().gammaSetting;
        this.hadNv = Wrapper.getPlayer().isPotionActive(Potion.nightVision);
    }
    
    @Override
    public void onDisable() {
        switch (this.modeProperty.getValue()) {
            case POTION: {
                if (this.addedNv && !this.hadNv) {
                    Wrapper.getPlayer().removePotionEffect(Potion.nightVision.id);
                    break;
                }
                break;
            }
            case GAMMA: {
                Wrapper.getGameSettings().gammaSetting = this.lastGamma;
                break;
            }
        }
    }
    
    private enum Mode
    {
        GAMMA("GAMMA", 0), 
        POTION("POTION", 1);
        
        private Mode(final String name, final int ordinal) {
        }
    }
}
