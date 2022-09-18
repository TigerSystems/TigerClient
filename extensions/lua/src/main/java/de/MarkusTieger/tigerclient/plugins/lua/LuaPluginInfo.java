package de.MarkusTieger.tigerclient.plugins.lua;

import org.luaj.vm2.Globals;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.plugins.IPluginInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public record LuaPluginInfo(String id, String name, String description, String version, String author, String icon, boolean ready, Globals globals) implements IPluginInfo {

	@Override
	public Component getDisplayName() {
		return new TextComponent(name);
	}

	@Override
	public Component getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getNarration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceLocation getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDisabled() {
		return !ready;
	}

	@Override
	public String getSearchName() {
		return getId();
	}

	@Override
	public void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) {

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}



}
