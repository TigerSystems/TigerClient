package de.MarkusTieger.common.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.MarkusTieger.annotations.NoObfuscation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@NoObfuscation
public @interface EventHandler {

	EventPriority priority() default EventPriority.NORMAL;

	enum EventPriority {

		LOWEST, LOW, NORMAL, HIGH, HIGHEST

	}

}
