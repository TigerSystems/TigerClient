package de.MarkusTieger.common.utils;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IKeyable<T extends Throwable> {

	boolean onKey(int keyCode, int flag) throws T;

	default boolean allowKeyable() {
		return true;
	}

}
