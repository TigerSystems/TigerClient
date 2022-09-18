package de.MarkusTieger.tigerclient.events.impl.screen;

import java.util.List;
import java.util.function.Consumer;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.events.IEvent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

@NoObfuscation
public class PostScreenInitEvent implements IEvent {

	private final Screen screen;
	private final List<GuiEventListener> array;
	private final Consumer<GuiEventListener> add, remove;

	public PostScreenInitEvent(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		this.screen = screen;
		this.array = array;
		this.add = add;
		this.remove = remove;
	}

	public void add(GuiEventListener listener) {
		add.accept(listener);
	}

	public void remove(GuiEventListener listener) {
		remove.accept(listener);
	}

	public Screen getScreen() {
		return screen;
	}

	public List<GuiEventListener> getArray() {
		return array;
	}
}
