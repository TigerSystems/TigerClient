package de.MarkusTieger.tigerclient.utils.player;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.IPlayer;

public class DummyPlayer implements IPlayer {

	@Override
	public IAnimationData getCape() {
		return null;
	}

	@Override
	public IAnimationData getElytra() {
		return null;
	}

	@Override
	public int getGlassesColor() {
		return 0;
	}

	@Override
	public IAnimationData getGlassesTexture() {
		return null;
	}

	@Override
	public int getHatColor() {
		return 0;
	}

	@Override
	public IAnimationData getHatTexture() {
		return null;
	}

	@Override
	public int getWingColor() {
		return 0;
	}

	@Override
	public IAnimationData getWingTexture() {
		return null;
	}

	@Override
	public boolean hasGlasses(String name) {
		return false;
	}

	@Override
	public boolean hasHat(String name) {
		return false;
	}

	@Override
	public boolean hasWing(String name) {
		return false;
	}

}
