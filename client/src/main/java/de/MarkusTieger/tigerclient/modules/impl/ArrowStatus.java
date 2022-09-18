package de.MarkusTieger.tigerclient.modules.impl;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.gui.screens.BasicDraggableModuleConfigurationScreen;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ArrowStatus extends GuiComponent
		implements IModule<Throwable>, IDraggable<Throwable>, ITickable<Throwable>, IConfigable<Throwable> {

	public static boolean enabled = false;

	public static int arrows = 0;
	public ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);

	@SuppressWarnings("unused")
	private final Font font = Minecraft.getInstance().font;

	@Override
	public void bindIcon() {
	}

	@Override
	public boolean canConfigure() {
		return true;
	}

	@Override
	public void configure(Screen parent) {
		Minecraft.getInstance()
				.setScreen(new BasicDraggableModuleConfigurationScreen(this, parent, this::updateShadow));
	}

	private boolean shadow = false;

	public void updateShadow() {
		IConfiguration config = Client.getInstance().getInstanceRegistry().load(IConfiguration.class);
		Object obj = config.get("modules#" + getId() + "#shadow");
		if (obj instanceof Boolean) {
			shadow = (boolean) obj;
		} else {
			shadow = false;
		}
	}

	public void renderShadow(PoseStack stack, ScreenPosition pos) {
		if (!shadow)
			return;

		fill(stack, (int) pos.getAbsouluteX() - 1, (int) pos.getAbsouluteY() - 1,
				(int) pos.getAbsouluteX() + getWidth() + 1, (int) pos.getAbsouluteY() + getHeight() + 1, 0x101010CC);
	}

	@Override
	public Component getInfo() {
		return isEnabled() ? new TranslatableComponent("modules.enabled")
				: new TranslatableComponent("modules.disabled");
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public Component getDescription() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".description");
	}

	@Override
	public String getSearchName() {
		return getId();
	}

	@Override
	public void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws Throwable {
		this.renderDefaultOverlay(stack, x, y, width, height, hover);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".display");
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/arrowstatus/icon.png");
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public String getId() {
		return ArrowStatus.class.getSimpleName();
	}

	@Override
	public int getWidth() {
		return 20;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean bool) {
		enabled = bool;
		updateShadow();
	}

	@Override
	public ScreenPosition load() {
		return pos;
	}

	@SuppressWarnings("resource")
	@Override
	public void onTick() {

		if (Minecraft.getInstance().player != null) {
			int temp = 0;
			Inventory inv = Minecraft.getInstance().player.getInventory();
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack stack = inv.getItem(i);

				if (stack.getItem() != null && stack.getItem() instanceof ArrowItem) {
					temp += stack.getMaxStackSize();
				}

			}
			arrows = temp;
		}

	}

	@Override
	public void render(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
				new ItemStack(Items.ARROW, arrows));

	}

	@Override
	public void renderDummy(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
				new ItemStack(Items.ARROW, 6));

	}

	@Override
	public void reset() {
		pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
	}

	@Override
	public void save(ScreenPosition p) {
		pos = p;
	}

}
