package de.MarkusTieger.tigerclient.cosmetics.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.cosmetics.ICosmeticManager;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.tigerclient.utils.animation.AlternativeAnimationData;
import de.MarkusTieger.tigerclient.utils.animation.OneFrameData;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	private final ElytraModel<AbstractClientPlayer> elytraModel;
	private final ICosmeticManager cosmetic;

	public ElytraRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_174493_,
			EntityModelSet p_174494_) {
		super(p_174493_);
		this.elytraModel = new ElytraModel<>(p_174494_.bakeLayer(ModelLayers.ELYTRA));
		this.cosmetic = Client.getInstance().getInstanceRegistry().load(ICosmeticManager.class);
	}

	public ResourceLocation getElytraTexture(ItemStack stack, AbstractClientPlayer entity) {
		return TEXTURE_ELYTRA;
	}

	@Override
	public void render(PoseStack p_116951_, MultiBufferSource p_116952_, int p_116953_, AbstractClientPlayer p_116954_,
			float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {

		IAnimationData data = cosmetic.getElytraData(p_116954_);

		if (data instanceof AlternativeAnimationData
				&& ((AlternativeAnimationData) data).getId().equalsIgnoreCase("Minecraft")) {
			ResourceLocation res = p_116954_.getElytraTextureLocation();
			if (res != null) {
				data = new OneFrameData(res);
			}

		}

		ItemStack itemstack = p_116954_.getItemBySlot(EquipmentSlot.CHEST);
		if (this.shouldRender(itemstack, p_116954_)) {
			ResourceLocation resourcelocation;
			if (p_116954_ instanceof AbstractClientPlayer) {
				AbstractClientPlayer abstractclientplayer = p_116954_;
				if (abstractclientplayer.isElytraLoaded() && abstractclientplayer.getElytraTextureLocation() != null) {
					resourcelocation = abstractclientplayer.getElytraTextureLocation();
				} else if (abstractclientplayer.isCapeLoaded() && abstractclientplayer.getCloakTextureLocation() != null
						&& abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
					resourcelocation = abstractclientplayer.getCloakTextureLocation();
				} else {
					resourcelocation = this.getElytraTexture(itemstack, p_116954_);
				}
			} else {
				resourcelocation = this.getElytraTexture(itemstack, p_116954_);
			}

			p_116951_.pushPose();
			p_116951_.translate(0.0D, 0.0D, 0.125D);
			this.getParentModel().copyPropertiesTo(this.elytraModel);
			this.elytraModel.setupAnim(p_116954_, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
			VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_116952_,
					RenderType.armorCutoutNoCull(resourcelocation), false, itemstack.hasFoil());
			this.elytraModel.renderToBuffer(p_116951_, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F,
					1.0F, 1.0F);
			p_116951_.popPose();
		}

	}

	public boolean shouldRender(ItemStack stack, AbstractClientPlayer entity) {
		return stack.getItem() == Items.ELYTRA;
	}

}
