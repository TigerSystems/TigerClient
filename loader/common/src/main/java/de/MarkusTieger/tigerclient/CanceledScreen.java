package de.MarkusTieger.tigerclient;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class CanceledScreen extends Screen {

	public static final CanceledScreen INSTANCE = new CanceledScreen();

	private CanceledScreen() {
		super(new TextComponent("Canceled"));
	}

}
