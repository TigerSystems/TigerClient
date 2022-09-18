package de.MarkusTieger.tigerclient.services;

import java.util.ArrayList;
import java.util.List;

import de.MarkusTieger.common.services.IService;
import de.MarkusTieger.common.services.IServiceRegistry;

public class ServiceRegistry implements IServiceRegistry {

	private final ArrayList<IService> services = new ArrayList<>();

	@Override
	public List<IService> toArray() {
		return services;
	}
}
