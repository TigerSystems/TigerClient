package de.MarkusTieger.tigerclient.cosmetics;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.cosmetics.ICosmetic;
import de.MarkusTieger.common.cosmetics.ICosmeticManager;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.player.IPlayer;
import de.MarkusTieger.tigerclient.cosmetics.render.CapeRenderer;
import de.MarkusTieger.tigerclient.cosmetics.render.ElytraRenderer;
import de.MarkusTieger.tigerclient.cosmetics.util.EditableColor;
import de.MarkusTieger.tigerclient.utils.player.DummyPlayer;
import de.MarkusTieger.tigerclient.utils.player.PlayerUtils;
import de.MarkusTieger.tigerclient.utils.player.TigerClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CosmeticManager implements ICosmeticManager {

	public static final Gson GSON = new GsonBuilder().create();
	public final HashMap<UUID, IPlayer> clients = new HashMap<>();
	public ArrayList<ICosmetic> buyed = new ArrayList<>();
	private CosmeticRegistery registery;
	private GameProfile profile = null;

	// private static Gson GSON = new GsonBuilder().create();

	/*
	 * private String createPost(String access) { return "access=" + access; }
	 */
	@Override
	public void initPlayer(AbstractClientPlayer playerEntity) {
		initPlayer(playerEntity.getGameProfile());
	}

	@Override
	public void initPlayer(GameProfile profile) {
		clients.remove(profile.getId());
		Client.getInstance().getLogger().debug(LoggingCategory.COSMETICS,
				"Loading TigerClient-Player for " + profile.getName() + "...");
		TigerClientPlayer player = PlayerUtils.getClientPlayer(profile.getId());
		IPlayer pp = PlayerUtils.prepare(profile, player);
		clients.put(profile.getId(), pp);
	}

	@Override
	public IPlayer getClient(AbstractClientPlayer player) {
		IPlayer client = clients.get(player.getGameProfile().getId());
		if (client == null) {
			if (profile == null) {
				profile = player.getGameProfile();
			}
			client = new DummyPlayer();
		}
		return client;
	}

	@Override
	public IAnimationData getCapeData(AbstractClientPlayer player) {
		IPlayer client = getClient(player);
		return client.getCape();
	}

	@Override
	public IAnimationData getElytraData(AbstractClientPlayer player) {
		IPlayer client = getClient(player);
		return client.getElytra();
	}

	@Override
	public int getGlassesColor(AbstractClientPlayer player) {
		IPlayer client = getClient(player);
		return client.getGlassesColor();
	}

	@Override
	public int getHatColor(AbstractClientPlayer player) {
		IPlayer client = getClient(player);
		return client.getHatColor();
	}

	@Override
	public CosmeticRegistery getRegistery() {
		if (registery == null) {
			registery = new CosmeticRegistery();
			registery
					.register(new Cosmetic("TigerClient Cape", "TigerClient", "TigerClient_Cape",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/capes/tigerclient/icon.png"),
							ICosmetic.CosmeticType.CAPE))
					.register(new Cosmetic("MrDemonEye's Cape", "demoneye", "DemonEye_Cape",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/capes/demoneye/icon.png"),
							ICosmetic.CosmeticType.CAPE))
					.register(new Cosmetic("MrDemonEye's Elytra", "demoneye", "DemonEye_Elytra",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/elytras/demoneye/icon.png"),
							ICosmetic.CosmeticType.ELYTRA))
					.register(new Cosmetic("Dragon Wing", "dragon", "Dragon_Wing",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/wings/dragon/icon.png"),
							ICosmetic.CosmeticType.WING,
							new EditableColor(0.0F, 0.5F, 1.0F, 0.0F, true, true, true, false)))
					.register(new Cosmetic("Transparent Wing", "dragon", "Transparent_Wing",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/wings/transparent_icon.png"),
							ICosmetic.CosmeticType.WING,
							new EditableColor(0.0F, 0.5F, 1.0F, 255.0F, true, true, true, false)))
					.register(new Cosmetic("Crown", "crown", "Crown_Hat",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/hats/crown/icon.png"),
							ICosmetic.CosmeticType.HAT,
							new EditableColor(1.0F, 0.0F, 0.0F, 255.0F, true, true, true, false)))
					.register(new Cosmetic("Top Hat", "top", "Top_Hat",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/hats/top/icon.png"),
							ICosmetic.CosmeticType.HAT,
							new EditableColor(1.0F, 0.0F, 0.0F, 255.0F, true, true, true, false)))
					.register(new Cosmetic("Sun Glasses", "sun", "Sun_Glasses",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/glasses/sun/icon.png"),
							ICosmetic.CosmeticType.GLASSES,
							new EditableColor(0F, 0F, 0F, 255F, true, true, true, false)))
					.register(new Cosmetic("Googly Eyes", "googlyEyes", "googlyEyes_Glasses",
							new ResourceLocation(Client.getInstance().getModId(),
									"textures/cosmetics/glasses/googlyeyes/icon.png"),
							ICosmetic.CosmeticType.GLASSES,
							new EditableColor(0.0F, 0.0F, 0.0F, 255.0F, true, true, true, false)));
		}
		return registery;
	}

	@Override
	public int getWingColor(AbstractClientPlayer player) {
		IPlayer client = getClient(player);
		return client.getWingColor();
	}

	@Override
	public boolean hasGlasses(AbstractClientPlayer player, String name) {
		IPlayer client = getClient(player);
		return client.hasGlasses(name);
	}

	@Override
	public boolean hasHat(AbstractClientPlayer player, String name) {
		IPlayer client = getClient(player);
		return client.hasHat(name);
	}

	@Override
	public boolean hasWing(AbstractClientPlayer player, String name) {
		IPlayer client = getClient(player);
		return client.hasWing(name);
	}

	@Override
	public void inject() {
		for (EntityRenderer<? extends Player> renderer : Minecraft.getInstance().getEntityRenderDispatcher()
				.getSkinMap().values()) {
			if (renderer instanceof PlayerRenderer pr) {
				try {
					removeLayers(pr);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				try {
					pr.addLayer(new CapeRenderer(pr));
					pr.addLayer(new ElytraRenderer(pr, Minecraft.getInstance().getEntityModels()));
					/*
					 * ((PlayerRenderer) renderer).addLayer(new CrownRenderer( renderer));
					 */
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isBuyed(Cosmetic cosmetic) {
		return buyed.contains(cosmetic);
	}

	@SuppressWarnings("deprecation")
	private void removeLayers(PlayerRenderer renderer) throws IllegalArgumentException, IllegalAccessException {
		for (Field f : LivingEntityRenderer.class.getDeclaredFields()) {

			boolean accessible = f.isAccessible();
			f.setAccessible(true);
			if (f.getType().equals(List.class)) {

				List<?> list = (List<?>) f.get(renderer);
				ArrayList<Object> toRemove = new ArrayList<>();
				for (Object item : list) {
					if (item.getClass().equals(CapeLayer.class) || item.getClass().equals(ElytraLayer.class)) {
						toRemove.add(item);
					}
				}
				list.removeAll(toRemove);
			}
			f.setAccessible(accessible);

		}
	}

	@Override
	public void resetBuyed() {
		buyed = new ArrayList<>();
	}

	@SuppressWarnings("unused")
	private void setBuyed(ArrayList<String> cosmetics) {
		if (cosmetics != null) {
			for (ICosmetic c : getRegistery().toArray()) {
				if (cosmetics.contains(c.getNameType())) {
					buyed.add(c);
				}
			}
		}
	}

	@Override
	public void onTick() {

	}

	/*
	 * public static void setBuyed(Authication auth) {
	 * 
	 * try { HttpURLConnection con = (HttpURLConnection) new URL(
	 * "http://markustieger.bplaced.net/TigerClient/cosmetics.php").openConnection()
	 * ; byte[] bytes = createPost(auth.access).getBytes();
	 * con.setRequestProperty("Content-Length", "" + bytes.length);
	 * con.setDoOutput(true); OutputStream out = con.getOutputStream();
	 * out.write(bytes); out.flush(); out.close();
	 * 
	 * InputStream in = con.getInputStream(); ByteArrayOutputStream o = new
	 * ByteArrayOutputStream(); int len; byte[] buffer = new byte[1024]; while ((len
	 * = in.read(buffer)) > 0) { o.write(buffer, 0, len); } String json = new
	 * String(o.toByteArray()); System.out.println("JSON: -" + json + "-");
	 * CosmeticResponse cosmetic = GSON.fromJson(json, CosmeticResponse.class);
	 * in.close(); setBuyed(cosmetic.cosmetics);
	 * 
	 * } catch (Exception ex) { //
	 * gui.addMessage("[Authicate] Exception Authicated!"); ex.printStackTrace(); }
	 * 
	 * }
	 * 
	 * public static void setBuyed(AuthicationResponse response) {
	 * setBuyed(response.profile); }
	 */

}
