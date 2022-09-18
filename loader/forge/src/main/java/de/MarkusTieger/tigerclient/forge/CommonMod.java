package de.MarkusTieger.tigerclient.forge;

import java.util.Optional;

import de.MarkusTieger.tigerclient.loader.ClientLoader;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

@Mod(CommonMod.MODID)
public class CommonMod {

    public static final String MODID = "tc_loader_common";

    public CommonMod() {
        Optional<? extends ModContainer> mod = ModList.get().getModContainerById(MODID);
        if (mod.isPresent()) {
            ModInfo info = (ModInfo) mod.get().getModInfo();
            info.getVersion().parseVersion(ClientLoader.VERSION);
        }
    }

}
