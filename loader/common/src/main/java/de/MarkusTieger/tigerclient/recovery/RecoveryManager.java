package de.MarkusTieger.tigerclient.recovery;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.minecraft.client.Minecraft;

public class RecoveryManager {



    public static boolean isRecoveryEnabled(){
        File recovery = getFile();
        return recovery.exists();
    }

    public static void enableRecovery() {
        if(isRecoveryEnabled()) return;
        File recovery = getFile();
        if(!recovery.exists()) {
            try {
                recovery.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("resource")
	private static File getFile() {
        return new File(Minecraft.getInstance().gameDirectory, "recovery");
    }

    public static void exitRecovery() {
        File recovery = getFile();
        try {
            delete(recovery);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delete(File file) throws IOException {
        if(!file.exists()) return;
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                delete(f);
            }
        }
        Files.delete(file.toPath());
    }

}
