package de.MarkusTieger.tigerclient.gui.screens.pack;

import java.util.ArrayList;
import java.util.List;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.pack.IPack;
import de.MarkusTieger.common.pack.IPackStore;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleListScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PackStoreScreen extends SimpleListScreen<IPack> {

	private List<IPack> objects = new ArrayList<>();
	private final IPackStore store;

	public PackStoreScreen(IPackStore store, Screen p_101338_) {
		super(new TranslatableComponent("pack.store.title"),
				new ResourceLocation(Client.getInstance().getModId(), "textures/gui/pack_selection.png"), p_101338_);
		this.store = store;
	}

	@Override
	protected void init() {
		new Thread(this::initialSearch, "Pack Searcher").start();
	}

	private void initialSearch() {

	}

	@Override
	public void click(IPack object) {
		this.minecraft.setScreen(new PackDownloadScreen(this, store, object));
	}

	@Override
	public List<IPack> getObjects() {
		return objects;
	}

	@Override
	public void updateButtonData(IPack object) {

	}

}
