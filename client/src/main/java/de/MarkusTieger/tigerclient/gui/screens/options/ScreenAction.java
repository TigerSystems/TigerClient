package de.MarkusTieger.tigerclient.gui.screens.options;

import java.util.function.Consumer;

import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.gui.screens.options.Option;
import de.MarkusTieger.tigerclient.help.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class ScreenAction extends Option<ScreenAction> {

	private final Screen s;
	private final String translation;

	public ScreenAction(String translation, Screen screen) {
		s = screen;
		this.translation = translation;
	}

	@Override
	public Button createWidget(IConfiguration configuration, int x, int y, int width) {
		Button btn = new Button(x, y, width, 20, new TranslatableComponent(translation),
				ScreenHelper.convert(new Consumer<Button>() {

					@Override
					public void accept(Button t) {
						Minecraft.getInstance().setScreen(s);
					}
				}));
		btn.active = enabled;
		return btn;
	}

}
