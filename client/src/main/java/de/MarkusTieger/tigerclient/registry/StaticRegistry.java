package de.MarkusTieger.tigerclient.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import de.MarkusTieger.common.registry.IRegistry;

public class StaticRegistry<T> implements IRegistry<StaticRegistry<T>, T>, Iterable<T> {

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

	@Override
	public Spliterator<T> spliterator() {
		return array.spliterator();
	}

	@Override
	public Iterator<T> iterator() {
		return array.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		array.forEach(action);
	}
	
	public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
