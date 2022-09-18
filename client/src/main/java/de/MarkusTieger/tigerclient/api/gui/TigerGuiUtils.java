package de.MarkusTieger.tigerclient.api.gui;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TigerGuiUtils {

	public static final ResourceLocation WIDGETS = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/widgets.png");
	private static final ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

	public static void renderHotbarItem(int x, int y, ItemStack stack) {
		if (!stack.isEmpty()) {
			renderer.renderGuiItem(stack, x, y);
		}
	}

	public static <T extends GuiEventListener & Widget & NarratableEntry> void addRenderableWidget(Screen screen,
			T widget) {
		screen.renderables.add(widget);
		addWidget(screen, widget);
	}

	public static <T extends GuiEventListener & NarratableEntry> void addWidget(Screen screen, T widget) {
		addEventOnly(screen, widget);
		// narratable only with reflect accessable
	}

	@SuppressWarnings("unchecked")
	public static <T extends GuiEventListener> void addEventOnly(Screen screen, T widget) {
		((List<GuiEventListener>) screen.children()).add(widget);
	}

	public static void renderTexture(PoseStack stack, int x, int y, float textureX, float textureY, int width,
			int height, int textureWidth, int textureHeight) {
		GuiComponent.blit(stack, x, y, textureX, textureY, width, height, textureWidth, textureHeight);
	}
}
