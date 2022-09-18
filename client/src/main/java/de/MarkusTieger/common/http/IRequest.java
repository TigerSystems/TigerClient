package de.MarkusTieger.common.http;

import java.net.Socket;
import java.util.Properties;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IRequest {

	String getAccept();

	String getAcceptEncoding();

	String getAcceptLanguage();

	String getAgent();

	String getCacheControl();

	Socket getClient();

	String getConnection();

	String getHost();

	IMethod getMethod();

	IPath getPath();

	Properties getProperties();

	String getUpgradeInsecureRequests();

	String getUserAgent();
}
