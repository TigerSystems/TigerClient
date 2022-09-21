package de.MarkusTieger.tigerclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.MarkusTieger.tigerclient.loader.AbstractConfig;
import de.MarkusTieger.tigerclient.loader.ClientClassLoader;
import de.MarkusTieger.tigerclient.loader.ClientLoader;
import de.MarkusTieger.tigerclient.loader.ClientVerification;

public class VersionPrinter0 {

	public static final Gson GSON = new GsonBuilder().create();
	
	static {
		
		final String first = "--::tc_common=";
		final String last = "=tc_common::--";
		System.out.println(first + ClientLoader.VERSION + last);
		
	}
	
	public static void __internal__() {}
	
	public static void recursivly() {
		try {
			recursivly0();
		} catch (Throwable ex) {
			throw new RuntimeException("Recursivly failed.", ex);
		}
	}
	
	private static void recursivly0() throws Throwable {
		
		final File debugDirectory = new File("../../data/compiled/");
		
		String str = null;
		File cfgf = new File(debugDirectory, "config/loader.json");
		try (FileInputStream in = new FileInputStream(cfgf)) {
			str = new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
		
		ArrayList<URL> sources = new ArrayList<>();
		
		AbstractConfig.RootLoaderConfig cfg = GSON.fromJson(str, AbstractConfig.RootLoaderConfig.class);
		
		List<AbstractConfig.DownloadableFile> files = new ArrayList<>();
		files.addAll(cfg.all);
		files.addAll(cfg.def);
		// No Extensions or Loader Specific stuff...
		
		files.stream().map((df) -> {
			try {
				return new File(debugDirectory, df.name.toLowerCase() + "." + df.type).toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return (URL) null;
			}
		}).filter(Objects::nonNull).forEachOrdered(sources::add);
		
		// TODO: find sources in debug directory
		
		InputStream in = ClientLoader.class.getResourceAsStream("/cert");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }
        in.close();

        ClientVerification verify = new ClientVerification(new String(out.toByteArray(), StandardCharsets.UTF_8));
		
		ClientClassLoader loader = new ClientClassLoader(verify, sources.toArray(new URL[0]));
		
		Class<?> clazz = Class.forName("de.MarkusTieger.VersionPrinter", true, loader);
		
		Method m = clazz.getDeclaredMethod("recursivly");
		m.invoke(null);
	}
	
}
