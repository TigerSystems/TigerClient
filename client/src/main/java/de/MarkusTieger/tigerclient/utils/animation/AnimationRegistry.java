package de.MarkusTieger.tigerclient.utils.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.common.utils.animation.IAnimationRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AnimationRegistry implements IAnimationRegistry {

	private final HashMap<String, IAnimationData> map = new HashMap<>();
	private final ArrayList<ITickable<?>> ticks = new ArrayList<>();

	@Override
	public Map<String, IAnimationData> toMap() {
		return map;
	}

	public void update() {
		ticks.clear();
		for (Map.Entry<String, IAnimationData> e : map.entrySet()) {
			IAnimationData data = e.getValue();
			if (data instanceof ITickable) {
				ticks.add((ITickable<?>) data);
			}
		}
	}

	@Override
	public void onTick() {
		for (ITickable<?> tick : ticks) {
			try {
				tick.onTick();
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Tickable \"" + tick.getClass().getName()
						+ "\" throwed an Exception on Animation-Tick-Event. Will be ignored.", ex);
			}
		}
	}

	@Override
	public void bindIcon() {
	}

	@Override
	public Component getDescription() {
		return Client.getInstance().getDescription();
	}

	@Override
	public Component getDisplayName() {
		return Client.getInstance().getDisplayName();
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/animation/icon.png");
	}

	@Override
	public String getId() {
		return "animation";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean bool) {
	}
}
