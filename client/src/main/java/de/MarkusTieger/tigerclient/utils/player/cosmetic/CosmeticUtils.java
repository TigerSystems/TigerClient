package de.MarkusTieger.tigerclient.utils.player.cosmetic;

import com.mojang.authlib.GameProfile;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.cosmetic.providers.ICosmeticProvider;
import de.MarkusTieger.tigerclient.data.Data;
import de.MarkusTieger.tigerclient.utils.animation.AlternativeAnimationData;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;
import de.MarkusTieger.tigerclient.utils.player.cosmetic.providers.MinecraftProvider;
import de.MarkusTieger.tigerclient.utils.player.cosmetic.providers.OptifineProvider;
import de.MarkusTieger.tigerclient.utils.player.cosmetic.providers.TigerClientProvider;

public class CosmeticUtils {

	// Minecraft, TigerClient, OptiFine, BAC, NoRiskClient

	public static final ICosmeticProvider MINECRAFT = new MinecraftProvider();
	public static final ICosmeticProvider TIGERCLIENT = new TigerClientProvider();
	public static final ICosmeticProvider OPTIFINE = new OptifineProvider();

	public static final ICosmeticProvider DEFAULT = TIGERCLIENT;

	public static IAnimationData getCape(GameProfile profile, TigerClientPlayer player) {

		IAnimationData data = null;

		CosmeticPriorities priorities = player.cosmeticPriorities;

		if (priorities == null)
			priorities = new CosmeticPriorities();

		ICosmeticProvider provider1 = getById(priorities.provider1), provider2 = getById(priorities.provider2),
				provider3 = getById(priorities.provider3), provider4 = getById(priorities.provider4),
				provider5 = getById(priorities.provider5);

		Data<IAnimationData> altData = Data.empty(IAnimationData.class);

		if (provider1 instanceof MinecraftProvider) {
			data = new AlternativeAnimationData("Minecraft", altData);
		} else {
			data = provider1.getCape(profile, player);
		}

		if (!(provider1 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider1.getCape(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider2 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider2.getCape(profile, player);
			}
		}

		if (!(provider2 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider2.getCape(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider3 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider3.getCape(profile, player);
			}
		}

		if (!(provider3 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider3.getCape(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider4 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider4.getCape(profile, player);
			}
		}

		if (!(provider4 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider4.getCape(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider5 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider5.getCape(profile, player);
			}
		}

		if (!(provider5 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider5.getCape(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		return data;
	}

	public static ICosmeticProvider getById(Number id) {
		if (id == null)
			return DEFAULT;

		int i = id.intValue();

		if (i == 0)
			return MINECRAFT;
		if (i == 1)
			return TIGERCLIENT;
		if (i == 2)
			return OPTIFINE;

		return DEFAULT;
	}

	public static IAnimationData getElytra(GameProfile profile, TigerClientPlayer player) {

		IAnimationData data = null;

		CosmeticPriorities priorities = player.cosmeticPriorities;

		if (priorities == null)
			priorities = new CosmeticPriorities();

		ICosmeticProvider provider1 = getById(priorities.provider1), provider2 = getById(priorities.provider2),
				provider3 = getById(priorities.provider3), provider4 = getById(priorities.provider4),
				provider5 = getById(priorities.provider5);

		Data<IAnimationData> altData = Data.empty(IAnimationData.class);

		if (provider1 instanceof MinecraftProvider) {
			data = new AlternativeAnimationData("Minecraft", altData);
		} else {
			data = provider1.getElytra(profile, player);
		}

		if (!(provider1 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider1.getElytra(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider2 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider2.getElytra(profile, player);
			}
		}

		if (!(provider2 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider2.getElytra(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider3 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider3.getElytra(profile, player);
			}
		}

		if (!(provider3 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider3.getElytra(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider4 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider4.getElytra(profile, player);
			}
		}

		if (!(provider4 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider4.getElytra(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		if (data == null) {
			if (provider5 instanceof MinecraftProvider) {
				data = new AlternativeAnimationData("Minecraft", altData);
			} else {
				data = provider5.getElytra(profile, player);
			}
		}

		if (!(provider5 instanceof MinecraftProvider) && altData.isEmpty()) {
			IAnimationData dat = provider5.getElytra(profile, player);
			if (dat != null)
				altData.setData(dat);
		}

		return data;
	}

}
