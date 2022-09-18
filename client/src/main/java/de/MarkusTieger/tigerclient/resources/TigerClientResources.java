package de.MarkusTieger.tigerclient.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonObject;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.language.ILanguage;
import de.MarkusTieger.common.language.ILanguageRegistry;
import de.MarkusTieger.common.logger.LoggingCategory;
import net.minecraft.SharedConstants;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSectionSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSectionSerializer;

public class TigerClientResources implements PackResources {

	@SuppressWarnings("unused")
	private final String modid;
	private final JsonObject meta;
	private final HashMap<String, String> links = new HashMap<>();

	private static final List<String> namespaces = new ArrayList<>();

	public TigerClientResources(String modid, ILanguageRegistry registry) {
		this.modid = modid;
		this.meta = new JsonObject();

		JsonObject pack = new JsonObject();

		pack.addProperty("pack_format", PackType.CLIENT_RESOURCES.getVersion(SharedConstants.getCurrentVersion()));
		pack.addProperty("description", "The Resources for the TigerClient");

		meta.add("pack", pack);

		JsonObject language = new JsonObject();

		for (ILanguage lang : registry.toArray()) {
			addLanguage(language, lang.getID(), lang.getRegion(), lang.getName());
		}

		meta.add("language", language);

		links.put("pack.png", "assets/tigerclient/textures/gui/icon.png");
		links.put("pack.mcmeta", "not_reachable");

		namespaces.add(modid);
	}

	public static void addNamespace(String namespace) {
		namespaces.add(namespace);
	}

	private void addLanguage(JsonObject language, String en_us, String us, String english) {
		JsonObject lang = new JsonObject();

		lang.addProperty("region", us);
		lang.addProperty("name", english);

		language.add(en_us, lang);
	}

	@Override
	public InputStream getRootResource(String s) throws IOException {
		for (Map.Entry<String, String> e : links.entrySet()) {
			if (e.getKey().equalsIgnoreCase(s)) {
				return getRootResource(e.getValue());
			}
			if (e.getKey().endsWith("*")
					&& s.toLowerCase().startsWith(e.getKey().toLowerCase().substring(0, e.getKey().length() - 1))) {
				String path = e.getValue() + s.substring(e.getKey().length() - 1);
				return getRootResource(path);
			}
		}
		// System.out.println("Res: " + s);
		return TigerClientResources.class.getResourceAsStream("/" + s);
	}

	@Override
	public InputStream getResource(PackType packType, ResourceLocation resourceLocation) throws IOException {
		return getRootResource("assets/" + resourceLocation.getNamespace() + "/" + resourceLocation.getPath());
	}

	@Override
	public Collection<ResourceLocation> getResources(PackType packType, String s, String s1, int i,
			Predicate<String> predicate) {
		return new ArrayList<>();
	}

	@Override
	public boolean hasResource(PackType packType, ResourceLocation resourceLocation) {
		try {
			return getResource(packType, resourceLocation) != null;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Set<String> getNamespaces(PackType packType) {
		return Set.of(namespaces.toArray(new String[0]));
	}

	@Override
	public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
		Client.getInstance().getLogger().debug(LoggingCategory.MAIN, metadataSectionSerializer.getClass().getName());
		if (metadataSectionSerializer instanceof PackMetadataSectionSerializer) {
			return metadataSectionSerializer.fromJson(meta.get("pack").getAsJsonObject());
		}
		if (metadataSectionSerializer instanceof LanguageMetadataSectionSerializer) {
			return metadataSectionSerializer.fromJson(meta.get("language").getAsJsonObject());
		}
		return metadataSectionSerializer.fromJson(meta);
	}

	@Override
	public String getName() {
		return "TigerClient";
	}

	@Override
	public void close() {
	}
}
