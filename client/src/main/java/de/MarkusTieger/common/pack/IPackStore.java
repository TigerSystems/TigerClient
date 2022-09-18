package de.MarkusTieger.common.pack;

import java.util.List;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IPackStore {

	public List<IPack> getPacks();

	public List<IPack> searchPacks(String data);

}
