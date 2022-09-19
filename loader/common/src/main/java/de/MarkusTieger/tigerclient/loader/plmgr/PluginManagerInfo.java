package de.MarkusTieger.tigerclient.loader.plmgr;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PluginManagerInfo {

	public String id = null;
	public String api = null;
	public boolean sandbox = false;
	public String language = null;
	
	@SerializedName("multi-compatibly")
	public boolean multi_compatibly = false;
	
	public String initializer = null;
	
	public List<String> examples = new ArrayList<>();
	
	public boolean validate() {
		return id != null && api != null && language != null && initializer != null;
	}
	
	public boolean multi() {
		return multi_compatibly;
	}
	
}
