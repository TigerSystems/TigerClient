package de.MarkusTieger.common.lua;

import org.luaj.vm2.LuaValue;

public interface ILuaConverter {

	public boolean canConvert(Object obj);

	public LuaValue convert(Object obj) throws IllegalArgumentException;

}
