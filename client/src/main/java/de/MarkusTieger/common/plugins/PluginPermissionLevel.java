package de.MarkusTieger.common.plugins;

public enum PluginPermissionLevel {

	SANDBOXED_WITHOUT_INTERNAL_SCRIPTING(false), SANDBOXED(false), STANDARD(true), DEBUG(true);

	private final boolean dangerous;

	private PluginPermissionLevel(boolean dangerous) {
		this.dangerous = dangerous;
	}

	public boolean isDangerous() {
		return dangerous;
	}

}
