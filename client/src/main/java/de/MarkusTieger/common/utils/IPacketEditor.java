package de.MarkusTieger.common.utils;

import net.minecraft.network.protocol.Packet;

public interface IPacketEditor<T extends Throwable> {

	public boolean accept(Packet<?> packet) throws T;

	public PacketSides getSides() throws T;

	public <E extends Packet<?>> E edit(E packet) throws T;

	public static enum PacketSides {

		SEND, RECIEVE, BOTH;

	}

	default boolean allowPacketEditor() {
		return true;
	}

}
