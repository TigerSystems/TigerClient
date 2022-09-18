package de.MarkusTieger.tigerclient.modules.impl;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IPacketEditor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.resources.ResourceLocation;

public class FourthPerspective implements IModule<Throwable>, IConfigable<Throwable>, IPacketEditor<Throwable> {

	public static boolean PERSPECTIVE = false;

	@Override
	public Component getInfo() {
		return isEnabled() ? new TranslatableComponent("modules.enabled")
				: new TranslatableComponent("modules.disabled");
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public String getSearchName() {
		return getId();
	}

	@Override
	public void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws Throwable {
		renderDefaultOverlay(stack, x, y, width, height, hover);
	}

	@Override
	public boolean canConfigure() {
		return false;
	}

	@Override
	public void configure(Screen parent) {
	}

	@Override
	public void reset() {
		PERSPECTIVE = false;
	}

	@Override
	public void bindIcon() {
	}

	@Override
	public Component getDescription() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".description");
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".display");
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/4th-perspective/icon.png");
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnabled(boolean bool) {

	}

	@Override
	public String getId() {
		return "4th-perspective";
	}

	@Override
	public boolean accept(Packet<?> packet) {
		return packet instanceof ClientboundPlayerPositionPacket;
	}

	@Override
	public PacketSides getSides() {
		return PacketSides.SEND;
	}

	private float xRot = Float.MIN_VALUE, yRot = Float.MIN_VALUE;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Packet<?>> T edit(T packet) {

		if (!(packet instanceof ClientboundPlayerPositionPacket cbppp))
			return packet;

		if (xRot == Float.MIN_VALUE)
			xRot = cbppp.getXRot();
		if (yRot == Float.MIN_VALUE)
			yRot = cbppp.getYRot();

		if (PERSPECTIVE) {

			return (T) new ClientboundPlayerPositionPacket(cbppp.getX(), cbppp.getY(), cbppp.getZ(), xRot, yRot,
					cbppp.getRelativeArguments(), cbppp.getId(), cbppp.requestDismountVehicle());

		} else {
			xRot = cbppp.getXRot();
			yRot = cbppp.getYRot();
		}

		return packet;
	}

	@Override
	public boolean isDevOnly() {
		return true;
	}

}
