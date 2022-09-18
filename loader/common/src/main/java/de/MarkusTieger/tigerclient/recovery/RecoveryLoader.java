package de.MarkusTieger.tigerclient.recovery;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.ILoader;
import de.MarkusTieger.tigerclient.CanceledScreen;
import de.MarkusTieger.tigerclient.loader.ClientType;
import de.MarkusTieger.tigerclient.loader.VersionInformation;
import de.MarkusTieger.tigerclient.recovery.gui.screens.RecoveryMenu;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

public class RecoveryLoader implements ILoader {

    @Override
    public void initialize(String modid, BiConsumer<String, Float> consumer, ClientType type) {

    }

    @Override
    public Screen onScreen(Screen screen, boolean canceled) {
    	if(canceled) return CanceledScreen.INSTANCE;

        if(screen == null) {
        	return screen;
        }
        if(screen instanceof TitleScreen){
            return new RecoveryMenu();
        }

        return screen;
    }

    @Override
    public VersionInformation getVersionInformation() {
        return new VersionInformation("Recovery", "-", "0.0.0-0000", "0.0.0", "0000");
    }

	@Override
	public void start() {
	}

	@Override
	public void postStart() {
	}

	@Override
	public void onKey(int key, int action) {
	}

	@Override
	public boolean onMouse(int button, int action, boolean canceled) {
		return canceled;
	}

	@Override
	public void onIngameRender(PoseStack stack) {
	}

	@Override
	public boolean onScreenPre(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
		return false;
	}

	@Override
	public void onScreenPost(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add,
			Consumer<GuiEventListener> remove) {
	}
}
