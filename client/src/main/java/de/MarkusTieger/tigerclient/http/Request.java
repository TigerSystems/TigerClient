package de.MarkusTieger.tigerclient.http;

import java.net.Socket;
import java.util.Properties;

import de.MarkusTieger.common.http.IRequest;

public class Request implements IRequest {

	private final Path path;
	private final Socket client;
	private String agent;
	private final Method method;
	private final Properties prop;

	public Request(Socket client, Path path, Method method, Properties prop) {
		this.client = client;
		this.path = path;
		this.prop = prop;
		this.method = method;
	}

	@Override
	public String getAccept() {
		return prop.getProperty("Accept");
	}

	@Override
	public String getAcceptEncoding() {
		return prop.getProperty("Accept-Encoding");
	}

	@Override
	public String getAcceptLanguage() {
		return prop.getProperty("Accept-Language");
	}

	@Override
	public String getAgent() {
		return agent;
	}

	@Override
	public String getCacheControl() {
		return prop.getProperty("Cache-Control");
	}

	@Override
	public Socket getClient() {
		return client;
	}

	@Override
	public String getConnection() {
		return prop.getProperty("Connection");
	}

	@Override
	public String getHost() {
		return prop.getProperty("Host");
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Path getPath() {
		return path;
	}

	@Override
	public Properties getProperties() {
		return prop;
	}

	@Override
	public String getUpgradeInsecureRequests() {
		return prop.getProperty("Upgrade-Insecure-Requests");
	}

	@Override
	public String getUserAgent() {
		return prop.getProperty("User-Agent");
	}

}
