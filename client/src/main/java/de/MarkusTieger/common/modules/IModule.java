package de.MarkusTieger.common.modules;

import de.MarkusTieger.annotations.NoObfuscation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public interface IModule<T extends Throwable> {

	void bindIcon() throws T;

	Component getDescription();

	Component getDisplayName();

	ResourceLocation getIcon();

	boolean isEnabled() throws T;

	void setEnabled(boolean bool) throws T;

	String getId();

	default boolean isDevOnly() {
		return false;
	}

}
