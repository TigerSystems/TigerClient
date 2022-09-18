package de.MarkusTieger.tac.connect.listener;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.tac.gui.screen.TACDisconnectScreen;
import net.minecraft.client.ClientTelemetryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;

public class TACClientPacketListener extends ClientPacketListener {

	private static final Component GENERIC_DISCONNECT_MESSAGE = new TranslatableComponent("disconnect.lost");

	private final Minecraft minecraft;
	private final Screen callbackScreen;

	public TACClientPacketListener(Minecraft p_194193_, Screen p_194194_, Connection p_194195_, GameProfile p_194196_,
			ClientTelemetryManager p_194197_) {
		super(p_194193_, p_194194_, p_194195_, p_194196_, p_194197_);
		this.minecraft = p_194193_;
		this.callbackScreen = p_194194_;
	}

	@Override
	public void onDisconnect(Component p_104954_) {
		this.minecraft.clearLevel();
		if (this.callbackScreen != null) {
			if (this.callbackScreen instanceof RealmsScreen) {
				this.minecraft.setScreen(
						new DisconnectedRealmsScreen(this.callbackScreen, GENERIC_DISCONNECT_MESSAGE, p_104954_));
			} else {
				this.minecraft.setScreen(new TACDisconnectScreen(this.callbackScreen, GENERIC_DISCONNECT_MESSAGE,
						p_104954_, TACDisconnectScreen.DisconnectReason.GENERIC));
			}
		} else {
			this.minecraft.setScreen(new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()),
					GENERIC_DISCONNECT_MESSAGE, p_104954_));
		}

	}
}
