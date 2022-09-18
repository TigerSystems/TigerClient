package de.MarkusTieger.common.gui.screens.options;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.config.IConfiguration;
import net.minecraft.client.gui.components.Button;

@NoObfuscation
public abstract class Option<T extends Option<T>> {

	protected boolean enabled = true;

	public abstract Button createWidget(IConfiguration configuration, int x, int y, int width);

	public boolean isEnabled() {
		return enabled;
	}

	@SuppressWarnings("unchecked")
	public T setEnabled(boolean b) {
		enabled = b;
		return (T) this;
	}

}
