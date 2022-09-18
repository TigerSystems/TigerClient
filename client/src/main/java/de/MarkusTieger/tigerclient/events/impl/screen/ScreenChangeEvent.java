package de.MarkusTieger.tigerclient.events.impl.screen;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.events.IEvent;
import net.minecraft.client.gui.screens.Screen;

@NoObfuscation
public class ScreenChangeEvent implements IEvent {

	private Screen screen;
	private boolean cancelled;

	public ScreenChangeEvent(Screen screen, boolean cancelled) {
		this.screen = screen;
		this.cancelled = cancelled;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
