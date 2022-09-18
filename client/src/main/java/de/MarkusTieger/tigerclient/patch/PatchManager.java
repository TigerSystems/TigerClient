package de.MarkusTieger.tigerclient.patch;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import de.MarkusTieger.common.patch.IPatchManager;
import de.MarkusTieger.common.patch.PatchException;
import de.MarkusTieger.common.patch.PatchType;

public class PatchManager implements IPatchManager {

	@SuppressWarnings("unused")
	private final boolean data = false; // required to be first

	private static VarHandle MODIFIERS = null;

	static {
		try {
			var lookup = MethodHandles.privateLookupIn(Field.class, MethodHandles.lookup());
			MODIFIERS = lookup.findVarHandle(Field.class, "modifiers", int.class);
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Optional<Throwable> checkPatchable() {
		Field f = PatchManager.class.getDeclaredFields()[0];
		try {
			patch(f, PatchType.BOTH);
			f.setBoolean(this, true);
			return f.getBoolean(this) ? null : Optional.empty();
		} catch (Throwable ex) {
			return Optional.of(ex);
		}
	}

	@Override
	public void patch(Field field, PatchType type) throws PatchException {
		try {
			if (type != PatchType.FINAL) {
				field.setAccessible(true);
			}
			if (type != PatchType.ACESSIBLE) {
				if (Modifier.isFinal(field.getModifiers()))
					MODIFIERS.set(field, field.getModifiers() & ~Modifier.FINAL);
			}
		} catch (Throwable e) {
			throw new PatchException(e);
		}
	}

}
