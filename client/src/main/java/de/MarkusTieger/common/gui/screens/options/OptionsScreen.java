package de.MarkusTieger.common.gui.screens.options;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@NoObfuscation
public abstract class OptionsScreen extends Screen {

	private final ArrayList<Option<?>> op;
	private final Screen parent;

	public OptionsScreen(Component titleIn, Screen parent) {
		super(titleIn);
		op = new ArrayList<>();
		this.parent = parent;
	}

	public <T extends Option<T>> T addOption(T o) {
		op.add(o);
		return o;
	}

	@Override
	protected void init() {

		int i = 0;

		for (Option<?> option : op) {
			int j = this.width / 2 - 155 + i % 2 * 160;
			int k = this.height / 6 - 12 + 24 * (i >> 1);
			addRenderableWidget(option
					.createWidget(Client.getInstance().getInstanceRegistry().load(IConfiguration.class), j, k, 150));
			i++;
		}

		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20,
				CommonComponents.GUI_DONE, (p_213056_1_) -> {
					this.minecraft.setScreen(this.parent);
				}));

	}

	@Override
	public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(PoseStack);
		super.render(PoseStack, mouseX, mouseY, partialTicks);
	}

}
