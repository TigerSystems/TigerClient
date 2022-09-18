package de.MarkusTieger.tac.gui.screen;

import java.awt.Point;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.tac.gui.widgets.connect.TACConnectWidget;
import de.MarkusTieger.tac.gui.widgets.connect.registry.PointRegistry;
import de.MarkusTieger.tac.gui.widgets.connect.renderer.IconRenderer;
import de.MarkusTieger.tac.gui.widgets.connect.renderer.LineRenderer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TACDisconnectScreen extends Screen {

	private final Component reason;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private final Screen parent;
	private int textHeight;
	private final DisconnectReason tac_reason;

	public TACDisconnectScreen(Screen parent, Component connectFailed, Component component, DisconnectReason r) {
		super(connectFailed);
		this.parent = parent;
		this.reason = component;
		this.tac_reason = r;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	private TACConnectWidget widget;
	private LineRenderer lineRenderer;
	private IconRenderer iconRenderer;
	@SuppressWarnings("unused")
	private boolean initialized = false, connected = false;

	@Override
	protected void init() {
		this.message = MultiLineLabel.create(this.font, this.reason, this.width - 50);
		this.textHeight = this.message.getLineCount() * 9;

		int bHeight = Math.min(this.height / 4 + this.textHeight / 2 + 9, this.height - 30);

		if (initialized) {
			bHeight += widget.getHeight();
			int y = bHeight - 12;
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

			int y = bHeight - 12;

			widget = new TACConnectWidget(this, y);

			bHeight += widget.getHeight();
			y = bHeight - 12;
			widget.updateSize(y);

			lineRenderer = new LineRenderer(widget, registry, true);
			iconRenderer = new IconRenderer(widget, registry);

			lineRenderer.setMinecraft(tac_reason.isMinecraft());
			lineRenderer.setMojang(tac_reason.isMojang());
			lineRenderer.setServer(tac_reason.isServer());

			widget.addRenderer(lineRenderer);
			widget.addRenderer(iconRenderer);

		}

		this.addRenderableWidget(new Button(this.width / 2 - 100, bHeight, 200, 20,
				new TranslatableComponent("gui.toMenu"), (p_96002_) -> {
					this.minecraft.setScreen(this.parent);
				}));
	}

	@Override
	public void render(PoseStack p_95997_, int p_95998_, int p_95999_, float p_96000_) {
		this.renderBackground(p_95997_);

		widget.render(p_95997_, p_95998_, p_95999_, p_96000_);

		drawCenteredString(p_95997_, this.font, this.title, this.width / 2,
				this.height / 4 - this.textHeight / 2 - 9 * 2, 11184810);
		this.message.renderCentered(p_95997_, this.width / 2, this.height / 4 - this.textHeight / 2);
		super.render(p_95997_, p_95998_, p_95999_, p_96000_);
	}

	public enum DisconnectReason {

		LOGIN_FAILED_SERVERS_UNAVAILABLE(
				new TranslatableComponent("disconnect.loginFailedInfo",
						new TranslatableComponent("disconnect.loginFailedInfo.serversUnavailable")),
				true, false, true, false),
		LOGIN_FAILED_INVALID_SESSION(
				new TranslatableComponent("disconnect.loginFailedInfo",
						new TranslatableComponent("disconnect.loginFailedInfo.invalidSession")),
				true, false, true, false),
		LOGIN_FAILED_INSUFFICIENT_PREVILEGS(
				new TranslatableComponent("disconnect.loginFailedInfo",
						new TranslatableComponent("disconnect.loginFailedInfo.insufficientPrivileges")),
				true, false, true, false),
		LOGIN_FAILED_INFO(new TranslatableComponent("disconnect.loginFailedInfo"), false, false, true, false) {

			@Override
			public Component getTxt(Object... args) {
				return new TranslatableComponent("disconnect.loginFailedInfo", args);
			}
		},
		GENERIC(new TranslatableComponent("multiplayer.disconnect.generic"), true, false, false, true),
		UNKNOWN_HOST(TACConnectScreen.UNKNOWN_HOST_MESSAGE, true, true, false, false);

		private final Component txt;
		private final boolean identical;
		private final boolean mc, mj, sv;

		DisconnectReason(Component txt, boolean identical, boolean mcError, boolean mjError, boolean svError) {
			this.txt = txt;
			this.identical = identical;

			this.mc = mcError;
			this.mj = mjError;
			this.sv = svError;
		}

		public boolean isMinecraft() {
			return mc;
		}

		public boolean isMojang() {
			return mj;
		}

		public boolean isServer() {
			return sv;
		}

		public Component getTxt(Object... args) {
			return txt;
		}

		public boolean isIdentical() {
			return identical;
		}
	}

}
