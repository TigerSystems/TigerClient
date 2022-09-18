package de.MarkusTieger.tigerclient.help;

import java.util.function.Consumer;

import net.minecraft.client.gui.components.Button;

public class ScreenHelper {

	public static Button.OnPress convert(final Consumer<Button> consumer) {
		return (Button btn) -> {
			consumer.accept(btn);
		};
	}

}
