package de.MarkusTieger.tigerclient.language;

import java.util.ArrayList;
import java.util.List;

import de.MarkusTieger.common.language.ILanguage;
import de.MarkusTieger.common.language.ILanguageRegistry;

public class LanguageRegistry implements ILanguageRegistry {

	private final List<ILanguage> array = new ArrayList<>();

	@Override
	public List<ILanguage> toArray() {
		return array;
	}
}
