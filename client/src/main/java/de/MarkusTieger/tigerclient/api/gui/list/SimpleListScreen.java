package de.MarkusTieger.tigerclient.api.gui.list;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.api.gui.list.IListObject;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public abstract class SimpleListScreen<T extends IListObject> extends Screen {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
	protected final Screen lastScreen;
	private List<FormattedCharSequence> toolTip;
	private final ArrayList<Button> selectingButtons = new ArrayList<>();
	protected EditBox searchBox;
	protected SimpleList<T> list;
	private final ResourceLocation location;

	public SimpleListScreen(Component title, ResourceLocation overlayLocation, Screen p_101338_) {
		super(title);
		this.location = overlayLocation;
		this.lastScreen = p_101338_;
	}

	@Override
	public boolean mouseScrolled(double p_101343_, double p_101344_, double p_101345_) {
		return super.mouseScrolled(p_101343_, p_101344_, p_101345_);
	}

	@Override
	public void tick() {
		this.searchBox.tick();
	}

	public Button addSelectingButton(Button button) {
		selectingButtons.add(button);
		addRenderableWidget(button);
		return button;
	}

	@Override
	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox,
				new TranslatableComponent("screen.mods.search"));
		this.searchBox.setResponder((p_101362_) -> {
			this.list.refreshList(() -> {
				return p_101362_;
			});
		});
		this.list = new SimpleList<>(this, this.minecraft, this.width, this.height, 48, this.height - 64, 36,
				this.searchBox::getValue, this.list);
		this.addWidget(this.searchBox);
		this.addWidget(this.list);
		this.updateButtonStatus(false);
		this.setInitialFocus(this.searchBox);
	}

	@Override
	public boolean keyPressed(int p_101347_, int p_101348_, int p_101349_) {
		return super.keyPressed(p_101347_, p_101348_, p_101349_)
				|| this.searchBox.keyPressed(p_101347_, p_101348_, p_101349_);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.lastScreen);
	}

	@Override
	public boolean charTyped(char p_101340_, int p_101341_) {
		return this.searchBox.charTyped(p_101340_, p_101341_);
	}

	@Override
	public void render(PoseStack p_101351_, int p_101352_, int p_101353_, float p_101354_) {
		this.toolTip = null;
		this.list.render(p_101351_, p_101352_, p_101353_, p_101354_);
		this.searchBox.render(p_101351_, p_101352_, p_101353_, p_101354_);
		drawCenteredString(p_101351_, this.font, this.title, this.width / 2, 8, 16777215);
		super.render(p_101351_, p_101352_, p_101353_, p_101354_);
		if (this.toolTip != null) {
			this.renderTooltip(p_101351_, this.toolTip, p_101352_, p_101353_);
		}

	}

	public void setToolTip(List<FormattedCharSequence> p_101364_) {
		this.toolTip = p_101364_;
	}

	public void updateButtonStatus(boolean p_101370_) {
		for (Button btn : selectingButtons)
			btn.active = p_101370_;
	}

	@Override
	public void removed() {
		if (this.list != null) {
			this.list.children().forEach(SimpleList.SimpleListEntry::close);
		}
	}

	public void click(SimpleList<T>.SimpleListEntry<T> entry) {
		click(entry.obj);
	}

	public abstract void click(T object);

	public abstract List<T> getObjects();

	public ResourceLocation getOverlayLocation() {
		return location;
	}

	public void updateButton() {
		SimpleList<T>.SimpleListEntry<T> entry = list.getSelected();
		if (entry == null) {
			updateButtonStatus(false);
		} else {
			updateButtonStatus(true);
			updateButtonData(entry);
		}
	}

	public void updateButtonData(SimpleList<T>.SimpleListEntry<T> entry) {
		this.updateButtonData(entry.obj);
	}

	public abstract void updateButtonData(T object);
}
