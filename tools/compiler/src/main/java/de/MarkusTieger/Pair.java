package de.MarkusTieger;

public record Pair<T, E>(T key, E value) {

	public T getKey() {
		return key;
	}
	
	public E getValue() {
		return value;
	}
	
}
