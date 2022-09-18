package de.MarkusTieger.tigerclient.lua.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.lwjgl.glfw.GLFW;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.IPacketEditor.PacketSides;

public class ConstantsAPI extends TwoArgFunction {

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {

		LuaValue library = tableOf();

		library.set("packet", packet());
		library.set("glfw", glfw());

		env.set("constants", library);

		return library;
	}

	private LuaValue glfw() {

		LuaValue library = tableOf();

		for(Field f : GLFW.class.getDeclaredFields()) {
			if(f.getType() == int.class && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
				try {
					library.set(f.getName(), f.getInt(null));
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Can't Set the GLFW Variable from " + GLFW.class.getName() + " . " + f.getName(), ex);
				}
			}
			if(f.getType() == long.class && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
				try {
					library.set(f.getName(), f.getLong(null));
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Can't Set the GLFW Variable from " + GLFW.class.getName() + " . " + f.getName(), ex);
				}
			}
			if(f.getType() == float.class && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
				try {
					library.set(f.getName(), f.getFloat(null));
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Can't Set the GLFW Variable from " + GLFW.class.getName() + " . " + f.getName(), ex);
				}
			}
		}

		return library;
	}

	private LuaValue packet() {

		LuaValue library = tableOf();

		for(PacketSides side : PacketSides.values()) {
			library.set(side.name(), side.ordinal());
		}

		return library;

	}

}
