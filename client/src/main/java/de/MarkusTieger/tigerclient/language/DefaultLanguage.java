package de.MarkusTieger.tigerclient.language;

import de.MarkusTieger.common.language.ILanguage;

public class DefaultLanguage implements ILanguage {

	private final String id, region, name;

	public DefaultLanguage(String id, String region, String name) {
		this.id = id;
		this.region = region;
		this.name = name;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getRegion() {
		return region;
	}
}
