package de.MarkusTieger.tigerclient.utils.player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.IPlayer;
import de.MarkusTieger.tigerclient.utils.player.cosmetic.CosmeticUtils;
import net.minecraft.client.Minecraft;

public class PlayerUtils {

	private static final Gson GSON = new GsonBuilder().create();

	/*
	 * public static TigerClientPlayer getClientPlayer(UUID uuid) { try { return
	 * GSON.fromJson(new InputStreamReader(new
	 * URL("http://markustieger.bplaced.net/tigerclient.php?player=" +
	 * uuid).openStream()), TigerClientPlayer.class); } catch (JsonSyntaxException |
	 * JsonIOException | IOException e) { return new TigerClientPlayer(); } }
	 */

	public static TigerClientPlayer getClientPlayer(UUID uuid) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(
					"https://tigersystems.cf/tigerclient/user.php?uuid=" + uuid)
					.openConnection(Minecraft.getInstance().getProxy());
			con.setRequestProperty("User-Agent", "TigerSystems");
			InputStreamReader reader = new InputStreamReader(con.getInputStream());
			JsonObject obj = GSON.fromJson(reader, JsonObject.class);
			reader.close();
			if (obj.get("code").getAsNumber().intValue() != 0 || !obj.has("data")) {
				return null;
			}
			return GSON.fromJson(obj.get("data"), TigerClientPlayer.class);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static IPlayer prepare(GameProfile profile, TigerClientPlayer player) {

		if (player == null) {
			player = new TigerClientPlayer();
			player.cape = "None";
			player.elytra = "None";

			player.glasses = "None";
			player.hat = "None";
			player.wing = "None";

			player.glassesTexture = "None";
			player.hatTexture = "None";
			player.wingTexture = "None";

			player.hatColor = "00000000";
			player.wingColor = "00000000";
			player.glassesColor = "00000000";
		}

		IAnimationData cape = CosmeticUtils.getCape(profile, player);
		IAnimationData elytra = CosmeticUtils.getElytra(profile, player);

		String wing = player.wing;
		String hat = player.hat;
		String glasses = player.glasses;

		int wingColor = 0;
		int hatColor = 0;
		int glassesColor = 0;

		try {
			wingColor = Integer.parseInt(player.wingColor, 16);
			hatColor = Integer.parseInt(player.hatColor, 16);
			glassesColor = Integer.parseInt(player.glassesColor, 16);
		} catch (NumberFormatException e) {

		}

		IAnimationData wingTexture = null;
		IAnimationData hatTexture = null;
		IAnimationData glassesTexture = null;

		return new PreparedTigerClientPlayer(cape, elytra, wing, hat, glasses, wingColor, hatColor, glassesColor,
				wingTexture, hatTexture, glassesTexture);
	}

}
