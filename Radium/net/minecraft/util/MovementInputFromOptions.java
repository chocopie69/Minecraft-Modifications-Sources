// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import org.lwjgl.input.Keyboard;
import vip.radium.utils.Wrapper;
import net.minecraft.client.gui.GuiChat;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.player.InventoryMove;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;
    
    public MovementInputFromOptions(final GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }
    
    @Override
    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (ModuleManager.getInstance(InventoryMove.class).isEnabled()) {
            if (!(Wrapper.getCurrentScreen() instanceof GuiChat)) {
                if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                    ++this.moveForward;
                }
                if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                    --this.moveForward;
                }
                if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                    ++this.moveStrafe;
                }
                if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                    --this.moveStrafe;
                }
                this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            }
            else {
                this.jump = this.gameSettings.keyBindJump.isKeyDown();
            }
        }
        else {
            if (this.gameSettings.keyBindForward.isKeyDown()) {
                ++this.moveForward;
            }
            if (this.gameSettings.keyBindBack.isKeyDown()) {
                --this.moveForward;
            }
            if (this.gameSettings.keyBindLeft.isKeyDown()) {
                ++this.moveStrafe;
            }
            if (this.gameSettings.keyBindRight.isKeyDown()) {
                --this.moveStrafe;
            }
            this.jump = this.gameSettings.keyBindJump.isKeyDown();
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        }
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (this.sneak) {
            this.moveStrafe *= (float)0.3;
            this.moveForward *= (float)0.3;
        }
    }
}
