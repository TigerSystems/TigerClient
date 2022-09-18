package de.MarkusTieger.mod;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import de.MarkusTieger.Installer;

public class ModFetcher {

	private static final String CURSEAPI = "https://api.curseforge.com/v1/";
	
	public static List<ModFile> fetchMods(Consumer<Double> progress) throws IOException {
    	
		progress.accept(0D);
		
		List<ModFile> files = new ArrayList<>();
		
    	URLConnection con = new URL(Installer.BASE_URL + "config/recommends.json").openConnection();
        if(con instanceof HttpURLConnection http) http.setRequestProperty("User-Agent", "TigerSystems");
        InputStream in = con.getInputStream();
        byte[] data = in.readAllBytes();
        in.close();
        
        progress.accept(0.10D);
        
        JsonObject obj = Installer.GSON.fromJson(new String(data, StandardCharsets.UTF_8), JsonObject.class);
    	if(!obj.has("recommendations")) return files;
    	if(!obj.get("recommendations").isJsonArray()) return files;
    	
    	JsonArray array = obj.getAsJsonArray("recommendations");
    	
    	progress.accept(0.20D);
    	
    	for(int i = 0; i < array.size(); i++) {
    		JsonElement ele = array.get(i);
    		if(!ele.isJsonPrimitive()) continue;
    		if(!ele.getAsJsonPrimitive().isString()) continue;
    		String str = ele.getAsString();
    		try {
				files.add(fetchMod(str));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
    		progress.accept(0.20 + ((((double)i) * 0.8) / ((double)array.size())));
    	}
    	
    	return files;
    }
    
    public static ModFile fetchMod(String str) throws URISyntaxException {
		URI uri = new URI(str);
		
		System.out.println("MOD: " + uri);
		if(uri.getScheme().equalsIgnoreCase("optifine")) {
			System.out.println("OptiFine: " + uri);
			
			String version = uri.getPath();
			if(version.startsWith("/")) version = version.substring(1);
			if(version.endsWith("/")) version = version.substring(0, version.length() - 1);
			
			System.out.println("MC-Version: " + uri.getHost());
			System.out.println("OF-Version: " + version);
			
			try {
				return fetchOptiFine("1.18.2", version);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		if(uri.getScheme().equalsIgnoreCase("curseforge")) {
			String api = "$2a$10$jM0ezUtpxV9txmMYpb/HAu8mXmqKReKpO/aR2Thw48DHKPXcbDIpy";
			
			try {
				HttpURLConnection con = (HttpURLConnection) new URL(CURSEAPI + "mods/" + uri.getHost()).openConnection();
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("x-api-key", api);
				InputStream in = con.getInputStream();
				byte[] data = in.readAllBytes();
				in.close();
				String json = new String(data, StandardCharsets.UTF_8);
				
				JsonObject obj = Installer.GSON.fromJson(json, JsonObject.class);
				JsonObject mod_data = obj.getAsJsonObject("data");
				
				String name = mod_data.get("name").getAsString();
				
				con = (HttpURLConnection) new URL(CURSEAPI + "mods/" + uri.getHost() + "/files").openConnection();
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("x-api-key", api);
				in = con.getInputStream();
				data = in.readAllBytes();
				in.close();
				json = new String(data, StandardCharsets.UTF_8);
				obj = Installer.GSON.fromJson(json, JsonObject.class);
				JsonArray mod_files = obj.getAsJsonArray("data");
				
				String path = uri.getPath();
				if(path.startsWith("/")) path = path.substring(1);
				if(path.endsWith("/")) path = path.substring(0, path.length() - 1);
				String[] criteria = path.split("/");
				if(criteria.length != 2) throw new IllegalArgumentException("Criteria musst be have the length of two.");
				
				String file_download = null;
				String file_name = null;
				
				for(int i = 0; i < mod_files.size(); i++) {
					obj = mod_files.get(i).getAsJsonObject();
					boolean available = obj.get("isAvailable").getAsBoolean();
					
					if(!available) continue;
					
					String download = obj.get("downloadUrl").getAsString();
					String fn = obj.get("fileName").getAsString();
					
					JsonArray gameVersions = obj.getAsJsonArray("gameVersions");
					
					if(!gameVersions.contains(new JsonPrimitive(criteria[0]))) continue;
					
					if(criteria[1].equalsIgnoreCase("latest")) {
						file_download = download;
						file_name = fn;
						break;
					}
					
					if(criteria[1].equalsIgnoreCase("forge-latest") && gameVersions.contains(new JsonPrimitive("Forge"))) {
						file_download = download;
						file_name = fn;
						break;
					}
					
					
				}
				
				if(file_name == null || file_download == null) return null;
				
				return new ModFile(name, file_name, file_download);
				
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
    
    public static ModFile fetchOptiFine(String releaseVersion, String version) throws MalformedURLException, IOException {
    	
    	// <td class='colMirror'><a href="http://optifine.net/adloadx?f=OptiFine_1.18.2_HD_U_H6.jar">(Mirror)</a></td>
    	
    	HttpURLConnection con = (HttpURLConnection) new URL("https://optifine.net/downloads").openConnection();
    	con.setRequestProperty("User-Agent", "TigerSystems");
    	InputStream in = con.getInputStream();
    	@SuppressWarnings("resource")
		Scanner x = new Scanner(in);
    	
    	String adloadx_url = null;
    	
    	while(x.hasNextLine()) {
    		String line = x.nextLine();
    		
    		// System.out.println("Line: (without downloadx)" + line);
    		
    		if(!line.contains("colMirror")) continue;
    		
    		
    		
    		line = filter(line);
    		
    		// System.out.println("Line: " + line);
    		
    		if(line.toLowerCase().startsWith("<tdclass='colmirror'><ahref=\"") && line.toLowerCase().endsWith("\">(mirror)</a></td>")) {
    			line = line.substring(29);
    			
    			line = line.substring(0, line.length() - 19);
    			
    			if(line.toLowerCase().startsWith("http://optifine.net/adloadx?f=optifine_" + releaseVersion.toLowerCase())) {
    				if(version.equalsIgnoreCase("latest")) {
    					adloadx_url = line;
    					break;
    				} else if(line.toLowerCase().startsWith("http://optifine.net/adloadx?f=optifine_" + releaseVersion.toLowerCase() + "_" + version.toLowerCase())) {
    					adloadx_url = line;
    					break;
    				}
    				
    			}
    			
    			// in.close();
    			
    			// return new URL("https://optifine.net/" + line);
    		}
    		
    	}
    	
    	if(adloadx_url != null) {
    		
    		String filename = adloadx_url.substring(30);
    		
    		if(adloadx_url.toLowerCase().startsWith("http://")) {
    			adloadx_url = adloadx_url.substring(7);
    			adloadx_url = "https://" + adloadx_url;
    		}
    		
    		URL url = fetchOptiFine(new URL(adloadx_url));
    		
    		in.close();
    		
    		return new ModFile("OptiFine " + releaseVersion + " (" + version + ")", filename, url + "");
    		
    	}
    	
    	in.close();
    	
    	return null;
    	
    	/*fetchOptiFine(new URL("https://optifine.net/adloadx?f=OptiFine_1.18.2_HD_U_H7.jar"));
    	
    	return null;*/
    }
    
    public static URL fetchOptiFine(URL adloadx_url) throws IOException {
    	
    	HttpURLConnection con = (HttpURLConnection) adloadx_url.openConnection();
    	con.setRequestProperty("User-Agent", "TigerSystems");
    	InputStream in = con.getInputStream();
    	@SuppressWarnings("resource")
		Scanner x = new Scanner(in);
    	
    	while(x.hasNextLine()) {
    		String line = x.nextLine();
    		
    		// System.out.println("Line: (without downloadx)" + line);
    		
    		if(!line.contains("downloadx")) continue;
    		
    		
    		line = filter(line);
    		
    		
    		if(line.toLowerCase().startsWith("<ahref='downloadx") && line.toLowerCase().endsWith("'onclick='ondownload()'>download</a>")) {
    			line = line.substring(8);
    			line = line.substring(0, line.length() - 36);
    			
    			in.close();
    			
    			return new URL("https://optifine.net/" + line);
    		}
    		
    	}
    	
    	in.close();
    	
    	return null;
    }
    
    private static final String allowed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRTUVWXYZ&=\'/<>()?_0123456789\".:";

	private static String filter(String line) {
		String result = "";
		for(char c : line.toCharArray()) {
			if(!allowed.contains(c + "")) continue;
			result += c;
		}
		return result;
	}
	
}
