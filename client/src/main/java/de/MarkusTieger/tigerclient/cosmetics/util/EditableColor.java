package de.MarkusTieger.tigerclient.cosmetics.util;

public class EditableColor {

	public float[] defaultColor = new float[] { 255.0F, 255.0F, 255.0F, 255.0F };
	public boolean[] editableColor = new boolean[] { false, false, false, false };

	public EditableColor() {
	}

	public EditableColor(float red, float green, float blue, float alpha, boolean enabledRed, boolean enabledGreen,
			boolean enabledBlue, boolean enabledAlpha) {
		defaultColor = new float[] { red, green, blue, alpha };
		editableColor = new boolean[] { enabledRed, enabledGreen, enabledBlue, enabledAlpha };
	}

	@Override
	public EditableColor clone() throws CloneNotSupportedException {
		EditableColor color = new EditableColor();
		color.defaultColor = defaultColor.clone();
		color.editableColor = editableColor.clone();
		return color;
	}

	public boolean isEditable() {
		return editableColor[0] || editableColor[1] || editableColor[2] || editableColor[3];
	}

}
