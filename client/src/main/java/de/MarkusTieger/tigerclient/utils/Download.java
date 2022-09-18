package de.MarkusTieger.tigerclient.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {

	public static byte[] readData(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		int len;
		byte[] buffer = new byte[1024];
		InputStream in = con.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		return out.toByteArray();
	}

}
