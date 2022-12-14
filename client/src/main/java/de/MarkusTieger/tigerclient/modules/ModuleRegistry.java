package de.MarkusTieger.tigerclient.modules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.modules.IModuleRegistry;
import de.MarkusTieger.common.registry.IRegistry;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.IKeyable;
import de.MarkusTieger.common.utils.IMouseable;
import de.MarkusTieger.common.utils.IMultiDrag;
import de.MarkusTieger.common.utils.IPacketEditor;
import de.MarkusTieger.common.utils.IPacketEditor.PacketSides;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.PositionLine;
import de.MarkusTieger.tigerclient.gui.screens.ModsPositionScreen;
import de.MarkusTieger.tigerclient.registry.StaticRegistry;
import net.minecraft.network.protocol.Packet;

@SuppressWarnings("rawtypes")
public class ModuleRegistry implements IModuleRegistry {

	private final ArrayList<IModule<?>> modules = new ArrayList<>();

	@Override
	public List<IModule<?>> toArray() {
		return modules;
	}

	private final StaticRegistry<ITickable<?>> ticks = new StaticRegistry<>();
	private final StaticRegistry<IHighspeedTick<?>> hticks = new StaticRegistry<>();
	private final StaticRegistry<IDraggable<?>> drags = new StaticRegistry<>();
	private final StaticRegistry<IKeyable<?>> keys = new StaticRegistry<>();
	private final StaticRegistry<IMouseable<?>> mouses = new StaticRegistry<>();
	private final StaticRegistry<IConfigable<?>> configs = new StaticRegistry<>();
	private final StaticRegistry<IPacketEditor<?>> packets = new StaticRegistry<>();
	
	private final List<PositionLine> pos_lines = new ArrayList<>();
	
	private final Predicate<IModule<?>> PREDICATE = (mod) -> {

		if (mod.isDevOnly() && !Client.getInstance().isDev())
			return false;

		return true;
	};

	@Override
	public void update() {
		final StaticRegistry<ITickable> ticks = new StaticRegistry<>();
		final StaticRegistry<IHighspeedTick> hticks = new StaticRegistry<>();
		final StaticRegistry<IDraggable> drags = new StaticRegistry<>();
		final StaticRegistry<IKeyable> keys = new StaticRegistry<>();
		final StaticRegistry<IMouseable> mouses = new StaticRegistry<>();
		final StaticRegistry<IConfigable> configs = new StaticRegistry<>();
		final StaticRegistry<IPacketEditor> packets = new StaticRegistry<>();
		
		fill(ticks, ITickable.class, false);
		fill(hticks, IHighspeedTick.class, false);
		fill(drags, IDraggable.class, false);
		fill(keys, IKeyable.class, false);
		fill(mouses, IMouseable.class, false);
		fill(configs, IConfigable.class, true);
		fill(packets, IPacketEditor.class, false);

		for (IModule<?> module : modules) {
			try {
				if (module instanceof IMultiDrag && module.isEnabled()) {
					((IMultiDrag<?>) module).getDraggables().forEach(drags::register);
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Module \"" + module.getId()
						+ "\" throwed an Exception on Enabled-Check. Will be marked as disabled.", ex);
			}
		}

		ticks.mapForGenericTypes(this.ticks, (t) -> (ITickable<?>) t);
		hticks.mapForGenericTypes(this.hticks, (t) -> (IHighspeedTick<?>) t);
		drags.mapForGenericTypes(this.drags, (t) -> (IDraggable<?>) t);
		keys.mapForGenericTypes(this.keys, (t) -> (IKeyable<?>) t);
		mouses.mapForGenericTypes(this.mouses, (t) -> (IMouseable<?>) t);
		configs.mapForGenericTypes(this.configs, (t) -> (IConfigable<?>) t);
		packets.mapForGenericTypes(this.packets, (t) -> (IPacketEditor<?>)t);
		
		pos_lines.clear();
		
		for(double x = 0.0D; x <= 1.0D; x += 0.125D) {
			pos_lines.add(new PositionLine(true, x, 0D, 1D, 0xFFFF0000));
		}
		
		for(double y = 0.0D; y <= 1.0D; y += 0.125D) {
			pos_lines.add(new PositionLine(false, y, 0D, 1D, 0xFFFF0000));
		}
		
	}

