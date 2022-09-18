package de.MarkusTieger.tigerclient.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import de.MarkusTieger.common.config.IConfiguration;

public class Configuration implements IConfiguration {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final File config;
	public LinkedTreeMap<String, Object> data = new LinkedTreeMap<>();

	public Configuration(File cfg) {
		config = cfg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object get(String option) {
		String[] args = option.contains("#") ? option.split("#") : new String[] { option };
		LinkedTreeMap<String, Object> map = data;
		for (int i = 0; i < args.length; i++) {
			if ((i + 1) == args.length) {
				return map.get(args[i]);
			} else {
				Object obj = map.get(args[i]);
				if (obj == null) {
					return null;
				} else {
					if (obj instanceof LinkedTreeMap<?, ?>) {

						map = (LinkedTreeMap<String, Object>) obj;

					} else {
						return obj;
					}
				}
			}
		}
		return data.get(option);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrDefault(String option, T def) {
		Object obj = get(option);
		if (obj == null) {
			return def;
		}
		return (T) obj;
	}

	@Override
	public void load() {
		try {
			if (config.exists()) {
				FileReader reader = new FileReader(config);
				@SuppressWarnings("unchecked")
				LinkedTreeMap<String, Object> cfg = GSON.fromJson(reader, LinkedTreeMap.class);
				this.data = cfg == null ? new LinkedTreeMap<>() : cfg;
				reader.close();
			} else {
				config.createNewFile();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			if (!config.exists()) {
				config.createNewFile();
			}
			FileWriter writer = new FileWriter(config);
			writer.write(GSON.toJson(data));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(String option, Object value) {
		// Client.getInstance().getLogger().info("Set " + option + " to " +
		// value.toString());
		String[] args = option.contains("#") ? option.split("#") : new String[] { option };
		// Client.getInstance().getLogger().info("Data: " + Arrays.toString(args));
		LinkedTreeMap<String, Object> map = data;
		for (int i = 0; i < args.length; i++) {
			// Client.getInstance().getLogger().info("Config " + i + " " + args[i]);
			if ((i + 1) == args.length) {

				if (map.containsKey(args[i])) {
					map.replace(args[i], value);
				} else {
					map.put(args[i], value);
				}
			} else {
				Object obj = map.get(args[i]);
				if (obj == null) {
					obj = new LinkedTreeMap<String, Object>();
					map.put(args[i], obj);
					map = (LinkedTreeMap<String, Object>) obj;
					// Client.getInstance().getLogger().info("Adding Map");
				} else {
					if (obj instanceof LinkedTreeMap<?, ?>) {

						map = (LinkedTreeMap<String, Object>) obj;

					} else {
						// Client.getInstance().getLogger().info("Error");
						return;
					}
				}
			}
		}
	}

	@Override
	public void settingDefault(String option, Object obj) {
		if (get(option) == null) {
			set(option, obj);
		}
	}

}
