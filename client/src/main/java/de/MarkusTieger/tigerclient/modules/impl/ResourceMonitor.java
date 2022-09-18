package de.MarkusTieger.tigerclient.modules.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.tigerclient.api.optifine.OptiFineAPI;
import de.MarkusTieger.tigerclient.gui.screens.BasicDraggableModuleConfigurationScreen;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

public class ResourceMonitor extends GuiComponent
		implements IModule<Throwable>, IDraggable<Throwable>, IConfigable<Throwable>, ITickable<Throwable> {

	private ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
	private final Font font = Minecraft.getInstance().font;

	private String fps = "Unknown";
	private final String cpuString;
	private final List<String> gpuString = new ArrayList<>();

	public ResourceMonitor() {
		SystemInfo info = new SystemInfo();
		HardwareAbstractionLayer layer = info.getHardware();

		CentralProcessor cpu = layer.getProcessor();
		cpuString = "CPU: " + cpu.getLogicalProcessorCount() + "x " + cpu.getProcessorIdentifier().getName();

		for (GraphicsCard card : layer.getGraphicsCards()) {
			gpuString.add("GPU: " + card.getName());
		}
	}

	private long toKB(long l) {
		return l / 1000;
	}

	private long toMB(long l) {
		return toKB(l) / 1000;
	}

	@SuppressWarnings("unused")
	private long toGB(long l) {
		return toMB(l) / 1000;
	}

	@Override
	public void bindIcon() {
	}

	@Override
	public Component getDescription() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".description");
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".display");
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/resource_monitor/icon.png");
	}

	private boolean enabled = false;

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean bool) {
		enabled = bool;
		updateShadow();
	}

	@Override
	public String getSearchName() {
		return getId();
	}

	@Override
	public String getId() {
		return ResourceMonitor.class.getSimpleName();
	}

	@Override
	public Component getInfo() {
		return isEnabled() ? new TranslatableComponent("modules.enabled")
				: new TranslatableComponent("modules.disabled");
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws Throwable {
		renderDefaultOverlay(stack, x, y, width, height, hover);
	}

	private String shader = "Unknown";

	@Override
	public void onTick() {
		@SuppressWarnings("resource")
		String fps = Minecraft.getInstance().fpsString;
		this.fps = fps.substring(0, fps.indexOf(' '));

		of_load: {
			OptiFineAPI api = Client.getInstance().getInstanceRegistry().load(OptiFineAPI.class);

			if (api == null)
				break of_load;

			try {
				shader = api.getShaderName();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean canConfigure() {
		return true;
	}

	@Override
	public void configure(Screen parent) {
		Minecraft.getInstance()
				.setScreen(new BasicDraggableModuleConfigurationScreen(this, parent, this::updateShadow));
	}

	private boolean shadow = false;

	public void updateShadow() {
		IConfiguration config = Client.getInstance().getInstanceRegistry().load(IConfiguration.class);
		Object obj = config.get("modules#" + getId() + "#shadow");
		if (obj instanceof Boolean b) {
			shadow = b.booleanValue();
		} else {
			shadow = false;
		}
	}

	public void renderShadow(PoseStack stack, ScreenPosition pos) {
		if (!shadow)
			return;

		fill(stack, (int) pos.getAbsouluteX() - 1, (int) pos.getAbsouluteY() - 1,
				(int) pos.getAbsouluteX() + getWidth() + 1, (int) pos.getAbsouluteY() + getHeight() + 1, 0x101010CC);
	}

	@Override
	public void reset() {
		pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
	}

	@Override
	public int getHeight() {
		return (6 + gpuString.size()) * font.lineHeight;
	}

	@Override
	public int getWidth() {
		int width = 0;

		List<String> render = new ArrayList<>();

		render.add(cpuString);
		render.add("CPU-Average: " + (Math.round(getCPULoadAverage() * 100D) / 100D));
		render.add("");

		render.addAll(gpuString);
		render.add("FPS: " + fps);
		render.add("Shader: hfueuh fheufheuhfue");
		render.add("");

		render.add("Memory: " + 50 + " " + 8192 + " / " + (2 * 8192) + " MB");

		for (String l : render) {
			if (font.width(l) > width)
				width = font.width(l);
		}

		return width;
	}

	@Override
	public ScreenPosition load() {
		return pos;
	}

	@Override
	public void render(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		Runtime rt = Runtime.getRuntime();

		long current = (rt.totalMemory() - rt.freeMemory());
		double percantage = ((((double) toMB(current)) * 100D) / ((double) toMB(rt.totalMemory())));
		percantage = Math.round(percantage);

		List<String> render = new ArrayList<>();

		render.add(cpuString);
		render.add("CPU-Average: " + (Math.round(getCPULoadAverage() * 100D) / 100D));
		render.add("");

		render.addAll(gpuString);
		render.add("FPS: " + fps);
		render.add("Shader: " + shader);
		render.add("");

		render.add("Memory: " + ((int) percantage) + "% " + toMB(current) + " / " + toMB(rt.totalMemory()) + " MB");

		int p = 0;
		for (String str : render) {

			int y = p * font.lineHeight;

			drawString(stack, font, str, (int) pos.getAbsouluteX(), (int) pos.getAbsouluteY() + y,
					Client.getInstance().getModuleRegistry().getColor());

			p++;
		}

	}

	@Override
	public void renderDummy(PoseStack stack, ScreenPosition pos) {
		renderShadow(stack, pos);

		List<String> render = new ArrayList<>();

		render.add(cpuString);
		render.add("CPU-Average: " + (Math.round(getCPULoadAverage() * 100D) / 100D));
		render.add("");

		render.addAll(gpuString);
		render.add("FPS: " + fps);
		render.add("");

		render.add("Memory: " + 50 + "% " + 8192 + " / " + (2 * 8192) + " MB");

		int p = 0;
		for (String str : render) {

			int y = p * font.lineHeight;

			drawString(stack, font, str, (int) pos.getAbsouluteX(), (int) pos.getAbsouluteY() + y,
					Client.getInstance().getModuleRegistry().getColor());

			p++;
		}
	}

	private double getCPULoadAverage() {
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
		return osBean.getSystemLoadAverage();
	}

	@Override
	public void save(ScreenPosition screenPosition) {
		if (screenPosition == null)
			reset();
	}

}
