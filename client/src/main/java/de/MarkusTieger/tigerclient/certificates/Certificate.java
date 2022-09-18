package de.MarkusTieger.tigerclient.certificates;

import java.security.Principal;
import java.security.cert.X509Certificate;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.api.gui.list.IListObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("deprecation")
public class Certificate implements IListObject {

	private final X509Certificate cert;

	public Certificate(X509Certificate cert) {
		this.cert = cert;
	}

	@Override
	public Component getDisplayName() {
		String txt = "<subject>";
		Principal prinsipal = cert.getSubjectDN();
		if (prinsipal != null && prinsipal.getName() != null)
			txt = prinsipal.getName();

		return new TextComponent(txt);
	}

	@Override
	public Component getDescription() {
		String txt = "<issuer>";
		Principal prinsipal = cert.getIssuerDN();
		if (prinsipal != null && prinsipal.getName() != null)
			txt = prinsipal.getName();

		return new TextComponent(txt);
	}

	@Override
	public Component getInfo() {
		return new TextComponent(cert.getNotBefore() + " - " + cert.getNotAfter());
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public ResourceLocation getIcon() {
		return null;
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public String getSearchName() {
		return "x509-cert";
	}

	@Override
	public void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) {

	}
}
