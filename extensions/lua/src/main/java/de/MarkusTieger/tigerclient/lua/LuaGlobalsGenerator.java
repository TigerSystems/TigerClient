package de.MarkusTieger.tigerclient.lua;

import java.util.function.BooleanSupplier;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JsePlatform;

import de.MarkusTieger.common.plugins.PluginPermissionLevel;

public class LuaGlobalsGenerator {

	public static final Globals generate(PluginPermissionLevel level) {
		if(level == PluginPermissionLevel.STANDARD) return JsePlatform.standardGlobals();
		if(level == PluginPermissionLevel.DEBUG) return JsePlatform.debugGlobals();
		if(level != PluginPermissionLevel.SANDBOXED && level != PluginPermissionLevel.SANDBOXED_WITHOUT_INTERNAL_SCRIPTING) throw new IllegalArgumentException("Invalid Permission Level: " + level);

		Globals user_globals = new BooleanSuppliedGlobals();
		user_globals.load(new JseBaseLib());
		user_globals.load(new PackageLib());
		user_globals.load(new Bit32Lib());
		user_globals.load(new TableLib());
		user_globals.load(new StringLib());
		user_globals.load(new JseMathLib());

		// user_globals.load(new JseIoLib());
		// user_globals.load(new JseOsLib());

		if(level == PluginPermissionLevel.SANDBOXED) {
			LoadState.install(user_globals);
			LuaC.install(user_globals);
		}

		return user_globals;
	}

	public static Globals generateCompilationGlobals() {
		Globals server_globals = new Globals();
		server_globals.load(new JseBaseLib());
		server_globals.load(new PackageLib());
		server_globals.load(new StringLib());


		server_globals.load(new JseMathLib());
		LoadState.install(server_globals);
		LuaC.install(server_globals);
		return server_globals;
	}

	public static class BooleanSuppliedGlobals extends Globals {

		private BooleanSupplier supplier = () -> false;

		public BooleanSupplier getSupplier() {
			return supplier;
		}

		public void setSupplier(BooleanSupplier supplier) {
			this.supplier = supplier;
		}

	}


}
