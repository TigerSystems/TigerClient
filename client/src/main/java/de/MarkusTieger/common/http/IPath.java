package de.MarkusTieger.common.http;

import java.util.Properties;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IPath {

	String getPath();

	Properties getProperties();

}
