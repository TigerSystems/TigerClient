package de.MarkusTieger.tigerclient.gui.screens;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import de.MarkusTieger.common.Client;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class AcceptScreen extends DisconnectedScreen {

	private static final Component reason = new TranslatableComponent("screen.accept.message");

	public AcceptScreen() {
		super(null, new TranslatableComponent("screen.accept.title"), reason);
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
						20, new TranslatableComponent("button.accept.title"), (p_213033_1_) -> {
							Client.getInstance().accept();
							minecraft.setScreen(new TitleScreen(true));
						}));

		this.addRenderableWidget(
				new Button(this.width / 2 - 100, Math.min(this.height / 2 + textHeight / 2 + 9, this.height - 30) + 25,
						200, 20, new TranslatableComponent("button.read.title"), (p_213033_1_) -> {
							minecraft.setScreen(new ConfirmLinkScreen(new BooleanConsumer() {

								@Override
								public void accept(boolean t) {
									if (t)
										try {
											Util.getPlatform().openUrl(new URL(Client.getInstance().getAcceptURL()));
										} catch (MalformedURLException e) {
											try {
												Util.getPlatform()
														.openUri(new URI(Client.getInstance().getAcceptURL()));
											} catch (URISyntaxException e1) {
												Util.getPlatform().openUri(Client.getInstance().getAcceptURL());
											}
										}
									minecraft.setScreen(AcceptScreen.this);
								}

							}, Client.getInstance().getAcceptURL(), true));
						}));

		this.addRenderableWidget(
				new Button(this.width / 2 - 100, Math.min(this.height / 2 + textHeight / 2 + 9, this.height - 30) + 50,
						200, 20, new TranslatableComponent("menu.quit"), (p_213033_1_) -> {
							Minecraft.getInstance().close();
						}));
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

}
