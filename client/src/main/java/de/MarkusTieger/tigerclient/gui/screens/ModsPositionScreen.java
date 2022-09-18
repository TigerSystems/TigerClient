package de.MarkusTieger.tigerclient.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ModsPositionScreen extends Screen {

	private static final ResourceLocation EXAMPLE_BG = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/mods/example_background.png");

	protected ModsPositionScreen(Screen parent) {
		super(new TranslatableComponent("screen.mods.position.title"));
	}

	private boolean inBounds(IDraggable<?> drag, ScreenPosition pos, double mouseX, double mouseY) throws Throwable {
		return mouseX >= pos.getAbsouluteX() && mouseY >= pos.getAbsouluteY()
				&& mouseX <= (pos.getAbsouluteX() + drag.getWidth())
				&& mouseY <= (pos.getAbsouluteY() + drag.getHeight());
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {

		for (IDraggable<?> drag : Client.getInstance().getModuleRegistry().getDraggable().toArray()) {
			try {
				ScreenPosition pos = drag.load();
				if (inBounds(drag, pos, mouseX, mouseY)) {
					pos.setAbsolute(pos.getAbsouluteX() + dragX, pos.getAbsouluteY() + dragY);
					drag.save(pos);
					break;
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
						"Can't check bounds for Draggable: " + drag.getId(), ex);
			}

		}

		return true;
	}

	@Override
	public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {

		if (this.minecraft.level != null) {
			// this.fillGradient(PoseStack, 0, 0, this.width, this.height, -1072689136,
			// -804253680);
			Client.getInstance().getCompatiblityExecutor().drawBackground(this, PoseStack);
		} else {
			renderExampleBackground(PoseStack);
		}

		super.render(PoseStack, mouseX, mouseY, partialTicks);

		for (IDraggable<?> drag : Client.getInstance().getModuleRegistry().getDraggable().toArray()) {
			try {
				ScreenPosition pos = drag.load();

				drag.renderDummy(PoseStack, pos);

				if (inBounds(drag, pos, mouseX, mouseY)) {

					this.hLine(PoseStack, (int) pos.getAbsouluteX() - 2,
							(int) pos.getAbsouluteX() + drag.getWidth() + 2, (int) pos.getAbsouluteY() - 2, 0xFFFF0000);

					this.vLine(PoseStack, (int) pos.getAbsouluteX() - 2, (int) pos.getAbsouluteY() - 2,
							(int) pos.getAbsouluteY() + drag.getHeight() + 2, 0xFFFF0000);

					this.vLine(PoseStack, (int) pos.getAbsouluteX() + drag.getWidth() + 2,
							(int) pos.getAbsouluteY() - 2, (int) pos.getAbsouluteY() + drag.getHeight() + 2,
							0xFFFF0000);

					this.hLine(PoseStack, (int) pos.getAbsouluteX() - 2,
							(int) pos.getAbsouluteX() + drag.getWidth() + 2,
							(int) pos.getAbsouluteY() + drag.getHeight() + 2, 0xFFFF0000);
				} else {
					this.hLine(PoseStack, (int) pos.getAbsouluteX() - 2,
							(int) pos.getAbsouluteX() + drag.getWidth() + 2, (int) pos.getAbsouluteY() - 2, 0xFFfce803);

					this.vLine(PoseStack, (int) pos.getAbsouluteX() - 2, (int) pos.getAbsouluteY() - 2,
							(int) pos.getAbsouluteY() + drag.getHeight() + 2, 0xFFfce803);

					this.vLine(PoseStack, (int) pos.getAbsouluteX() + drag.getWidth() + 2,
							(int) pos.getAbsouluteY() - 2, (int) pos.getAbsouluteY() + drag.getHeight() + 2,
							0xFFfce803);

					this.hLine(PoseStack, (int) pos.getAbsouluteX() - 2,
							(int) pos.getAbsouluteX() + drag.getWidth() + 2,
							(int) pos.getAbsouluteY() + drag.getHeight() + 2, 0xFFfce803);
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
						"Can't Render Draggable (Dummy): " + drag.getId(), ex);
			}
		}

	}

	private void renderExampleBackground(PoseStack matrix) {
		RenderSystem.setShaderTexture(0, EXAMPLE_BG);
		blit(matrix, 0, 0, 0.0F, 0.0F, width, height, width, height);
	}

}
