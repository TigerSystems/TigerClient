package de.MarkusTieger.tigerclient.logger.handler;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;

public class TCUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Client.getInstance().getLogger().warn(LoggingCategory.MAIN, "Caught previously unhandled exception", e);
	}
}
