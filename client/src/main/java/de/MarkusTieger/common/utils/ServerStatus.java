package de.MarkusTieger.common.utils;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface ServerStatus {

	void close();

	void failed(Exception e);

	void success();

}
