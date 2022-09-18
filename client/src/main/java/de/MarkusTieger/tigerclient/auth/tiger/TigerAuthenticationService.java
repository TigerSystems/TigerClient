package de.MarkusTieger.tigerclient.auth.tiger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import de.MarkusTieger.common.auth.IAuthicatedUser;
import de.MarkusTieger.common.auth.IAuthicationService;

public class TigerAuthenticationService implements IAuthicationService {

	@Override
	public IAuthicatedUser getUser() {
		return null;
	}

	@Override
	public void updateUserInfo() {

	}

	public TigerSystemsAccount login(String username, String password) throws IOException {

		HttpURLConnection con = (HttpURLConnection) new URL("https://tigersystems.cf/accounts/login?redirect=/success")
				.openConnection();
		con.setInstanceFollowRedirects(false);
		con.setRequestProperty("User-Agent", "TigerClient v2 Authentication Service");

		return null;
	}

}
