package de.MarkusTieger.tigerclient.lua.converter.impl;

import java.nio.charset.StandardCharsets;

import org.luaj.vm2.LuaValue;

import de.MarkusTieger.common.lua.ILuaConverter;
import de.MarkusTieger.tigerclient.events.impl.client.PostStartEvent;
import de.MarkusTieger.tigerclient.events.impl.message.ServerMessageEvent;
import de.MarkusTieger.tigerclient.events.impl.resource.ResourcesLoadedEvent;
import de.MarkusTieger.tigerclient.events.impl.screen.PostScreenInitEvent;
import de.MarkusTieger.tigerclient.events.impl.screen.PreScreenInitEvent;
import de.MarkusTieger.tigerclient.events.impl.screen.ScreenChangeEvent;
import de.MarkusTieger.tigerclient.lua.converter.LuaConverterRegistry;

public class LuaEventConverter implements ILuaConverter {

	@Override
	public boolean canConvert(Object obj) {
		if((obj instanceof PostStartEvent) || (obj instanceof ServerMessageEvent) || (obj instanceof ResourcesLoadedEvent)
				|| (obj instanceof PreScreenInitEvent)) return true;
		if(obj instanceof PostScreenInitEvent) return true;
		if(obj instanceof ScreenChangeEvent) return true;
		return false;
	}

	@Override
	public LuaValue convert(Object obj) throws IllegalArgumentException {
		LuaValue result = LuaValue.tableOf();
		result.set("full-name", obj.getClass().getName());
		result.set("simple-name", obj.getClass().getSimpleName());

		if(obj instanceof PostStartEvent pse) {
			return result;
		}

		if(obj instanceof ServerMessageEvent sme) {
			result.set("channel", LuaConverterRegistry.INSTANCE.map(sme.getChannel()));
			result.set("data", new String(sme.getData(), StandardCharsets.UTF_8));
			return result;
		}

		if(obj instanceof ResourcesLoadedEvent rle) {
			return result;
		}

		if((obj instanceof PreScreenInitEvent psie)) {
			result.set("screen", LuaConverterRegistry.INSTANCE.map(psie.getScreen()));
			return result;
		}
		if((obj instanceof PostScreenInitEvent psie)) {
			result.set("screen", LuaConverterRegistry.INSTANCE.map(psie.getScreen()));
			return result;
		}

		if(obj instanceof ScreenChangeEvent sce) {
			result.set("screen", LuaConverterRegistry.INSTANCE.map(sce.getScreen()));
			return result;
		}

		throw new IllegalArgumentException("Illegal Argument: " + obj);
	}

}
