package de.MarkusTieger.tigerclient.modules.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IMultiDrag;
import de.MarkusTieger.tigerclient.api.gui.TigerGuiUtils;
import de.MarkusTieger.tigerclient.gui.screens.BasicDraggableModuleConfigurationScreen;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
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
	
	public ArrayList<IDraggable<Throwable>> draggs = new ArrayList<>();
	public final IDraggable<Throwable> fixed;
	
	@SuppressWarnings("unused")
	private final Font font = Minecraft.getInstance().font;

	public ArmorStatus() {

		draggs.add(new IDraggable<Throwable>() {

			public ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);

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
			public ScreenPosition load() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, ScreenPosition pos) throws Throwable {
				renderShadow(stack, pos, this);

				try {
					Iterator<ItemStack> armor = Minecraft.getInstance().getCameraEntity().getArmorSlots().iterator();
					TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, ScreenPosition pos) throws Throwable {
				renderShadow(stack, pos, this);

				TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
						new ItemStack(Items.DIAMOND_BOOTS));
			}

			@Override
			public void save(ScreenPosition p) {
				pos = p;
			}
		});

		draggs.add(new IDraggable<Throwable>() {

			public ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);

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
			public ScreenPosition load() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, ScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, ScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
						new ItemStack(Items.DIAMOND_LEGGINGS));
			}

			@Override
			public void save(ScreenPosition p) {
				pos = p;
			}

		});

		draggs.add(new IDraggable<Throwable>() {

			public ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);

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
			public ScreenPosition load() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, ScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, ScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
						new ItemStack(Items.DIAMOND_CHESTPLATE));
			}

			@Override
			public void save(ScreenPosition p) {
				pos = p;
			}

		});

		draggs.add(new IDraggable<Throwable>() {

			public ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);

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
			public ScreenPosition load() {
				return pos;
			}

			@Override
			public void render(PoseStack stack, ScreenPosition pos) {
				try {
					Iterator<ItemStack> armor = (Minecraft.getInstance().getCameraEntity()).getArmorSlots().iterator();
					armor.next();
					armor.next();
					armor.next();
					TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
							armor.next());
				} catch (Exception ex) {

				}
			}

			@Override
			public void renderDummy(PoseStack stack, ScreenPosition pos) {
				TigerGuiUtils.renderHotbarItem((int) pos.getAbsouluteX() + 2, (int) pos.getAbsouluteY() + 2,
						new ItemStack(Items.DIAMOND_HELMET));
			}

			@Override
			public void save(ScreenPosition p) {
				pos = p;
			}

		});
	
		fixed = new IDraggable<Throwable>() {

			private ScreenPosition pos = ScreenPosition.fromRelativePosition(0.5D, 0.5D);
			
			@Override
			public int getHeight() throws Throwable {
				return (20 * draggs.size()) + (4 * draggs.size());
			}

			@Override
			public int getWidth() throws Throwable {
				return 20;
			}

			@Override
			public ScreenPosition load() throws Throwable {
				return pos;
			}

			@Override
			public void render(PoseStack stack, ScreenPosition pos) throws Throwable {
				int p = 0;
				for(IDraggable<Throwable> drag : draggs) {
					
					int y = p * 24;
					
					drag.render(stack, ScreenPosition.fromAbsolutePosition((int) pos.getAbsouluteX(), (int) pos.getAbsouluteY() + y));
					
					p++;
				}
			}

			@Override
			public void renderDummy(PoseStack stack, ScreenPosition pos) throws Throwable {
				int p = 0;
				for(IDraggable<Throwable> drag : draggs) {
					
					int y = p * 24;
					
					drag.renderDummy(stack, ScreenPosition.fromAbsolutePosition((int) pos.getAbsouluteX(), (int) pos.getAbsouluteY() + y));
					
					p++;
				}
			}

			@Override
			public void save(ScreenPosition screenPosition) throws Throwable {
				pos = screenPosition;
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

	public void renderShadow(PoseStack stack, ScreenPosition pos, IDraggable<Throwable> draggable) throws Throwable {
		if (!shadow)
			return;

		fill(stack, (int) pos.getAbsouluteX() - 1, (int) pos.getAbsouluteY() - 1,
				(int) pos.getAbsouluteX() + draggable.getWidth() + 1,
				(int) pos.getAbsouluteY() + draggable.getHeight() + 1, 0x101010CC);
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
			drag.save(ScreenPosition.fromRelativePosition(0.5D, 0.5D));
		}
	}

}
