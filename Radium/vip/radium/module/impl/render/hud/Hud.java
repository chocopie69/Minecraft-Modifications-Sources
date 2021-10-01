// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.module.impl.render.hud;

import java.util.Collection;
import java.util.ArrayList;
import java.util.function.Supplier;
import vip.radium.utils.render.Translate;
import vip.radium.gui.font.FontRenderer;
import vip.radium.utils.render.LockedResolution;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.utils.StringUtils;
import net.minecraft.client.gui.ScaledResolution;
import vip.radium.RadiumClient;
import net.minecraft.client.gui.Gui;
import vip.radium.utils.render.RenderingUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.Minecraft;
import java.util.Comparator;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.Colors;
import java.util.HashMap;
import io.github.nevalackin.homoBus.Priority;
import vip.radium.event.impl.render.Render2DEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import io.github.nevalackin.homoBus.EventLink;
import vip.radium.event.impl.render.WindowResizeEvent;
import io.github.nevalackin.homoBus.Listener;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.Property;
import java.util.List;
import java.util.Map;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.Module;

@ModuleInfo(label = "HUD", category = ModuleCategory.RENDER)
public final class Hud extends Module
{
    private static final float DARK_FACTOR = 0.49f;
    private static final Map<Module, String> displayLabelCache;
    private static List<Module> moduleCache;
    private final Property<Boolean> cFontProperty;
    private final Property<Boolean> watermarkProperty;
    public final Property<String> watermarkTextProperty;
    private final EnumProperty<ColorMode> watermarkColorModeProperty;
    private final Property<Integer> watermarkColorProperty;
    private final Property<Integer> secondWatermarkColorProperty;
    private final DoubleProperty watermarkFadeSpeedProperty;
    private final Property<Boolean> arrayListProperty;
    private final EnumProperty<ArrayListPosition> arrayListPositionProperty;
    @EventLink
    public final Listener<WindowResizeEvent> onWindowResizeEvent;
    private final EnumProperty<SortingMode> sortingModeProperty;
    private final EnumProperty<ColorMode> arrayListColorModeProperty;
    private final DoubleProperty arrayListFadeSpeedProperty;
    private final Property<Boolean> arrayListBackgroundProperty;
    private final Property<Boolean> arrayListLineProperty;
    private final Property<Boolean> arrayListOutlineProperty;
    private final Property<Integer> arrayListColorProperty;
    private final Property<Integer> secondaryArrayListColorProperty;
    private final Property<Boolean> notificationsProperty;
    private final Property<Boolean> bpsProperty;
    private final Property<Boolean> coordsProperty;
    private final Property<Boolean> fpsProperty;
    private final Property<Boolean> potionsProperty;
    private double lastDist;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent;
    @EventLink
    @Priority(0)
    public final Listener<Render2DEvent> onRender2DEvent;
    
    static {
        displayLabelCache = new HashMap<Module, String>();
    }
    
