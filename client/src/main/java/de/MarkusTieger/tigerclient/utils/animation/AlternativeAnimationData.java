package de.MarkusTieger.tigerclient.utils.animation;

import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.tigerclient.data.Data;
import net.minecraft.resources.ResourceLocation;

public class AlternativeAnimationData implements IAnimationData {

	private final String id;
	private final Data<IAnimationData> data;

	public AlternativeAnimationData(String id, Data<IAnimationData> data) {
		this.id = id;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	@Override
	public ResourceLocation getCurrentFrame() {
		return data.isEmpty() ? null : data.getData().getCurrentFrame();
	}

	@Override
	public int getFrameLength() {
		return data.isEmpty() ? 0 : data.getData().getFrameLength();
	}

	@Override
	public int getIndex() {
		return data.isEmpty() ? -1 : data.getData().getIndex();
	}
}
