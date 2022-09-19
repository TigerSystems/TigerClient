package de.MarkusTieger.tigerclient.loader.plmgr;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import de.MarkusTieger.tigerclient.loader.ClientClassLoader;
import de.MarkusTieger.tigerclient.loader.ClientLoader;

public class PluginManagerInjector {

	public static void injectPluginManager(ClientClassLoader loader) {
		if(loader == null) throw new RuntimeException("Plugin Manager can't initialized.");
		
		try {
			Enumeration<URL> urls = loader.getResources("plmgr.json");
			
			List<PluginManagerInfo> plmgrs = toValidPluginManagers(urls);
			
			if(plmgrs.size() == 0) {
				System.out.println("No Valid Plugin Manager to load...");
			} else if(plmgrs.size() == 1) {
				System.out.println("Loading Plugin Manager \"" + plmgrs.get(0).id + "\" ...");
				initializePluginManager(loader, plmgrs.get(0));
			} else {
				System.out.println("Checking Multi-Loader compatiblity...");
				if (plmgrs.stream().allMatch(PluginManagerInfo::multi)) {
					System.out.println("Loading Multi-Loader...");
					
					System.err.println("Not implemented Yet."); // TODO: Multi-Loader
					
				} else System.out.println("Not all Loaders are compatible :(");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initializePluginManager(ClientClassLoader loader, PluginManagerInfo pluginManagerInfo) {
		
		try {
			Class<?> clazz = Class.forName(pluginManagerInfo.initializer, true, loader);
			
			Method m = clazz.getDeclaredMethod("__init__");
			m.invoke(null);
			System.out.println("Plugin Manager registred.");
			
		} catch (Throwable ex) {
			System.out.println("Plugin Manager can't loaded...");
		}
		
	}

	private static List<PluginManagerInfo> toValidPluginManagers(Enumeration<URL> urls) {
		List<PluginManagerInfo> managers = new ArrayList<>();
		
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			
			if(url == null) continue;
			
			try (InputStream in = url.openStream()) {
				byte[] data = in.readAllBytes();
				
				PluginManagerInfo info = ClientLoader.GSON.fromJson(new String(data, StandardCharsets.UTF_8), PluginManagerInfo.class);
				if(info.validate()) managers.add(info);
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		List<String> cache = new ArrayList<>();
		
		for(PluginManagerInfo info : managers) {
			if(cache.contains(info.id)) throw new RuntimeException("Duplicate Extension: " + info.id);
			cache.add(info.id);
		}
		
		return managers;
	}
	
}
