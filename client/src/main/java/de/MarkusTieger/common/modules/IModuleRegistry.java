package de.MarkusTieger.common.modules;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.registry.IRegistry;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.IKeyable;
import de.MarkusTieger.common.utils.IMouseable;
import de.MarkusTieger.common.utils.ITickable;

@NoObfuscation
public interface IModuleRegistry extends IRegistry<IModuleRegistry, IModule<?>> {

	void update();

	IRegistry<?, ITickable<?>> getTickable();

	IRegistry<?, IHighspeedTick<?>> getHighTickable();

	IRegistry<?, IDraggable<?>> getDraggable();

	IRegistry<?, IKeyable<?>> getKeyable();

	IRegistry<?, IMouseable<?>> getMouseable();

	IRegistry<?, IConfigable<?>> getConfigable();

	// Use GLFW.GLFW_PRESS & RELEASE & REPEAT
	void onKey(int keyCode, int action);

	void onIngameOverlay(PoseStack matrix);

	boolean onMouse(int button, int action, boolean cancelled);

	int getColor();
}
