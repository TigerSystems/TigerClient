package de.MarkusTieger.tigerclient.loader.profilefiller;

import java.util.function.Supplier;

import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricCategory;

public class EmptyProfileFiller implements ProfilerFiller {

	@Override
	public void startTick() {}

	@Override
	public void push(Supplier<String> p_18582_) {}

	@Override
	public void push(String p_18581_) {}

	@Override
	public void popPush(Supplier<String> p_18584_) {}

	@Override
	public void popPush(String p_18583_) {}

	@Override
	public void pop() {}

	@Override
	public void markForCharting(MetricCategory p_145959_) {}

	@Override
	public void incrementCounter(Supplier<String> p_18586_) {}

	@Override
	public void incrementCounter(String p_18585_) {}

	@Override
	public void endTick() {}

	@Override
	public void incrementCounter(String p_185258_, int p_185259_) {
	}

	@Override
	public void incrementCounter(Supplier<String> p_185260_, int p_185261_) {
	}

}
