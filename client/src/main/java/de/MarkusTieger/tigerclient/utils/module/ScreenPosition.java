package de.MarkusTieger.tigerclient.utils.module;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;

public class ScreenPosition {

	private double absX, relX;
	private double absY, relY;

	public ScreenPosition(double x, double y) {
		setRelative(x, y);
	}

	public static ScreenPosition fromAbsolutePosition(int x, int y) {
		ScreenPosition pos = new ScreenPosition(0.5D, 0.5D);
		pos.setAbsolute(x, y);
		return pos;
	}

	public static ScreenPosition fromRelativePosition(double x, double y) {
		return new ScreenPosition(x, y);
	}

	public double getAbsouluteX() {
		return absX;
	}

	public double getAbsouluteY() {
		return absY;
	}

	public double getRelativeX() {
		return relX;
	}

	public double getRelativeY() {
		return relY;
	}

	public void setAbsolute(double x, double y) {
		this.absX = x;
		this.absY = y;
		Window window = Minecraft.getInstance().getWindow();
		relX = absX / window.getGuiScaledWidth();
		relY = absY / window.getGuiScaledHeight();
	}

	public void setRelative(double x, double y) {
		relX = x;
		relY = y;
		Window window = Minecraft.getInstance().getWindow();
		absX = (relX * window.getGuiScaledWidth());
		absY = (relY * window.getGuiScaledHeight());
	}

}
