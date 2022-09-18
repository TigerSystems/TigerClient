package de.MarkusTieger.common.compatiblity;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public interface ClientCompatiblityExecutor {

	String getModId();

	void drawBackground(Screen screen, PoseStack stack);

	void setServerListIconSheet(ResourceLocation location);

	void resetServerListIconSheet();

	boolean checkTooltip(int width, int height, int relativeMouseX, int relativeMouseY);

	void enableRecovery();

	boolean onCustomPayload(ClientboundCustomQueryPacket p_104545_, Connection connection); // net.minecraftforge.fmllegacy.network.NetworkHooks.onCustomPayload(p_104545_,
																							// this.connection)

	void onClientLoginSuccess(Connection connection); // net.minecraftforge.fmllegacy.network.NetworkHooks.handleClientLoginSuccess(this.connection);
}
