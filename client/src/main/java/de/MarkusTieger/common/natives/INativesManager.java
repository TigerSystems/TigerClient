package de.MarkusTieger.common.natives;

import java.io.File;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface INativesManager {
	File getDestination(String name);

	void load();
}
