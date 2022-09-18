package de.MarkusTieger.common.utils;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.api.gui.list.IListObject;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import net.minecraft.client.gui.screens.Screen;

@NoObfuscation
public interface IConfigable<T extends Throwable> extends IListObject, IModule<T> {

	boolean canConfigure() throws T;

	void configure(Screen parent) throws T;

	void reset() throws T;

	default void renderDefaultOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) throws T {
		float textureX = isEnabled() ? 32F : 0F;
		float textureY = hover ? 32F : 0F;

		TigerGuiUtils.renderTexture(stack, x, y, textureX, textureY, width, height, 256, 256);
	}

	void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws T;

	default void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) {
		try {
			renderOverlay0(stack, x, y, width, height, hover);
		} catch (Throwable ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Configable \"" + this.getClass().getName()
					+ "\" throwed an Exception on Render-Overlay-Event. Will be ignored.", ex);
		}
	}

	default boolean allowConfigable() {
		return true;
	}

}
