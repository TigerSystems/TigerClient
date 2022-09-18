package de.MarkusTieger.tigerclient.api.gui.list;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.api.gui.list.IListObject;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SimpleList<E extends IListObject> extends ObjectSelectionList<SimpleList<E>.SimpleListEntry<E>> {

	static final Logger LOGGER = LogManager.getLogger();
	private final SimpleListScreen<E> screen;

	public SimpleList(SimpleListScreen<E> p_101658_, Minecraft p_101659_, int p_101660_, int p_101661_, int p_101662_,
			int p_101663_, int p_101664_, Supplier<String> p_101665_, SimpleList<E> p_101666_) {
		super(p_101659_, p_101660_, p_101661_, p_101662_, p_101663_, p_101664_);
		this.screen = p_101658_;
		this.refreshList(p_101665_);
	}

	public void refreshList(Supplier<String> p_101677_) {
		this.clearEntries();
		String str = p_101677_.get();
		for (E obj : screen.getObjects()) {
			if (str.isBlank() || str.toLowerCase().contains(obj.getSearchName().toLowerCase())) {
				addEntry(new SimpleListEntry<>(this, obj));
			}
		}
	}

	@Override
	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 20;
	}

	@Override
	public int getRowWidth() {
		return super.getRowWidth() + 50;
	}

	@Override
	protected boolean isFocused() {
		return this.screen.getFocused() == this;
	}

	@Override
	public void setSelected(SimpleList<E>.SimpleListEntry<E> p_101675_) {
		super.setSelected(p_101675_);
		this.screen.updateButton();
	}

	@Override
	protected void moveSelection(AbstractSelectionList.SelectionDirection p_101673_) {
		this.moveSelection(p_101673_, (p_101681_) -> {
			return !p_101681_.obj.isDisabled();
		});
	}

	public Optional<SimpleList<E>.SimpleListEntry<E>> getSelectedOpt() {
		return Optional.ofNullable(this.getSelected());
	}

	public SimpleListScreen<E> getScreen() {
		return this.screen;
	}

	public final class SimpleListEntry<T extends IListObject>
			extends ObjectSelectionList.Entry<SimpleList<T>.SimpleListEntry<T>> implements AutoCloseable {
		@SuppressWarnings("unused")
		private static final int ICON_WIDTH = 32, ICON_HEIGHT = 32, ICON_OVERLAY_X_JOIN = 0,
				ICON_OVERLAY_X_JOIN_WITH_NOTIFY = 32, ICON_OVERLAY_X_WARNING = 64, ICON_OVERLAY_X_ERROR = 96,
				ICON_OVERLAY_Y_UNSELECTED = 0, ICON_OVERLAY_Y_SELECTED = 32;
		private final Minecraft minecraft;
		private final SimpleListScreen<T> screen;
		final T obj;
		private final ResourceLocation iconLocation;
		private long lastClickTime;

		public SimpleListEntry(SimpleList<T> p_101702_, T obj) {
			this.screen = p_101702_.getScreen();
			this.obj = obj;
			this.minecraft = Minecraft.getInstance();
			this.iconLocation = obj.getIcon();
		}

		@Override
		public Component getNarration() {
			return obj.getNarration();
		}

		@Override
		public void render(PoseStack p_101721_, int p_101722_, int p_101723_, int p_101724_, int p_101725_,
				int p_101726_, int p_101727_, int p_101728_, boolean p_101729_, float p_101730_) {
			Component var11 = this.obj.getDisplayName();
			Component var12 = this.obj.getDescription();
			Component var13 = this.obj.getInfo();
			this.minecraft.font.draw(p_101721_, var11, p_101724_ + 32 + 3, p_101723_ + 1, 16777215);
			Font var17 = this.minecraft.font;
			float var10003 = p_101724_ + 32 + 3;
			Objects.requireNonNull(this.minecraft.font);
			var17.draw(p_101721_, var12, var10003, p_101723_ + 9 + 3, 8421504);
			var17 = this.minecraft.font;
			var10003 = p_101724_ + 32 + 3;
			Objects.requireNonNull(this.minecraft.font);
			int var10004 = p_101723_ + 9;
			Objects.requireNonNull(this.minecraft.font);
			var17.draw(p_101721_, var13, var10003, var10004 + 9 + 3, 8421504);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, this.iconLocation);
			RenderSystem.enableBlend();
			GuiComponent.blit(p_101721_, p_101724_, p_101723_, 0.0F, 0.0F, 32, 32, 32, 32);
			RenderSystem.disableBlend();
			if (this.minecraft.options.touchscreen || p_101729_) {
				RenderSystem.setShaderTexture(0, screen.getOverlayLocation());
				GuiComponent.fill(p_101721_, p_101724_, p_101723_, p_101724_ + 32, p_101723_ + 32, -1601138544);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				int var14 = p_101727_ - p_101724_;
				boolean var15 = var14 < 32;
				obj.renderOverlay(p_101721_, p_101724_, p_101723_, 32, 32, var15);
			}

		}

		public T getObject() {
			return obj;
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean mouseClicked(double p_101706_, double p_101707_, int p_101708_) {
			if (obj.isDisabled()) {
				return true;
			} else {
				SimpleList.this.setSelected((SimpleList<E>.SimpleListEntry<E>) this);
				this.screen.updateButton();
				if (p_101706_ - SimpleList.this.getRowLeft() <= 32.0D) {
					this.click();
					return true;
				} else if (Util.getMillis() - this.lastClickTime < 250L) {
					this.click();
					return true;
				} else {
					this.lastClickTime = Util.getMillis();
					return false;
				}
			}
		}

		@SuppressWarnings("unchecked")
		public void click() {
			screen.click((SimpleList<T>.SimpleListEntry<T>) this);
		}

		@Override
		public void close() {
		}
	}

}
