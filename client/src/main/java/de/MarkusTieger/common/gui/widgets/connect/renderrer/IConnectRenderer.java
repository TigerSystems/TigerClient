package de.MarkusTieger.common.gui.widgets.connect.renderrer;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IConnectRenderer {

	public void render(PoseStack stack, double multiplicator, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int xImg, int yImg);

}
