package de.MarkusTieger.tigerclient.cosmetics.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.cosmetics.ICosmeticManager;
import de.MarkusTieger.common.utils.animation.IAnimationData;
import de.MarkusTieger.tigerclient.utils.animation.AlternativeAnimationData;
import de.MarkusTieger.tigerclient.utils.animation.OneFrameData;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CapeRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

	private final ICosmeticManager cosmetic;

	public CapeRenderer(PlayerRenderer entityRendererIn) {
		super(entityRendererIn);
		cosmetic = Client.getInstance().getInstanceRegistry().load(ICosmeticManager.class);
	}

	@Override
	public void render(PoseStack p_116615_, MultiBufferSource p_116616_, int p_116617_, AbstractClientPlayer p_116618_,
			float p_116619_, float p_116620_, float p_116621_, float p_116622_, float p_116623_, float p_116624_) {

		IAnimationData data = cosmetic.getCapeData(p_116618_);

		if (data instanceof AlternativeAnimationData
				&& ((AlternativeAnimationData) data).getId().equalsIgnoreCase("Minecraft")) {
			ResourceLocation res = p_116618_.getCloakTextureLocation();
			if (res != null) {
				data = new OneFrameData(res);
			}
		}

		if (p_116618_.isCapeLoaded() && !p_116618_.isInvisible() && p_116618_.isModelPartShown(PlayerModelPart.CAPE)
				&& p_116618_.getCloakTextureLocation() != null) {
			ItemStack var11 = p_116618_.getItemBySlot(EquipmentSlot.CHEST);
			if (!var11.is(Items.ELYTRA)) {
				p_116615_.pushPose();
				p_116615_.translate(0.0D, 0.0D, 0.125D);
				double var12 = Mth.lerp(p_116621_, p_116618_.xCloakO, p_116618_.xCloak)
						- Mth.lerp(p_116621_, p_116618_.xo, p_116618_.getX());
				double var14 = Mth.lerp(p_116621_, p_116618_.yCloakO, p_116618_.yCloak)
						- Mth.lerp(p_116621_, p_116618_.yo, p_116618_.getY());
				double var16 = Mth.lerp(p_116621_, p_116618_.zCloakO, p_116618_.zCloak)
						- Mth.lerp(p_116621_, p_116618_.zo, p_116618_.getZ());
				float var18 = p_116618_.yBodyRotO + (p_116618_.yBodyRot - p_116618_.yBodyRotO);
				double var19 = Mth.sin(var18 * 0.017453292F);
				double var21 = -Mth.cos(var18 * 0.017453292F);
				float var23 = (float) var14 * 10.0F;
				var23 = Mth.clamp(var23, -6.0F, 32.0F);
				float var24 = (float) (var12 * var19 + var16 * var21) * 100.0F;
				var24 = Mth.clamp(var24, 0.0F, 150.0F);
				float var25 = (float) (var12 * var21 - var16 * var19) * 100.0F;
				var25 = Mth.clamp(var25, -20.0F, 20.0F);
				if (var24 < 0.0F) {
					var24 = 0.0F;
				}

				float var26 = Mth.lerp(p_116621_, p_116618_.oBob, p_116618_.bob);
				var23 += Mth.sin(Mth.lerp(p_116621_, p_116618_.walkDistO, p_116618_.walkDist) * 6.0F) * 32.0F * var26;
				if (p_116618_.isCrouching()) {
					var23 += 25.0F;
				}

				p_116615_.mulPose(Vector3f.XP.rotationDegrees(6.0F + var24 / 2.0F + var23));
				p_116615_.mulPose(Vector3f.ZP.rotationDegrees(var25 / 2.0F));
				p_116615_.mulPose(Vector3f.YP.rotationDegrees(180.0F - var25 / 2.0F));
				VertexConsumer var27 = p_116616_.getBuffer(RenderType.entitySolid(p_116618_.getCloakTextureLocation()));
				this.getParentModel().renderCloak(p_116615_, var27, p_116617_, OverlayTexture.NO_OVERLAY);
				p_116615_.popPose();
			}
		}

	}

}
