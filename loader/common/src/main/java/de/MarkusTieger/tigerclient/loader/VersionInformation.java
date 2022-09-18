package de.MarkusTieger.tigerclient.loader;

public class VersionInformation {

    private final String name, type, version, clean, build;

    public VersionInformation(String name, String type, String version, String clean, String build){
        this.name = name;
        this.type = type;
        this.version = version;
        this.clean = clean;
        this.build = build;
    }

    public String getCleanVersion() {
        return clean;
    }

    public String getBuild(){
        return build;
    }

    public String getVersion(){
        return version;
    }

    public String getVersionType(){
        return type;
    }

    public String getName(){
        return name;
    }
}
