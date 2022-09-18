package de.MarkusTieger.tac.bridge.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class RequestBuilder {

	private static final Gson GSON = new GsonBuilder().create();

	private RequestBuilder() {
	}

	public static RequestBuilder create() {
		return new RequestBuilder();
	}

	private RequestObject data = new RequestObject();

	public RequestBuilder setRequest(String request) {
		data.request = request;
		return this;
	}

	public RequestBuilder addArgument(String name, Object arg) {
		data.args.put(name, arg);
		return this;
	}

	public RequestBuilder importJson(JsonElement element) {
		data = GSON.fromJson(element, RequestObject.class);
		return this;
	}

	public JsonElement exportJson() {
		return GSON.toJsonTree(data);
	}

	public RequestObject build() {
		return data;
	}
}
