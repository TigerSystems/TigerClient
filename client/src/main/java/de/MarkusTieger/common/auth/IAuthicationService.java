package de.MarkusTieger.common.auth;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IAuthicationService {

	IAuthicatedUser getUser();

	void updateUserInfo();
}
