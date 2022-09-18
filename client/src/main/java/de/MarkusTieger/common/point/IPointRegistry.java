package de.MarkusTieger.common.point;

import java.awt.Point;
import java.util.Map;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IPointRegistry {

	default IPointRegistry register(String name, Point point) {
		toMap().put(name, point);
		return this;
	}

	default IPointRegistry unregister(String name) {
		toMap().remove(name);
		return this;
	}

	default Point load(String name) {
		return toMap().get(name);
	}

	default int size() {
		return toMap().size();
	}

	default void clear() {
		toMap().clear();
	}

	Map<String, Point> toMap();

}
