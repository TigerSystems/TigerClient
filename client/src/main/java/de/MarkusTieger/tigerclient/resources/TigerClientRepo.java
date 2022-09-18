package de.MarkusTieger.tigerclient.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.language.ILanguageRegistry;
import de.MarkusTieger.common.logger.LoggingCategory;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

public class TigerClientRepo implements RepositorySource {

	private final List<PackResources> resources = new ArrayList<>();
	private final PackResources tcr;

	public TigerClientRepo(ILanguageRegistry registry) {
		resources.add(tcr = new TigerClientResources(Client.getInstance().getModId(), registry));
	}

	@Override
	public void loadPacks(Consumer<Pack> packs, Pack.PackConstructor constructor) {
		Client.getInstance().getLogger().debug(LoggingCategory.MAIN, "Loading Packs...");

		Pack pack = constructor.create(Client.getInstance().getModId(), new TranslatableComponent("pack.name"), true,
				() -> tcr,
				new PackMetadataSection(new TranslatableComponent("pack.description"),
						PackType.CLIENT_RESOURCES.getVersion(SharedConstants.getCurrentVersion())),
				Pack.Position.BOTTOM, PackSource.BUILT_IN, tcr.isHidden());

		packs.accept(pack);
	}

	public List<PackResources> getResources() {
		return resources;
	}
}
