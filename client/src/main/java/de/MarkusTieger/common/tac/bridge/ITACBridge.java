package de.MarkusTieger.common.tac.bridge;

import java.util.Optional;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.tac.bridge.exceptions.TACAlreadyEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.TACIOException;
import de.MarkusTieger.tac.bridge.exceptions.TACMultiClientException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotAktiveException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderAlreadyEnabled;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderNotEnabled;
import de.MarkusTieger.tac.pinger.TACServerPinger;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;

@NoObfuscation
public interface ITACBridge {

	String getLastDetection();

	String detect() throws TACAlreadyEnabledException, TACIOException;

	String enable() throws TACAlreadyEnabledException, TACNotAktiveException, TACIOException, TACMultiClientException;

	void enableReader() throws TACNotEnabledException, TACReaderAlreadyEnabled;

	boolean isReaderEnabled();

	void disableReader() throws TACNotEnabledException, TACReaderNotEnabled;

	void clean() throws TACAlreadyEnabledException;

	boolean isEnabled();

	void disable() throws TACNotEnabledException, TACIOException;

	Optional<ResolvedServerAddress> resolve(String ip) throws TACNotEnabledException;

	static TACServerPinger PINGER = new TACServerPinger();
}
