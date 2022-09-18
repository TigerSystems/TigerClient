package de.MarkusTieger.common.font;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IFont {

	void renderString(PoseStack stack, String text, int x, int y);

	int getWidth(String text);

	int getWidth(char c);

	int getHeight(String text);

	int getHeight(char c);

}
