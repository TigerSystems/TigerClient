package de.MarkusTieger.common.collector;

import com.google.gson.JsonObject;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IInformationCollector {

	public JsonObject collect();

}
