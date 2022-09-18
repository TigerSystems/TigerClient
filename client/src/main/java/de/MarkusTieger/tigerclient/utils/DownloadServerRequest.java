package de.MarkusTieger.tigerclient.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.utils.ServerStatus;

public class DownloadServerRequest {

	public static void writeBytesAsync(String string, FileOutputStream fos, ServerStatus serverStatus) {

		try {
			int len;
			byte[] buffer = new byte[1024];
			URL url = new URL(string);

			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestProperty("User-Agent",
					"TigerClient " + Client.getInstance().getVersionType() + " v." + Client.getInstance().getVersion());

			http.connect();

			InputStream in = http.getInputStream();
			while ((len = in.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
			serverStatus.success();
			serverStatus.close();
		} catch (Exception e) {
			serverStatus.failed(e);
			serverStatus.close();
		}

	}

}
