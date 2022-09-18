package de.MarkusTieger.tigerclient.loader;

import java.util.ArrayList;

public class AbstractConfig {

    public String version = null;

    public ArrayList<DownloadableFile> all = new ArrayList<>();
    public ArrayList<DownloadableFile> obf = new ArrayList<>();
    public ArrayList<DownloadableFile> def = new ArrayList<>();

    public static class DownloadableFile {

        public String name;
        public String type;

    }

    public static class LoaderConfig extends AbstractConfig {

        public AbstractConfig forge = new AbstractConfig();
        public AbstractConfig vanilla = new AbstractConfig();

    }

}
