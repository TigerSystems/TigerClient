package de.MarkusTieger.common.cosmetics;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.IPlayer;
import de.MarkusTieger.tigerclient.cosmetics.Cosmetic;
import net.minecraft.client.player.AbstractClientPlayer;

@NoObfuscation
public interface ICosmeticManager extends ITickable<Throwable> {
	/*
	 * private String createPost(String access) { return "access=" + access; }
	 */
	void initPlayer(AbstractClientPlayer playerEntity);

	void initPlayer(GameProfile profile);

	IPlayer getClient(AbstractClientPlayer player);

	IAnimationData getCapeData(AbstractClientPlayer player);

	IAnimationData getElytraData(AbstractClientPlayer player);

	int getGlassesColor(AbstractClientPlayer player);

	int getHatColor(AbstractClientPlayer player);

	ICosmeticRegistry getRegistery();

	int getWingColor(AbstractClientPlayer player);

	boolean hasGlasses(AbstractClientPlayer player, String name);

	boolean hasHat(AbstractClientPlayer player, String name);

	boolean hasWing(AbstractClientPlayer player, String name);

	void inject();

	boolean isBuyed(Cosmetic cosmetic);

	void resetBuyed();
}
