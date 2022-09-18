package de.MarkusTieger.tigerclient.data;

public class BiData<T, E> {

	private T data1;
	private E data2;

	private boolean empty1 = true;
	private boolean empty2 = true;

	public static <F, G> BiData<F, G> empty(Class<F> clazz1, Class<G> clazz2) {
		BiData<F, G> bData = new BiData<>();
		bData.clear1();
		bData.clear2();
		return bData;
	}

	public T getData1() {
		return data1;
	}

	public void setData1(T data1) {
		this.data1 = data1;
	}

	public E getData2() {
		return data2;
	}

	public void setData2(E data2) {
		this.data2 = data2;
	}

	public void clear1() {
		empty1 = true;
		data1 = null;
	}

	public void clear2() {
		empty2 = true;
		data2 = null;
	}

	public boolean isEmpty1() {
		return empty1;
	}

	public boolean isEmpty2() {
		return empty2;
	}

}
