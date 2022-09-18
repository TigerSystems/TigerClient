package de.MarkusTieger.tac.core.dns;

import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;

public interface TACResolvedServerAddress extends ResolvedServerAddress {

	boolean isAllowed();

	boolean isTrusted();

	default ResolvedServerAddress toNormal() {
		return this;
	}

}
