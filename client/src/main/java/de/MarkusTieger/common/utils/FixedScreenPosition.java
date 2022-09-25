package de.MarkusTieger.common.utils;

public class FixedScreenPosition {
	
	private int x, y;
	
	public FixedScreenPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public FixedScreenPosition() {
		this(0, 0);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void applyTo(FixedScreenPosition pos) {
		pos.x = x;
		pos.y = y;
	}
}
