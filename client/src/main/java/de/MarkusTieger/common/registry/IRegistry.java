package de.MarkusTieger.common.registry;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IRegistry<E extends IRegistry<E, T>, T> {

	@SuppressWarnings("unchecked")
	default E register(T entry) {
		if (!getPredicate().test(entry))
			return (E) this;
		toArray().add(entry);
		return (E) this;
	}

	@SuppressWarnings("unchecked")
	default E unregister(T entry) {
		toArray().remove(entry);
		return (E) this;
	}

	List<T> toArray();

	default boolean isEmpty() {
		return toArray().isEmpty();
	}

	default Iterator<T> iterator() {
		return toArray().iterator();
	}

	default void forEach(Consumer<? super T> consumer) {
		toArray().forEach(consumer);
	}

	default int size() {
		return toArray().size();
	}

	default void clear() {
		toArray().clear();
	}

	default Spliterator<T> spliterator() {
		return toArray().spliterator();
	}

	default Predicate<T> getPredicate() {
		return (obj) -> true;
	}

}
