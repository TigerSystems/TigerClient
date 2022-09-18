package de.MarkusTieger.common.cosmetics;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.tigerclient.cosmetics.Cosmetic;
import de.MarkusTieger.tigerclient.cosmetics.util.EditableColor;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public interface ICosmetic {

	EditableColor getColor();

	void setColor(EditableColor color);

	ResourceLocation getIcon();

	void setIcon(ResourceLocation icon);

	String getName();

	void setName(String name);

	String getNameType();

	void setNameType(String nameType);

	ICosmetic.CosmeticType getType();

	void setType(Cosmetic.CosmeticType type);

	String getTypeName();

	void setTypeName(String typeName);

	enum CosmeticType {

		CAPE, WING, HAT, GLASSES, ELYTRA

	}
}
