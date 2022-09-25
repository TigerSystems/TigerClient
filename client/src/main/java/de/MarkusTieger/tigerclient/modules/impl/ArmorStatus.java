package de.MarkusTieger.tigerclient.modules.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.CalculatableScreenPosition;
import de.MarkusTieger.common.utils.FixedScreenPosition;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IMultiDrag;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.gui.screens.BasicDraggableModuleConfigurationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ArmorStatus extends GuiComponent
		implements IModule<Throwable>, IMultiDrag<Throwable>, IConfigable<Throwable> {

	public static boolean enabled = false;
	
	private static final Supplier<CalculatableScreenPosition> DEFAULT = () -> CalculatableScreenPosition.createDefault(8, 0, 0, 8, 17);
	
	public ArrayList<IDraggable<Throwable>> draggs = new ArrayList<>();
	public final IDraggable<Throwable> fixed;
	
	@SuppressWarnings("unused")
	private final Font font = Minecraft.getInstance().font;

	public ArmorStatus() {

		draggs.add(new IDraggable<Throwable>() {

			public CalculatableScreenPosition pos = CalculatableScreenPosition.DEFAULT.get();

			@Override
			public int getHeight() {
				return 20;
			}

			@Override
			public String getId() {
				return ArmorStatus.this.getId() + "#" + "Boots";
			}

			@Override
			public int getWidth() {
				return 20;
			}

			@Override
			public CalculatableScreenPosition position() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, FixedScreenPosition pos) throws Throwable {
				renderShadow(stack, pos, this);

				try {
					Iterator<ItemStack> armor = Minecraft.getInstance().getCameraEntity().getArmorSlots().iterator();
					TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, FixedScreenPosition pos) throws Throwable {
				renderShadow(stack, pos, this);

				TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
						new ItemStack(Items.DIAMOND_BOOTS));
			}

			@Override
			public void position_set(CalculatableScreenPosition p) {
				pos = p;
			}
		});

		draggs.add(new IDraggable<Throwable>() {

			public CalculatableScreenPosition pos = CalculatableScreenPosition.DEFAULT.get();

			@Override
			public int getHeight() {
				return 20;
			}

			@Override
			public String getId() {
				return ArmorStatus.this.getId() + "#" + "Leggings";
			}

			@Override
			public int getWidth() {
				return 20;
			}

			@Override
			public CalculatableScreenPosition position() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, FixedScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, FixedScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
						new ItemStack(Items.DIAMOND_LEGGINGS));
			}

			@Override
			public void position_set(CalculatableScreenPosition p) {
				pos = p;
			}

		});

		draggs.add(new IDraggable<Throwable>() {

			public CalculatableScreenPosition pos = CalculatableScreenPosition.DEFAULT.get();

			@Override
			public int getHeight() {
				return 20;
			}

			@Override
			public String getId() {
				return ArmorStatus.this.getId() + "#" + "Chestplate";
			}

			@Override
			public int getWidth() {
				return 20;
			}

			@Override
			public CalculatableScreenPosition position() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, FixedScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, FixedScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
						new ItemStack(Items.DIAMOND_CHESTPLATE));
			}

			@Override
			public void position_set(CalculatableScreenPosition p) {
				pos = p;
			}

		});

		draggs.add(new IDraggable<Throwable>() {

			public CalculatableScreenPosition pos = CalculatableScreenPosition.DEFAULT.get();

			@Override
			public int getHeight() {
				return 20;
			}

			@Override
			public String getId() {
				return ArmorStatus.this.getId() + "#" + "Helmet";
			}

			@Override
			public int getWidth() {
				return 20;
			}

			@Override
			public CalculatableScreenPosition position() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, FixedScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					armor.next();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, FixedScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getX() + 2, (int) pos.getY() + 2,
						new ItemStack(Items.DIAMOND_HELMET));
			}

			@Override
			public void position_set(CalculatableScreenPosition p) {
				pos = p;
			}

		});
	
		final List<IDraggable<Throwable>> reverse_draggs = draggs.subList(0, draggs.size());
		Collections.reverse(reverse_draggs);
		
		fixed = new IDraggable<Throwable>() {

			private CalculatableScreenPosition pos = DEFAULT.get();
			
			@Override
			public int getHeight() throws Throwable {
				return (16 * reverse_draggs.size()) + 4;
			}

			@Override
			public int getWidth() throws Throwable {
				return 20;
			}

			@Override
			public CalculatableScreenPosition position() throws Throwable {
				return pos;
			}

			@Override
			public void render(PoseStack stack, FixedScreenPosition pos) throws Throwable {
				int p = 0;
				for(IDraggable<Throwable> drag : reverse_draggs) {
					
					int y = p * 16;
					
					drag.render(stack, new FixedScreenPosition((int) pos.getX(), (int) pos.getY() + y - 2));
					
					p++;
				}
			}

			@Override
			public void renderDummy(PoseStack stack, FixedScreenPosition pos) throws Throwable {
				int p = 0;
				for(IDraggable<Throwable> drag : reverse_draggs) {
					
					int y = p * 16;
					
					drag.renderDummy(stack, new FixedScreenPosition((int) pos.getX(), (int) pos.getY() + y - 2));
					
					p++;
				}
			}

			@Override
			public void position_set(CalculatableScreenPosition FixedScreenPosition) throws Throwable {
				pos = FixedScreenPosition;
			}

			@Override
			public String getId() {
				return ArmorStatus.this.getId();
			}
		};
	}

	@Override
	public void bindIcon() {

	}

	@Override
	public boolean canConfigure() {
		return true;
	}

	@Override
	public void configure(Screen parent) {
		Minecraft.getInstance()
				.setScreen(new BasicDraggableModuleConfigurationScreen(this, parent, true, this::update));
	}

	private boolean shadow = false;
	private boolean fix = true;

	public void update() {
		IConfiguration config = Client.getInstance().getInstanceRegistry().load(IConfiguration.class);
		Object obj = config.get("modules#" + getId() + "#shadow");
		if (obj instanceof Boolean) {
			shadow = (boolean) obj;
		} else {
			shadow = false;
		}
		
		obj = config.get("modules#" + getId() + "#fixed");
		if (obj instanceof Boolean) {
			fix = (boolean) obj;
		} else {
			fix = true;
		}
		Client.getInstance().getModuleRegistry().update();
	}

	public void renderShadow(PoseStack stack, FixedScreenPosition pos, IDraggable<Throwable> draggable) throws Throwable {
		if (!shadow)
			return;

		fill(stack, (int) pos.getX() - 1, (int) pos.getY() - 1,
				(int) pos.getX() + draggable.getWidth() + 1,
				(int) pos.getY() + draggable.getHeight() + 1, 0x101010CC);
	}

	@Override
	public Component getDescription() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".description");
	}

	@Override
	public Component getInfo() {
		return isEnabled() ? new TranslatableComponent("modules.enabled")
				: new TranslatableComponent("modules.disabled");
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public String getSearchName() {
		return getId();
	}

	@Override
	public void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws Throwable {
		renderDefaultOverlay(stack, x, y, width, height, hover);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("modules." + getId().toLowerCase() + ".display");
	}

	@Override
	public List<IDraggable<Throwable>> getDraggables() {
		return fix ? List.of(fixed) : draggs;
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(Client.getInstance().getModId(), "textures/modules/armorstatus/icon.png");
	}

	@Override
	public Component getNarration() {
		return getDisplayName();
	}

	@Override
	public String getId() {
		return ArmorStatus.class.getSimpleName();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean bool) {
		enabled = bool;
		update();
	}

	@Override
	public void reset() throws Throwable {
		for (IDraggable<Throwable> drag : getDraggables()) {
			drag.position_set(CalculatableScreenPosition.DEFAULT.get());
		}
		fixed.position_set(DEFAULT.get());
		shadow = false;
		fix = true;
	}

}
