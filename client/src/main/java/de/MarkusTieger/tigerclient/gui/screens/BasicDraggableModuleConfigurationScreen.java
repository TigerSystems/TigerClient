package de.MarkusTieger.tigerclient.gui.screens;

import de.MarkusTieger.common.gui.screens.options.OptionsScreen;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.tigerclient.gui.screens.options.BooleanOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class BasicDraggableModuleConfigurationScreen extends OptionsScreen {

	private final Runnable update;

	public BasicDraggableModuleConfigurationScreen(IModule<?> mod, Screen parent, Runnable update) {
		super(new TranslatableComponent("screen.configure.mods.title"), parent);

		this.update = update;

		addOption(new BooleanOption("modules#" + mod.getId() + "#shadow", "modules.config.shadow", false));
	}

	@Override
	public void removed() {
		super.removed();
		update.run();
	}

}
