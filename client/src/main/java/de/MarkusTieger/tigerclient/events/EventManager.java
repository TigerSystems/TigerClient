package de.MarkusTieger.tigerclient.events;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.events.EventHandler;
import de.MarkusTieger.common.events.IEvent;
import de.MarkusTieger.common.events.IEventManager;

@NoObfuscation
public class EventManager implements IEventManager {

	private final ArrayList<Object> targets = new ArrayList<>();

	@Override
	public List<Object> toArray() {
		return targets;
	}

	@Override
	public void call(IEvent event) {
		for (EventHandler.EventPriority prio : EventHandler.EventPriority.values()) {
			for (Object target : targets) {
				Class<?> clazz = target.getClass();
				for (Method m : clazz.getDeclaredMethods()) {
					for (Annotation at : m.getDeclaredAnnotations()) {
						if (at instanceof EventHandler) {
							if (((EventHandler) at).priority().equals(prio) && m.getParameterCount() == 1
									&& m.getParameters()[0].getType().isInstance(event)) {
								try {
									m.setAccessible(true);
									if (Modifier.isStatic(m.getModifiers())) {
										m.invoke(null, event);
									} else {
										m.invoke(target, event);
									}
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void call(Object t, IEvent event) throws IOException {
		for (EventHandler.EventPriority prio : EventHandler.EventPriority.values()) {
			for (Object target : targets) {
				if (target != t) {
					continue;
				}
				Class<?> clazz = target.getClass();
				for (Method m : clazz.getDeclaredMethods()) {
					for (Annotation at : m.getDeclaredAnnotations()) {
						if (at instanceof EventHandler) {
							if (((EventHandler) at).priority().equals(prio) && m.getParameterCount() == 1
									&& m.getParameters()[0].getType().isInstance(event)) {
								try {
									m.setAccessible(true);
									if (Modifier.isStatic(m.getModifiers())) {
										m.invoke(null, event);
									} else {
										m.invoke(target, event);
									}
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

}
