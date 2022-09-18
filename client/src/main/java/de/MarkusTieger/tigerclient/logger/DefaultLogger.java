package de.MarkusTieger.tigerclient.logger;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.MarkusTieger.common.logger.ILogger;
import de.MarkusTieger.common.logger.LoggingCategory;

public class DefaultLogger implements ILogger {

	private final Logger logger;
	@SuppressWarnings("unused")
	private final File logs;
	private static final boolean debug = System.getProperty("tigerclient.debug", "false").equalsIgnoreCase("true");

	public DefaultLogger(Logger apacheLogger, File logs) {
		this.logger = apacheLogger;
		this.logs = logs;
		logs.mkdirs();
	}

	@Override
	public String info(LoggingCategory category, String msg) {
		println(category, "Info", msg);
		return msg;
	}

	@Override
	public String debug(LoggingCategory category, String msg) {
		if (debug)
			println(category, "Debug", msg);
		return msg;
	}

	@Override
	public String warn(LoggingCategory category, String msg) {
		println(category, "Warn", msg);
		return msg;
	}

	@Override
	public Throwable warn(LoggingCategory category, Throwable th) {
		println(category, "Warn", th);
		return th;
	}

	@Override
	public String warn(LoggingCategory category, String msg, Throwable th) {
		println(category, "Error", msg);
		println(category, "Error", th);
		return msg;
	}

	@Override
	public String error(LoggingCategory category, String msg) {
		println(category, "Error", msg);
		return msg;
	}

	@Override
	public Throwable error(LoggingCategory category, Throwable th) {
		println(category, "Error", th);
		return th;
	}

	@Override
	public String error(LoggingCategory category, String msg, Throwable th) {
		println(category, "Error", msg);
		println(category, "Error", th);
		return msg;
	}

	private void println(String msg, String type) {
		logger.info("[TigerClient] [" + type + "] " + msg);
	}

	private void println(LoggingCategory category, String type, String msg) {
		println("[" + category.name().toLowerCase() + "] " + msg, type);
	}

	private void println(LoggingCategory category, String type, Throwable ex) {
		println(category, type,
				ex.getClass().getName() + ": " + ex.getLocalizedMessage() + " (" + ex.getMessage() + ")");
		for (StackTraceElement element : ex.getStackTrace()) {
			println(category, type, "  at " + element);
		}
		if (ex.getCause() != null) {
			printlnCause(category, type, ex.getCause());
		}
	}

	private void printlnCause(LoggingCategory category, String type, Throwable ex) {
		println(category, type, "Caused by " + ex.getClass().getName() + ": " + ex.getLocalizedMessage() + " ("
				+ ex.getMessage() + ")");
		for (StackTraceElement element : ex.getStackTrace()) {
			println(category, type, "  at " + element);
		}
		if (ex.getCause() != null) {
			printlnCause(category, type, ex.getCause());
		}
	}

}
