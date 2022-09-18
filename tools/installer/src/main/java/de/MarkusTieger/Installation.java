package de.MarkusTieger;

import java.io.File;

public class Installation {

    private final File dir, js;

    public Installation(File dir, File js) {
        this.dir = dir;
        this.js = js;
    }

    public File getDir() {
        return dir;
    }

    public File getJs() {
        return js;
    }

    @Override
    public String toString() {
        return dir.getName();
    }
    
    public static class VanillaInstallation extends Installation {

		public VanillaInstallation(File dir, File js) {
			super(dir, js);
		}
    	
    }
    
}
