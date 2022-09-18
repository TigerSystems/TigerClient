package de.MarkusTieger.common.screen;

import java.util.List;
import java.util.function.Consumer;

import de.MarkusTieger.annotations.NoObfuscation;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

@NoObfuscation
public interface IScreenManipulator {

	boolean canManipulate(Screen screen);

	void preManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove);

	void postManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove);

}
