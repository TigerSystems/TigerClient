package de.MarkusTieger;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.internal.LinkedTreeMap;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class AbstractConfig {

    public ArrayList<DownloadableFile> all = new ArrayList<DownloadableFile>();
    public ArrayList<DownloadableFile> obf = new ArrayList<DownloadableFile>();
    public ArrayList<DownloadableFile> def = new ArrayList<DownloadableFile>();
    
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DownloadableFile {

    	public String name;
        public String type;
        
        public DownloadableFile(File output, File target) {
			String path = output.getAbsolutePath();
			path = target.getAbsolutePath().substring(path.length());
			
			if(path.startsWith("/")) path = path.substring(1);
			
			String[] args = path.split("\\.");
			if(args.length != 2) throw new RuntimeException("Can't Trim Name and Type.");
			
			this.name = args[0];
			this.type = args[1];
		}
    }

    public static class LoaderConfig extends AbstractConfig {

        public AbstractConfig forge = new AbstractConfig();
        public AbstractConfig vanilla = new AbstractConfig();

    }
    
    public static class RootLoaderConfig extends LoaderConfig {
    	
    	public String version = null;
    	
    	public LinkedTreeMap<String, LoaderConfig> extensions = new LinkedTreeMap<>();
    	
    }

}
