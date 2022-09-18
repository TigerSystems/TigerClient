package de.MarkusTieger.tac.connect.listener;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.function.Consumer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.util.Pair;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.tac.gui.screen.TACConnectScreen;
import de.MarkusTieger.tac.gui.screen.TACDisconnectScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.KeybindComponent;
import net.minecraft.network.chat.NbtComponent;
import net.minecraft.network.chat.ScoreComponent;
import net.minecraft.network.chat.SelectorComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.util.HttpUtil;

public class TACClientHandshakePacketListenerImpl extends ClientHandshakePacketListenerImpl {

	private final Consumer<TACConnectScreen.ConnectState> update;
	private final Connection connection;
	private final Minecraft minecraft;
	private final Screen parent;
	private GameProfile localGameProfile;

	public TACClientHandshakePacketListenerImpl(Connection p_104526_, Minecraft p_104527_, Screen p_104528_,
			Consumer<TACConnectScreen.ConnectState> p_104529_) {
		super(p_104526_, p_104527_, p_104528_, (comp) -> {
		});
		this.connection = p_104526_;
		this.minecraft = p_104527_;
		this.parent = p_104528_;
		this.update = p_104529_;
	}

	@Override
	public void handleHello(ClientboundHelloPacket p_104549_) {
		Cipher cipher;
		Cipher cipher1;
		String s;
		ServerboundKeyPacket serverboundkeypacket;
		try {
			SecretKey secretkey = Crypt.generateSecretKey();
			PublicKey publickey = p_104549_.getPublicKey();
			s = (new BigInteger(Crypt.digestData(p_104549_.getServerId(), publickey, secretkey))).toString(16);
			cipher = Crypt.getCipher(2, secretkey);
			cipher1 = Crypt.getCipher(1, secretkey);
			serverboundkeypacket = new ServerboundKeyPacket(secretkey, publickey, p_104549_.getNonce());
		} catch (CryptException cryptexception) {
			throw new IllegalStateException("Protocol error", cryptexception);
		}

		this.update.accept(TACConnectScreen.ConnectState.AUTHORIZING);
		HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
			Pair<TACDisconnectScreen.DisconnectReason, Object[]> component = this.authenticateServer(s);
			if (component != null) {
				if (this.minecraft.getCurrentServer() == null || !this.minecraft.getCurrentServer().isLan()) {
					this.connection.disconnect(component.getFirst().getTxt(component.getSecond()));
					return;
				}

				Client.getInstance().getLogger().warn(LoggingCategory.TAC,
						component.getFirst().getTxt(component.getSecond()).getString());
			}

			this.update.accept(TACConnectScreen.ConnectState.ENCRYPTING);
			this.connection.send(serverboundkeypacket, (p_171627_) -> {
				this.connection.setEncryptionKey(cipher, cipher1);
			});
		});
	}

	private Pair<TACDisconnectScreen.DisconnectReason, Object[]> authenticateServer(String p_104532_) {
		try {
			this.getMinecraftSessionService().joinServer(Client.getInstance().getMCUser().getGameProfile(),
					Client.getInstance().getMCUser().getAccessToken(), p_104532_);
			return null;
		} catch (AuthenticationUnavailableException authenticationunavailableexception) {
			return Pair.of(TACDisconnectScreen.DisconnectReason.LOGIN_FAILED_SERVERS_UNAVAILABLE, from());
		} catch (InvalidCredentialsException invalidcredentialsexception) {
			return Pair.of(TACDisconnectScreen.DisconnectReason.LOGIN_FAILED_INVALID_SESSION, from());
		} catch (InsufficientPrivilegesException insufficientprivilegesexception) {
			return Pair.of(TACDisconnectScreen.DisconnectReason.LOGIN_FAILED_INSUFFICIENT_PREVILEGS, from());
		} catch (AuthenticationException authenticationexception) {
			return Pair.of(TACDisconnectScreen.DisconnectReason.LOGIN_FAILED_INFO,
					from(authenticationexception.getMessage()));
		}
	}

	private Object[] from(Object... args) {
		return args;
	}

	private MinecraftSessionService getMinecraftSessionService() {
		return this.minecraft.getMinecraftSessionService();
	}

	@Override
	public void handleGameProfile(ClientboundGameProfilePacket p_104547_) {
		this.update.accept(TACConnectScreen.ConnectState.JOIN);
		this.localGameProfile = p_104547_.getGameProfile();
		this.connection.setProtocol(ConnectionProtocol.PLAY);
		Client.getInstance().getCompatiblityExecutor().onClientLoginSuccess(this.connection);
		this.connection.setListener(new TACClientPacketListener(this.minecraft, this.parent, this.connection,
				this.localGameProfile, this.minecraft.createTelemetryManager()));
	}

	@Override
	public void onDisconnect(Component component) {
		TACDisconnectScreen.DisconnectReason r = null;
		for (TACDisconnectScreen.DisconnectReason reason : TACDisconnectScreen.DisconnectReason.values()) {
			if (reason.isIdentical()) {
				if (component.equals(reason)) {
					r = reason;
					break;
				}
			} else {
				if (simpleCheck(component, reason.getTxt())) {
					r = reason;
					break;
				}
			}
		}
		if (r == null) {
			r = TACDisconnectScreen.DisconnectReason.GENERIC;
		}
		if (this.parent != null && this.parent instanceof RealmsScreen) {
			this.minecraft
					.setScreen(new DisconnectedRealmsScreen(this.parent, CommonComponents.CONNECT_FAILED, component));
		} else {
			this.minecraft
					.setScreen(new TACDisconnectScreen(this.parent, CommonComponents.CONNECT_FAILED, component, r));
		}
	}

	private boolean simpleCheck(Component comp1, Component comp2) {
		if (comp1 instanceof TranslatableComponent ocomp1 && comp2 instanceof TranslatableComponent ocomp2) {
			return ocomp1.getKey().equalsIgnoreCase(ocomp2.getKey());
		}
		if (comp1 instanceof TextComponent ocomp1 && comp2 instanceof TextComponent ocomp2) {
			return ocomp1.getText().equalsIgnoreCase(ocomp2.getText());
		}
		if (comp1 instanceof ScoreComponent ocomp1 && comp2 instanceof ScoreComponent ocomp2) {
			return ocomp1.getObjective().equalsIgnoreCase(ocomp2.getObjective())
					&& ocomp1.getSelector().equals(ocomp2.getSelector());
		}
		if (comp1 instanceof KeybindComponent ocomp1 && comp2 instanceof KeybindComponent ocomp2) {
			return ocomp1.getName().equalsIgnoreCase(ocomp2.getName());
		}
		if (comp1 instanceof SelectorComponent ocomp1 && comp2 instanceof SelectorComponent ocomp2) {
			return ocomp1.getSelector().equals(ocomp2.getSelector())
					&& ocomp1.getPattern().equalsIgnoreCase(ocomp2.getPattern())
					&& ocomp1.getSeparator().equals(ocomp2.getSeparator());
		}
		if (comp1 instanceof NbtComponent ocomp1 && comp2 instanceof NbtComponent ocomp2) {
			return ocomp1.getNbtPath().equalsIgnoreCase(ocomp2.getNbtPath())
					&& ocomp1.isInterpreting() == ocomp2.isInterpreting();
		}
		return false;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public void handleDisconnect(ClientboundLoginDisconnectPacket p_104553_) {
		this.connection.disconnect(p_104553_.getReason());
	}

	@Override
	public void handleCompression(ClientboundLoginCompressionPacket p_104551_) {
		if (!this.connection.isMemoryConnection()) {
			this.connection.setupCompression(p_104551_.getCompressionThreshold(), false);
		}

	}

	@Override
	public void handleCustomQuery(ClientboundCustomQueryPacket p_104545_) {
		if (Client.getInstance().getCompatiblityExecutor().onCustomPayload(p_104545_, this.connection))
			return;
		this.update.accept(TACConnectScreen.ConnectState.NEGOTIATING);
		this.connection.send(new ServerboundCustomQueryPacket(p_104545_.getTransactionId(), (FriendlyByteBuf) null));
	}
}
