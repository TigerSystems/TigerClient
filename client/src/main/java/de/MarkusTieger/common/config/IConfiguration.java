package de.MarkusTieger.common.config;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IConfiguration {

	Object get(String option);

	<T> T getOrDefault(String option, T def);

	void load();

	void save();

	void set(String option, Object value);

	void settingDefault(String option, Object value);

}
