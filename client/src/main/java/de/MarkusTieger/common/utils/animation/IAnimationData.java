package de.MarkusTieger.common.utils.animation;

import de.MarkusTieger.annotations.NoObfuscation;
import net.minecraft.resources.ResourceLocation;

@NoObfuscation
public interface IAnimationData {

	ResourceLocation getCurrentFrame();

	int getFrameLength();

	int getIndex();
}
