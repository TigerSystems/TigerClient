package de.MarkusTieger.common.auth;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IAuthicatedUser {

	boolean isBanned();

	String getReason();
}
