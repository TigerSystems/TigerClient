package de.MarkusTieger.tigerclient.loader;

public enum ClientType {
	
	FORGE("de.MarkusTieger.tigerclient.forge.ForgeExecutor"),
	VANILLA("de.MarkusTieger.tigerclient.vanilla.VanillaExecutor");

	private final String executor;
	
	private ClientType(String executor) {
		this.executor = executor;
	}
	
	public String getExecutor() {
		return executor;
	}

}
