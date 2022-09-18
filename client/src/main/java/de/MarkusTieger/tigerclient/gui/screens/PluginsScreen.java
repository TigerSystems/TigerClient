package de.MarkusTieger.tigerclient.gui.screens;

import java.io.IOException;
import java.util.List;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.plugins.IPluginInfo;
import de.MarkusTieger.common.plugins.IPluginManager;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleList;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleListScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PluginsScreen extends SimpleListScreen<IPluginInfo> {

	@SuppressWarnings("unused")
	private Button toggleButton = null, configButton = null, resetButton = null, repairButton = null;

	private static final ResourceLocation OVERLAY_LOCATION = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/mods_selection.png");

	public PluginsScreen(Screen screenIn) {
		super(new TranslatableComponent("screen.plugins.title"), OVERLAY_LOCATION, screenIn);
	}

	@Override
	protected void init() {
		this.toggleButton = this.addSelectingButton(new Button(this.width / 2 - 154, this.height - 52, 150, 20,
				new TranslatableComponent("button.mods.true"), (p_214325_1_) -> {
					toggle();
				}));
		/*
		 * this.addRenderableWidget(new Button(this.width / 2 + 4, this.height - 52,
		 * 150, 20, new TranslatableComponent("button.mods.editPos"), (p_214326_1_) -> {
		 * this.minecraft.setScreen(new ModsPositionScreen(this)); }));
		 */
		this.configButton = this.addRenderableWidget(new Button(this.width / 2 - 154, this.height - 28, 72, 20,
				new TranslatableComponent("button.mods.configure"), (p_214323_1_) -> {
					configure();
				}));
		this.resetButton = this.addSelectingButton(new Button(this.width / 2 - 76, this.height - 28, 72, 20,
				new TranslatableComponent("button.mods.reset"), (p_214330_1_) -> {
					reset();
				}));
		this.repairButton = this.addRenderableWidget(new Button(this.width / 2 + 4, this.height - 28, 72, 20,
				new TranslatableComponent("button.mods.repair"), (p_214328_1_) -> {
					// Coming Soon
				}));
		repairButton.active = false;
		configButton.active = false;

		this.addRenderableWidget(new Button(this.width / 2 + 82, this.height - 28, 72, 20, CommonComponents.GUI_CANCEL,
				(p_214327_1_) -> {
					this.minecraft.setScreen(this.lastScreen);
				}));
		super.init();
	}

	@Override
	public void removed() {
		super.removed();
	}

	public void reset() {
		SimpleList<IPluginInfo>.SimpleListEntry<IPluginInfo> entry = list.getSelected();

		if (entry != null) {
			reset(entry.getObject());
		}
	}

	public void configure() {

		SimpleList<IPluginInfo>.SimpleListEntry<IPluginInfo> entry = list.getSelected();

		if (entry != null) {
			configure(entry.getObject());
		}

	}

	public void configure(IPluginInfo configable) {
		Client.getInstance().getInstanceRegistry().load(IPluginManager.class).configure(configable);
		updateButton();
	}

	public void reset(IPluginInfo configable) {
		Client.getInstance().getInstanceRegistry().load(IPluginManager.class).reset(configable);
		updateButton();
	}

	@Override
	public void click(IPluginInfo object) {
		IPluginManager manager = Client.getInstance().getInstanceRegistry().load(IPluginManager.class);
		if (object.isEnabled()) {
			manager.disablePlugin(object);
			updateButton();
		} else {
			ConfirmScreen screen = new ConfirmScreen((b) -> {
				if (b) {
					try {
						manager.enablePlugin(object);
					} catch (IOException e) {
						Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
								"Plugin \"" + object.getId() + "\" can't enabled!", e);
					}
					minecraft.setScreen(PluginsScreen.this);
					updateButton();
				} else {
					minecraft.setScreen(PluginsScreen.this);
				}
			}, new TranslatableComponent("confirm.plugins.title"),
					new TranslatableComponent("confirm.plugins.warning"));
			minecraft.setScreen(screen);
		}

	}

	public void toggle() {
		SimpleList<IPluginInfo>.SimpleListEntry<IPluginInfo> entry = list.getSelected();

		if (entry != null) {
			click(entry.getObject());
		}
	}

	@Override
	public List<IPluginInfo> getObjects() {
		return Client.getInstance().getInstanceRegistry().load(IPluginManager.class).getLoadedPlugins();
	}

	@Override
	public void updateButtonData(IPluginInfo object) {

	}
}
