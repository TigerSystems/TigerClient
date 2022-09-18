package de.MarkusTieger.tigerclient.events.lua;

import java.util.function.BooleanSupplier;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.events.EventHandler;
import de.MarkusTieger.common.events.IEvent;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.tigerclient.lua.converter.LuaConverterRegistry;

public class LuaEventAdapter {

	private final BooleanSupplier supplier;
	private final LuaFunction func;

	public LuaEventAdapter(BooleanSupplier supplier, LuaFunction func) {
		this.func = func;
		this.supplier = supplier;
	}

	@EventHandler
	public void onEvent(IEvent event) {

		if(!supplier.getAsBoolean()) return;

		LuaValue val = LuaConverterRegistry.INSTANCE.map(event);
		if(val == null) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "The Event \"" + event.getClass().getName() + "\" can't be converted to Lua.");
			return;
		}
		func.call(val);
	}

}
