package de.MarkusTieger.common.utils.player.cosmetic.providers;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;

@NoObfuscation
public interface ICosmeticProvider {

	IAnimationData getCape(GameProfile profile, TigerClientPlayer player);

	IAnimationData getElytra(GameProfile profile, TigerClientPlayer player);

}
