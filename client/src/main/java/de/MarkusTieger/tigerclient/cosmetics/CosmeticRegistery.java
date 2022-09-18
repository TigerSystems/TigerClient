package de.MarkusTieger.tigerclient.cosmetics;

import java.util.ArrayList;
import java.util.List;

import de.MarkusTieger.common.cosmetics.ICosmeticRegistry;

public class CosmeticRegistery implements ICosmeticRegistry {

	private final ArrayList<Cosmetic> cosmetics = new ArrayList<>();

	@Override
	public List<Cosmetic> toArray() {
		return cosmetics;
	}
}
