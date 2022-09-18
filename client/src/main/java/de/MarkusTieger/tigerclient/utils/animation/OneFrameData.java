package de.MarkusTieger.tigerclient.utils.animation;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import net.minecraft.resources.ResourceLocation;

public class OneFrameData implements IAnimationData {

	private final ResourceLocation frame;

	public OneFrameData(ResourceLocation frame) {
		this.frame = frame;
	}

	@Override
	public ResourceLocation getCurrentFrame() {
		return frame;
	}

	@Override
	public int getFrameLength() {
		return 1;
	}

	@Override
	public int getIndex() {
		return 0;
	}
}
