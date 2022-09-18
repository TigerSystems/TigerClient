package de.MarkusTieger.obf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import proguard.Configuration;
import proguard.ConfigurationParser;
import proguard.ParseException;
import proguard.ProGuard;

public class Obfuscator {

	public void run(ObfuscationConfig cfg) throws IOException {
		
		
		File file = new File("temp_proguard_config.cfg");
		
		String data = cfg.build();
		
		if(!file.exists()) file.createNewFile();
		
		FileOutputStream out = new FileOutputStream(file);
		out.write(data.getBytes(StandardCharsets.UTF_8));
		out.flush();
		out.close();
		
		Configuration config = new Configuration();
		
		try (ConfigurationParser parser = new ConfigurationParser(file, System.getProperties())) {
			try {
				parser.parse(config);
			} catch (ParseException e) {
				throw new IOException(e);
			} catch (IOException e) {
				throw e;
			}
		}
		
		ProGuard proguard = new ProGuard(config);
		try {
			proguard.execute();
		} catch (Exception e) {
			throw new IOException(e);
		}
		
	}
	
}
