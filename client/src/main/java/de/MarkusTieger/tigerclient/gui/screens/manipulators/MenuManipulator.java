package de.MarkusTieger.tigerclient.gui.screens.manipulators;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import de.MarkusTieger.common.screen.IScreenManipulator;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.gui.screens.TigerClientOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class MenuManipulator implements IScreenManipulator {

	@Override
	public boolean canManipulate(Screen screen) {
		return screen instanceof TitleScreen;
	}

	@Override
	public void preManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		TitleScreen title = (TitleScreen) screen;
		PanoramaRenderer renderer = null;
		for (Field field : TitleScreen.class.getDeclaredFields()) {
			if (field.getType().equals(PanoramaRenderer.class)) {
				field.setAccessible(true);
				try {
					renderer = (PanoramaRenderer) field.get(title);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (renderer == null)
			return;

		CubeMap map = null;

		for (Field field : PanoramaRenderer.class.getDeclaredFields()) {
			if (field.getType().equals(CubeMap.class)) {
				field.setAccessible(true);
				try {
					map = (CubeMap) field.get(renderer);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (map == null)
			return;

		ResourceLocation[] frames = null;

		for (Field field : CubeMap.class.getDeclaredFields()) {
			if (field.getType().equals(ResourceLocation[].class)) {
				field.setAccessible(true);
				try {
					frames = (ResourceLocation[]) field.get(map);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (frames == null)
			return;

	}

	@Override
	public void postManipulate(Screen screen, List<GuiEventListener> array, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		int j = screen.height / 4 + 48;
		ImageButton btn = new ImageButton(screen.width / 2 - 124, j + 24 * 2, 20, 20, 20 /* 0 */, 106, 20,
				TigerGuiUtils.WIDGETS, 256, 256, (p_96788_) -> {

					Minecraft.getInstance().setScreen(new TigerClientOptions(screen));

				}, new TranslatableComponent("narration.button.tigerclient"));

		add.accept(btn);
	}
}
