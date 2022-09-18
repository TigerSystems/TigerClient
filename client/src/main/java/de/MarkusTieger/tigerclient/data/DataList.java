package de.MarkusTieger.tigerclient.data;

import java.util.ArrayList;

public class DataList {

	private final ArrayList<Data<?>> datas = new ArrayList<>();
	private final ArrayList<BiData<?, ?>> datas2 = new ArrayList<>();
	private final ArrayList<DataList> datas3 = new ArrayList<>();

	public void addData(Data<?> data) {
		datas.add(data);
		datas2.add(null);
		datas3.add(null);
	}

	public void addBiData(BiData<?, ?> data) {
		datas.add(null);
		datas2.add(data);
		datas3.add(null);
	}

	public void addDataList(DataList data) {
		datas.add(null);
		datas2.add(null);
		datas3.add(data);
	}

	public void remove(int index) {
		datas.remove(index);
		datas2.remove(index);
		datas3.remove(index);
	}

	public void remove(Object data) {
		remove(getIndex(data));
	}

	public int getList(Object data) {
		if (data == null)
			return -1;
		if (datas.contains(data))
			return 0;
		if (datas2.contains(data))
			return 1;
		if (datas3.contains(data))
			return 2;
		return -1;
	}

	public int getIndex(Object data) {
		return getIndex(data, getList(data));
	}

	public Object get(int index) {
		Object data = null;
		data = datas.get(index);
		if (data != null)
			return data;
		data = datas2.get(index);
		if (data != null)
			return data;
		data = datas3.get(index);
		return data;
	}

	public int getIndex(Object data, int i) {
		if (i == 0) {
			int pos = 0;
			for (Data<?> d : datas) {
				if (d == null)
					continue;
				if (d.equals(data)) {
					return pos;
				}
				pos++;
			}
		}
		if (i == 2) {
			int pos = 0;
			for (BiData<?, ?> d : datas2) {
				if (d == null)
					continue;
				if (d.equals(data)) {
					return pos;
				}
				pos++;
			}
		}
		if (i == 2) {
			int pos = 0;
			for (DataList d : datas3) {
				if (d == null)
					continue;
				if (d.equals(data)) {
					return pos;
				}
				pos++;
			}
		}
		return -1;
	}

}
