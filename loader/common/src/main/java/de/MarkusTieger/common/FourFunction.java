package de.MarkusTieger.common;

@FunctionalInterface
public interface FourFunction<T, E, F, G, R> {

	R apply(T t, E e, F f, G g);

}
