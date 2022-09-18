package de.MarkusTieger.common.api.gui.list;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public interface IListObject {

	Component getDisplayName();

	Component getDescription();

	Component getInfo();

	Component getNarration();

	ResourceLocation getIcon();

	boolean isDisabled();

	String getSearchName();

	void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover);
}
