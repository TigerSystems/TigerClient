package de.MarkusTieger.common.instance;

import java.util.Map;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IInstanceRegistry {

	default <T, R extends T> IInstanceRegistry register(Class<T> clazz, T instance) {
		toMap().put(clazz, instance);
		return this;
	}

	default IInstanceRegistry unregister(Class<?> clazz) {
		toMap().remove(clazz);
		return this;
	}

	@SuppressWarnings("unchecked")
	default <T> T load(Class<T> clazz) {
		return (T) toMap().get(clazz);
	}

	default int size() {
		return toMap().size();
	}

	default void clear() {
		toMap().clear();
	}

	Map<Class<?>, Object> toMap();

}
