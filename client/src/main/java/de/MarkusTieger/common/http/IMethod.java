package de.MarkusTieger.common.http;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IMethod {

	byte[] getData();

	String getMethodString();
}
