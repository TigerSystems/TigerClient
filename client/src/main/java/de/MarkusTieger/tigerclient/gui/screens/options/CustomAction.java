package de.MarkusTieger.tigerclient.gui.screens.options;

import java.util.function.Consumer;

import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.gui.screens.options.Option;
import de.MarkusTieger.tigerclient.help.ScreenHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class CustomAction extends Option<CustomAction> {

	private final Consumer<Button> c;
	private final String translation;

	public CustomAction(String translation, Consumer<Button> consumer) {
		c = consumer;
		this.translation = translation;
	}

	@Override
	public Button createWidget(IConfiguration configuration, int x, int y, int width) {
		Button btn = new Button(x, y, width, 20, new TranslatableComponent(translation), ScreenHelper.convert(c));
		btn.active = enabled;
		return btn;
	}

}
