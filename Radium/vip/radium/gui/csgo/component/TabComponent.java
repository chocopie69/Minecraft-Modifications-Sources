// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo.component;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.utils.render.LockedResolution;

public abstract class TabComponent extends Component
{
    private final String name;
    
    public TabComponent(final Component parent, final String name, final float x, final float y, final float width, final float height) {
        super(parent, x, y, width, height);
        this.setupChildren();
        this.name = name;
    }
    
    public abstract void setupChildren();
    
    @Override
    public void drawComponent(final LockedResolution resolution, final int mouseX, final int mouseY) {
        SkeetUI.FONT_RENDERER.drawStringWithShadow(this.name, this.getX() + 8.0f, this.getY() + 8.0f - 3.0f, SkeetUI.getColor(16777215));
        float x = 8.0f;
        for (int i = 0; i < this.children.size(); ++i) {
            final Component child = this.children.get(i);
            child.setX(x);
            if (i < 3) {
                child.setY(14.0f);
            }
            child.drawComponent(resolution, mouseX, mouseY);
            x += 102.333336f;
            if (x + 8.0f + 94.333336f > 315.0f) {
                x = 8.0f;
            }
            if (i > 2) {
                int above = i - 3;
                int totalY = 14;
                do {
                    final Component componentAbove = this.getChildren().get(above);
                    totalY += (int)(componentAbove.getHeight() + 8.0f);
                    above -= 3;
                } while (above >= 0);
                child.setY((float)totalY);
            }
        }
    }
}
