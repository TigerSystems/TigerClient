package de.MarkusTieger.tigerclient.utils.player.cosmetic.providers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.NativeImage;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.cosmetic.providers.ICosmeticProvider;
import de.MarkusTieger.tigerclient.utils.animation.OneFrameData;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class OptifineProvider implements ICosmeticProvider {

	private final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");

	private final HashMap<String, ResourceLocation> capes = new HashMap<>();
	private final ArrayList<String> elytras = new ArrayList<>();

	public void downloadCape(GameProfile profile) {
		String s = profile.getName();

		if (s != null && !s.isEmpty() && !s.contains("\u0000") && PATTERN_USERNAME.matcher(s).matches()) {
			String imageUrl = "http://s.optifine.net/capes/" + s + ".png";
			ResourceLocation resourcelocation = new ResourceLocation(Client.getInstance().getModId(),
					"textures/cosmetics/capes/optifine/" + s.toLowerCase());
			TextureManager texturemanager = Minecraft.getInstance().getTextureManager();

			HttpURLConnection httpurlconnection = null;
			Client.getInstance().getLogger().debug(LoggingCategory.COSMETICS,
					"Downloading http texture from '" + imageUrl + "'");

			try {
				httpurlconnection = (HttpURLConnection) (new URL(imageUrl))
						.openConnection(Minecraft.getInstance().getProxy());
				httpurlconnection.setRequestProperty("User-Agent", "TigerSystems");
				httpurlconnection.setDoInput(true);
				httpurlconnection.setDoOutput(false);
				httpurlconnection.connect();
				if (httpurlconnection.getResponseCode() / 100 == 2) {
					InputStream inputstream;
					inputstream = httpurlconnection.getInputStream();

					BufferedImage image = ImageIO.read(inputstream);

					BufferedImage imageOriginal = parseCape(image);

					// Store into Memory

					ByteArrayOutputStream imageStreamOut = new ByteArrayOutputStream();
					ImageIO.write(imageOriginal, "png", imageStreamOut);
					ByteArrayInputStream imageStreamIn = new ByteArrayInputStream(imageStreamOut.toByteArray());

					imageStreamOut.reset();
					imageStreamIn.reset();

					DynamicTexture texture = new DynamicTexture(NativeImage.read(imageStreamIn));
					texturemanager.getTexture(resourcelocation, texture)
							.load(Minecraft.getInstance().getResourceManager());

					setElytraOfCape(profile, isElytraCape(image, imageOriginal));
					setLocationOfCape(profile, resourcelocation);

					return;
				}
			} catch (Exception exception) {
				Client.getInstance().getLogger().error(LoggingCategory.COSMETICS, "Couldn't download http texture",
						exception);
				return;
			} finally {
				if (httpurlconnection != null) {
					httpurlconnection.disconnect();
				}

			}

		}
	}

	public void setLocationOfCape(GameProfile profile, ResourceLocation location) {
		capes.remove(profile.getName());
		capes.put(profile.getName(), location);
	}

	public void setElytraOfCape(GameProfile profile, boolean elytra) {
		elytras.remove(profile.getName());
		if (elytra)
			elytras.add(profile.getName());
	}

	public BufferedImage parseCape(BufferedImage img) {
		int i = 64;
		int j = 32;
		int k = img.getWidth();

		for (int l = img.getHeight(); i < k || j < l; j *= 2) {
			i *= 2;
		}

		BufferedImage image = new BufferedImage(i, j, BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();
		g.drawImage(img, 0, 0, null);

		return image;
	}

	public boolean isElytraCape(NativeImage imageRaw, NativeImage imageFixed) {
		return imageRaw.getWidth() > imageFixed.getHeight();
	}

	public boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed) {
		return imageRaw.getWidth() > imageFixed.getHeight();
	}

	@Override
	public IAnimationData getCape(GameProfile profile, TigerClientPlayer player) {
		if (!capes.containsKey(profile.getName())) {
			downloadCape(profile);
		}
		ResourceLocation frame = capes.get(profile.getName());
		if (frame == null)
			return null;
		return new OneFrameData(capes.get(profile.getName()));
	}

	@Override
	public IAnimationData getElytra(GameProfile profile, TigerClientPlayer player) {
		if (!elytras.contains(profile.getName()))
			return null;
		return getCape(profile, player);
	}

}
