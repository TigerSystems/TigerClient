package de.MarkusTieger.tigerclient.natives;

import java.io.File;
import java.util.Locale;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.natives.INativesManager;

public class NativesManager implements INativesManager {

	private File natives;

	@Override
	public File getDestination(String name) {
		return new File(natives, name);
	}

	@Override
	public void load() {
		natives = new File(Client.getInstance().getDataDirectory(), "natives");
		if (!natives.exists()) {
			natives.mkdir();
		}
		String path = System.getProperty("java.library.path");

		boolean ends = path.toLowerCase(Locale.ROOT).endsWith(File.pathSeparator.toLowerCase(Locale.ROOT));

		if (ends) {
			path += natives.getAbsolutePath() + File.pathSeparator;
		} else {
			path += File.pathSeparator + natives.getAbsolutePath();
		}

		System.setProperty("java.library.path", path);
	}

}
