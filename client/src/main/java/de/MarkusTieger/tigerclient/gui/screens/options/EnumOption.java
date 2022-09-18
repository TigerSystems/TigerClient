package de.MarkusTieger.tigerclient.gui.screens.options;

import java.util.function.Consumer;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.gui.screens.options.Option;
import de.MarkusTieger.tigerclient.help.ScreenHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class EnumOption extends Option<EnumOption> {

	private final String option;
	private final String translation;
	private double value;
	private final Enum<?>[] values;

	public EnumOption(int id, String option, String translation, Enum<?>[] values, Enum<?> def) {
		this.option = option;
		this.translation = translation;
		this.values = values;
		Client.getInstance().getInstanceRegistry().load(IConfiguration.class).settingDefault(option,
				(double) def.ordinal());
		Object obj = Client.getInstance().getInstanceRegistry().load(IConfiguration.class).get(option);
		if (obj instanceof Double) {
			value = (double) obj;
		} else {
			value = def.ordinal();
		}
	}

	@Override
	public Button createWidget(IConfiguration configuration, int x, int y, int width) {
		Button btn = new Button(x, y, width, 20, new TranslatableComponent(translation, values[(int) value].name()),
				ScreenHelper.convert(new Consumer<Button>() {

					@Override
					public void accept(Button t) {
						value++;
						if (value == values.length) {
							value = 0;
						}
						configuration.set(option, value);
						t.setMessage(new TranslatableComponent(translation, values[(int) value].name()));
					}
				}));
		btn.active = enabled;
		return btn;
	}

}