	@SuppressWarnings("unchecked")
	private <T> void fill(StaticRegistry<T> sub, Class<T> target, boolean enabled) {
		sub.clear();
		for (IModule<?> module : modules) {
			try {
				if (!module.isEnabled() && !enabled)
					continue;
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Module \"" + module.getId()
						+ "\" throwed an Exception on Enabled-Check. Will be marked as disabled.", ex);
				continue;
			}
			if (target.isInstance(module) && checkAllow(target, module)) {
				sub.register((T) module);
			}
		}
	}

	private boolean checkAllow(Class<?> target, IModule<?> module) {

		for (Method m : target.getDeclaredMethods()) {
			if (m.getName().startsWith("allow") && m.getReturnType() == boolean.class && m.getParameterCount() == 0) {
				try {
					return (boolean) m.invoke(module);
				} catch (Throwable e) {
					Client.getInstance().getLogger().error(LoggingCategory.MODULES, "Allow check failed.", e);
				}
			}
		}

		return true;
	}

	@Override
	public StaticRegistry<ITickable<?>> getTickable() {
		return ticks;
	}

	@Override
	public StaticRegistry<IHighspeedTick<?>> getHighTickable() {
		return hticks;
	}

	@Override
	public StaticRegistry<IDraggable<?>> getDraggable() {
		return drags;
	}

	@Override
	public StaticRegistry<IKeyable<?>> getKeyable() {
		return keys;
	}

	@Override
	public StaticRegistry<IMouseable<?>> getMouseable() {
		return mouses;
	}

	@Override
	public StaticRegistry<IConfigable<?>> getConfigable() {
		return configs;
	}
	
	@Override
	public IRegistry<?, IPacketEditor<?>> getPacketable() {
		return packets;
	}

	@Override
	public void onKey(int keyCode, int action) {
		for (IKeyable<?> key : keys.toArray()) {
			try {
				key.onKey(keyCode, action);
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Keyable \"" + key.getClass().getName()
						+ "\" throwed an Exception on Key-Event. Will be ignored.", ex);
			}
		}
	}

	@SuppressWarnings("resource")
	@Override
	public void onIngameOverlay(PoseStack matrix) {
		if (Client.getMinecraft().screen instanceof ModsPositionScreen) {
			return;
		}
		for (IDraggable<?> drag : drags.toArray()) {
			try {
				drag.render(matrix, drag.position().calculateFixed(drag.getWidth(), drag.getHeight()));
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Draggable \""
						+ drag.getClass().getName() + "\" throwed an Exception on Render-Event. Will be ignored.", ex);
			}
		}
	}

	@Override
	public boolean onMouse(int button, int action, boolean cancelled) {
		for (IMouseable<?> mouse : mouses.toArray()) {

			try {
				if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
					cancelled = cancelled || mouse.onLeftClick(action);
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
						.warn(LoggingCategory.MODULES,
								"Mouseable \"" + mouse.getClass().getName()
										+ "\" throwed an Exception on Mouse-Event (BUTTON_1/Left). Will be ignored.",
								ex);
			}

			try {
				if (button == GLFW.GLFW_MOUSE_BUTTON_2) {
					cancelled = cancelled || mouse.onRightClick(action);
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
						.warn(LoggingCategory.MODULES,
								"Mouseable \"" + mouse.getClass().getName()
										+ "\" throwed an Exception on Mouse-Event (BUTTON_2/Right). Will be ignored.",
								ex);
			}

			try {
				if (button == GLFW.GLFW_MOUSE_BUTTON_3) {
					cancelled = cancelled || mouse.onMiddleClick(action);
				}
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
						.warn(LoggingCategory.MODULES,
								"Mouseable \"" + mouse.getClass().getName()
										+ "\" throwed an Exception on Mouse-Event (BUTTON_3/Middle). Will be ignored.",
								ex);
			}

		}
		return cancelled;
	}

	@Override
	public int getColor() {
		return 0xFFFF5E00;
	}

	@Override
	public Predicate<IModule<?>> getPredicate() {
		return PREDICATE;
	}

	@Override
	public boolean accept(Packet<?> packet) {
		return packets.toArray().stream().anyMatch((edit) -> {
			try {
				return edit.accept(packet);
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
				.warn(LoggingCategory.MODULES,
						"PacketEditor \"" + edit.getClass().getName()
								+ "\" throwed an Exception on Packet-Accept-Event (Packet: \"" + packet.getClass().getName() + "\"). Will be ignored.",
						ex);
			}
			return false;
		});
	}

	@Override
	public PacketSides getSides() {
		PacketSides sides = PacketSides.NONE;
		for (IPacketEditor<?> packet : packets) {
			try {
				if(packet.getSides() == PacketSides.BOTH) sides = PacketSides.BOTH;
				if(packet.getSides() == PacketSides.NONE) continue;
				if(packet.getSides() == PacketSides.SEND && sides != PacketSides.SEND && sides != PacketSides.BOTH) sides = PacketSides.SEND;
				if(packet.getSides() == PacketSides.RECIEVE && sides != PacketSides.RECIEVE && sides != PacketSides.BOTH) sides = PacketSides.RECIEVE;
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
				.warn(LoggingCategory.MODULES,
						"PacketEditor \"" + packet.getClass().getName()
								+ "\" throwed an Exception on Packet-Sides-Event. Will be ignored.",
						ex);
			}
			
		}
		return sides;
	}

	@Override
	public <E extends Packet<?>> E edit(E packet) {
		final E packetfinal = packet;
		List<IPacketEditor<?>> l = packets.toArray().stream().filter((edit) -> {
			try {
				return edit.accept(packetfinal);
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
				.warn(LoggingCategory.MODULES,
						"PacketEditor \"" + edit.getClass().getName()
								+ "\" throwed an Exception on Packet-Edit-Event (Packet: \"" + packetfinal.getClass().getName() + "\"). Will be ignored.",
						ex);
			}
			return false;
		}).toList();
		
		for(IPacketEditor<?> p : l)
			try {
				packet = p.edit(packet);
			} catch (Throwable ex) {
				Client.getInstance().getLogger()
				.warn(LoggingCategory.MODULES,
						"PacketEditor \"" + p.getClass().getName()
								+ "\" throwed an Exception on Packet-Edot-Event (Packet: \"" + packetfinal.getClass().getName() + "\"). Will be ignored.",
						ex);
			}
		
		return packet;
	}

	@Override
	public List<PositionLine> getPositionLines() {
		return pos_lines;
	}

}
