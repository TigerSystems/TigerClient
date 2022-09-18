package de.MarkusTieger.tac.manipulators;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.events.EventHandler;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.screen.IScreenManipulator;
import de.MarkusTieger.common.utils.IKeyable;
import de.MarkusTieger.tac.server.TACServerSelectionList;
import de.MarkusTieger.tigerclient.events.impl.resource.ResourcesLoadedEvent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.server.LanServerDetection;
import net.minecraft.resources.ResourceLocation;

public class ServerManipulator implements IScreenManipulator, IKeyable<Throwable> {

	public boolean mod = false;

	@Override
	public boolean canManipulate(Screen screen) {
		if (System.getProperty("tac.skipServer", "false").equalsIgnoreCase("true"))
			return false;
		return screen instanceof JoinMultiplayerScreen;
	}

	@Override
	public void preManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		JoinMultiplayerScreen sc = (JoinMultiplayerScreen) screen;

		TACServerSelectionList tac = null;

		boolean init = false;

		for (Field f : JoinMultiplayerScreen.class.getDeclaredFields()) {
			if (f.getType().equals(boolean.class)) {
				try {
					f.setAccessible(true);
					f.set(sc, true);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		for (Field f : JoinMultiplayerScreen.class.getDeclaredFields()) {
			if (f.getType().equals(ServerSelectionList.class)) {
				try {
					f.setAccessible(true);
					Object sl = f.get(sc);
					if (sl != null) {
						if ((sl instanceof TACServerSelectionList)) {
							init = true;
						}
					}
					if (!init) {
						tac = new TACServerSelectionList(this, sc, sc.getMinecraft(), sc.width, sc.height, 32,
								sc.height - 64, 36);
						f.set(sc, tac);
					} else {
						tac = (TACServerSelectionList) f.get(sc);
					}
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (!init) {

			ServerList sl = new ServerList(sc.getMinecraft());
			sl.load();

			tac.updateOnlineServers(sl);

			LanServerDetection.LanServerList lsl = new LanServerDetection.LanServerList();
			LanServerDetection.LanServerDetector lsd = null;

			try {
				lsd = new LanServerDetection.LanServerDetector(lsl);
				lsd.start();
			} catch (Exception ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.TAC, "Unable to start LAN server detection", ex);
			}

			for (Field f : JoinMultiplayerScreen.class.getDeclaredFields()) {
				try {
					if (f.equals(ServerList.class)) {
						f.setAccessible(true);
						f.set(sc, sl);
					}
					if (f.equals(LanServerDetection.LanServerList.class)) {
						f.setAccessible(true);
						f.set(sc, lsl);
					}
					if (f.equals(LanServerDetection.LanServerDetector.class)) {
						f.setAccessible(true);
						f.set(sc, lsd);
					}
					if (f.equals(ServerStatusPinger.class)) {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			sc.getMinecraft().setScreen(sc);
		}

	}

	@EventHandler
	public void onResourcesLoaded(ResourcesLoadedEvent e) {
		Client.getInstance().getCompatiblityExecutor()
				.setServerListIconSheet(new ResourceLocation(Client.getInstance().getModId(), "textures/empty.png"));
	}

	@Override
	public void postManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {

	}

	@Override
	public boolean onKey(int keyCode, int flag) {
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			if (flag == GLFW.GLFW_PRESS) {
				mod = true;
				Client.getInstance().getCompatiblityExecutor().resetServerListIconSheet();
			}
			if (flag == GLFW.GLFW_RELEASE) {
				mod = false;
				Client.getInstance().getCompatiblityExecutor().setServerListIconSheet(
						new ResourceLocation(Client.getInstance().getModId(), "textures/empty.png"));
			}
		}
		return false;
	}
}
