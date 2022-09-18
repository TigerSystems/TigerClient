package de.MarkusTieger.tigerclient.utils.player.cosmetic.providers;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.animation.IAnimationRegistry;
import de.MarkusTieger.common.utils.player.cosmetic.providers.ICosmeticProvider;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;

public class TigerClientProvider implements ICosmeticProvider {

	private final IAnimationRegistry registry = Client.getInstance().getAnimationRegistry();

	@Override
	public IAnimationData getCape(GameProfile profile, TigerClientPlayer player) {
		return registry.load("cape_" + player.cape);
	}

	@Override
	public IAnimationData getElytra(GameProfile profile, TigerClientPlayer player) {
		return registry.load("elytra_" + player.elytra);
	}

}
