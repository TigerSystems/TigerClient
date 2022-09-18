package de.MarkusTieger.tigerclient.events.impl.message;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.events.IEvent;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public class ServerMessageEvent implements IEvent {

	private final ResourceLocation channel;
	private final byte[] data;

	public ServerMessageEvent(ResourceLocation channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public ResourceLocation getChannel() {
		return channel;
	}
}
