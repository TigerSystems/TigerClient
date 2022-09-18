package de.MarkusTieger.tigerclient.api.optifine;

import java.lang.reflect.Method;

public class OptiFineAPI {

	private final String prefix;
	private final Class<?> handler;

	public OptiFineAPI(String prefix, Class<?> handler) {
		this.prefix = prefix;
		this.handler = handler;
	}

	public static OptiFineAPI create() {
		String[] prefixes = new String[] { "", "notch.", "srg." };

		for (String prefix : prefixes) {
			OptiFineAPI api = create(prefix);
			if (api == null)
				continue;
			return api;
		}

		return null;
	}

	public static OptiFineAPI create(String prefix) {
		try {
			Class<?> clazz = Class.forName(prefix + "net.optifine.shaders.Shaders");

			return new OptiFineAPI(prefix, clazz);

		} catch (Throwable e) {
			return null;
		}
	}

	public Object getShaderObject() throws Throwable {
		Method m = handler.getDeclaredMethod("getShaderPack");

		m.setAccessible(true);
		Object shader = m.invoke(null);
		return shader;
	}

	public String getShaderName() throws Throwable {
		Object obj = getShaderObject();

		Class<?> clazz = Class.forName(prefix + "net.optifine.shaders.IShaderPack");

		Method m = clazz.getDeclaredMethod("getName");
		m.setAccessible(true);
		Object result = m.invoke(obj);
		return result + "";
	}

}
