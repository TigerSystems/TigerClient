package de.MarkusTieger;

import de.MarkusTieger.tigerclient.TigerClient;

public class VersionPrinter {

	static {
		final String first = "--::tc_version=";
		final String last = "=tc_version::--";
		System.out.println(first + String.format(TigerClient.version_format, TigerClient.clVersion, TigerClient.build) + last);
	}
	
	public static void __internal__() {}
	
	public static void recursivly() {}
	
}
