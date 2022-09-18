package de.MarkusTieger.common.utils;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IMouseable<T extends Throwable> {

	boolean onLeftClick(int action) throws T;

	boolean onMiddleClick(int action) throws T;

	boolean onRightClick(int action) throws T;

	default boolean allowMouseable() {
		return true;
	}

}
