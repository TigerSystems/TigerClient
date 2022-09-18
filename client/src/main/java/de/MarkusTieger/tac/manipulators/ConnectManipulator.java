package de.MarkusTieger.tac.manipulators;

import java.lang.reflect.Field;

import de.MarkusTieger.common.events.EventHandler;
import de.MarkusTieger.tac.gui.screen.TACConnectScreen;
import de.MarkusTieger.tigerclient.events.impl.screen.ScreenChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

public class ConnectManipulator {

	@EventHandler
	public void onScreen(ScreenChangeEvent e) {
		if (System.getProperty("tac.skipConnect", "false").equalsIgnoreCase("true") || (e.getScreen() == null))
			return;
		if (e.getScreen() instanceof ConnectScreen cs) {
			Screen parent = null;
			for (Field f : ConnectScreen.class.getDeclaredFields()) {
				if (f.getType().equals(boolean.class)) {
					try {
						f.setAccessible(true);
						f.set(cs, true);
					} catch (Exception ex) {

					}
				}
				if (f.getType().equals(Screen.class)) {
					try {
						f.setAccessible(true);
						parent = (Screen) f.get(cs);
					} catch (Exception ex) {

					}
				}
			}
			if (parent == null)
				parent = new TitleScreen();
			TACConnectScreen sc = TACConnectScreen.startConnecting(parent, Minecraft.getInstance(),
					Minecraft.getInstance().getCurrentServer());
			e.setScreen(sc);
		}
	}

}
