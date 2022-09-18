package de.MarkusTieger.tigerclient.gui.screens.pack;

import de.MarkusTieger.common.pack.IPack;
import de.MarkusTieger.common.pack.IPackStore;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class PackDownloadScreen extends Screen {

	public PackDownloadScreen(PackStoreScreen parent, IPackStore store, IPack pack) {
		super(new TranslatableComponent("pack.download.title"));
	}

}
