package de.MarkusTieger.tigerclient.utils.animation;

import java.util.ArrayList;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import net.minecraft.resources.ResourceLocation;

public class AnimationUtils {

	public static IAnimationData aniFrames(String location, int count) {
		ArrayList<ResourceLocation> loc = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			loc.add(new ResourceLocation(Client.getInstance().getModId(),
					"textures/" + location + "frame" + i + ".png"));
		}
		return new AnimationData(loc);
	}

	public static IAnimationData frame(String location) {
		return new OneFrameData(new ResourceLocation(Client.getInstance().getModId(), "textures/" + location + ".png"));
	}

	public static ArrayList<ResourceLocation> loc(String... location) {
		ArrayList<ResourceLocation> res = new ArrayList<>();
		for (String element : location) {
			res.add(new ResourceLocation(Client.getInstance().getModId(), "textures/" + element + ".png"));
		}
		return res;
	}

}
