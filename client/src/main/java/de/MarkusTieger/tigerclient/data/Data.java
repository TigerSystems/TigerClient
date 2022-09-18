package de.MarkusTieger.tigerclient.data;

public class Data<T> {

	private T data;
	private boolean empty = true;

	public static <T> Data<T> empty(Class<T> o) {
		Data<T> data = new Data<>();
		data.clear();
		return data;
	}

	public T getData() {
		if (empty)
			throw new RuntimeException("The Data is empty!");
		return data;
	}

	public void setData(T data) {
		this.data = data;
		empty = false;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void clear() {
		empty = true;
		data = null;
	}

}
