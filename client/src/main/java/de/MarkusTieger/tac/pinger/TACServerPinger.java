package de.MarkusTieger.tac.pinger;

import java.net.UnknownHostException;
import java.util.Optional;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.tac.bridge.exceptions.TACNotEnabledException;
import de.MarkusTieger.tac.core.dns.TACResolvedServerAddress;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.chat.TextComponent;

public class TACServerPinger extends ServerStatusPinger {

	@Override
	public void pingServer(ServerData d, Runnable runnable) throws UnknownHostException {
		Optional<ResolvedServerAddress> optional = Optional.empty();

		if (Client.getInstance().getTAC().isEnabled()) {
			try {
				optional = Client.getInstance().getTAC().resolve(d.ip);
			} catch (TACNotEnabledException e) {
				Client.getInstance().getLogger().warn(LoggingCategory.TAC, e);
			}
		} else {
			optional = ServerNameResolver.DEFAULT.resolveAddress(ServerAddress.parseString(d.ip));
		}
		if (optional.isPresent()) {
			if (optional.get() instanceof TACResolvedServerAddress toptional) {
				if (!toptional.isAllowed()) {
					d.ping = -1L;
					d.motd = new TextComponent(ChatFormatting.DARK_RED + "Protected by TAC");
					runnable.run();
					return;
				}
			}

		}
		try {
			if (d.ip.contains("#")) {
				int index = d.ip.indexOf('#');
				String ip = d.ip.substring(0, index);
				String paramter = d.ip.substring(index + 1);
				@SuppressWarnings("unused")
				String[] args = paramter.contains(",") ? paramter.split(",") : new String[] { paramter };

				final String old = d.ip;
				d.ip = ip;
				super.pingServer(d, runnable);
				d.ip = old;

			} else
				super.pingServer(d, runnable);

		} catch (UnknownHostException e) {
			throw e;
		}
	}
}
