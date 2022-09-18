package de.MarkusTieger.tigerclient.auth;

import de.MarkusTieger.common.auth.IAuthicatedUser;
import de.MarkusTieger.common.auth.IAuthicationService;

public class DummyAuthicationService implements IAuthicationService {

	@Override
	public IAuthicatedUser getUser() {
		return null;
	}

	@Override
	public void updateUserInfo() {

	}
}
