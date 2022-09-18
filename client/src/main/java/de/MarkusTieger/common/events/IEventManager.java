package de.MarkusTieger.common.events;

import java.io.IOException;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.registry.IRegistry;

@NoObfuscation
public interface IEventManager extends IRegistry<IEventManager, Object> {

	void call(IEvent event);

	void call(Object t, IEvent event) throws IOException;
}
