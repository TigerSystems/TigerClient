package de.MarkusTieger.tac.gui.screen;

import java.awt.Point;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.tac.connect.listener.TACClientHandshakePacketListenerImpl;
import de.MarkusTieger.tac.core.dns.TACResolvedServerAddress;
import de.MarkusTieger.tac.gui.widgets.connect.TACConnectWidget;
import de.MarkusTieger.tac.gui.widgets.connect.registry.PointRegistry;
import de.MarkusTieger.tac.gui.widgets.connect.renderer.IconRenderer;
import de.MarkusTieger.tac.gui.widgets.connect.renderer.LineRenderer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;

public class TACConnectScreen extends Screen {

	private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
	@SuppressWarnings("unused")
	private static final long NARRATION_DELAY_MS = 2000L;
	public static final Component UNKNOWN_HOST_MESSAGE = new TranslatableComponent("disconnect.genericReason",
			new TranslatableComponent("disconnect.unknownHost"));
	volatile Connection connection;
	volatile boolean aborted;
	final Screen parent;
	private ConnectState status;
	private boolean initialized = false, connected = false;
	private long lastNarration = -1L;
	private final ServerData data;

	private TACConnectScreen(Screen p_169263_, ServerData data) {
		super(NarratorChatListener.NO_TITLE);
		this.parent = p_169263_;
		this.data = data;
	}

	public static TACConnectScreen startConnecting(Screen p_169268_, Minecraft p_169269_, ServerData p_169271_) {
		final TACConnectScreen connectscreen = new TACConnectScreen(p_169268_, p_169271_);
		p_169269_.clearLevel();
		p_169269_.setCurrentServer(p_169271_);
		return connectscreen;
	}

	public void startConnect() {
		if (!initialized)
			return;
		if (!connected) {
			this.connect(minecraft, data, false);
			connected = true;
		}
	}

	private void connect(final Minecraft p_169265_, final ServerData p_169266_, final boolean confirmed) {
		Client.getInstance().getLogger().debug(LoggingCategory.TAC, "Connecting to " + p_169266_.ip + " using TAC");
		Thread thread = new Thread("TAC-Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet()) {
			@Override
			public void run() {
				InetSocketAddress inetsocketaddress = null;

				try {

					updateStatus(ConnectState.RESOLVE);

					if (TACConnectScreen.this.aborted) {
						updateStatus(ConnectState.CANCELED);
						return;
					}

					Optional<ResolvedServerAddress> resolved = Optional.empty();

					if (Client.getInstance().getTAC().isEnabled()) {
						resolved = Client.getInstance().getTAC().resolve(p_169266_.ip);
					} else {
						resolved = ServerNameResolver.DEFAULT.resolveAddress(ServerAddress.parseString(p_169266_.ip));
					}

					if (TACConnectScreen.this.aborted) {
						updateStatus(ConnectState.CANCELED);
						return;
					}

					if (!confirmed && Client.getInstance().getTAC().isEnabled()
							&& resolved.get() instanceof TACResolvedServerAddress tac) {
						if (!tac.isAllowed()) {
							updateStatus(ConnectState.CONFIRM);
							ConfirmScreen screen = new ConfirmScreen((b) -> {
								if (b) {
									p_169265_.setScreen(TACConnectScreen.this);
									connect(p_169265_, p_169266_, true);
								} else {
									aborted = true;
									updateStatus(ConnectState.CANCELED);
									minecraft.setScreen(parent);
								}
							}, new TranslatableComponent("tac.confirm.connect.title"),
									new TranslatableComponent("tac.confirm.connect.warning"));
							p_169265_.execute(() -> p_169265_.setScreen(screen));
							return;
						}

					}

					Optional<InetSocketAddress> optional = resolved.map(ResolvedServerAddress::asInetSocketAddress);
					if (TACConnectScreen.this.aborted) {
						updateStatus(ConnectState.CANCELED);
						return;
					}

					if (!optional.isPresent()) {
						p_169265_.execute(() -> {
							p_169265_.setScreen(new TACDisconnectScreen(TACConnectScreen.this.parent,
									CommonComponents.CONNECT_FAILED, TACConnectScreen.UNKNOWN_HOST_MESSAGE,
									TACDisconnectScreen.DisconnectReason.UNKNOWN_HOST));
						});
						return;
					}

					updateStatus(ConnectState.CONNECT);

					inetsocketaddress = optional.get();
					TACConnectScreen.this.connection = Connection.connectToServer(inetsocketaddress,
							p_169265_.options.useNativeTransport());
					TACConnectScreen.this.connection
							.setListener(new TACClientHandshakePacketListenerImpl(TACConnectScreen.this.connection,
									p_169265_, TACConnectScreen.this.parent, TACConnectScreen.this::updateStatus));
					TACConnectScreen.this.connection.send(new ClientIntentionPacket(inetsocketaddress.getHostName(),
							inetsocketaddress.getPort(), ConnectionProtocol.LOGIN));
					TACConnectScreen.this.connection
							.send(new ServerboundHelloPacket(p_169265_.getUser().getGameProfile()));
				} catch (Exception exception) {
					if (TACConnectScreen.this.aborted) {
						return;
					}

					Client.getInstance().getLogger().error(LoggingCategory.TAC, "Couldn't connect to server",
							exception);
					String s = inetsocketaddress == null ? exception.toString()
							: exception.toString().replaceAll(
									inetsocketaddress.getHostName() + ":" + inetsocketaddress.getPort(), "");
					p_169265_.execute(() -> {
						p_169265_.setScreen(
								new DisconnectedScreen(TACConnectScreen.this.parent, CommonComponents.CONNECT_FAILED,
										new TranslatableComponent("disconnect.genericReason", s)));
					});
				}

			}
		};
		thread.setUncaughtExceptionHandler((th, tw) -> Client.getInstance().getLogger().error(LoggingCategory.TAC,
				"Caught previously unhandled exception", tw));
		thread.start();
	}

