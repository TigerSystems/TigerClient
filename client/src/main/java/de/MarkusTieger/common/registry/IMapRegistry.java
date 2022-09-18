package de.MarkusTieger.common.registry;

import java.util.Map;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IMapRegistry<E extends IMapRegistry<E, U, T>, U, T> {

	@SuppressWarnings("unchecked")
	default E register(U key, T value) {
		toMap().put(key, value);
		return (E) this;
	}

	@SuppressWarnings("unchecked")
	default E unregister(U key) {
		toMap().remove(key);
		return (E) this;
	}

	default T load(U key) {
		return toMap().get(key);
	}

	default int size() {
		return toMap().size();
	}

	default void clear() {
		toMap().clear();
	}

	Map<U, T> toMap();

}
