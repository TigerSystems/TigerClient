package de.MarkusTieger.tigerclient.http;

import java.util.Properties;

import de.MarkusTieger.common.http.IPath;

public class Path implements IPath {

	private final String path;
	private final Properties properties;

	public Path(String path, Properties p) {
		this.path = path;
		this.properties = p;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

}