    public Hud() {
        this.cFontProperty = new Property<Boolean>("CFont", true);
        this.watermarkProperty = new Property<Boolean>("Watermark", true);
        this.watermarkTextProperty = new Property<String>("Watermark Text", String.valueOf("Radium".charAt(0)) + "§R§F" + "Radium".substring(1) + " " + "v1.0", this.watermarkProperty::getValue);
        this.watermarkColorModeProperty = new EnumProperty<ColorMode>("W-Color Mode", ColorMode.FADE, this.watermarkProperty::getValue);
        this.watermarkColorProperty = new Property<Integer>("Watermark Color", Colors.PINK, this.watermarkProperty::getValue);
        this.secondWatermarkColorProperty = new Property<Integer>("Second W-Color", Colors.BLUE, () -> this.watermarkProperty.getValue() && this.watermarkColorModeProperty.getValue() == ColorMode.BLEND);
        this.watermarkFadeSpeedProperty = new DoubleProperty("W-Fade Speed", 1.0, () -> this.watermarkProperty.getValue() && this.watermarkColorModeProperty.getValue() != ColorMode.RAINBOW && this.watermarkColorModeProperty.getValue() != ColorMode.STATIC, 0.1, 5.0, 0.1);
        this.arrayListProperty = new Property<Boolean>("ArrayList", true);
        this.arrayListPositionProperty = new EnumProperty<ArrayListPosition>("ArrayList Pos", ArrayListPosition.TOP, this.arrayListProperty::getValue);
        this.onWindowResizeEvent = (event -> this.updateModulePositions(event.getScaledResolution()));
        this.sortingModeProperty = new EnumProperty<SortingMode>("A-Sort Mode", SortingMode.LENGTH, this.arrayListProperty::getValue);
        this.arrayListColorModeProperty = new EnumProperty<ColorMode>("A-Color Mode", ColorMode.FADE, this.arrayListProperty::getValue);
        this.arrayListFadeSpeedProperty = new DoubleProperty("A-Fade Speed", 1.0, () -> this.arrayListProperty.getValue() && this.arrayListColorModeProperty.getValue() != ColorMode.RAINBOW && this.arrayListColorModeProperty.getValue() != ColorMode.STATIC, 0.1, 5.0, 0.1);
        this.arrayListBackgroundProperty = new Property<Boolean>("Background", true, this.arrayListProperty::getValue);
        this.arrayListLineProperty = new Property<Boolean>("Line", true, this.arrayListProperty::getValue);
        this.arrayListOutlineProperty = new Property<Boolean>("Outline", true, this.arrayListProperty::getValue);
        this.arrayListColorProperty = new Property<Integer>("ArrayList Color", Colors.BLUE, () -> this.arrayListProperty.getValue() && this.arrayListColorModeProperty.getValue() != ColorMode.RAINBOW);
        this.secondaryArrayListColorProperty = new Property<Integer>("Second A-Color", Colors.BLUE, () -> this.arrayListProperty.getValue() && this.arrayListColorModeProperty.getValue() == ColorMode.BLEND);
        this.notificationsProperty = new Property<Boolean>("Notifications", true);
        this.bpsProperty = new Property<Boolean>("BPS", true);
        this.coordsProperty = new Property<Boolean>("Coords", false);
        this.fpsProperty = new Property<Boolean>("FPS", true);
        this.potionsProperty = new Property<Boolean>("Potions", true);
        EntityPlayerSP player;
        double xDist;
        double zDist;
        final Iterator<Module> iterator;
        Module module;
        this.onUpdatePositionEvent = (event -> {
            if (event.isPre()) {
                if (this.bpsProperty.getValue()) {
                    player = Wrapper.getPlayer();
                    xDist = player.posX - player.lastTickPosX;
                    zDist = player.posZ - player.lastTickPosZ;
                    this.lastDist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
                }
                Hud.moduleCache.iterator();
                while (iterator.hasNext()) {
                    module = iterator.next();
                    Hud.displayLabelCache.put(module, getDisplayLabel(module));
                }
                Hud.moduleCache.sort(this.sortingModeProperty.getValue().getSorter());
            }
            return;
        });
        final boolean topArrayList;
        final int offset;
        final int color;
        final LockedResolution lockedResolution;
        final int screenX;
        final int screenY;
        int notificationYOffset;
        final FontRenderer fontRenderer;
        int potionY;
        String coords;
        String fps;
        final Iterator<PotionEffect> iterator2;
        PotionEffect effect;
        Potion potion;
        String effectName;
        float potionNameHeight;
        final boolean watermark;
        final boolean arraylist;
        long currentMillis;
        long ms;
        int watermarkColor;
        int wColor = 0;
        String watermarkText;
        long ms2;
        int arrayListColor;
        int sArrayListColor;
        int y;
        boolean cFont;
        boolean background;
        boolean line;
        boolean outline;
        float previousModuleWidth;
        int moduleCacheSize;
        int lastVisibleModuleIndex;
        int firstVisibleModuleIndex;
        int visibleModuleIndex;
        int i;
        Module module2;
        Translate translate;
        String name;
        float moduleWidth;
        boolean visible;
        double translateX;
        double translateY;
        float aOffset;
        int aColor = 0;
        double top;
        double outlineTop;
        double outlineBottom;
        Module nextModule;
        int indexOffset;
        String nextModuleName;
        float nextModuleWidth;
        this.onRender2DEvent = (e -> {
            topArrayList = (this.arrayListPositionProperty.getValue() == ArrayListPosition.TOP);
            offset = (topArrayList ? 11 : -11);
            color = 1343032589;
            lockedResolution = e.getResolution();
            screenX = lockedResolution.getWidth();
            screenY = lockedResolution.getHeight();
            notificationYOffset = 2;
            fontRenderer = this.getFontRenderer();
            potionY = (topArrayList ? (screenY - offset) : 1);
            if (this.coordsProperty.getValue()) {
                coords = String.format("X: §7%.0f §FY: §7%.1f §FZ: §7%.0f", Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                fontRenderer.drawStringWithShadow(coords, screenX - 2 - fontRenderer.getWidth(coords), (float)potionY, -1);
                if (topArrayList) {
                    notificationYOffset += 11;
                }
                potionY -= offset;
            }
            if (this.fpsProperty.getValue()) {
                fps = "FPS: " + Minecraft.getDebugFPS();
                fontRenderer.drawStringWithShadow(fps, screenX - 2 - fontRenderer.getWidth(fps), (float)potionY, -1);
                if (topArrayList) {
                    notificationYOffset += 11;
                }
                potionY -= offset;
            }
            if (this.potionsProperty.getValue()) {
                Wrapper.getPlayer().getActivePotionEffects().iterator();
                while (iterator2.hasNext()) {
                    effect = iterator2.next();
                    potion = Potion.potionTypes[effect.getPotionID()];
                    effectName = String.valueOf(I18n.format(potion.getName(), new Object[0])) + " " + (effect.getAmplifier() + 1) + " §7" + Potion.getDurationString(effect);
                    fontRenderer.drawStringWithShadow(effectName, screenX - 2 - fontRenderer.getWidth(effectName), (float)potionY, potion.getLiquidColor());
                    potionNameHeight = fontRenderer.getHeight(effectName);
                    if (!topArrayList) {
                        potionNameHeight = -potionNameHeight;
                    }
                    else {
                        notificationYOffset += (int)potionNameHeight;
                    }
                    potionY -= (int)potionNameHeight;
                }
            }
            watermark = this.watermarkProperty.getValue();
            arraylist = this.arrayListProperty.getValue();
            if (this.bpsProperty.getValue()) {
                fontRenderer.drawStringWithShadow(String.format("%.2f blocks/s", this.lastDist * 20.0 * Wrapper.getTimer().timerSpeed), 2.0f, (float)(screenY - ((Wrapper.getCurrentScreen() instanceof GuiChat) ? 24 : 11)), -1);
            }
            currentMillis = -1L;
            if (watermark) {
                ms = (long)(this.watermarkFadeSpeedProperty.getValue().floatValue() * 1000.0f);
                currentMillis = System.currentTimeMillis();
                watermarkColor = this.watermarkColorProperty.getValue();
                switch (this.watermarkColorModeProperty.getValue()) {
                    case FADE: {
                        wColor = RenderingUtils.fadeBetween(watermarkColor, RenderingUtils.darker(watermarkColor, 0.49f), currentMillis % ms / (ms / 2.0f));
                        break;
                    }
                    case BLEND: {
                        wColor = RenderingUtils.fadeBetween(watermarkColor, this.secondWatermarkColorProperty.getValue(), currentMillis % ms / (ms / 2.0f));
                        break;
                    }
                    case RAINBOW: {
                        wColor = RenderingUtils.getRainbow(currentMillis, 2000, 0);
                        break;
                    }
                    default: {
                        wColor = watermarkColor;
                        break;
                    }
                }
                watermarkText = this.watermarkTextProperty.getValue();
                fontRenderer.drawStringWithShadow(watermarkText, 2.0f, 2.0f, wColor);
            }
            if (arraylist) {
                ms2 = (long)(this.arrayListFadeSpeedProperty.getValue().floatValue() * 1000.0f);
                if (currentMillis == -1L) {
                    currentMillis = System.currentTimeMillis();
                }
                arrayListColor = this.arrayListColorProperty.getValue();
                sArrayListColor = this.secondaryArrayListColorProperty.getValue();
                if (Hud.moduleCache == null) {
                    this.updateModulePositions(RenderingUtils.getScaledResolution());
                }
                y = (topArrayList ? 2 : (screenY - 9));
                cFont = this.cFontProperty.getValue();
                background = this.arrayListBackgroundProperty.getValue();
                line = this.arrayListLineProperty.getValue();
                outline = this.arrayListOutlineProperty.getValue();
                previousModuleWidth = -1.0f;
                moduleCacheSize = Hud.moduleCache.size();
                for (lastVisibleModuleIndex = moduleCacheSize - 1; lastVisibleModuleIndex > 0 && !Hud.moduleCache.get(lastVisibleModuleIndex).isVisible(); --lastVisibleModuleIndex) {}
                firstVisibleModuleIndex = -1;
                visibleModuleIndex = 0;
                for (i = 0; i < moduleCacheSize; ++i) {
                    module2 = Hud.moduleCache.get(i);
                    translate = module2.getTranslate();
                    name = Hud.displayLabelCache.get(module2);
                    moduleWidth = fontRenderer.getWidth(name);
                    visible = module2.isVisible();
                    if (visible) {
                        if (firstVisibleModuleIndex == -1) {
                            firstVisibleModuleIndex = i;
                        }
                        translate.animate(screenX - moduleWidth - (line ? 2 : 1), y);
                        if (!topArrayList) {
                            notificationYOffset += 11;
                        }
                        y += offset;
                    }
                    else {
                        translate.animate(screenX, y);
                    }
                    translateX = translate.getX();
                    translateY = translate.getY();
                    if (visible || translateX < screenX) {
                        aOffset = (currentMillis + visibleModuleIndex * 100L) % ms2 / (ms2 / 2.0f);
                        switch (this.arrayListColorModeProperty.getValue()) {
                            case FADE: {
                                aColor = RenderingUtils.fadeBetween(arrayListColor, RenderingUtils.darker(arrayListColor, 0.49f), aOffset);
                                break;
                            }
                            case BLEND: {
                                aColor = RenderingUtils.fadeBetween(arrayListColor, sArrayListColor, aOffset);
                                break;
                            }
                            case RAINBOW: {
                                aColor = RenderingUtils.getRainbow(currentMillis, 2000, visibleModuleIndex);
                                break;
                            }
                            default: {
                                aColor = arrayListColor;
                                break;
                            }
                        }
                        top = translateY - 2.0;
                        if (background) {
                            Gui.drawRect(translateX - 1.0, top, screenX, translateY + 9.0, color);
                        }
                        fontRenderer.drawStringWithShadow(name, (float)translateX, (float)translateY - (float)(cFont ? 1 : 0), aColor);
                        if (outline) {
                            Gui.drawRect(translateX - 2.0, translateY - 2.0, translateX - 1.0, translateY + 9.0, aColor);
                            outlineTop = (topArrayList ? (top - 1.0) : (translateY + 9.0));
                            outlineBottom = (topArrayList ? (translateY + 9.0) : (top - 1.0));
                            if (i != firstVisibleModuleIndex && moduleWidth - previousModuleWidth > 0.0f) {
                                Gui.drawRect(translateX - 2.0, outlineTop, screenX - previousModuleWidth - 3.0f, outlineTop + 1.0, aColor);
                            }
                            if (i != lastVisibleModuleIndex) {
                                nextModule = null;
                                indexOffset = 1;
                                while (i + indexOffset <= lastVisibleModuleIndex) {
                                    nextModule = Hud.moduleCache.get(i + indexOffset);
                                    if (nextModule.isVisible()) {
                                        break;
                                    }
                                    else {
                                        nextModule = null;
                                        ++indexOffset;
                                    }
                                }
                                if (nextModule != null) {
                                    nextModuleName = Hud.displayLabelCache.get(nextModule);
                                    nextModuleWidth = fontRenderer.getWidth(nextModuleName);
                                    if (moduleWidth - nextModuleWidth > 0.5) {
                                        Gui.drawRect(translateX - 2.0, outlineBottom, screenX - nextModuleWidth - 3.0f, outlineBottom + 1.0, aColor);
                                    }
                                }
                            }
                            else {
                                Gui.drawRect(translateX - 2.0, outlineBottom, screenX, outlineBottom + 1.0, aColor);
                            }
                        }
                        if (line) {
                            Gui.drawRect(screenX - 1, translateY - 2.0, screenX, translateY + 9.0, aColor);
                        }
                        ++visibleModuleIndex;
                        previousModuleWidth = moduleWidth;
                    }
                }
            }
            if (this.notificationsProperty.getValue()) {
                RadiumClient.getInstance().getNotificationManager().render(null, lockedResolution, true, notificationYOffset);
            }
            return;
        });
        this.toggle();
        final Object o;
        int length;
        int j = 0;
        final SortingMode[] array;
        SortingMode mode;
        this.cFontProperty.addValueChangeListener((oldValue, value) -> {
            SortingMode.values();
            for (length = o.length; j < length; ++j) {
                mode = array[j];
                mode.getSorter().setFontRenderer(this.getFontRenderer());
            }
            return;
        });
        this.watermarkTextProperty.addValueChangeListener((oldValue, value) -> {
            if (value.contains("&") || value.contains("<3")) {
                this.watermarkTextProperty.setValue(StringUtils.replaceUserSymbols(value.trim()));
            }
        });
    }
    
    private static String getDisplayLabel(final Module m) {
        final String label = m.getLabel();
        final Supplier<String> suffix = m.getSuffix();
        final String updatedSuffix = m.getUpdatedSuffix();
        if (suffix != null || updatedSuffix != null) {
            return String.valueOf(label) + " §7" + ((updatedSuffix != null) ? updatedSuffix : suffix.get());
        }
        return label;
    }
    
    private void updateModulePositions(final ScaledResolution scaledResolution) {
        if (Hud.moduleCache == null) {
            Hud.moduleCache = new ArrayList<Module>(RadiumClient.getInstance().getModuleManager().getModules());
        }
        int y = 1;
        for (final Module module : Hud.moduleCache) {
            if (module.isEnabled()) {
                module.getTranslate().setX(scaledResolution.getScaledWidth() - Wrapper.getFontRenderer().getWidth(getDisplayLabel(module)) - 2.0f);
            }
            else {
                module.getTranslate().setX((float)scaledResolution.getScaledWidth());
            }
            module.getTranslate().setY((float)y);
            if (module.isEnabled()) {
                y += ((this.arrayListPositionProperty.getValue() == ArrayListPosition.TOP) ? 11 : -11);
            }
        }
    }
    
    private FontRenderer getFontRenderer() {
        return ((boolean)this.cFontProperty.getValue()) ? Wrapper.getFontRenderer() : Wrapper.getMinecraftFontRenderer();
    }
    
    private enum ArrayListPosition
    {
        TOP("TOP", 0), 
        BOTTOM("BOTTOM", 1);
        
        private ArrayListPosition(final String name, final int ordinal) {
        }
    }
    
    private enum ColorMode
    {
        FADE("FADE", 0), 
        BLEND("BLEND", 1), 
        RAINBOW("RAINBOW", 2), 
        STATIC("STATIC", 3);
        
        private ColorMode(final String name, final int ordinal) {
        }
    }
    
    private abstract static class ModuleComparator implements Comparator<Module>
    {
        protected FontRenderer fontRenderer;
        
        @Override
        public abstract int compare(final Module p0, final Module p1);
        
        public FontRenderer getFontRenderer() {
            return this.fontRenderer;
        }
        
        public void setFontRenderer(final FontRenderer fontRenderer) {
            this.fontRenderer = fontRenderer;
        }
    }
    
    private enum SortingMode
    {
        LENGTH("LENGTH", 0, (ModuleComparator)new LengthComparator(null)), 
        ALPHABETICAL("ALPHABETICAL", 1, (ModuleComparator)new AlphabeticalComparator(null));
        
        private final ModuleComparator sorter;
        
        private SortingMode(final String name, final int ordinal, final ModuleComparator sorter) {
            this.sorter = sorter;
        }
        
        public ModuleComparator getSorter() {
            return this.sorter;
        }
    }
    
    private static class LengthComparator extends ModuleComparator
    {
        private LengthComparator() {
            super(null);
        }
        
        @Override
        public int compare(final Module o1, final Module o2) {
            return Float.compare(this.fontRenderer.getWidth(Hud.displayLabelCache.get(o2)), this.fontRenderer.getWidth(Hud.displayLabelCache.get(o1)));
        }
    }
    
    private static class AlphabeticalComparator extends ModuleComparator
    {
        private AlphabeticalComparator() {
            super(null);
        }
        
        @Override
        public int compare(final Module o1, final Module o2) {
            final String n = Hud.displayLabelCache.get(o1);
            final String n2 = Hud.displayLabelCache.get(o2);
            final char char0 = n.charAt(0);
            final char char2 = n2.charAt(0);
            if (char0 == char2) {
                return n.charAt(1) - n2.charAt(1);
            }
            return char0 - char2;
        }
    }
}
