package de.MarkusTieger.common.logger;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface ILogger {

	String info(LoggingCategory category, String msg);

	String debug(LoggingCategory category, String msg);

	String warn(LoggingCategory category, String msg);

	Throwable warn(LoggingCategory category, Throwable th);

	String warn(LoggingCategory category, String msg, Throwable th);

	String error(LoggingCategory category, String msg);

	Throwable error(LoggingCategory category, Throwable th);

	String error(LoggingCategory category, String msg, Throwable th);

}
