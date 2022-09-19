package de.MarkusTieger.tigerclient.forge;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.tigerclient.CanceledScreen;
import de.MarkusTieger.tigerclient.loader.ClientLoader;
import de.MarkusTieger.tigerclient.loader.ClientType;
import de.MarkusTieger.tigerclient.loader.VersionInformation;
import de.MarkusTieger.tigerclient.loader.overlay.AdvancedOverlay;
import de.MarkusTieger.tigerclient.recovery.RecoveryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.internal.BrandingControl;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;

@Mod(ForgeMod.MODID)
public class ForgeMod {

    public static final String MODID = "tigerclient";
    private boolean initialized = false,
                    started = false;
    
    
    private final ClientLoader loader;

    public ForgeMod() {
        if (initialized) throw new RuntimeException("Already Initialized!");
        registerEvent(this::doClientStuff);
        registerEvent(this::doPostStuff);

        /*Optional<? extends ModContainer> mod = ModList.get().getModContainerById(MODID);
        if (mod.isPresent()) {
            ModInfo info = (ModInfo) mod.get().getModInfo();
            info.getVersion().parseVersion(Client.getInstance().getVersionType() + " v." + Client.getInstance().getVersion());
        }*/

        MinecraftForge.EVENT_BUS.register(this);

        loader = new ClientLoader();
    }

    public <T extends Event> void registerEvent(Consumer<T> f) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(f);
    }

    @SubscribeEvent
    public void onKey(final InputEvent.KeyInputEvent e){
        if(!initialized) return;
        loader.onKey(e.getKey(), e.getAction());
    }

    @SubscribeEvent
    public void onMouse(final InputEvent.RawMouseEvent event) {
        if (!initialized) return;
        // event.setCanceled(Client.getInstance().getModuleRegistry().onMouse(event.getButton(), event.getAction(), event.isCanceled()));
        event.setCanceled(loader.onMouse(event.getButton(), event.getAction(), event.isCanceled()));
    }

    @SubscribeEvent
    public void onIngameRender(final RenderGameOverlayEvent.Pre e) {
        if (!initialized) return;
        if (e.getType() != ElementType.ALL)
            return;
        // Client.getInstance().getModuleRegistry().onIngameOverlay(e.getMatrixStack());
        loader.onIngameRender(e.getMatrixStack());
    }

    @SubscribeEvent
    public void onGuiOpen(final ScreenOpenEvent e) {
        if (!initialized) return;
        
        Screen sc = loader.onScreen(e.getScreen(), e.isCanceled());
        if(sc != null && sc instanceof CanceledScreen) {
        	e.setCanceled(true);
        } else e.setScreen(sc);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        boolean recovery = RecoveryManager.isRecoveryEnabled();

        if(recovery){



        } else {

            BiConsumer<String, Float> consumer = (str, f) -> {};

            if(Minecraft.getInstance().getOverlay() != null && Minecraft.getInstance().getOverlay() instanceof LoadingOverlay lo){
    			
            	AdvancedOverlay overlay = new AdvancedOverlay(lo);
                consumer = overlay::setProgress;
                
                
                Minecraft.getInstance().setOverlay(overlay);
            }

            consumer.accept("Initializing...", 0F);

            loader.initialize(MODID, consumer, ClientType.FORGE);
            initialized = true;
            
            Optional<? extends ModContainer> mod = ModList.get().getModContainerById(MODID);
            if (mod.isPresent()) {
                ModInfo info = (ModInfo) mod.get().getModInfo();
                info.getVersion().parseVersion(loader.getVersionInformation().getCleanVersion());
            }

            loader.start();

            started = true;

        }
    }

    private Field f = null,
    		f2 = null;
    private List<String> brandings, brandingsNoMC;
    
    public String getDisplayName() {
    	return loader.getVersionInformation().getName() + " v." + loader.getVersionInformation().getVersion();
    }
    
    private void initializeBrandings(VersionInformation ver) throws Throwable {
    	ImmutableList.Builder<String> brd = ImmutableList.builder();
        brd.add("Forge " + ForgeVersion.getVersion());
        brd.add(ver.getName() + " v." + ver.getVersion());
        brd.add("Minecraft " + MCPVersion.getMCVersion());
        // brd.add("MCP " + MCPVersion.getMCPVersion()); Uninteresting
        int tModCount = ModList.get().size();
        brd.add(ForgeI18n.parseMessage("fml.menu.loadingmods", tModCount));
        
        for(Field f_tmp : BrandingControl.class.getDeclaredFields()) {
        	if(f_tmp.getType() == List.class) {
        		f = f_tmp;
        		break;
        	}
        }
        if(f == null) return;
        
        for(Field f_tmp : BrandingControl.class.getDeclaredFields()) {
        	if(f_tmp.getType() == List.class && !f_tmp.getName().equals(f.getName())) {
        		f2 = f_tmp;
        		break;
        	}
        }
        
        brandings = brd.build();
        brandingsNoMC = brandings.subList(1, brandings.size());
        
        
	}
    
    private void setBrandings() throws Throwable {
    	
    	if(f == null || f2 == null) {
    		System.out.println("Fields not initialized.");
    		return;
    	}
    	
    	f.setAccessible(true);
        f.set(null, brandings);
        
        f2.setAccessible(true);
        f2.set(null, brandingsNoMC);
    }
    
    private GuiEventListener createBrandingWidget() {
		return new AbstractWidget(25, 25, 50, 50, new TextComponent("Nothing to see here :)")) {
			
			@Override
			public void updateNarration(NarrationElementOutput p_169152_) {
			}
			
			@Override
			public void render(PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
				try {
					setBrandings();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void doPostStuff(final FMLLoadCompleteEvent e){
        if(!started) return;

        try {
			initializeBrandings(loader.getVersionInformation());
		} catch (Throwable ex) {
			ex.printStackTrace();
			System.out.println("Failed to set Brandings");
		}
        
        loader.postStart();
    }

    @SubscribeEvent
    public void onPreScreen(ScreenEvent.InitScreenEvent.Pre e){
        if(!initialized) return;
        
        loader.onScreenPre(e.getScreen(), e.getListenersList(), e::addListener, e::removeListener);
    }

    

	@SubscribeEvent
    public void onPostScreen(ScreenEvent.InitScreenEvent.Post e){
        if(!initialized) return;
        
        branding: {
        	if(e.getScreen() == null) break branding;
        	if(!(e.getScreen() instanceof TitleScreen)) break branding;
        	e.addListener(createBrandingWidget());
        }
        
        loader.onScreenPost(e.getScreen(), e.getListenersList(), e::addListener, e::removeListener);
    }

}
