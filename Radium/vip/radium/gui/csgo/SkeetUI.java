// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.gui.csgo;

import java.io.IOException;
import java.util.function.Consumer;
import vip.radium.gui.csgo.component.impl.sub.button.ButtonComponentImpl;
import java.util.Comparator;
import java.util.List;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.gui.csgo.component.impl.sub.comboBox.ComboBoxTextComponent;
import vip.radium.property.impl.EnumProperty;
import vip.radium.gui.csgo.component.impl.sub.slider.SliderTextComponent;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.gui.csgo.component.impl.sub.key.KeyBindComponent;
import vip.radium.gui.csgo.component.impl.sub.checkBox.CheckBoxTextComponent;
import vip.radium.gui.csgo.component.impl.GroupBoxComponent;
import vip.radium.module.Module;
import vip.radium.RadiumClient;
import vip.radium.utils.StringUtils;
import vip.radium.module.ModuleCategory;
import java.util.Iterator;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.render.OGLUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import vip.radium.utils.render.LockedResolution;
import java.awt.Font;
import vip.radium.utils.render.TTFUtils;
import vip.radium.utils.Wrapper;
import vip.radium.gui.csgo.component.TabComponent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.KeyPressEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.gui.csgo.component.Component;
import vip.radium.property.Property;
import net.minecraft.util.ResourceLocation;
import vip.radium.gui.font.TrueTypeFontRenderer;
import net.minecraft.client.gui.GuiScreen;

public final class SkeetUI extends GuiScreen
{
    public static final int GROUP_BOX_MARGIN = 8;
    public static final TrueTypeFontRenderer ICONS_RENDERER;
    public static final TrueTypeFontRenderer GROUP_BOX_HEADER_RENDERER;
    public static final TrueTypeFontRenderer FONT_RENDERER;
    public static final TrueTypeFontRenderer KEYBIND_FONT_RENDERER;
    public static final int GROUP_BOX_LEFT_MARGIN = 3;
    public static final int ENABLE_BUTTON_Y_OFFSET = 6;
    public static final int ENABLE_BUTTON_Y_GAP = 4;
    private static final int WIDTH = 370;
    private static final int HEIGHT = 350;
    private static final float TOTAL_BORDER_WIDTH = 3.5f;
    private static final float RAINBOW_BAR_WIDTH = 1.5f;
    private static final int TAB_SELECTOR_WIDTH = 48;
    public static final float USABLE_AREA_WIDTH = 315.0f;
    public static final float GROUP_BOX_WIDTH = 94.333336f;
    public static final float HALF_GROUP_BOX = 40.166668f;
    private static final SkeetUI INSTANCE;
    private static final ResourceLocation BACKGROUND_IMAGE;
    private static final char[] ICONS;
    private static final float USABLE_AREA_HEIGHT = 341.5f;
    private static final int TAB_SELECTOR_HEIGHT;
    private static final Property<Integer> colorProperty;
    private static double alpha;
    private static boolean open;
    private final Component rootComponent;
    private final Component tabSelectorComponent;
    private double targetAlpha;
    private boolean closed;
    @EventLink
    private final Listener<KeyPressEvent> onKeyPressEvent;
    private boolean dragging;
    private float prevX;
    private float prevY;
    private int selectorIndex;
    private TabComponent selectedTab;
    
