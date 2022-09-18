package de.MarkusTieger.tigerclient.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.auth.IAuthicationService;
import de.MarkusTieger.common.events.EventHandler;
import de.MarkusTieger.common.instance.IInstanceRegistry;
import de.MarkusTieger.common.patch.IPatchManager;
import de.MarkusTieger.common.screen.IScreenManipulator;
import de.MarkusTieger.tigerclient.events.impl.resource.ResourcesLoadedEvent;
import de.MarkusTieger.tigerclient.events.impl.screen.PostScreenInitEvent;
import de.MarkusTieger.tigerclient.events.impl.screen.ScreenChangeEvent;
import de.MarkusTieger.tigerclient.gui.screens.AcceptScreen;
import de.MarkusTieger.tigerclient.gui.screens.BannedScreen;
import de.MarkusTieger.tigerclient.gui.screens.PatchWarningScreen;
import de.MarkusTieger.tigerclient.gui.screens.pack.ModifiedPackSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

public class ScreenListener {

	private final ArrayList<IScreenManipulator> manipulators = new ArrayList<>();

	public ArrayList<IScreenManipulator> getManipulators() {
		return manipulators;
	}

	private boolean reloaded = false;

	@SuppressWarnings("resource")
	@EventHandler
	public void onScreen(ScreenChangeEvent e) {
		IInstanceRegistry instance = Client.getInstance().getInstanceRegistry();

		IAuthicationService auth = instance.load(IAuthicationService.class);

		if (auth.getUser() == null) {
			auth.updateUserInfo();
			if (auth.getUser() != null) {
				if (auth.getUser().isBanned())
					e.setScreen(new BannedScreen(auth.getUser().getReason()));
			}
		}
		if (e.getScreen() instanceof TitleScreen) {

			if (!reloaded) {
				reloaded = true;
				Minecraft.getInstance().reloadResourcePacks();
				Client.getInstance().getEventManager().call(new ResourcesLoadedEvent());
			}

			IPatchManager patch = Client.getInstance().getInstanceRegistry().load(IPatchManager.class);
			patch_screen: if (patch != null) {
				Optional<Throwable> result = patch.checkPatchable();
				if (PatchWarningScreen.IGNORED || result == null)
					break patch_screen;

				e.setScreen(new PatchWarningScreen());

			}
		}
		if (e.getScreen() instanceof PackSelectionScreen pss) {
			OptionsScreen sc = null;
			try {
				sc = (OptionsScreen) Arrays.stream(PackSelectionScreen.class.getDeclaredFields())
						.filter((f) -> f.getType() == Screen.class).toList().get(0).get(pss);
			} catch (Throwable e1) {
				sc = new OptionsScreen(new TitleScreen(), Minecraft.getInstance().options);
				e1.printStackTrace();
			}
			e.setScreen(new ModifiedPackSelectionScreen(sc, Minecraft.getInstance().getResourcePackRepository(),
					(Consumer<PackRepository>) this::updatePackList, Minecraft.getInstance().getResourcePackDirectory(),
					new TranslatableComponent("resourcePack.title")));
		}
		if (!Client.getInstance().hasAccepted() && !(e.getScreen() instanceof ConfirmLinkScreen)
				&& !(e.getScreen() instanceof AcceptScreen)) {
			e.setScreen(new AcceptScreen());
		}
	}

	@SuppressWarnings("resource")
	private void updatePackList(PackRepository p_96245_) {
		List<String> list = ImmutableList.copyOf(Minecraft.getInstance().options.resourcePacks);
		Minecraft.getInstance().options.resourcePacks.clear();
		Minecraft.getInstance().options.incompatibleResourcePacks.clear();

		for (Pack pack : p_96245_.getSelectedPacks()) {
			if (!pack.isFixedPosition()) {
				Minecraft.getInstance().options.resourcePacks.add(pack.getId());
				if (!pack.getCompatibility().isCompatible()) {
					Minecraft.getInstance().options.incompatibleResourcePacks.add(pack.getId());
				}
			}
		}

		Minecraft.getInstance().options.save();
		List<String> list1 = ImmutableList.copyOf(Minecraft.getInstance().options.resourcePacks);
		if (!list1.equals(list)) {
			Minecraft.getInstance().reloadResourcePacks();
		}

	}

	@EventHandler
	public void onScreenPre(PostScreenInitEvent e) {
		for (IScreenManipulator manipulator : manipulators) {
			if (manipulator.canManipulate(e.getScreen()))
				manipulator.preManipulate(e.getScreen(), e.getArray(), e::add, e::remove);
		}
	}

	@EventHandler
	public void onScreenPost(PostScreenInitEvent e) {
		for (IScreenManipulator manipulator : manipulators) {
			if (manipulator.canManipulate(e.getScreen()))
				manipulator.postManipulate(e.getScreen(), e.getArray(), e::add, e::remove);
		}
	}

}
