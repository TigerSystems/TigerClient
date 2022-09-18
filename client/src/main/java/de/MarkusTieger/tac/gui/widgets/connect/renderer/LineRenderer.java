package de.MarkusTieger.tac.gui.widgets.connect.renderer;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.gui.widgets.connect.renderrer.IConnectRenderer;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.tac.gui.widgets.connect.TACConnectWidget;
import de.MarkusTieger.tac.gui.widgets.connect.registry.PointRegistry;
import net.minecraft.resources.ResourceLocation;

public class LineRenderer implements IConnectRenderer, IHighspeedTick<Throwable> {

	private final TACConnectWidget widget;
	private final PointRegistry registry;

	private static final ResourceLocation LINE_DISABLED = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/connect/line_disabled.png"),
			LINE_ENABLED = new ResourceLocation(Client.getInstance().getModId(),
					"textures/gui/connect/line_enabled.png"),
			DATA = new ResourceLocation(Client.getInstance().getModId(), "textures/gui/connect/data.png"),
			LINE_BRIDGE_DISABLED = new ResourceLocation(Client.getInstance().getModId(),
					"textures/gui/connect/line_bridge_disabled.png"),
			LINE_BRIDGE_ENABLED = new ResourceLocation(Client.getInstance().getModId(),
					"textures/gui/connect/line_bridge_enabled.png"),
			LINE_DISABLED_UP = new ResourceLocation(Client.getInstance().getModId(),
					"textures/gui/connect/line_disabled_up.png"),
			LINE_ENABLED_UP = new ResourceLocation(Client.getInstance().getModId(),
					"textures/gui/connect/line_enabled_up.png"),
			ERROR = new ResourceLocation(Client.getInstance().getModId(), "textures/gui/connect/error.png");

	private boolean mcCon = false, svCon = false, mjCon = false;
	private int mcConTick = 0, svConTick = 0, mjConTick = 0;
	private boolean svConDirection = false, mcConDirection = false, mjConDirection = false;
	private final boolean errorMode;

	public LineRenderer(TACConnectWidget widget, PointRegistry registry, boolean errorMode) {
		this.widget = widget;
		this.registry = registry;
		this.errorMode = errorMode;
	}

	@Override
	public void render(PoseStack stack, double multiplicator, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int xImg, int yImg) {
		widget.img(stack, xImg, yImg, svCon ? LINE_ENABLED : LINE_DISABLED, funcX, funcY, registry.load("it").x,
				registry.load("it").y - 10, registry.load("sv").x - registry.load("it").x, 20);
		widget.img(stack, xImg, yImg, mcCon ? LINE_ENABLED : LINE_DISABLED, funcX, funcY, registry.load("mc").x,
				registry.load("it").y - 10, registry.load("it").x - registry.load("mc").x, 20);

		widget.img(stack, xImg, yImg, mjCon ? LINE_ENABLED_UP : LINE_DISABLED_UP, funcX, funcY,
				registry.load("it").x - 10, registry.load("itmj").y, 20,
				registry.load("it").y - registry.load("itmj").y);

		widget.img(stack, xImg, yImg, mjCon ? LINE_ENABLED : LINE_DISABLED, funcX, funcY, registry.load("itmj").x,
				registry.load("itmj").y - 10, registry.load("mj").x - registry.load("itmj").x, 20);

		widget.img(stack, xImg, yImg, mjCon ? LINE_BRIDGE_ENABLED : LINE_BRIDGE_DISABLED, funcX, funcY,
				registry.load("itmj").x - 10, registry.load("itmj").y - 10, 20, 20);

		if (errorMode) {

			if (mcCon) {
				widget.img(stack, xImg, yImg, ERROR, funcX, funcY,
						(registry.load("mc").x - 30) + ((registry.load("it").x - registry.load("mc").x) / 2),
						registry.load("mc").y - 30, 60, 60);
			}

			if (mjCon) {
				widget.img(stack, xImg, yImg, ERROR, funcX, funcY, (registry.load("it").x - 30),
						(registry.load("it").y - 30) - ((registry.load("it").y - registry.load("itmj").y) / 2), 60, 60);
			}

			if (svCon) {
				widget.img(stack, xImg, yImg, ERROR, funcX, funcY,
						(registry.load("it").x - 30) + ((registry.load("sv").x - registry.load("it").x) / 2),
						registry.load("it").y - 30, 60, 60);
			}

		} else {
			if (mcCon) {
				widget.img(stack, xImg, yImg, DATA, funcX, funcY, (registry.load("mc").x - 10) + mcConTick,
						registry.load("mc").y - 10, 20, 20);
			}

			if (mjCon) {
				int firstMax = registry.load("it").y - registry.load("itmj").y;

				if (mjConTick > firstMax) {
					widget.img(stack, xImg, yImg, DATA, funcX, funcY,
							(registry.load("itmj").x - 10) + (mjConTick - firstMax), registry.load("itmj").y - 10, 20,
							20);
				} else {
					widget.img(stack, xImg, yImg, DATA, funcX, funcY, (registry.load("it").x - 10),
							(registry.load("it").y - 10) - mjConTick, 20, 20);
				}

			}

			if (svCon) {
				widget.img(stack, xImg, yImg, DATA, funcX, funcY, (registry.load("it").x - 10) + svConTick,
						registry.load("it").y - 10, 20, 20);
			}
		}
	}

	@Override
	public void onHighTick() {
		if (errorMode)
			return;

		int maxSV = registry.load("sv").x - registry.load("it").x;
		int maxMC = registry.load("it").x - registry.load("mc").x;
		int maxMJ = registry.load("it").y - registry.load("itmj").y;
		maxMJ += registry.load("mj").x - registry.load("itmj").x;

		if (svCon) {
			if (svConTick == maxSV || svConTick < 0) {
				if (svConDirection) {
					svConDirection = false;
					svConTick = 0;
				} else {
					svConDirection = true;
					svConTick = maxSV - 1;
				}
			} else {
				if (svConDirection) {
					svConTick -= 1;
				} else {
					svConTick += 1;
				}
			}
		}

		if (mjCon) {
			if (mjConTick == maxMJ || mjConTick < 0) {
				if (mjConDirection) {
					mjConDirection = false;
					mjConTick = 0;
				} else {
					mjConDirection = true;
					mjConTick = maxMJ - 1;
				}
			} else {
				if (mjConDirection) {
					mjConTick -= 1;
				} else {
					mjConTick += 1;
				}
			}
		}

		if (mcCon) {
			if (mcConTick == maxMC || mcConTick < 0) {
				if (mcConDirection) {
					mcConDirection = false;
					mcConTick = 0;
				} else {
					mcConDirection = true;
					mcConTick = maxMC - 1;
				}
			} else {
				if (mcConDirection) {
					mcConTick -= 1;
				} else {
					mcConTick += 1;
				}
			}
		}
	}

	public void setMinecraft(boolean minecraft) {
		mcCon = minecraft;
	}

	public void setMojang(boolean mojang) {
		mjCon = mojang;
	}

	public void setServer(boolean server) {
		svCon = server;
	}
}
