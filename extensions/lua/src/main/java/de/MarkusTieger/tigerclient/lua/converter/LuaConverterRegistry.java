package de.MarkusTieger.tigerclient.lua.converter;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaValue;

import de.MarkusTieger.common.lua.ILuaConverter;
import de.MarkusTieger.common.lua.ILuaConverterRegistry;

public class LuaConverterRegistry implements ILuaConverterRegistry {

	public static final LuaConverterRegistry INSTANCE = new LuaConverterRegistry();

	private final List<ILuaConverter> array = new ArrayList<>();

	public LuaValue map(Object obj) {
		for(ILuaConverter converter : toArray()) {
			if(!converter.canConvert(obj)) continue;
			return converter.convert(obj);
		}
		return null;
	}

	@Override
	public List<ILuaConverter> toArray() {
		return array;
	}

}
