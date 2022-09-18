package de.MarkusTieger.tigerclient.services.impl;

import java.util.Timer;
import java.util.TimerTask;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.modules.IModuleRegistry;
import de.MarkusTieger.common.services.IService;
import de.MarkusTieger.common.services.IServiceRunnable;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.ITickable;

public class ModuleTimer implements IServiceRunnable {

	private Timer timer = null;

	@Override
	public void start(IService service) {
		timer = new Timer();

		IModuleRegistry module = Client.getInstance().getModuleRegistry();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				try {
					for (ITickable<?> tick : module.getTickable().toArray()) {
						try {
							tick.onTick();
						} catch (Throwable ex) {
							Client.getInstance().getLogger().warn(LoggingCategory.MODULES,
									"Tickable \"" + tick.getClass().getName()
											+ "\" throwed an Exception on Tick-Event. Will be ignored.",
									ex);
						}
					}
				} catch (Exception e) {
					Client.getInstance().getLogger().error(LoggingCategory.SERVICES, "Exception on Tick-Timer", e);
				}

			}
		}, 1000L / 20L, 1000L / 20L);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					for (IHighspeedTick<?> tick : module.getHighTickable().toArray()) {
						try {
							tick.onHighTick();
						} catch (Throwable ex) {
							Client.getInstance().getLogger().warn(LoggingCategory.MODULES,
									"High-Tickable \"" + tick.getClass().getName()
											+ "\" throwed an Exception on H-Tick-Event. Will be ignored.",
									ex);
						}
					}
				} catch (Exception e) {
					Client.getInstance().getLogger().error(LoggingCategory.SERVICES, "Exception on High-Tick-Timer", e);
				}
			}
		}, 2L, 2L);
	}

	@Override
	public void stop(IService service) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
