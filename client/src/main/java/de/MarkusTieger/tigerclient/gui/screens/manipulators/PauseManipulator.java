package de.MarkusTieger.tigerclient.gui.screens.manipulators;

import java.util.List;
import java.util.function.Consumer;

import de.MarkusTieger.common.screen.IScreenManipulator;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.gui.screens.TigerClientOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class PauseManipulator implements IScreenManipulator {

	@Override
	public boolean canManipulate(Screen screen) {
		return screen instanceof PauseScreen;
	}

	@Override
	public void preManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		if (!(screen instanceof PauseScreen ps))
			return;

	}

	@Override
	public void postManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		ImageButton btn = new ImageButton(screen.width / 2 - 124, screen.height / 4 + 96 + -16, 20, 20, 20 /* 0 */, 106,
				20, TigerGuiUtils.WIDGETS, 256, 256, (p_96788_) -> {

					Minecraft.getInstance().setScreen(new TigerClientOptions(screen));

				}, new TranslatableComponent("narration.button.tigerclient"));

		add.accept(btn);
	}
}
