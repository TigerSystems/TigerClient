package de.MarkusTieger.tigerclient.plugins.lua;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.plugins.IPluginInfo;
import de.MarkusTieger.common.plugins.IPluginManager;
import de.MarkusTieger.common.plugins.PluginPermissionLevel;
import de.MarkusTieger.tigerclient.lua.LuaGlobalsGenerator;
import de.MarkusTieger.tigerclient.lua.LuaGlobalsGenerator.BooleanSuppliedGlobals;

public class LuaPluginManager implements IPluginManager {

	private final List<LuaPluginInfo> loaded = new ArrayList<>();
	private final List<LuaPluginInfo> enabled = new ArrayList<>();

	private static final Globals COMPILE_GLOBALS = LuaGlobalsGenerator.generateCompilationGlobals();

	@Override
	public void enablePlugin(IPluginInfo object) throws IOException {
		if(!(object instanceof LuaPluginInfo lpi)) throw new IOException("Invalid Plugin.");

		if(enabled.contains(lpi)) throw new IOException("Plugin \"" + lpi.getId() + "\" is already enabled.");

		Client.getInstance().getLogger().info(LoggingCategory.PLUGINS, "Enabling Plugin \"" + lpi.id() + "\" ...");

		Globals globals = lpi.globals();

		try {
			globals.get("onEnable").call();
		} catch(LuaError ex) {
			throw new IOException("Plugin \"" + lpi.getId() + "\" can't enabled.", ex);
		}

		enabled.add(lpi);

		Client.getInstance().getLogger().info(LoggingCategory.PLUGINS, "Plugin enabled.");
	}

	@Override
	public void disablePlugin(IPluginInfo object) {
		if(!(object instanceof LuaPluginInfo lpi) || !enabled.contains(lpi)) return;

		try {
			disablePlugin0(lpi);
		} catch(IOException ex) {
			Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Plugin can't disabled: " + lpi.id(), ex);
			disablePluginForce(lpi);
		}
	}

	private void disablePluginForce(LuaPluginInfo lpi) {
		enabled.remove(lpi);
	}

	private void disablePlugin0(LuaPluginInfo lpi) throws IOException {

		Client.getInstance().getLogger().info(LoggingCategory.PLUGINS, "Disabling Plugin \"" + lpi.id() + "\" ...");

		Globals globals = lpi.globals();

		try {
			globals.get("onDisable").call();
		} catch(LuaError ex) {
			throw new IOException("Plugin \"" + lpi.getId() + "\" can't disabled.", ex);
		}

		enabled.remove(lpi);

		Client.getInstance().getLogger().info(LoggingCategory.PLUGINS, "Plugin disabled.");

	}

	@Override
	public List<IPluginInfo> getLoadedPlugins() {
		return loaded.stream().map((lua) -> (IPluginInfo)lua).toList();
	}

	@Override
	public void configure(IPluginInfo configable) {

	}

	@Override
	public void reset(IPluginInfo configable) {

	}

	@Override
	public void loadPlugins(File dataDirectory) throws IOException {
		loaded.clear();

		File plugins = new File(dataDirectory, "plugins");
		File lua_specific = new File(plugins, "lua");
		if(!plugins.exists()) plugins.mkdirs();
		if(!lua_specific.exists()) lua_specific.mkdirs();

		List<File> searchDirs = new ArrayList<>();
		searchDirs.add(plugins);
		searchDirs.add(lua_specific);

		final byte[] twoByteBuffer = new byte[2];
		final byte[] fivtyByteBuffer = new byte[50];

		FileFilter filter = (f) -> {
			if(!f.canRead()) return false;
			if(f.getName().toLowerCase().endsWith(".lua")) return true;

			try (FileInputStream fis = new FileInputStream(f)) {
				int len = fis.read(twoByteBuffer);
				if(len != 2) throw new IOException("Length is not 2: " + len);
				String str = new String(twoByteBuffer, StandardCharsets.UTF_8);
				if(str.equalsIgnoreCase("#!")) {
					len = fis.read(fivtyByteBuffer);
					str += new String(fivtyByteBuffer, 0, len, StandardCharsets.UTF_8);
					if(str.toLowerCase().startsWith("#!/usr/bin/env lua") || str.toLowerCase().startsWith("#!/bin/lua") || str.toLowerCase().startsWith("#!/usr/bin/lua")) return true;
				}
			} catch(IOException ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "Failed to read shebang for file: " + f.getAbsolutePath(), ex);
			}
			return false;
		};

		List<File> toLoad = new ArrayList<>();

		searchDirs.stream().map((f) -> f.listFiles(filter)).map(Arrays::stream).map(Stream::toList).forEachOrdered(toLoad::addAll);

		for(File f : toLoad) {
			try {
				loadPlugin(null, f);
			} catch (LuaError ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS, "The Plugin can't loaded: " + f.getAbsolutePath(), ex);
			}
		}

	}

	private final AtomicInteger UNIQUE_ID = new AtomicInteger();

	private void loadPlugin(PluginPermissionLevel level, File f) throws IOException {

		if (f.length() > 33554432L) throw new IOException("File is bigger than 32 MB: " + f.length());

		int uuid = UNIQUE_ID.getAndIncrement();

		Globals globals = LuaGlobalsGenerator.generate(level);

		globals.set("unique_id", uuid);
		globals.set("client_name", Client.getInstance().getName());
		globals.set("client_cleanversion", Client.getInstance().getCleanVersion());
		globals.set("client_version", Client.getInstance().getVersion());
		globals.set("client_versiontype", Client.getInstance().getVersionType());

		globals.set("file_name", f.getName());
		globals.set("file_path", f.getAbsolutePath());

		ZeroArgFunction dummy = new ZeroArgFunction() {

			@Override
			public LuaValue call() {
				return LuaValue.NIL;
			}
		};

		globals.set("onLoad", dummy);
		globals.set("onEnable", dummy);
		globals.set("onDisable", dummy);

		try (FileInputStream fis = new FileInputStream(f)) {
			byte[] data = fis.readAllBytes();
			String script = new String(data, StandardCharsets.UTF_8);
			LuaValue chunk = COMPILE_GLOBALS.load(script, "main", globals);

			chunk.call();

		}

		String id = globals.get("id").checkjstring();
		String name = globals.get("plugin_name").checkjstring();
		String description = globals.get("plugin_description").checkjstring();
		String version = globals.get("plugin_version").checkjstring();
		String author = globals.get("plugin_author").checkjstring();
		String icon = globals.get("plugin_icon").checkjstring();
		boolean ready = globals.get("ready").checkboolean();

		if(id          == null) throw new IOException("Plugin does not have an id: " + f.getAbsolutePath());

		if(name        == null) name        =  "Unknown Plugin # " + uuid;
		if(description == null) description = "<Insert Default Plugin Description here>";
		if(version     == null) version     =  "Not Specified";
		if(author      == null) author      =  "Not Specified";
		if(icon        == null) icon        =  IPluginManager.DEFAULT_ICON;

		LuaPluginInfo info = new LuaPluginInfo(
				id,
				name,
				description,
				version,
				author,
				icon,
				ready,
				globals
				);

		if(globals instanceof BooleanSuppliedGlobals bsglobals) {
			bsglobals.setSupplier(() -> enabled.contains(info));
		}

	}

	@Override
	public List<IPluginInfo> getEnabledPlugins() {
		// TODO Auto-generated method stub
		return null;
	}

}
