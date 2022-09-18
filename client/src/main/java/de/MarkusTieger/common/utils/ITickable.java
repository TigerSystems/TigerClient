package de.MarkusTieger.common.utils;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface ITickable<T extends Throwable> {

	void onTick() throws T;

	default boolean allowTickable() {
		return true;
	}

}