	private TACConnectWidget widget;
	private LineRenderer lineRenderer;
	private IconRenderer iconRenderer;

	private void updateStatus(ConnectState p_95718_) {
		this.status = p_95718_;
		lineRenderer.setMinecraft(p_95718_.getMinecraft());
		lineRenderer.setMojang(p_95718_.getMojang());
		lineRenderer.setServer(p_95718_.getServer());
	}

	@Override
	public void tick() {
		if (this.connection != null) {
			if (this.connection.isConnected()) {
				this.connection.tick();
			} else {
				this.connection.handleDisconnection();
			}
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	protected void init() {
		int y = this.height / 4 + 120;
		if (initialized) {
			widget.updateSize(y);
		} else {
			initialized = true;

			Point size = new Point(1024, 512);
			Point mjSize = new Point(700, 255);

			final int width = size.x; // 1024
			final int height = size.y; // 512

			int mjWidth = mjSize.x;
			int mjHeight = mjSize.y;

			int basic = height / 8;

			int y1 = ((height - ((basic * 2) + (basic)))) + basic; // auto-created

			Point mcPoint = new Point(basic * 2, y1);
			Point itPoint = new Point((int) (basic * 5.85), y1);
			Point mjPoint = new Point(((width / 2) - (mjWidth / 8)) + (mjWidth / 4), basic + (mjHeight / 4));
			Point svPoint = new Point((width - (basic * 3)) + basic, y1);

			Point itmjConnect = new Point(itPoint.x, mjPoint.y);

			PointRegistry registry = new PointRegistry();

			registry.register("size", size);
			registry.register("mjSize", mjSize);
			registry.register("mc", mcPoint);
			registry.register("it", itPoint);
			registry.register("mj", mjPoint);
			registry.register("sv", svPoint);
			registry.register("itmj", itmjConnect);

			widget = new TACConnectWidget(this, y);

			lineRenderer = new LineRenderer(widget, registry, false);
			iconRenderer = new IconRenderer(widget, registry);

			widget.addRenderer(lineRenderer);
			widget.addRenderer(iconRenderer);

			Client.getInstance().addHighTickable(lineRenderer);

			updateStatus(ConnectState.WAIT);

			startConnect();
		}
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20,
				CommonComponents.GUI_CANCEL, (p_95705_) -> {
					this.aborted = true;
					if (this.connection != null) {
						this.connection.disconnect(new TranslatableComponent("connect.aborted"));
					}

					this.minecraft.setScreen(this.parent);
				}));
	}

	@Override
	public void render(PoseStack stack, int p_95701_, int p_95702_, float p_95703_) {
		this.renderBackground(stack);
		long i = Util.getMillis();
		if (i - this.lastNarration > 2000L) {
			this.lastNarration = i;
			NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.joining"));
		}

		widget.render(stack, p_95701_, p_95702_, p_95703_);

		drawCenteredString(stack, this.font, this.status.getTxt(), this.width / 2, widget.getY() - 24, 16777215);

		// drawCenteredString(p_95700_, this.font, "TAC", this.width / 2, this.height /
		// 2 - 50, 16777215);

		super.render(stack, p_95701_, p_95702_, p_95703_);
	}

	@Override
	public void removed() {
		Client.getInstance().removeHighTickable(lineRenderer);
	}

	public enum ConnectState {

		WAIT(new TranslatableComponent("tac.waitConnect"), false, false, false),
		RESOLVE(new TranslatableComponent("tac.resolve"), true, false, false),

		KEY(new TranslatableComponent("tac.key.generate"), false, false, false),

		CONFIRM(new TranslatableComponent("tac.waitConfirm"), false, false, false),
		CANCELED(new TranslatableComponent("tac.canceled"), false, false, false),
		CONNECT(new TranslatableComponent("connect.connecting"), true, false, true),
		JOIN(new TranslatableComponent("connect.joining"), true, false, true),
		NEGOTIATING(new TranslatableComponent("connect.negotiating"), true, false, true),
		AUTHORIZING(new TranslatableComponent("connect.authorizing"), true, true, true),
		ENCRYPTING(new TranslatableComponent("connect.encrypting"), true, true, true);

		private final Component txt;
		private final boolean mc, mj, sv;

		private ConnectState(Component txt, boolean mc, boolean mj, boolean sv) {
			this.txt = txt;
			this.mc = mc;
			this.mj = mj;
			this.sv = sv;
		}

		public Component getTxt() {
			return txt;
		}

		public boolean getMinecraft() {
			return mc;
		}

		public boolean getMojang() {
			return mj;
		}

		public boolean getServer() {
			return sv;
		}
	}

}
