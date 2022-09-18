package de.MarkusTieger.tigerclient.lua.converter.impl;

import org.luaj.vm2.LuaValue;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.lua.ILuaConverter;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaScreenLocationData;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaStack;
import de.MarkusTieger.tigerclient.lua.api.ModuleAPI.LuaDraggable;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;

public class LuaDataConverter implements ILuaConverter {

	@Override
	public boolean canConvert(Object obj) {
		if((obj instanceof ScreenPosition) || (obj instanceof PoseStack) || (obj instanceof LuaDraggable)) return true;

		return false;
	}

	@Override
	public LuaValue convert(Object obj) throws IllegalArgumentException {
		if(obj instanceof ScreenPosition sp) return new LuaScreenLocationData(sp);
		if(obj instanceof PoseStack ps) return new LuaStack(ps);
		if(obj instanceof LuaDraggable ld) return ld.asTable();

		throw new IllegalArgumentException("Illegal Argument: " + obj);
	}

}
