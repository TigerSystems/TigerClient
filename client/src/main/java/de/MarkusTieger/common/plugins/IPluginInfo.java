package de.MarkusTieger.common.plugins;

import de.MarkusTieger.common.api.gui.list.IListObject;

public interface IPluginInfo extends IListObject {

	boolean isEnabled();

	String getId();

	String getVersion();

}
