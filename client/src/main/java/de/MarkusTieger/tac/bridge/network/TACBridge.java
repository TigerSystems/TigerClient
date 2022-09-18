package de.MarkusTieger.tac.bridge.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.tac.bridge.ITACBridge;
import de.MarkusTieger.tac.bridge.exceptions.TACAlreadyEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.TACIOException;
import de.MarkusTieger.tac.bridge.exceptions.TACMultiClientException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotAktiveException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderAlreadyEnabled;
import de.MarkusTieger.tac.bridge.exceptions.reader.TACReaderNotEnabled;
import de.MarkusTieger.tac.bridge.request.RequestBuilder;
import de.MarkusTieger.tac.bridge.request.RequestObject;
import de.MarkusTieger.tac.core.dns.TACResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;

public class TACBridge implements ITACBridge {

	private static final int PORT = 47653;
	private static final Gson GSON = new GsonBuilder().create();

	private boolean ready = false;

	private Socket client = null;

	private PrintStream writer = null;
	private BufferedReader reader = null;

	private Thread readerThread = null;

	private String lastDetection = null;

	@Override
	public String getLastDetection() {
		return lastDetection;
	}

	@Override
	public String detect() throws TACAlreadyEnabledException, TACIOException {
		try {
			String version = enable();
			try {
				disable();
			} catch (TACNotEnabledException e) {
			}
			return lastDetection = version;
		} catch (TACNotAktiveException e) {
			return lastDetection = null;
		} catch (TACMultiClientException e) {
			return lastDetection = "Unknown";
		}
	}

	@Override
	public String enable()
			throws TACAlreadyEnabledException, TACNotAktiveException, TACIOException, TACMultiClientException {
		if (isEnabled())
			throw new TACAlreadyEnabledException(TACAlreadyEnabledException.DEFAULT);

		clean();

		try {
			client = new Socket("127.0.0.1", PORT);
		} catch (Throwable e) {
			throw new TACNotAktiveException(TACNotAktiveException.DEFAULT, e);
		}

		try {
			writer = new PrintStream(client.getOutputStream(), true, StandardCharsets.UTF_8);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			cleanException();
			throw new TACIOException(e);
		}

		String version = "Unknown";

		writer.println(GSON.toJson(RequestBuilder.create().setRequest("enable").build()));

		try {
			String str = null;
			while ((str = reader.readLine()) != null) {

				RequestObject obj = RequestBuilder.create().importJson(GSON.fromJson(str, JsonElement.class)).build();
				if (obj.getRequest().equalsIgnoreCase("status")) {
					if (!obj.getArgs().containsKey("value"))
						continue;
					version = obj.getArgs().getOrDefault("version", "Unknown") + "";
					if ((obj.getArgs().get("value") + "").equalsIgnoreCase("ready")) {
						break;
					} else if ((obj.getArgs().get("value") + "").equalsIgnoreCase("multi-connection")) {
						throw new TACMultiClientException(TACMultiClientException.DEFAULT);
					}
				}

			}
		} catch (IOException e) {
			cleanException();
			throw new TACIOException(e);
		} catch (TACMultiClientException e) {
			cleanException();
			throw e;
		}

		ready = true;

		try {
			enableReader();
		} catch (TACNotEnabledException e) {
			throw new TACIOException(e);
		} catch (TACReaderAlreadyEnabled e) {
			throw new TACIOException(e);
		}

		return version;
	}

	private void cleanException() {
		ready = false;
	}

	private final HashMap<String, Optional<TACResolvedServerAddress>> address = new HashMap<>();

