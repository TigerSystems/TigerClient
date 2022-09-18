package de.MarkusTieger.common.plugins;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;

public interface IPluginManager {

	public static final String DEFAULT_ICON = "";

	void loadPlugins(File dataDirectory) throws IOException;

	default void loadPluginsSilently(File dataDirectory) {
		try {
			loadPlugins(dataDirectory);
		} catch (Throwable e) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Plugins loading failed.", e);
		}
	}

	void enablePlugin(IPluginInfo object) throws IOException;

	void disablePlugin(IPluginInfo object);

	List<IPluginInfo> getLoadedPlugins();

	void configure(IPluginInfo configable);

	void reset(IPluginInfo configable);

	List<IPluginInfo> getEnabledPlugins();

}
