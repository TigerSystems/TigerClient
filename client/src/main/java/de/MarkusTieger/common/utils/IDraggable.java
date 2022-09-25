package de.MarkusTieger.common.utils;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IDraggable<T extends Throwable> {

	int getHeight() throws T;

	int getWidth() throws T;

	CalculatableScreenPosition position() throws T;

	void render(PoseStack stack, FixedScreenPosition pos) throws T;

	void renderDummy(PoseStack stack, FixedScreenPosition pos) throws T;

	void position_set(CalculatableScreenPosition screenPosition) throws T;
	
	String getId();

	default boolean allowDraggable() {
		return true;
	}

}
