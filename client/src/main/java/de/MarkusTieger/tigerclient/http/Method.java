package de.MarkusTieger.tigerclient.http;

import de.MarkusTieger.common.http.IMethod;

public class Method implements IMethod {

	private String method = "GET";
	private byte[] data;

	public Method(String method) {
		this.method = method;
	}

	public Method(String method, byte[] data) {
		this.method = method;
		this.data = data;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public String getMethodString() {
		return method;
	}

}
