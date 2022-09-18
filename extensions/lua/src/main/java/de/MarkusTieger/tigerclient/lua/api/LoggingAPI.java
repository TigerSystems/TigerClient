package de.MarkusTieger.tigerclient.lua.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;

public class LoggingAPI extends TwoArgFunction {

	public LoggingAPI() {}

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {



		LuaValue library = LuaValue.tableOf();

		library.set("info", new info());
		library.set("warn", new warn());
		library.set("error", new error());
		library.set("debug", new debug());

		env.set("logger", library);

		return library;
	}


	public static class info extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(
					Client.getInstance().getLogger().info(
							LoggingCategory.PLUGINS,
							arg.checkjstring()
							)
					);
		}

	}

	public static class warn extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(
					Client.getInstance().getLogger().warn(
							LoggingCategory.PLUGINS,
							arg.checkjstring()
							)
					);
		}

	}


	public static class error extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(
					Client.getInstance().getLogger().error(
							LoggingCategory.PLUGINS,
							arg.checkjstring()
							)
					);
		}

	}

	public static class debug extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			return LuaValue.valueOf(
					Client.getInstance().getLogger().debug(
							LoggingCategory.PLUGINS,
							arg.checkjstring()
							)
					);
		}

	}


}
