package de.MarkusTieger.tac.server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.tac.bridge.ITACBridge;
import de.MarkusTieger.tac.bridge.exceptions.TACNotEnabledException;
import de.MarkusTieger.tac.core.dns.TACResolvedServerAddress;
import de.MarkusTieger.tac.manipulators.ServerManipulator;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.logger.handler.TCUncaughtExceptionHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class TACServerSelectionList extends ServerSelectionList {

	private static final ThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(5,
			(new ThreadFactoryBuilder()).setNameFormat("TAC Server Pinger #%d").setDaemon(true)
					.setUncaughtExceptionHandler(new TCUncaughtExceptionHandler()).build());
	private static final Component CANT_RESOLVE_TEXT = (new TranslatableComponent("multiplayer.status.cannot_resolve"))
			.withStyle(ChatFormatting.DARK_RED);
	private static final Component CANT_CONNECT_TEXT = (new TranslatableComponent("multiplayer.status.cannot_connect"))
			.withStyle(ChatFormatting.DARK_RED);
	private static final ResourceLocation LOGO = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/tac/logo.png");
	private static final ResourceLocation BLOCKED = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/tac/blocked.png");
	private final JoinMultiplayerScreen screen;
	private final ServerManipulator manipulator;
	private final HashMap<String, Optional<ResolvedServerAddress>> resolver = new HashMap<>();

	public TACServerSelectionList(ServerManipulator manipulator, JoinMultiplayerScreen p_99771_, Minecraft p_99772_,
			int p_99773_, int p_99774_, int p_99775_, int p_99776_, int p_99777_) {
		super(p_99771_, p_99772_, p_99773_, p_99774_, p_99775_, p_99776_, p_99777_);
		this.manipulator = manipulator;
		this.screen = p_99771_;
	}

	@Override
	protected void renderList(PoseStack p_93452_, int p_93453_, int p_93454_, int p_93455_, int p_93456_,
			float p_93457_) {

		int i = this.getItemCount();

		for (int j = 0; j < i; ++j) {

			ServerSelectionList.Entry e = this.getEntry(j);

			if (e instanceof OnlineServerEntry ose) {
				final ServerData serverData = ose.getServerData();
				if (!serverData.pinged) {
					serverData.pinged = true;
					serverData.ping = -2L;
					serverData.motd = TextComponent.EMPTY;
					serverData.status = TextComponent.EMPTY;
					THREAD_POOL.submit(() -> {
						try {
							ITACBridge.PINGER.pingServer(serverData, () -> {
								this.minecraft.execute(screen.getServers()::save);
							});
						} catch (UnknownHostException unknownhostexception) {
							serverData.ping = -1L;
							serverData.motd = CANT_RESOLVE_TEXT;
						} catch (Exception exception) {
							serverData.ping = -1L;
							serverData.motd = CANT_CONNECT_TEXT;
						}

					});
				}
			}

		}

		super.renderList(p_93452_, p_93453_, p_93454_, p_93455_, p_93456_, p_93457_);

		for (int j = 0; j < i; ++j) {
			int k = this.getRowTop(j);
			int l = this.getRowBottom(j);
			if (l >= this.y0 && k <= this.y1) {
				@SuppressWarnings("unused")
				int i1 = p_93454_ + j * this.itemHeight + this.headerHeight;
				int j1 = this.itemHeight - 4;
				ServerSelectionList.Entry e = this.getEntry(j);
				if (!(e instanceof OnlineServerEntry ose))
					continue;
				int k1 = this.getRowWidth();
				int j2 = this.getRowLeft();
				renderItem(p_93452_, j, k, j2, k1, j1, p_93455_, p_93456_, Objects.equals(this.getHovered(), e),
						p_93457_, ose);
			}
		}
	}

	private void renderItem(PoseStack p_99879_, int p_99880_, int p_99881_, int p_99882_, int p_99883_, int p_99884_,
			int p_99885_, int p_99886_, boolean p_99887_, float p_99888_, OnlineServerEntry e) {

		int i1 = p_99885_ - p_99882_;
		int i2 = p_99886_ - p_99881_;

		if (!manipulator.mod
				&& Client.getInstance().getCompatiblityExecutor().checkTooltip(p_99883_, p_99884_, i1, i2)) {
			screen.setToolTip(null);
		}

		Optional<ResolvedServerAddress> addr = resolver.get(e.getServerData().ip.toLowerCase());
		if (addr == null) {
			if (Client.getInstance().getTAC().isEnabled()) {
				try {
					addr = Client.getInstance().getTAC().resolve(e.getServerData().ip);
				} catch (TACNotEnabledException ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.TAC, ex);
				}
			} else {
				addr = ServerNameResolver.DEFAULT.resolveAddress(ServerAddress.parseString(e.getServerData().ip));
			}
			resolver.put(e.getServerData().ip.toLowerCase(), addr);
		}

		if (!manipulator.mod && addr.isPresent() && addr.get() instanceof TACResolvedServerAddress tresolved
				&& tresolved.isTrusted()) {
			RenderSystem.setShaderTexture(0, LOGO);
			// GuiComponent.blit(p_99879_, p_99882_ + p_99883_ - 18, p_99881_ + 18 /* +10
			// */, 28 /* 16 */, 8 /* 16 */, 0, 0 /* idx */, 425 /* 16 */, 138 /* 16 */, 425,
			// 138);

			// x, y, tx, ty, w, h, tw, th
			// param1Int3 + param1Int4 - 36 - 5, param1Int2 + 20, 0.0F, 0.0F, 36, 12, 36.0F,
			// 12.0F
			// GuiComponent.blit(p_99879_, p_99880_ + p_99883_ - 36 - 5, p_99882_ + 20,
			// 0.0F, 0.0F, 36, 12, 36, 12);

			// TigerGuiUtils.renderTexture(p_99879_, p_99880_ + p_99883_ - 36 - 5, p_99882_
			// + 20, 0, 0, 36, 12, 36, 12);
			TigerGuiUtils.renderTexture(p_99879_, p_99882_ + p_99883_ - 36 - 5, p_99881_ + 20, 0, 0, 36, 12, 36, 12);

			int mouseX = p_99885_;
			int mouseY = p_99886_;

			if (mouseX > p_99882_ + p_99883_ - 36 - 5 && mouseY > p_99881_ + 20 && mouseX < p_99882_ + p_99883_ - 5
					&& mouseY < p_99881_ + 20 + 12) {
				ArrayList<Component> tooltip = new ArrayList<>();
				tooltip.add(new TextComponent(ChatFormatting.DARK_RED + "Tiger AntiCheat"));
				screen.setToolTip(tooltip);
			}
		} else if (!manipulator.mod && addr.isPresent()
				&& ((addr.get() instanceof TACResolvedServerAddress tresolved) && !tresolved.isAllowed())) {

			RenderSystem.setShaderTexture(0, BLOCKED);
			// GuiComponent.blit(p_99879_, p_99882_ + p_99883_ - 18, p_99881_ + 18 /* +10
			// */, 28 /* 16 */, 8 /* 16 */, 0, 0 /* idx */, 425 /* 16 */, 138 /* 16 */, 425,
			// 138);

			// x, y, tx, ty, w, h, tw, th
			// param1Int3 + param1Int4 - 36 - 5, param1Int2 + 20, 0.0F, 0.0F, 36, 12, 36.0F,
			// 12.0F
			// GuiComponent.blit(p_99879_, p_99880_ + p_99883_ - 36 - 5, p_99882_ + 20,
			// 0.0F, 0.0F, 36, 12, 36, 12);

			// TigerGuiUtils.renderTexture(p_99879_, p_99880_ + p_99883_ - 36 - 5, p_99882_
			// + 20, 0, 0, 36, 12, 36, 12);
			TigerGuiUtils.renderTexture(p_99879_, p_99882_ + p_99883_ - 36 - 5, p_99881_ + 20, 0, 0, 36, 12, 36, 12);

			int mouseX = p_99885_;
			int mouseY = p_99886_;

			if (mouseX > p_99882_ + p_99883_ - 36 - 5 && mouseY > p_99881_ + 20 && mouseX < p_99882_ + p_99883_ - 5
					&& mouseY < p_99881_ + 20 + 12) {
				ArrayList<Component> tooltip = new ArrayList<>();
				tooltip.add(new TextComponent(ChatFormatting.DARK_RED + "Blocked!"));
				screen.setToolTip(tooltip);
			}

		}

		// if (bool4 &&
		// i1 > p_99882_ + p_99883_ - 36 - 5 && i2 > p_99881_ + 20 && i1 < p_99882_ +
		// p_99883_ - 5 && i2 < p_99881_ + 20 + 12)
	}

	public int getRowBottom(int p_93486_) {
		return this.getRowTop(p_93486_) + this.itemHeight;
	}
}
