package de.MarkusTieger.tigerclient.gui.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.utils.CalculatableScreenPosition;
import de.MarkusTieger.common.utils.CalculatableScreenPosition.StorePosition;
import de.MarkusTieger.common.utils.FixedScreenPosition;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.PositionLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import static de.MarkusTieger.common.utils.CalculatableScreenPosition.*;

public class ModsPositionScreen extends Screen {

	private static final ResourceLocation EXAMPLE_BG = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/mods/example_background.png");

	private final List<PositionLine> position_lines;

	private final Map<IDraggable<?>, FixedScreenPosition> positions = new HashMap<>();
	private final Map<IDraggable<?>, StorePosition> store = new HashMap<>();
	private final List<IDraggable<?>> moved = new ArrayList<>();
	
	protected ModsPositionScreen(Screen parent) {
		super(new TranslatableComponent("screen.mods.position.title"));
		this.position_lines = Client.getInstance().getModuleRegistry().getPositionLines();

		for (IDraggable<?> drag : Client.getInstance().getModuleRegistry().getDraggable()) {

			try {
				CalculatableScreenPosition pos = drag.position();
				positions.put(drag, pos.calculateFixed(drag.getWidth(), drag.getHeight()));
				store.put(drag, pos.getStorePosition());
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
						"Can't Fetch Location: " + drag.getId(), ex);
			}
		}
	}
	
	@Override
	public void removed() {

		for(IDraggable<?> drag : moved) {
			try {
				drag.position().storeByFixed(store.get(drag), positions.get(drag), drag.getWidth(), drag.getHeight());
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
						"Can't Store Location: " + drag.getId(), ex);
			}
		}
		
		super.removed();
		
	}

	private IDraggable<?> selected = null;

	@Override
	protected void init() {

		clearWidgets();

		for (Entry<IDraggable<?>, FixedScreenPosition> d : positions.entrySet()) {
			addRenderableWidget(new AbstractWidget(height, height, height, height, title) {

				@Override
				public void updateNarration(NarrationElementOutput p_169152_) {

				}

				@Override
				public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
				      if (this.active && this.visible) {
				         if (p_93643_ == GLFW.GLFW_MOUSE_BUTTON_1) {
				            boolean flag = this.clicked(p_93641_, p_93642_);
				            if (flag) {
				               this.playDownSound(Minecraft.getInstance().getSoundManager());
				               this.onClick(p_93641_, p_93642_);
				               return true;
				            }
				         }
				         if (p_93643_ == GLFW.GLFW_MOUSE_BUTTON_2) {
				        	 boolean flag = right_clicked(p_93641_, p_93642_);
				        	 if (flag) {
					               this.playDownSound(Minecraft.getInstance().getSoundManager());
					               this.onRightClick(p_93641_, p_93642_);
					               return true;
					            }
				         }

				         return false;
				      } else {
				         return false;
				      }
				   }
				
				private boolean right_clicked(double p_93681_, double p_93682_) {
					return this.active && this.visible && p_93681_ >= (double)this.x && p_93682_ >= (double)this.y && p_93681_ < (double)(this.x + this.width) && p_93682_ < (double)(this.y + this.height);
				}
				
				private boolean onRightClick(double p_93634_, double p_93635_) {
					
					FixedScreenPosition pos = d.getValue();
					StorePosition current = store.get(d.getKey());
					StorePosition nw = null;
					if((current.ordinal() + 1) < StorePosition.values().length) {
						nw = StorePosition.values()[current.ordinal() + 1];
					} else {
						nw = StorePosition.values()[0];
					}
					
					store.replace(d.getKey(), nw);
					
					return true;
				}
				
				@Override
				public void onClick(double p_93634_, double p_93635_) {

					if (selected == d.getKey()) {
						selected = null;
					} else {
						selected = d.getKey();
					}
					if(!moved.contains(d.getKey())) moved.add(d.getKey());

				}
				
				

				@Override
				public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {

					IDraggable<?> drag = d.getKey();

					try {
						width = drag.getWidth();
						height = drag.getHeight();
					} catch (Throwable ex) {
						Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
								"Can't Set Position on Render Widget: " + drag.getId(), ex);
					}

					if (drag == selected) {
						updatePosition(d.getValue(), mouseX, mouseY, width, height);
					}

					
					updateCoordinates(d.getValue().getX(), d.getValue().getY());
					
					super.render(PoseStack, mouseX, mouseY, partialTicks);
				}

				private void updateCoordinates(int x, int y) {
					
					StorePosition store = ModsPositionScreen.this.store.get(d.getKey());
					
					Pair<PositionLine, Boolean> nearestVertical = nearest(CalculatableScreenPosition.storePoint(store, d.getValue(), width, height),
							position_lines.stream().filter(PositionLine::vertical).toList());
					Pair<PositionLine, Boolean> nearestHorizontal = nearest(CalculatableScreenPosition.storePoint(store, d.getValue(), width, height),
							position_lines.stream().filter(PositionLine::horizontal).toList());
					
					/*if(nearestVertical.getValue()) {
						x = (int) nearestVertical.getKey().calculate();
					}
					
					if(nearestHorizontal.getValue()) {
						y = (int) nearestHorizontal.getKey().calculate();
					}*/
					
					
					
					this.x = x;
					this.y = y;
					
				}

				private void updatePosition(FixedScreenPosition position, int mouseX, int mouseY, int width,
						int height) {

					int x = mouseX - (width / 2);
					int y = mouseY - (height / 2);

					position.setX(x);
					position.setY(y);

				}

				@Override
				public void renderButton(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {

					IDraggable<?> drag = d.getKey();

					FixedScreenPosition pos = d.getValue();

					try {

						drag.renderDummy(PoseStack, pos);

						int color = selected == drag ? 0xFFEA0A8E : 0xFF7600bc;
						int sto_color = 0xFF00FFFD;
						
						StorePosition sto = store.get(drag);
						
						this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() + drag.getWidth() + 2,
								(int) pos.getY() - 2, sto.isTop() ? sto_color : color);

						this.vLine(PoseStack, (int) pos.getX() - 2, (int) pos.getY() - 2,
								(int) pos.getY() + drag.getHeight() + 2, sto.isLeft() ? sto_color : color);

						this.vLine(PoseStack, (int) pos.getX() + drag.getWidth() + 2, (int) pos.getY() - 2,
								(int) pos.getY() + drag.getHeight() + 2, sto.isRight() ? sto_color : color);

						this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() + drag.getWidth() + 2,
								(int) pos.getY() + drag.getHeight() + 2, sto.isButtom() ? sto_color : color);

					} catch (Throwable ex) {
						Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
								"Can't Render Draggable (Dummy): " + drag.getId(), ex);
					}
				}

			});
		}

		/*
		 * for (IDraggable<?> drag :
		 * Client.getInstance().getModuleRegistry().getDraggable().toArray()) { try {
		 * FixedScreenPosition pos = positions.get(drag);
		 * 
		 * drag.renderDummy(PoseStack, pos);
		 * 
		 * if (inBounds(drag, mouseX, mouseY)) {
		 * 
		 * this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() +
		 * drag.getWidth() + 2, (int) pos.getY() - 2, 0xFFEA0A8E);
		 * 
		 * this.vLine(PoseStack, (int) pos.getX() - 2, (int) pos.getY() - 2, (int)
		 * pos.getY() + drag.getHeight() + 2, 0xFFEA0A8E);
		 * 
		 * this.vLine(PoseStack, (int) pos.getX() + drag.getWidth() + 2, (int)
		 * pos.getY() - 2, (int) pos.getY() + drag.getHeight() + 2, 0xFFEA0A8E);
		 * 
		 * this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() +
		 * drag.getWidth() + 2, (int) pos.getY() + drag.getHeight() + 2, 0xFFEA0A8E); }
		 * else { this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() +
		 * drag.getWidth() + 2, (int) pos.getY() - 2, 0xFF7600bc);
		 * 
		 * this.vLine(PoseStack, (int) pos.getX() - 2, (int) pos.getY() - 2, (int)
		 * pos.getY() + drag.getHeight() + 2, 0xFF7600bc);
		 * 
		 * this.vLine(PoseStack, (int) pos.getX() + drag.getWidth() + 2, (int)
		 * pos.getY() - 2, (int) pos.getY() + drag.getHeight() + 2, 0xFF7600bc);
		 * 
		 * this.hLine(PoseStack, (int) pos.getX() - 2, (int) pos.getX() +
		 * drag.getWidth() + 2, (int) pos.getY() + drag.getHeight() + 2, 0xFF7600bc); }
		 * } catch (Throwable ex) {
		 * Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
		 * "Can't Render Draggable (Dummy): " + drag.getId(), ex); } }
		 */

	}

	@Override
	public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {

		if (this.minecraft.level != null) {
			// this.fillGradient(PoseStack, 0, 0, this.width, this.height, -1072689136,
			// -804253680);
			Client.getInstance().getCompatiblityExecutor().drawBackground(this, PoseStack);
		} else {
			renderExampleBackground(PoseStack);
		}

		

		for (PositionLine line : position_lines) {

			int override = -1;
			boolean doOverride = false;

			for (Entry<IDraggable<?>, FixedScreenPosition> d : positions.entrySet()) {
				IDraggable<?> drag = d.getKey();
				if (drag != selected)
					continue;

				
				
				try {
					StorePosition store = this.store.get(drag);
					
					Pair<PositionLine, Boolean> nearestVertical = nearest(CalculatableScreenPosition.storePoint(store, d.getValue(), drag.getWidth(), drag.getHeight()),
							position_lines.stream().filter(PositionLine::vertical).toList());
					Pair<PositionLine, Boolean> nearestHorizontal = nearest(CalculatableScreenPosition.storePoint(store, d.getValue(), drag.getWidth(), drag.getHeight()),
							position_lines.stream().filter(PositionLine::horizontal).toList());

					if (nearestVertical.getKey() == line) {
						override = nearestVertical.getValue() ? 0xFFFFFFFF : 0xFFFFFF00;
						doOverride = true;
					}
						
					if (nearestHorizontal.getKey() == line) {
						override = nearestHorizontal.getValue() ? 0xFFFFFFFF : 0xFFFFFF00;
						doOverride = true;
					}
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.PLUGINS,
							"Can't Get Nearest Position-Line: " + drag.getId(), ex);
				}
			}
			
			
			
			if (!doOverride) override = line.color();
			
			if (line.vertical()) {
				double o = line.o() * Minecraft.getInstance().getWindow().getGuiScaledWidth();
				double v1 = line.v1() * Minecraft.getInstance().getWindow().getGuiScaledHeight();
				double v2 = line.v2() * Minecraft.getInstance().getWindow().getGuiScaledHeight();

				this.vLine(PoseStack, (int) o, (int) v1, (int) v2, override);
			} else {

				double o = line.o() * Minecraft.getInstance().getWindow().getGuiScaledHeight();
				double v1 = line.v1() * Minecraft.getInstance().getWindow().getGuiScaledWidth();
				double v2 = line.v2() * Minecraft.getInstance().getWindow().getGuiScaledWidth();

				this.hLine(PoseStack, (int) v1, (int) v2, (int) o, override);
			}

		}
		
		super.render(PoseStack, mouseX, mouseY, partialTicks);

	}

	private void renderExampleBackground(PoseStack matrix) {
		RenderSystem.setShaderTexture(0, EXAMPLE_BG);
		blit(matrix, 0, 0, 0.0F, 0.0F, width, height, width, height);
	}

}
