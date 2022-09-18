package de.MarkusTieger.common.utils;

import java.util.List;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IMultiDrag<T extends Throwable> {

	List<IDraggable<T>> getDraggables() throws T;

}