    static {
        GROUP_BOX_HEADER_RENDERER = Wrapper.getCSGOFontRenderer();
        BACKGROUND_IMAGE = new ResourceLocation("radium/skeetchainmail.png");
        ICONS = new char[] { 'A', 'G', 'F', 'C', 'J', 'E', 'I', 'H' };
        TAB_SELECTOR_HEIGHT = 321 / SkeetUI.ICONS.length;
        colorProperty = new Property<Integer>("GUI Color", 10077246);
        (ICONS_RENDERER = new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("icons.ttf", 40), true, true)).generateTextures();
        (FONT_RENDERER = new TrueTypeFontRenderer(new Font("Tahoma", 0, 11), false, true)).generateTextures();
        (KEYBIND_FONT_RENDERER = new TrueTypeFontRenderer(new Font("Tahoma", 0, 9), false, false)).generateTextures();
        INSTANCE = new SkeetUI();
    }
    
    private SkeetUI() {
        this.onKeyPressEvent = (event -> {
            switch (event.getKey()) {
                case 54:
                case 210: {
                    Wrapper.getMinecraft().displayGuiScreen(this);
                    SkeetUI.alpha = 0.0;
                    this.targetAlpha = 255.0;
                    SkeetUI.open = true;
                    this.closed = false;
                    break;
                }
            }
            return;
        });
        this.rootComponent = new Component(null, 0.0f, 0.0f, 370.0f, 350.0f) {
            @Override
            public void drawComponent(final LockedResolution lockedResolution, final int mouseX, final int mouseY) {
                if (SkeetUI.this.dragging) {
                    this.setX(Math.max(0.0f, Math.min(lockedResolution.getWidth() - this.getWidth(), mouseX - SkeetUI.this.prevX)));
                    this.setY(Math.max(0.0f, Math.min(lockedResolution.getHeight() - this.getHeight(), mouseY - SkeetUI.this.prevY)));
                }
                final float borderX = this.getX();
                final float borderY = this.getY();
                final float width = this.getWidth();
                final float height = this.getHeight();
                Gui.drawRect(borderX, borderY, borderX + width, borderY + height, SkeetUI.getColor(1052942));
                Gui.drawRect(borderX + 0.5f, borderY + 0.5f, borderX + width - 0.5f, borderY + height - 0.5f, SkeetUI.getColor(3619386));
                Gui.drawRect(borderX + 1.0f, borderY + 1.0f, borderX + width - 1.0f, borderY + height - 1.0f, SkeetUI.getColor(2302755));
                Gui.drawRect(borderX + 3.0f, borderY + 3.0f, borderX + width - 3.0f, borderY + height - 3.0f, SkeetUI.getColor(3092271));
                float left = borderX + 3.5f;
                float top = borderY + 3.5f;
                float right = borderX + width - 3.5f;
                final float bottom = borderY + height - 3.5f;
                Gui.drawRect(left, top, right, bottom, SkeetUI.getColor(1381653));
                if (SkeetUI.alpha > 20.0) {
                    GL11.glEnable(3089);
                    OGLUtils.startScissorBox(lockedResolution, (int)left, (int)top, (int)(right - left), (int)(bottom - top));
                    RenderingUtils.drawImage(left, top, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, SkeetUI.BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 325.0f, top + 1.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, SkeetUI.BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 1.0f, top + 275.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, SkeetUI.BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 326.0f, top + 276.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, SkeetUI.BACKGROUND_IMAGE);
                    GL11.glDisable(3089);
                }
                final float xDif = (right - left) / 2.0f;
                top += 0.5f;
                left += 0.5f;
                right -= 0.5f;
                RenderingUtils.drawGradientRect(left, top, left + xDif, top + 1.5f - 0.5f, true, SkeetUI.getColor(RenderingUtils.darker(3957866, 1.5f)), SkeetUI.getColor(RenderingUtils.darker(7352943, 1.5f)));
                RenderingUtils.drawGradientRect(left + xDif, top, right, top + 1.5f - 0.5f, true, SkeetUI.getColor(RenderingUtils.darker(7352943, 1.5f)), SkeetUI.getColor(RenderingUtils.darker(8094516, 1.5f)));
                if (SkeetUI.alpha >= 70.0) {
                    Gui.drawRect(left, top + 1.5f - 1.0f, right, top + 1.5f - 0.5f, 1879048192);
                }
                for (final Component child : this.children) {
                    if (child instanceof TabComponent && SkeetUI.this.selectedTab != child) {
                        continue;
                    }
                    child.drawComponent(lockedResolution, mouseX, mouseY);
                }
            }
            
            @Override
            public void onKeyPress(final int keyCode) {
                for (final Component child : this.children) {
                    if (child instanceof TabComponent && SkeetUI.this.selectedTab != child) {
                        continue;
                    }
                    child.onKeyPress(keyCode);
                }
            }
            
            @Override
            public void onMouseClick(final int mouseX, final int mouseY, final int button) {
                for (final Component child : this.children) {
                    if (child instanceof TabComponent && SkeetUI.this.selectedTab != child) {
                        continue;
                    }
                    child.onMouseClick(mouseX, mouseY, button);
                }
                if (button == 0 && this.isHovered(mouseX, mouseY)) {
                    for (final Component child : this.getChildren()) {
                        if (child instanceof TabComponent && SkeetUI.this.selectedTab == child) {
                            for (final Component grandChild : child.getChildren()) {
                                if (grandChild.isHovered(mouseX, mouseY)) {
                                    return;
                                }
                            }
                        }
                        else {
                            if (!(child instanceof TabComponent) && child.isHovered(mouseX, mouseY)) {
                                return;
                            }
                            continue;
                        }
                    }
                    SkeetUI.access$6(SkeetUI.this, true);
                    SkeetUI.access$7(SkeetUI.this, mouseX - this.getX());
                    SkeetUI.access$8(SkeetUI.this, mouseY - this.getY());
                }
            }
            
            @Override
            public void onMouseRelease(final int button) {
                super.onMouseRelease(button);
                SkeetUI.access$6(SkeetUI.this, false);
            }
        };
        ModuleCategory[] values;
        for (int length = (values = ModuleCategory.values()).length, i = 0; i < length; ++i) {
            final ModuleCategory category = values[i];
            final TabComponent categoryTab = new TabComponent(this.rootComponent, StringUtils.upperSnakeCaseToPascal(category.name()), 51.5f, 5.0f, 315.0f, 341.5f) {
                @Override
                public void setupChildren() {
                    final List<Module> modulesInCategory = RadiumClient.getInstance().getModuleManager().getModulesForCategory(category);
                    for (final Module module : modulesInCategory) {
                        final GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, module.getLabel(), 0.0f, 0.0f, 94.333336f, 6.0f);
                        final CheckBoxTextComponent enabledButton = new CheckBoxTextComponent(groupBoxComponent, "Enabled", module::isEnabled, module::setEnabled);
                        enabledButton.addChild(new KeyBindComponent(enabledButton, module::getKey, module::setKey, 2.0f, 1.0f));
                        groupBoxComponent.addChild(enabledButton);
                        groupBoxComponent.addChild(new CheckBoxTextComponent(groupBoxComponent, "Hidden", module::isHidden, module::setHidden));
                        this.addChild(groupBoxComponent);
                        for (final Property<?> property : module.getElements()) {
                            Component component = null;
                            if (property.getType() == Boolean.class) {
                                final Property<Boolean> booleanProperty = (Property<Boolean>)property;
                                component = new CheckBoxTextComponent(groupBoxComponent, property.getLabel(), booleanProperty::getValue, booleanProperty::setValue, booleanProperty::isAvailable);
                            }
                            else if (property.getType() != Integer.class) {
                                if (property instanceof DoubleProperty) {
                                    final DoubleProperty doubleProperty = (DoubleProperty)property;
                                    component = new SliderTextComponent(groupBoxComponent, property.getLabel(), doubleProperty::getValue, doubleProperty::setValue, doubleProperty::getMin, doubleProperty::getMax, doubleProperty::getIncrement, doubleProperty::getRepresentation, doubleProperty::isAvailable);
                                }
                                else if (property instanceof EnumProperty) {
                                    final EnumProperty<?> enumProperty = (EnumProperty<?>)(EnumProperty)property;
                                    component = new ComboBoxTextComponent((Component)groupBoxComponent, property.getLabel(), enumProperty::getValues, enumProperty::setValue, enumProperty::getValue, () -> null, enumProperty::isAvailable, false);
                                }
                                else if (property instanceof MultiSelectEnumProperty) {
                                    final MultiSelectEnumProperty<?> enumProperty2 = (MultiSelectEnumProperty<?>)(MultiSelectEnumProperty)property;
                                    component = new ComboBoxTextComponent((Component)groupBoxComponent, property.getLabel(), enumProperty2::getValues, enumProperty2::setValue, () -> null, () -> enumProperty2.getValue(), enumProperty2::isAvailable, true);
                                }
                            }
                            if (component != null) {
                                groupBoxComponent.addChild(component);
                            }
                        }
                    }
                    this.getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
                }
            };
            this.rootComponent.addChild(categoryTab);
        }
        final TabComponent configTab = new TabComponent(this.rootComponent, "Settings", 51.5f, 5.0f, 315.0f, 341.5f) {
            @Override
            public void setupChildren() {
                final GroupBoxComponent configsGroupBox = new GroupBoxComponent(this, "Configs", 8.0f, 8.0f, 94.333336f, 140.0f);
                final int buttonHeight = 15;
                final Consumer<Integer> onPress = button -> {};
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Load", onPress, 88.333336f, 15.0f));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Save", onPress, 88.333336f, 15.0f));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Refresh", onPress, 88.333336f, 15.0f));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Delete", onPress, 88.333336f, 15.0f));
                this.addChild(configsGroupBox);
                final GroupBoxComponent guiSettingsGroupBox = new GroupBoxComponent(this, "GUI Settings", 8.0f, 8.0f, 94.333336f, 100.0f);
                this.addChild(guiSettingsGroupBox);
            }
        };
        this.rootComponent.addChild(configTab);
        this.selectedTab = this.rootComponent.getChildren().get(this.selectorIndex);
        this.tabSelectorComponent = new Component(this.rootComponent, 3.5f, 5.0f, 48.0f, 341.5f) {
            private double selectorY;
            
            @Override
            public void onMouseClick(final int mouseX, final int mouseY, final int button) {
                if (this.isHovered(mouseX, mouseY)) {
                    final float mouseYOffset = mouseY - SkeetUI.this.tabSelectorComponent.getY() - 10.0f;
                    if (mouseYOffset > 0.0f && mouseYOffset < SkeetUI.this.tabSelectorComponent.getHeight() - 10.0f) {
                        SkeetUI.access$12(SkeetUI.this, Math.min(SkeetUI.ICONS.length - 1, (int)(mouseYOffset / SkeetUI.TAB_SELECTOR_HEIGHT)));
                        SkeetUI.access$15(SkeetUI.this, SkeetUI.this.rootComponent.getChildren().get(SkeetUI.this.selectorIndex));
                    }
                }
            }
            
            @Override
            public void drawComponent(final LockedResolution resolution, final int mouseX, final int mouseY) {
                this.selectorY = RenderingUtils.progressiveAnimation(this.selectorY, SkeetUI.this.selectorIndex * SkeetUI.TAB_SELECTOR_HEIGHT + 10, 1.0);
                final float x = this.getX();
                final float y = this.getY();
                final float width = this.getWidth();
                final float height = this.getHeight();
                final int innerColor = SkeetUI.getColor(394758);
                final int outerColor = SkeetUI.getColor(2105376);
                Gui.drawRect(x, y, x + width, y + this.selectorY, SkeetUI.getColor(789516));
                Gui.drawRect(x + width - 1.0f, y, x + width, y + this.selectorY, innerColor);
                Gui.drawRect(x + width - 0.5f, y, x + width, y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY - 1.0, x + width - 0.5f, y + this.selectorY, innerColor);
                Gui.drawRect(x, y + this.selectorY - 0.5, x + width, y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, SkeetUI.getColor(789516));
                Gui.drawRect(x + width - 1.0f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, innerColor);
                Gui.drawRect(x + width - 0.5f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, outerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width - 0.5f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT + 1.0, innerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT + 0.5, outerColor);
                if (SkeetUI.shouldRenderText()) {
                    for (int i = 0; i < SkeetUI.ICONS.length; ++i) {
                        final String c = String.valueOf(SkeetUI.ICONS[i]);
                        SkeetUI.ICONS_RENDERER.drawString(c, x + 24.0f - SkeetUI.ICONS_RENDERER.getWidth(c) / 2.0f - 1.0f, y + 10.0f + i * SkeetUI.TAB_SELECTOR_HEIGHT + SkeetUI.TAB_SELECTOR_HEIGHT / 2.0f - SkeetUI.ICONS_RENDERER.getHeight(c) / 2.0f, SkeetUI.getColor((i == SkeetUI.this.selectorIndex) ? 16777215 : 8421504));
                    }
                }
            }
        };
        this.rootComponent.addChild(this.tabSelectorComponent);
    }
    
    public static double getAlpha() {
        return SkeetUI.alpha;
    }
    
    public static int getColor() {
        return getColor(SkeetUI.colorProperty.getValue());
    }
    
    public static boolean shouldRenderText() {
        return SkeetUI.alpha > 20.0;
    }
    
    private static boolean isVisible() {
        return SkeetUI.open || SkeetUI.alpha > 0.0;
    }
    
    public static int getColor(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        final int a = (int)SkeetUI.alpha;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }
    
    public static void init() {
        RadiumClient.getInstance().getEventBus().subscribe(SkeetUI.INSTANCE);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            if (SkeetUI.open) {
                this.targetAlpha = 0.0;
                SkeetUI.open = false;
                this.dragging = false;
            }
        }
        else {
            this.rootComponent.onKeyPress(keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (!SkeetUI.open && SkeetUI.alpha == 0.0 && !this.closed) {
            Wrapper.getMinecraft().displayGuiScreen(null);
            return;
        }
        if (isVisible()) {
            SkeetUI.alpha = RenderingUtils.linearAnimation(SkeetUI.alpha, this.targetAlpha, 10.0);
            this.rootComponent.drawComponent(RenderingUtils.getLockedResolution(), mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (isVisible()) {
            this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (isVisible()) {
            this.rootComponent.onMouseRelease(state);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    static /* synthetic */ void access$6(final SkeetUI skeetUI, final boolean dragging) {
        skeetUI.dragging = dragging;
    }
    
    static /* synthetic */ void access$7(final SkeetUI skeetUI, final float prevX) {
        skeetUI.prevX = prevX;
    }
    
    static /* synthetic */ void access$8(final SkeetUI skeetUI, final float prevY) {
        skeetUI.prevY = prevY;
    }
    
    static /* synthetic */ void access$12(final SkeetUI skeetUI, final int selectorIndex) {
        skeetUI.selectorIndex = selectorIndex;
    }
    
    static /* synthetic */ void access$15(final SkeetUI skeetUI, final TabComponent selectedTab) {
        skeetUI.selectedTab = selectedTab;
    }
}
