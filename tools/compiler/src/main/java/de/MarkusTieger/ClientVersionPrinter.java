package de.MarkusTieger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientVersionPrinter extends PrintStream {

	private final ByteArrayOutputStream bout;
	
	public ClientVersionPrinter(ByteArrayOutputStream bout) {
		super(bout);
		this.bout = bout;
	}
	
	public ClientVersion toVersion(String loader_name) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bout.toByteArray());
		return toVersion(loader_name, in, () -> {});
	}
	
	public static ClientVersion toVersion(String loader_name, InputStream in, Runnable destroy) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		
		final String first_loader_forge = "--::tc_loader=";
		final String last_loader_forge = "=tc_loader::--";
		
		final String first_loader_common = "--::tc_common=";
		final String last_loader_common = "=tc_common::--";
		
		final String first_client = "--::tc_version=";
		final String last_client = "=tc_version::--";
		
		String loader_common_ver = null;
    	String loader_specific_ver = null;
		String client_ver = null;
		
		String str = null;
		while((str = reader.readLine()) != null) {
			
			loader_specific: {
				if(!str.contains(first_loader_forge)) break loader_specific;
				if(!str.contains(last_loader_forge)) break loader_specific;
			
				int index1 = str.indexOf(first_loader_forge) + first_loader_forge.length();
				int index2 = str.indexOf(last_loader_forge);
			
				loader_specific_ver = str.substring(index1, index2);
			}
			
			loader_common: {
				if(!str.contains(first_loader_common)) break loader_common;
				if(!str.contains(last_loader_common)) break loader_common;
			
				int index1 = str.indexOf(first_loader_common) + first_loader_common.length();
				int index2 = str.indexOf(last_loader_common);
			
				loader_common_ver = str.substring(index1, index2);
			}
			
			client: {
				if(!str.contains(first_client)) break client;
				if(!str.contains(last_client)) break client;
			
				int index1 = str.indexOf(first_client) + first_client.length();
				int index2 = str.indexOf(last_client);
			
				client_ver = str.substring(index1, index2);
			}
			
			if(loader_specific_ver != null && loader_common_ver != null && client_ver != null) {
				destroy.run();
				
				Map<String, String> loaders = new HashMap<>();
				loaders.put(loader_name, loader_specific_ver);
				
				return new ClientVersion(client_ver, loaders, loader_common_ver);
			}
			
		}
		
		Map<String, String> loaders = new HashMap<>();
		loaders.put(loader_name, loader_specific_ver);
		
		return new ClientVersion(client_ver, loaders, loader_common_ver);
	}

}
