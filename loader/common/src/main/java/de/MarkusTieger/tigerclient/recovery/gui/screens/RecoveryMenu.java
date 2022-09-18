package de.MarkusTieger.tigerclient.recovery.gui.screens;

import de.MarkusTieger.tigerclient.recovery.RecoveryManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class RecoveryMenu extends Screen {

    public RecoveryMenu() {
        super(new TextComponent("Recovery"));
    }

    @Override
    protected void init() {

        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, new TextComponent("Exit Recovery"), (p_96257_) -> {
            RecoveryManager.exitRecovery();
            System.exit(0);
        }));

    }
}
