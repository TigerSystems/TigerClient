package de.MarkusTieger.tac.gui.widgets.connect.renderer;

import java.awt.Point;
import java.util.function.Function;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.gui.widgets.connect.renderrer.IConnectRenderer;
import de.MarkusTieger.tac.gui.widgets.connect.TACConnectWidget;
import de.MarkusTieger.tac.gui.widgets.connect.registry.PointRegistry;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class IconRenderer implements IConnectRenderer {

	public static final ResourceLocation INTERNET = new ResourceLocation(Client.getInstance().getModId(),
			"textures/gui/connect/internet.png"),
			MOJANG = new ResourceLocation(Client.getInstance().getModId(), "textures/gui/connect/mojang.png"),
			SERVER = new ResourceLocation(Client.getInstance().getModId(), "textures/gui/connect/server.png");

	private final TACConnectWidget widget;
	private final PointRegistry registry;
	private final ItemRenderer renderer;
	private final ItemStack MC;

	public IconRenderer(TACConnectWidget widget, PointRegistry registry) {
		this.widget = widget;
		this.registry = registry;
		this.renderer = Minecraft.getInstance().getItemRenderer();
		this.MC = new ItemStack(Items.CRAFTING_TABLE);
	}

	@Override
	public void render(PoseStack stack, double multiplicator, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int xImg, int yImg) {

		int mjWidth = registry.load("mjSize").x;
		int mjHeight = registry.load("mjSize").y;

		int basic = registry.load("size").y / 8;

		Point mcPoint = registry.load("mc");
		Point itPoint = registry.load("it");
		Point mjPoint = registry.load("mj");
		Point svPoint = registry.load("sv");

		renderAndDecorateItem(MC, funcX.apply(mcPoint.x - (basic - 23)) + xImg,
				funcY.apply(itPoint.y - (basic - 23)) + yImg, funcX.apply(((basic * 2))), funcY.apply(((basic * 2))));

		// widget.img(stack, xImg, yImg, MC, funcX, funcY, mcPoint.x - basic, mcPoint.y
		// - basic, basic * 2, basic * 2);

		widget.img(stack, xImg, yImg, INTERNET, funcX, funcY, itPoint.x - basic, itPoint.y - basic, ((basic * 2)),
				((basic * 2)));

		widget.img(stack, xImg, yImg, MOJANG, funcX, funcY, mjPoint.x - (mjHeight / 4), mjPoint.y - (mjHeight / 4),
				mjWidth / 2, mjHeight / 2);

		widget.img(stack, xImg, yImg, SERVER, funcX, funcY, svPoint.x - basic, svPoint.y - basic, basic * 2, basic * 2);
	}

	public float blitOffset;

	@SuppressWarnings("resource")
	public void renderAndDecorateItem(ItemStack p_115204_, int p_115205_, int p_115206_, float width, float height) {
		this.tryRenderGuiItem(Minecraft.getInstance().player, p_115204_, p_115205_, p_115206_, 0, width, height);
	}

	private void tryRenderGuiItem(LivingEntity p_174278_, ItemStack p_174279_, int p_174280_, int p_174281_,
			int p_174282_, float width, float height) {
		this.tryRenderGuiItem(p_174278_, p_174279_, p_174280_, p_174281_, p_174282_, 0, width, height);
	}

	private void tryRenderGuiItem(LivingEntity p_174236_, ItemStack p_174237_, int p_174238_, int p_174239_,
			int p_174240_, int p_174241_, float width, float height) {
		if (!p_174237_.isEmpty()) {
			BakedModel bakedmodel = renderer.getModel(p_174237_, (Level) null, p_174236_, p_174240_);
			this.blitOffset = bakedmodel.isGui3d() ? this.blitOffset + 50.0F + p_174241_ : this.blitOffset + 50.0F;

			try {
				this.renderGuiItem(p_174237_, p_174238_, p_174239_, bakedmodel, width, height);
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
				CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
				crashreportcategory.setDetail("Item Type", () -> {
					return String.valueOf(p_174237_.getItem());
				});
				crashreportcategory.setDetail("Registry Name",
						() -> String.valueOf(p_174237_.getItem().getRegistryName()));
				crashreportcategory.setDetail("Item Damage", () -> {
					return String.valueOf(p_174237_.getDamageValue());
				});
				crashreportcategory.setDetail("Item NBT", () -> {
					return String.valueOf(p_174237_.getTag());
				});
				crashreportcategory.setDetail("Item Foil", () -> {
					return String.valueOf(p_174237_.hasFoil());
				});
				throw new ReportedException(crashreport);
			}

			this.blitOffset = bakedmodel.isGui3d() ? this.blitOffset - 50.0F - p_174241_ : this.blitOffset - 50.0F;
		}
	}

	@SuppressWarnings("deprecation")
	protected void renderGuiItem(ItemStack p_115128_, int p_115129_, int p_115130_, BakedModel p_115131_, float width,
			float height) {
		Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(p_115129_, p_115130_, 100.0F + this.blitOffset);
		posestack.translate(8.0D, 8.0D, 0.0D);
		posestack.scale(1.0F, -1.0F, 1.0F);
		posestack.scale(width, width, height);
		RenderSystem.applyModelViewMatrix();
		PoseStack posestack1 = new PoseStack();
		MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers()
				.bufferSource();
		boolean flag = !p_115131_.usesBlockLight();
		if (flag) {
			Lighting.setupForFlatItems();
		}

		renderer.render(p_115128_, ItemTransforms.TransformType.GUI, false, posestack1, multibuffersource$buffersource,
				15728880, OverlayTexture.NO_OVERLAY, p_115131_);
		multibuffersource$buffersource.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			Lighting.setupFor3DItems();
		}

		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
	}

}
