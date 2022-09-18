package de.MarkusTieger.tigerclient.gui.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class BannedScreen extends DisconnectedScreen {

	private final Component reason;

	public BannedScreen(String reason) {
		super(null, new TranslatableComponent("screen.banned.title"),
				new TranslatableComponent("screen.banned.message", ChatFormatting.GOLD + reason));
		this.reason = new TranslatableComponent("screen.banned.message", ChatFormatting.GOLD + reason);
	}

	@Override
	protected void init() {
		super.init();

		MultiLineLabel message = MultiLineLabel.create(this.font, this.reason, this.width - 50);
		int var10001 = message.getLineCount();
		int textHeight = var10001 * 9;
		int var10003 = this.width / 2 - 100;
		int var10004 = this.height / 2 + textHeight / 2;

		this.clearWidgets();

		this.addRenderableWidget(new Button(var10003, Math.min(var10004 + 9, this.height - 30), 200, 20,
				new TranslatableComponent("menu.quit"), (p_96002_) -> {
					this.minecraft.close();
				}));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
