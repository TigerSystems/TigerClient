package de.MarkusTieger.tigerclient.cosmetics;

import de.MarkusTieger.common.cosmetics.ICosmetic;
import de.MarkusTieger.tigerclient.cosmetics.util.EditableColor;
import net.minecraft.resources.ResourceLocation;

public class Cosmetic implements ICosmetic {

	private String nameType;

	private String typeName;

	private EditableColor color;

	private String name;

	private ResourceLocation icon;

	private CosmeticType type;

	public Cosmetic(String name, String nameType, String typeName, ResourceLocation icon, CosmeticType type,
			EditableColor editableColor) {
		this.name = name;
		this.icon = icon;
		this.type = type;
		this.nameType = nameType;
		this.typeName = typeName;
		color = editableColor;
	}

	public Cosmetic(String name, String nameType, String typeName, ResourceLocation icon, CosmeticType type) {
		this.name = name;
		this.icon = icon;
		this.type = type;
		this.nameType = nameType;
		this.typeName = typeName;
		color = new EditableColor();
	}

	@Override
	public EditableColor getColor() {
		return color;
	}

	@Override
	public void setColor(EditableColor color) {
		this.color = color;
	}

	@Override
	public ResourceLocation getIcon() {
		return icon;
	}

	@Override
	public void setIcon(ResourceLocation icon) {
		this.icon = icon;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getNameType() {
		return nameType;
	}

	@Override
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	@Override
	public CosmeticType getType() {
		return type;
	}

	@Override
	public void setType(CosmeticType type) {
		this.type = type;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
