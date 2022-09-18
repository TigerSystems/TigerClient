package de.MarkusTieger.tigerclient.lua.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaObjectData;

public class ConfigAPI extends TwoArgFunction {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	private final File f;
	
	private JsonObject data = new JsonObject();

	public ConfigAPI(File f) {
		this.f = f;
	}
	
	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {

		LuaValue library = LuaValue.tableOf();

		library.set("exists", new exists());
		library.set("loadConfig", new loadConfig());
		library.set("saveConfig", new saveConfig());
		library.set("data", new LuaObjectData(() -> data));

		env.set("config", library);

		return library;
	}
	
	
	public class loadConfig extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			
			if(f.length() > 134217728L) throw new LuaError("Config file is too big.");
			
			try (FileInputStream in = new FileInputStream(f)) {
				data = GSON.fromJson(new String(in.readAllBytes()), JsonObject.class);
			} catch(IOException ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Plugin-Config can't loaded", ex);
				throw new LuaError(ex);
			}
			return LuaValue.NIL;
		}
		
		
	}

	public class exists extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			return LuaValue.valueOf(f.exists());
		}

	}

	public class saveConfig extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			
			try (FileOutputStream out = new FileOutputStream(f)) {
				out.write(GSON.toJson(data).getBytes(StandardCharsets.UTF_8));
				out.flush();
			} catch (IOException ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Failed to save Plugin-Config.", ex);
				throw new LuaError(ex);
			}
			
			return LuaValue.NIL;
		}



	}

}
