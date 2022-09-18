package de.MarkusTieger.common.utils;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IHighspeedTick<T extends Throwable> {

	public void onHighTick() throws T;

	default boolean allowHightick() {
		return true;
	}

}
