package de.MarkusTieger.tigerclient.forge;

import java.io.IOException;
import java.util.HashMap;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkHooks;

public class ForgeExecutor implements ClientCompatiblityExecutor {

    private final String MODID;
    private static final ResourceLocation ICON_SHEET = new ResourceLocation("forge", "textures/gui/icons.png");
    private static final HashMap<ResourceLocation, NativeImage> cache = new HashMap<>();
    private final Runnable recovery;

    public ForgeExecutor(String modid, Runnable recovery){
        this.MODID = modid;
        this.recovery = recovery;
    }

    @Override
    public void drawBackground(Screen screen, PoseStack matrixStack) {
        Event event = new ScreenEvent.BackgroundDrawnEvent(screen, matrixStack);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS
                .post(event);
    }

    @Override
    public void setServerListIconSheet(ResourceLocation resourceLocation) {
        Minecraft minecraft = Minecraft.getInstance();
        @SuppressWarnings("unused")
		SimpleTexture texture = (SimpleTexture) minecraft.getTextureManager().getTexture(resourceLocation);
        minecraft.getTextureManager().release(ICON_SHEET);

        NativeImage image = cache.get(resourceLocation);
        if(image == null){
            image = load(minecraft.getResourceManager(), resourceLocation);
            cache.put(resourceLocation, image);
        }

        minecraft.getTextureManager().register(ICON_SHEET, new DynamicTexture(image));
    }

    private NativeImage load(ResourceManager resourceManager, ResourceLocation location){
        try {
            Resource var2 = resourceManager.getResource(location);
            NativeImage var3 = null;
            try {
                var3 = NativeImage.read(var2.getInputStream());
            } catch (Throwable var8) {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                    }
                }

                throw null;
            }

            if (var2 != null) {
                var2.close();
            }
            return var3;
        } catch (IOException var9) {
            return null;
        }
    }

    @Override
    public void resetServerListIconSheet() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().release(ICON_SHEET);
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
        return NetworkHooks.onCustomPayload(clientboundCustomQueryPacket, connection);
    }

    @Override
    public void onClientLoginSuccess(Connection connection) {
        NetworkHooks.handleClientLoginSuccess(connection);
    }

    @Override
    public String getModId() {
        return MODID;
    }

}
