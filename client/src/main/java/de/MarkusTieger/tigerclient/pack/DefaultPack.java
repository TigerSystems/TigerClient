package de.MarkusTieger.tigerclient.pack;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.pack.IPack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public record DefaultPack(String id, String name, String description, ResourceLocation icon) implements IPack {

	@Override
	public Component getDisplayName() {
		return new TextComponent(name);
	}

	@Override
	public Component getDescription() {
		return new TextComponent(description);
	}

	@Override
	public Component getInfo() {
		return new TextComponent(id);
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public ResourceLocation getIcon() {
		return icon;
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public String getSearchName() {
		return name;
	}

	@Override
	public void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) {

	}

}
