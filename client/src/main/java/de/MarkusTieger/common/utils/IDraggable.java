package de.MarkusTieger.common.utils;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;

@NoObfuscation
public interface IDraggable<T extends Throwable> {

	int getHeight() throws T;

	int getWidth() throws T;

	ScreenPosition load() throws T;

	void render(PoseStack stack, ScreenPosition pos) throws T;

	void renderDummy(PoseStack stack, ScreenPosition pos) throws T;

	void save(ScreenPosition screenPosition) throws T;

	String getId();

	default boolean allowDraggable() {
		return true;
	}

}
