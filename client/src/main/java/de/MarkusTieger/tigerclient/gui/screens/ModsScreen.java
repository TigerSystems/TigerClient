package de.MarkusTieger.tigerclient.gui.screens;

import java.util.List;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleList;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleListScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ModsScreen extends SimpleListScreen<IConfigable<?>> {

	@SuppressWarnings("unused")
	private Button toggleButton = null, configButton = null, resetButton = null, repairButton = null;

	private static final ResourceLocation OVERLAY_LOCATION = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/mods_selection.png");

	public ModsScreen(Screen screenIn) {
		super(new TranslatableComponent("screen.mods.title"), OVERLAY_LOCATION, screenIn);
	}

	@Override
	protected void init() {
		this.toggleButton = this.addSelectingButton(new Button(this.width / 2 - 154, this.height - 52, 150, 20,
				new TranslatableComponent("button.mods.true"), (p_214325_1_) -> {
					toggle();
				}));
		this.addRenderableWidget(new Button(this.width / 2 + 4, this.height - 52, 150, 20,
				new TranslatableComponent("button.mods.editPos"), (p_214326_1_) -> {
					this.minecraft.setScreen(new ModsPositionScreen(this));
				}));
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
		Client.getInstance().getModuleRegistry().update();
		super.removed();
	}

	public void reset() {
		SimpleList<IConfigable<?>>.SimpleListEntry<IConfigable<?>> entry = list.getSelected();

		if (entry != null) {
			reset(entry.getObject());
		}
	}

	public void configure() {

		SimpleList<IConfigable<?>>.SimpleListEntry<IConfigable<?>> entry = list.getSelected();

		if (entry != null) {
			configure(entry.getObject());
		}

	}

	public void configure(IConfigable<?> configable) {
		try {
			configable.configure(this);
		} catch (Throwable ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
					"Can't configure Module: " + configable.getId() + ". Will be ignored.", ex);
		}
		updateButton();
	}

	public void reset(IConfigable<?> configable) {
		try {
			configable.reset();
		} catch (Throwable ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
					"Can't reset Module: " + configable.getId() + ". Will be ignored.", ex);
		}
		updateButton();
	}

	@Override
	public void click(IConfigable<?> object) {
		try {
			object.setEnabled(!object.isEnabled());
		} catch (Throwable ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
					"Can't toggle Module: " + object.getId() + ". Will be ignored.", ex);
		}
		this.updateButton();
	}

	public void toggle() {
		SimpleList<IConfigable<?>>.SimpleListEntry<IConfigable<?>> entry = list.getSelected();

		if (entry != null) {
			click(entry.getObject());
		}
	}

	@Override
	public List<IConfigable<?>> getObjects() {
		return Client.getInstance().getModuleRegistry().getConfigable().toArray();
	}

	@Override
	public void updateButtonData(IConfigable<?> object) {
		updateButtonStatus(true);
		this.repairButton.active = false;
		try {
			this.configButton.active = object.canConfigure();
		} catch (Throwable ex) {
			this.configButton.active = false;
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
					"Can't investigating Configuring Status: " + object.getId() + ". False will be used.", ex);
		}
		try {
			this.toggleButton.setMessage(new TranslatableComponent("button.mods." + !object.isEnabled()));
		} catch (Throwable ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
					"Can't investigating Module Status: " + object.getId() + ". False will be used.", ex);
		}
	}
}
