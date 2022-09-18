package de.MarkusTieger.tigerclient.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.MarkusTieger.common.registry.IRegistry;

public class StaticRegistry<T> implements IRegistry<StaticRegistry<T>, T> {

	private final List<T> array = new ArrayList<>();

	@Override
	public List<T> toArray() {
		return array;
	}

	public <E> StaticRegistry<E> mapForGenericTypes(Function<T, E> mapper) {
		StaticRegistry<E> registry = new StaticRegistry<>();
		array.stream().map(mapper).forEachOrdered(registry::register);
		return registry;
	}

	public <E> StaticRegistry<E> mapForGenericTypes(StaticRegistry<E> registry, Function<T, E> mapper) {
		registry.clear();
		array.stream().map(mapper).forEachOrdered(registry::register);
		return registry;
	}
}
