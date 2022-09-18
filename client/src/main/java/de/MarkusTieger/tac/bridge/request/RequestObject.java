package de.MarkusTieger.tac.bridge.request;

import com.google.gson.internal.LinkedTreeMap;

public class RequestObject {

	String request = null;
	LinkedTreeMap<String, Object> args = new LinkedTreeMap<>();

	public String getRequest() {
		return request;
	}

	public LinkedTreeMap<String, Object> getArgs() {
		return args;
	}
}
