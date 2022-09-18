package de.MarkusTieger.tigerclient.utils.player.cosmetic.providers;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.common.utils.player.cosmetic.providers.ICosmeticProvider;
import de.MarkusTieger.tigerclient.utils.animation.AnimationData;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;

public class MinecraftProvider implements ICosmeticProvider {

	@Override
	public AnimationData getCape(GameProfile profile, TigerClientPlayer player) {
		return null;
	}

	@Override
	public AnimationData getElytra(GameProfile profile, TigerClientPlayer player) {
		return null;
	}

}
