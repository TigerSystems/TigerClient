package de.MarkusTieger.tigerclient.lua;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.plugins.IPluginManager;
import de.MarkusTieger.tigerclient.lua.converter.LuaConverterRegistry;
import de.MarkusTieger.tigerclient.lua.converter.impl.LuaDataConverter;
import de.MarkusTieger.tigerclient.lua.converter.impl.LuaEventConverter;
import de.MarkusTieger.tigerclient.plugins.lua.LuaPluginManager;

public class LuaInitializer {

	public static void __init__() {
		LuaConverterRegistry.INSTANCE.register(new LuaEventConverter());

		LuaConverterRegistry.INSTANCE.register(new LuaDataConverter());

		Client.getInstance().getInstanceRegistry().register(IPluginManager.class, new LuaPluginManager());
	}

}
