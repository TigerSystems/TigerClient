package de.MarkusTieger.tac.bridge.uninstall;

import java.util.Optional;

import de.MarkusTieger.common.tac.bridge.ITACBridge;
import de.MarkusTieger.tac.bridge.exceptions.TACAlreadyEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.TACIOException;
import de.MarkusTieger.tac.bridge.exceptions.TACMultiClientException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotAktiveException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderAlreadyEnabled;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderNotEnabled;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;

public class UninstalledTACBridge implements ITACBridge {

	@Override
	public String getLastDetection() {
		return null;
	}

	@Override
	public String detect() throws TACAlreadyEnabledException, TACIOException {
		throw new TACIOException("TAC is not installed.");
	}

	@Override
	public String enable()
			throws TACAlreadyEnabledException, TACNotAktiveException, TACIOException, TACMultiClientException {

		throw new TACIOException("TAC is not installed.");
	}

	@Override
	public void enableReader() throws TACNotEnabledException, TACReaderAlreadyEnabled {
		throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);
	}

	@Override
	public boolean isReaderEnabled() {
		return false;
	}

	@Override
	public void disableReader() throws TACNotEnabledException, TACReaderNotEnabled {
		throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);
	}

	@Override
	public void clean() throws TACAlreadyEnabledException {
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void disable() throws TACNotEnabledException, TACIOException {
		throw new TACIOException("TAC is not installed.");
	}

	@Override
	public Optional<ResolvedServerAddress> resolve(String ip) throws TACNotEnabledException {
		return Optional.empty();
	}

}
