package de.MarkusTieger.tigerclient.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class PatchWarningScreen extends DisconnectedScreen {

	private static final Component reason = new TranslatableComponent("screen.patch.message");

	public static boolean IGNORED = false;

	public PatchWarningScreen() {
		super(null, new TranslatableComponent("screen.patch.title"), reason);
	}

	@Override
	@SuppressWarnings("unused")
	protected void init() {

		super.init();

		MultiLineLabel message = MultiLineLabel.create(this.font, reason, this.width - 50);
		int var10001 = message.getLineCount();
		int textHeight = var10001 * 9;
		int var10003 = this.width / 2 - 100;
		int var10004 = this.height / 2 + textHeight / 2;

		this.clearWidgets();

		this.addRenderableWidget(
				new Button(this.width / 2 - 100, Math.min(this.height / 2 + textHeight / 2 + 9, this.height - 30), 200,
						20, new TranslatableComponent("button.ignore.title"), (p_213033_1_) -> {
							IGNORED = true;
							minecraft.setScreen(new TitleScreen(true));
						}));

		this.addRenderableWidget(
				new Button(this.width / 2 - 100, Math.min(this.height / 2 + textHeight / 2 + 9, this.height - 30) + 25,
						200, 20, new TranslatableComponent("menu.quit"), (p_213033_1_) -> {
							Minecraft.getInstance().close();
						}));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

}
