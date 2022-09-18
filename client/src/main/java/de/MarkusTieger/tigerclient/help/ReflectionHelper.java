package de.MarkusTieger.tigerclient.help;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {

	public static Field getFirstField(Class<?> clazz, Object obj, boolean searchInSuperclass, boolean typeSuperclass,
			boolean stat) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : ((Class<?>) (obj instanceof Class ? obj : obj.getClass())).getDeclaredFields()) {
			Class<?> cl = field.getType();
			if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
				return field;
			} else if (typeSuperclass) {
				Class<?> cla = cl;
				while ((cla = cla.getSuperclass()) != null) {
					for (Field f : cla.getDeclaredFields()) {
						cl = f.getType();
						if (cl.equals(clazz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
							return f;
						}
					}

				}
			}
		}
		if (searchInSuperclass) {
			Class<?> claz = ((Class<?>) (obj instanceof Class ? obj : obj.getClass()));
			while ((claz = claz.getSuperclass()) != null) {
				if (claz.equals(Object.class))
					break;
				for (Field field : claz.getDeclaredFields()) {
					Class<?> cl = field.getType();

					if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
						return field;
					} else if (typeSuperclass) {
						Class<?> cla = cl;
						while ((cla = cla.getSuperclass()) != null) {
							for (Field f : cla.getDeclaredFields()) {
								cl = f.getType();
								if (cl.equals(clazz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
									return f;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFirstOfType(Class<T> clazz, Object obj, boolean searchInSuperclass, boolean typeSuperclass,
			boolean stat) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : ((Class<?>) (obj instanceof Class ? obj : obj.getClass())).getDeclaredFields()) {
			Class<?> cl = field.getType();
			if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
				field.setAccessible(true);
				return (T) field.get(obj);
			} else if (typeSuperclass) {
				Class<?> cla = cl;
				while ((cla = cla.getSuperclass()) != null) {
					for (Field f : cla.getDeclaredFields()) {
						cl = f.getType();
						if (cl.equals(clazz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
							f.setAccessible(true);
							return (T) f.get(obj);
						}
					}

				}
			}
		}
		if (searchInSuperclass) {
			Class<?> claz = ((Class<?>) (obj instanceof Class ? obj : obj.getClass()));
			while ((claz = claz.getSuperclass()) != null) {
				if (claz.equals(Object.class))
					break;
				for (Field field : claz.getDeclaredFields()) {
					Class<?> cl = field.getType();

					if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
						field.setAccessible(true);
						return (T) field.get(obj);
					} else if (typeSuperclass) {
						Class<?> cla = cl;
						while ((cla = cla.getSuperclass()) != null) {
							for (Field f : cla.getDeclaredFields()) {
								cl = f.getType();
								if (cl.equals(clazz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
									f.setAccessible(true);
									return (T) f.get(obj);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static void removeFinal(Field f) throws Exception {
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
	}

	public static <T> void setFirstOfType(Class<T> clazz, Object obj, T value, boolean searchInSuperclass,
			boolean typeSuperclass, boolean stat) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : ((Class<?>) (obj instanceof Class ? obj : obj.getClass())).getDeclaredFields()) {
			Class<?> cl = field.getType();
			if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
				field.setAccessible(true);
				if (Modifier.isFinal(field.getModifiers())) {
					try {
						removeFinal(field);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				field.set(obj, value);
			} else if (typeSuperclass) {
				Class<?> cla = cl;
				while ((cla = cla.getSuperclass()) != null) {
					for (Field f : cla.getDeclaredFields()) {
						cl = f.getType();
						if (cl.equals(clazz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
							f.setAccessible(true);
							if (Modifier.isFinal(f.getModifiers())) {
								try {
									removeFinal(f);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							f.set(obj, value);
						}
					}
				}
			}
		}
		if (searchInSuperclass) {
			Class<?> claz = ((Class<?>) (obj instanceof Class ? obj : obj.getClass()));
			while ((claz = claz.getSuperclass()) != null) {
				if (claz.equals(Object.class))
					break;
				for (Field field : claz.getDeclaredFields()) {
					Class<?> cl = field.getType();
					if (cl.equals(clazz) && (Modifier.isStatic(field.getModifiers()) == stat)) {
						field.setAccessible(true);
						if (Modifier.isFinal(field.getModifiers())) {
							try {
								removeFinal(field);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						field.set(obj, value);
					} else if (typeSuperclass) {
						Class<?> cla = cl;
						while ((cla = cla.getSuperclass()) != null) {
							for (Field f : cla.getDeclaredFields()) {
								cl = f.getType();
								if (cl.equals(claz) && (Modifier.isStatic(f.getModifiers()) == stat)) {
									f.setAccessible(true);
									if (Modifier.isFinal(f.getModifiers())) {
										try {
											removeFinal(f);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									f.set(obj, value);
								}
							}
						}
					}
				}
			}
		}
	}

	public static <T> void setFirstOfTypeWithFinal(Class<T> clazz, Object obj, T value, boolean searchInSuperclass,
			boolean typeSuperclass) throws Exception {
		for (Field field : ((Class<?>) (obj instanceof Class ? obj : obj.getClass())).getDeclaredFields()) {
			Class<?> cl = field.getType();
			if (cl.equals(clazz)) {
				field.setAccessible(true);
				removeFinal(field);
				field.set(obj, value);
			} else if (typeSuperclass) {
				Class<?> cla;
				while ((cla = cl.getSuperclass()) != null) {
					if (cla.equals(clazz)) {
						field.setAccessible(true);
						removeFinal(field);
						field.set(obj, value);
					}
				}
			}
		}
		Class<?> claz;
		while ((claz = clazz.getSuperclass()) != null) {
			for (Field field : claz.getDeclaredFields()) {
				Class<?> cl = field.getType();
				if (cl.equals(claz)) {
					field.setAccessible(true);
					removeFinal(field);
					field.set(obj, value);
				} else if (typeSuperclass) {
					Class<?> cla;
					while ((cla = cl.getSuperclass()) != null) {
						if (cla.equals(claz)) {
							field.setAccessible(true);
							removeFinal(field);
							field.set(obj, value);
						}
					}
				}
			}
		}
	}

}
