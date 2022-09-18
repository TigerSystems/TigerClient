package de.MarkusTieger.tigerclient.utils.animation;

import java.util.ArrayList;

import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import net.minecraft.resources.ResourceLocation;

public class AnimationData implements IAnimationData, ITickable<Throwable> {

	private final ArrayList<ResourceLocation> frames;
	private int index = 0;

	public AnimationData(ArrayList<ResourceLocation> frames) {
		this.frames = frames;
	}

	@Override
	public ResourceLocation getCurrentFrame() {
		return frames.get(getIndex());
	}

	@Override
	public int getFrameLength() {
		return frames.size();
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void onTick() {
		if ((index + 1) == frames.size()) {
			index = 0;
		} else {
			index++;
		}
	}

}
