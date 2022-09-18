package de.MarkusTieger.common.utils.player;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.utils.animation.IAnimationData;

@NoObfuscation
public interface IPlayer {

	IAnimationData getCape();

	IAnimationData getElytra();

	int getGlassesColor();

	IAnimationData getGlassesTexture();

	int getHatColor();

	IAnimationData getHatTexture();

	int getWingColor();

	IAnimationData getWingTexture();

	boolean hasGlasses(String name);

	boolean hasHat(String name);

	boolean hasWing(String name);

}