	@Override
	public void enableReader() throws TACNotEnabledException, TACReaderAlreadyEnabled {
		if (!isEnabled())
			throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);
		if (readerThread != null && readerThread.isAlive())
			throw new TACReaderAlreadyEnabled(TACReaderAlreadyEnabled.DEFAULT);
		readerThread = new Thread(() -> {
			try {
				String str = null;
				while (isEnabled() && isReaderEnabled() && (str = reader.readLine()) != null) {
					try {
						RequestObject obj = RequestBuilder.create().importJson(GSON.fromJson(str, JsonElement.class))
								.build();
						if (obj.getRequest().equalsIgnoreCase("address")) {
							boolean present = obj.getArgs().getOrDefault("present", "false").toString()
									.equalsIgnoreCase("true");
							String ip = obj.getArgs().getOrDefault("request_ip", "__null__").toString();
							if (present) {
								address.put(ip, Optional.of(new TACResolvedServerAddress() {
									@Override
									public boolean isAllowed() {
										return obj.getArgs().getOrDefault("allowed", "true").toString()
												.equalsIgnoreCase("true");
									}

									@Override
									public boolean isTrusted() {
										return obj.getArgs().getOrDefault("trusted", "false").toString()
												.equalsIgnoreCase("true");
									}

									@Override
									public String getHostName() {
										return obj.getArgs().getOrDefault("host_name", "localhost").toString();
									}

									@Override
									public String getHostIp() {
										return obj.getArgs().getOrDefault("host_ip", "127.0.0.1").toString();
									}

									@Override
									public int getPort() {
										try {
											return Integer
													.parseInt(obj.getArgs().getOrDefault("port", "25565").toString());
										} catch (NumberFormatException e) {
										}
										return 25565;
									}

									@Override
									public InetSocketAddress asInetSocketAddress() {
										return null;
									}
								}));
							} else {
								address.put(ip, Optional.empty());
							}
						}
					} catch (Throwable e) {
						Client.getInstance().getLogger().warn(LoggingCategory.TAC, e);
					}
				}
			} catch (IOException e) {
				if (isEnabled()) {
					e.printStackTrace();
				}
			}
		}, "Reader");
		readerThread.start();
	}

	@Override
	public boolean isReaderEnabled() {
		return (readerThread != null && readerThread.isAlive());
	}

	@Override
	public void disableReader() throws TACNotEnabledException, TACReaderNotEnabled {
		if (!isEnabled())
			throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);
		if (readerThread == null || !readerThread.isAlive())
			throw new TACReaderNotEnabled(TACReaderNotEnabled.DEFAULT);
		readerThread = null;
	}

	@Override
	public void clean() throws TACAlreadyEnabledException {
		if (isEnabled())
			throw new TACAlreadyEnabledException(TACAlreadyEnabledException.DEFAULT);

		client = null;
		reader = null;
		writer = null;

		readerThread = null;
		ready = false;
		address.clear();
	}

	@Override
	public boolean isEnabled() {
		if (client == null || client.isClosed() || !ready)
			return false;
		return true;
	}

	@Override
	public void disable() throws TACNotEnabledException, TACIOException {
		if (!isEnabled())
			throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);

		if (isReaderEnabled()) {
			try {
				disableReader();
			} catch (TACReaderNotEnabled e) {
				throw new TACIOException(e);
			}
		}

		ready = false;

		writer.println(GSON.toJson(RequestBuilder.create().setRequest("disable").build()));
		writer.close();

		try {
			reader.close();
		} catch (IOException e) {
		}

		try {
			client.close();
		} catch (IOException e) {
		}
	}

	@Override
	public Optional<ResolvedServerAddress> resolve(String ip) throws TACNotEnabledException {
		if (!isEnabled())
			throw new TACNotEnabledException(TACNotEnabledException.DEFAULT);

		if (address.containsKey(ip.toLowerCase())) {
			if (!address.get(ip.toLowerCase()).isPresent())
				return Optional.empty();
			address.remove(ip.toLowerCase());
		}

		writer.println(GSON.toJson(RequestBuilder.create().setRequest("resolve").addArgument("address", ip).build()));

		while (true) {
			if (address.containsKey(ip.toLowerCase())) {
				return address.get(ip.toLowerCase()).map(TACResolvedServerAddress::toNormal);
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
