package de.MarkusTieger.common.utils;

import net.minecraft.client.Minecraft;

public record PositionLine(boolean vertical, double o, double v1, double v2, int color) {

	public boolean horizontal() {
		return !vertical;
	}

	public double calculate() {
		return o * (
				vertical ? (
						Minecraft.getInstance().getWindow().getGuiScaledWidth())
						:
							(Minecraft.getInstance().getWindow().getGuiScaledHeight()));
	}
	
}
