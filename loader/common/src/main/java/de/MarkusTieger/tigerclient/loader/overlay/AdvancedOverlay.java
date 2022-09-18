package de.MarkusTieger.tigerclient.loader.overlay;

import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.tigerclient.loader.profilefiller.EmptyProfileFiller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;

public class AdvancedOverlay extends Overlay {

    private final Overlay parent;
    private float currentProgress = 0.5F;
    private String text = "Idle";

    private boolean fontInitialized = false;
    private Font font = null;

    public AdvancedOverlay(Overlay parent){
        this.parent = parent;
    }

    @SuppressWarnings({ "unused" })
	@Override
    public void render(PoseStack stack, int p_94670_, int p_94671_, float p_94672_) {

        parent.render(stack, p_94670_, p_94671_, p_94672_);

        if(this.currentProgress == 1F) return;

        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int j = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int k1 = (int)(Minecraft.getInstance().getWindow().getGuiScaledHeight() * 0.8325D);
        double d1 = Math.min(Minecraft.getInstance().getWindow().getGuiScaledWidth() * 0.75D, Minecraft.getInstance().getWindow().getGuiScaledHeight()) * 0.25D;
        double d0 = d1 * 4.0D;
        int j1 = (int)(d0 * 0.5D);

        this.drawProgressBar(stack, i / 2 - j1, k1 + 15, i / 2 + j1, k1 + 25, 1.0F);

        drawText(stack, i, k1);

    }

    private void drawText(PoseStack stack, int width, int y) {

    	if(!fontInitialized) {

    		AdvancedFontManager fontManager = new AdvancedFontManager(Minecraft.getInstance().getTextureManager());

    		ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
    		ProfilerFiller profiler = new EmptyProfileFiller();

    		Map<ResourceLocation, List<GlyphProvider>> map = fontManager.prepare(resourceManager, profiler);
    		fontManager.apply(map, resourceManager, profiler);

    		font = fontManager.createFont();

    		fontInitialized = true;
    	}

    	drawCenteredString(stack, font, new TextComponent(text), width / 2, y - 25, 0xFFFFFFFF);
	}

	private void drawProgressBar(PoseStack p_96183_, int p_96184_, int p_96185_, int p_96186_, int p_96187_, float p_96188_) {

        int i = Mth.ceil((p_96186_ - p_96184_ - 2) * this.currentProgress);
        int j = Math.round(p_96188_ * 255.0F);
        int k = FastColor.ARGB32.color(j, 255, 255, 255);
        fill(p_96183_, p_96184_ + 2, p_96185_ + 2, p_96184_ + i, p_96187_ - 2, k);
        fill(p_96183_, p_96184_ + 1, p_96185_, p_96186_ - 1, p_96185_ + 1, k);
        fill(p_96183_, p_96184_ + 1, p_96187_, p_96186_ - 1, p_96187_ - 1, k);
        fill(p_96183_, p_96184_, p_96185_, p_96184_ + 1, p_96187_, k);
        fill(p_96183_, p_96186_, p_96185_, p_96186_ - 1, p_96187_, k);
    }

    public void setProgress(String str, float p) {
    	if(str != null) text = str;
        if(p != -1F) currentProgress = p;
    }
}
