package de.MarkusTieger.tigerclient.gui.screens;

import java.util.List;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.certificates.ICertificateManager;
import de.MarkusTieger.tigerclient.api.gui.list.SimpleListScreen;
import de.MarkusTieger.tigerclient.certificates.Certificate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class CertificatesScreen extends SimpleListScreen<Certificate> {

	private static final ResourceLocation OVERLAY_LOCATION = new ResourceLocation(Client.getInstance().getModId(),
			"textures/empty.png");

	public CertificatesScreen(Screen parent) {
		super(new TranslatableComponent("screen.certs.title"), OVERLAY_LOCATION, parent);
	}

	@Override
	public void click(Certificate object) {

	}

	@Override
	public List<Certificate> getObjects() {
		return Client.getInstance().getInstanceRegistry().load(ICertificateManager.class).getLastLoadedCertificates();
	}

	@Override
	public void updateButtonData(Certificate object) {

	}
}
