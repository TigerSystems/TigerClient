package de.MarkusTieger.tigerclient.services.impl;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.services.IService;
import de.MarkusTieger.common.services.IServiceRunnable;

public class Stopper implements IServiceRunnable {

	private final Runnable runnable = Client.getInstance()::stop;
	private final Thread thread = new Thread(runnable);

	@Override
	public void start(IService service) {
		Runtime.getRuntime().addShutdownHook(thread);
	}

	@Override
	public void stop(IService service) {
		Runtime.getRuntime().removeShutdownHook(thread);
	}
}
