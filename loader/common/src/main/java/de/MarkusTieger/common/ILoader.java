package de.MarkusTieger.common;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.tigerclient.loader.ClientType;
import de.MarkusTieger.tigerclient.loader.VersionInformation;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

public interface ILoader
{
    void initialize(
    		String modid,
    		BiConsumer<String, Float> progress,
    		ClientType type
    		);

    void start();

    void postStart();

    Screen onScreen(Screen screen, boolean canceled);

    void onKey(int key, int action);

    boolean onMouse(int button, int action, boolean canceled);

    void onIngameRender(PoseStack stack);

    boolean onScreenPre(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove);

    void onScreenPost(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove);

    VersionInformation getVersionInformation();
}
