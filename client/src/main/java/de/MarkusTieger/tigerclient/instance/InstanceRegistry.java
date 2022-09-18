package de.MarkusTieger.tigerclient.instance;

import java.util.HashMap;
import java.util.Map;

import de.MarkusTieger.common.instance.IInstanceRegistry;

public class InstanceRegistry implements IInstanceRegistry {

	private final HashMap<Class<?>, Object> instance = new HashMap<>();

	@Override
	public Map<Class<?>, Object> toMap() {
		return instance;
	}
}
