package de.MarkusTieger.tigerclient.forge;

import java.util.Optional;

import de.MarkusTieger.VersionPrinter;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

@Mod(LoaderMod.MODID)
public class LoaderMod {

    public static final String MODID = "tc_loader_forge";
    public static final String VERSION = "1.5.0";

    public LoaderMod(){
        Optional<? extends ModContainer> mod = ModList.get().getModContainerById(MODID);
        if (mod.isPresent()) {
            ModInfo info = (ModInfo) mod.get().getModInfo();
            info.getVersion().parseVersion(VERSION);
        }
        
        VersionPrinter.__internal__();
    }

}
