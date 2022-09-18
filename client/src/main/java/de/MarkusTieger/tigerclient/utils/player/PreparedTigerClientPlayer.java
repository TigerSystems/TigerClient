package de.MarkusTieger.tigerclient.utils.player;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.IPlayer;

public class PreparedTigerClientPlayer implements IPlayer {

	private final IAnimationData cape;
	private final IAnimationData elytra;
	private final String wing;
	private final String hat;
	private final String glasses;
	private final int wingColor;
	private final int hatColor;
	private final int glassesColor;
	private final IAnimationData wingTexture;
	private final IAnimationData hatTexture;
	private final IAnimationData glassesTexture;

	public PreparedTigerClientPlayer(IAnimationData cape, IAnimationData elytra, String wing, String hat,
			String glasses, int wingColor, int hatColor, int glassesColor, IAnimationData wingTexture,
			IAnimationData hatTexture, IAnimationData glassesTexture) {
		super();
		this.cape = cape;
		this.elytra = elytra;
		this.wing = wing;
		this.hat = hat;
		this.glasses = glasses;
		this.wingColor = wingColor;
		this.hatColor = hatColor;
		this.glassesColor = glassesColor;
		this.wingTexture = wingTexture;
		this.hatTexture = hatTexture;
		this.glassesTexture = glassesTexture;
	}

	@Override
	public IAnimationData getCape() {
		return cape;
	}

	@Override
	public IAnimationData getElytra() {
		return elytra;
	}

	@Override
	public int getGlassesColor() {
		return glassesColor;
	}

	@Override
	public IAnimationData getGlassesTexture() {
		return glassesTexture;
	}

	@Override
	public int getHatColor() {
		return hatColor;
	}

	@Override
	public IAnimationData getHatTexture() {
		return hatTexture;
	}

	@Override
	public int getWingColor() {
		return wingColor;
	}

	@Override
	public IAnimationData getWingTexture() {
		return wingTexture;
	}

	@Override
	public boolean hasGlasses(String name) {
		return glasses.equalsIgnoreCase(name);
	}

	@Override
	public boolean hasHat(String name) {
		return hat.equalsIgnoreCase(name);
	}

	@Override
	public boolean hasWing(String name) {
		return wing.equalsIgnoreCase(name);
	}

}
