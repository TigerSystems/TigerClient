package de.MarkusTieger.tigerclient.gui.screens.options;

import java.util.function.Consumer;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.gui.screens.options.Option;
import de.MarkusTieger.tigerclient.help.ScreenHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class BooleanOption extends Option<BooleanOption> {

	private final String option;
	private final String translation;
	private boolean value;

	public BooleanOption(String option, String translation, boolean def) {
		this.option = option;
		this.translation = translation;
		Client.getInstance().getInstanceRegistry().load(IConfiguration.class).settingDefault(option, def);
		Object obj = Client.getInstance().getInstanceRegistry().load(IConfiguration.class).get(option);
		if (obj instanceof Boolean) {
			value = (boolean) obj;
		} else {
			value = def;
		}
	}

	@Override
	public Button createWidget(IConfiguration configuration, int x, int y, int width) {
		Button btn = new Button(x, y, width, 20,
				new TranslatableComponent(translation + "." + (value ? "true" : "false")),
				ScreenHelper.convert(new Consumer<Button>() {

					@Override
					public void accept(Button t) {
						value = !value;
						t.setMessage(new TranslatableComponent(translation + "." + value));
						configuration.set(option, value);
					}
				}));
		btn.active = enabled;
		return btn;
	}

}
