package de.MarkusTieger.tac.gui.widgets.connect.renderer;

import java.awt.Point;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.gui.widgets.connect.renderrer.IConnectRenderer;
import de.MarkusTieger.common.point.IPointRegistry;
import de.MarkusTieger.tac.gui.widgets.connect.TACConnectWidget;

public class ProgressRenderer implements IConnectRenderer {

	private float progress = 0F;
	private final IPointRegistry registry;
	private final TACConnectWidget widget;

	public ProgressRenderer(TACConnectWidget widget, IPointRegistry registry) {
		this.widget = widget;
		this.registry = registry;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public float getProgress() {
		return progress;
	}

	@Override
	public void render(PoseStack stack, double multiplicator, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int xImg, int yImg) {

		Point size = registry.load("size");

		widget.fill(stack, xImg, yImg, funcX, funcY, 0, size.y - 2, size.x, size.y, 0xFF4a4a4a);

		widget.fill(stack, xImg, yImg, funcX, funcY, 0, size.y - 2, (int) (progress * size.x), size.y, 0xFF4a4a4a);
	}
}
