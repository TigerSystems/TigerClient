package de.MarkusTieger.tigerclient.services;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.services.IService;
import de.MarkusTieger.common.services.IServiceRunnable;

public class Service implements IService {

	private final String name;
	private final IServiceRunnable runnable;
	private ServiceStatus status = ServiceStatus.STOPPED;

	public Service(String name, IServiceRunnable runnable) {
		this.name = name;
		this.runnable = runnable;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void start() {
		Client.getInstance().getLogger().info(LoggingCategory.SERVICES, "Starting " + getName() + "...");
		new Thread(() -> {

			status = ServiceStatus.STARTING;

			try {
				runnable.start(this);
				status = ServiceStatus.RUNNING;
			} catch (Throwable t) {
				status = ServiceStatus.ERROR;
				Client.getInstance().getLogger().warn(LoggingCategory.SERVICES,
						"The Service " + getName() + " can't Start!", t);
			}

		}, "Service-Starter").start();
	}

	@Override
	public void stop() {
		Client.getInstance().getLogger().info(LoggingCategory.SERVICES, "Stopping " + getName() + "...");
		new Thread(() -> {

			status = ServiceStatus.STOPPING;

			try {
				runnable.stop(this);
				status = ServiceStatus.STOPPED;
			} catch (Throwable t) {
				status = ServiceStatus.ERROR;
				Client.getInstance().getLogger().warn(LoggingCategory.SERVICES,
						"The Service " + getName() + " can't Stop!", t);
			}

		}, "Service-Stopper").start();
	}

	@Override
	public void restart() {
		Client.getInstance().getLogger().info(LoggingCategory.SERVICES, "Restarting " + getName() + "...");
		new Thread(() -> {

			status = ServiceStatus.STOPPING;

			try {
				runnable.stop(this);
				status = ServiceStatus.STARTING;
				runnable.start(this);
				status = ServiceStatus.RUNNING;
			} catch (Throwable t) {
				status = ServiceStatus.ERROR;
				Client.getInstance().getLogger().warn(LoggingCategory.SERVICES,
						"The Service " + getName() + " can't Restart!", t);
			}

		}, "Service-Restarter").start();
	}

	@Override
	public ServiceStatus getStatus() {
		return status;
	}
}
