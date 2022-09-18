package de.MarkusTieger.tigerclient.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;

public class VanillaExecutor implements ClientCompatiblityExecutor {

    private final String MODID;
    private final Runnable recovery;

    public VanillaExecutor(String modid, Runnable recovery){
        this.MODID = modid;
        this.recovery = recovery;
    }

    @Override
    public void drawBackground(Screen screen, PoseStack matrixStack) {
        screen.renderBackground(matrixStack);
    }

    @Override
    public void setServerListIconSheet(ResourceLocation resourceLocation) {
        
    }

    @Override
    public void resetServerListIconSheet() {
    	
    }

    @Override
    public boolean checkTooltip(int width, int height, int relativeMouseX, int relativeMouseY) {
        return relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26;
    }

    @Override
    public void enableRecovery() {
        recovery.run();
    }

    @Override
    public boolean onCustomPayload(ClientboundCustomQueryPacket clientboundCustomQueryPacket, Connection connection) {
        return false;
    }

    @Override
    public void onClientLoginSuccess(Connection connection) {
        
    }

    @Override
    public String getModId() {
        return MODID;
    }

}
