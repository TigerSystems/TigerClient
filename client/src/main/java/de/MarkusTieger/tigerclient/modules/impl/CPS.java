package de.MarkusTieger.tigerclient.modules.impl;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IMouseable;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.tigerclient.gui.screens.BasicDraggableModuleConfigurationScreen;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class CPS extends GuiComponent implements IModule<Throwable>, IDraggable<Throwable>, ITickable<Throwable>,
		IMouseable<Throwable>, IConfigable<Throwable> {

	private int lcps = 0, lcpsTemp = 0;
	private int rcps = 0, rcpsTemp = 0;
	private int timer = 0;
	private boolean enabled = false;

	private ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
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
				.setScreen(new BasicDraggableModuleConfigurationScreen(this, parent, false, this::updateShadow));
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
		return font.lineHeight;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/cps/icon.png");
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	public String getValue() {
		return lcps + " | " + rcps;
	}

	@Override
	public int getWidth() {
		return font.width("CPS: 100 | 100");
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
	public String getId() {
		return CPS.class.getSimpleName();
	}

	@Override
	public ScreenPosition load() {
		return pos;
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
	public boolean onLeftClick(int action) {
		if (GLFW.GLFW_PRESS == action)
			lcpsTemp++;
		return false;
	}

	@Override
	public boolean onMiddleClick(int action) {
		// DO Nothing
		return false;
	}

	@Override
	public boolean onRightClick(int action) {
		if (GLFW.GLFW_PRESS == action)
			rcpsTemp++;
		return false;
	}

	@Override
	public void onTick() {
		timer++;
		if (timer == 20) {
			timer = 0;
			lcps = lcpsTemp;
			lcpsTemp = 0;
			rcps = rcpsTemp;
			rcpsTemp = 0;
		}
	}

	@Override
	public void render(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		font.draw(stack, "CPS: " + lcps + " | " + rcps, (int) pos.getAbsouluteX(), (int) pos.getAbsouluteY(),
				Client.getInstance().getModuleRegistry().getColor());
	}

	@Override
	public void renderDummy(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		drawString(stack, font, "CPS: " + 30 + " | " + 30, (int) pos.getAbsouluteX(), (int) pos.getAbsouluteY(),
				Client.getInstance().getModuleRegistry().getColor());

	}

	@Override
	public void reset() {
		pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
	}

	@Override
	public void save(ScreenPosition screenPosition) {
		pos = screenPosition;
	}

}
