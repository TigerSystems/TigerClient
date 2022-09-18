package de.MarkusTieger.common.patch;

import java.lang.reflect.Field;
import java.util.Optional;

public interface IPatchManager {

	Optional<Throwable> checkPatchable();

	void patch(Field field, PatchType type) throws PatchException;

}
